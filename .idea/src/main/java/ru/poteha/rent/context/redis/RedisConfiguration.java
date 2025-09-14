package ru.poteha.rent.context.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    @Bean
    RedisSubscriberListener redisSubscriberListener(RedisMessageListenerContainer container, RedisSerializer<Object> serializer) {
        return new RedisSubscriberListener(container, serializer);
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, RedisSerializer<Object> serializer) {
        var template = new RedisTemplate<String, Object>();
        template.setEnableTransactionSupport(true);
        template.setConnectionFactory(factory);

        //  Сериализатор ключей
        var stringSerializer = new StringRedisSerializer();
        template.setHashKeySerializer(stringSerializer);
        template.setKeySerializer(stringSerializer);

        //  Сериализатор значений
        template.setHashValueSerializer(serializer);
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory factory) {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }

    @Bean
    RedisSerializer<Object> redisSerializer(ObjectMapper mapper) {
        return new GenericJackson2JsonRedisSerializer(mapper);
    }

    @Bean
    CacheManager cacheManager(RedisConnectionFactory factory, RedisSerializer<Object> serializer) {
        var cacheDefault = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(30))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
        return RedisCacheManager.builder(factory)
                .withCacheConfiguration("feature", cacheDefault.entryTtl(Duration.ofHours(3)))
                .cacheDefaults(cacheDefault)
                .transactionAware()
                .build();
    }

}
