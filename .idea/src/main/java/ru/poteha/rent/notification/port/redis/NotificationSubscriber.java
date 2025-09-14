package ru.poteha.rent.notification.port.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.poteha.rent.context.redis.annotation.RedisSubscriber;
import ru.poteha.rent.notification.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSubscriber {
    private final NotificationService notificationService;

    @RedisSubscriber(pattern = "notification:event:*")
    public void notificationSubscribe(String channel, Object message) {
        notificationService.broadcastEmitters((userId, sseEmitter) -> {
            if (!channel.contains(userId.toString())) return;

            try {
                sseEmitter.send(message, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                log.warn("Ошибка при отправке уведомления", e);
                notificationService.removeEmitter(userId);
            }
        });
    }

}
