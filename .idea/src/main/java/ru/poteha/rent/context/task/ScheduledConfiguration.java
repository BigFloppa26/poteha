package ru.poteha.rent.context.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledConfiguration {

    @Bean
    TaskScheduler statefulScheduler() {
        return new SimpleAsyncTaskScheduler();
    }

    @Bean
    TaskScheduler taskScheduler(LockProvider lockProvider) {
        var scheduler = new SimpleAsyncTaskScheduler();
        scheduler.setTaskDecorator(runnable -> lockProvider.lock(runnable) ? runnable : null);
        scheduler.setErrorHandler(error -> log.error("Ошибка фонового процесса: ", error));
        return scheduler;
    }

    @Bean
    LockProvider dbLockProvider(StringRedisTemplate redisTemplate) {
        return task -> redisTemplate.opsForValue().setIfAbsent("lock:" + task, "true", 5, TimeUnit.SECONDS);
    }

    interface LockProvider {

        Boolean lock(String task);

        default boolean lock(Runnable runnable) {
            return lock(runnable.toString().replaceAll(".*\\.([^.]+\\.[^.]+)$", "$1"));
        }
    }
}