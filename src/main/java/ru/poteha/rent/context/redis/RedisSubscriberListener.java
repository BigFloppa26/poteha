package ru.poteha.rent.context.redis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import ru.poteha.rent.context.redis.annotation.RedisSubscriber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
public class RedisSubscriberListener implements ApplicationListener<ContextRefreshedEvent> {
    private final RedisMessageListenerContainer listenerContainer;
    private final RedisSerializer<?> serializer;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        event.getApplicationContext().getBeansWithAnnotation(Component.class).forEach(this::register);
    }

    void register(String name, Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            var annotation = method.getAnnotation(RedisSubscriber.class);
            if (annotation != null) {
                var adapter = new DelegateMessageListener(serializer, bean, method);
                listenerContainer.addMessageListener(adapter, new PatternTopic(annotation.pattern()));
            }
        }
    }

    @RequiredArgsConstructor
    static class DelegateMessageListener implements MessageListener {
        private final RedisSerializer<?> serializer;
        private final Object delegate;
        private final Method method;

        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                var body = serializer == null ? message : serializer.deserialize(message.getBody());
                var channel = new String(message.getChannel());

                method.invoke(delegate, channel, body);
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.error("Error invoking delegate method", e);
                throw new RuntimeException(e);
            }
        }
    }
}
