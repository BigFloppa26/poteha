package ru.poteha.rent.notification.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.poteha.rent.notification.NotificationRepository;
import ru.poteha.rent.notification.NotificationService;
import ru.poteha.rent.notification.impl.jpa.Notification_;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationJobs {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 1 1/3 *")
    public void cleanup() {
        notificationRepository.delete((root, query, builder) -> {
            log.debug("Удаление писем старше 3-ёх месяцев");
            return builder.lessThan(root.get(Notification_.CREATED), LocalDateTime.now().minusMonths(3));
        });
    }

    @Scheduled(scheduler = "statefulScheduler", cron = "*/40 * * * * *")
    public void broadcastPing() {
        notificationService.broadcastEmitters((userId, sseEmitter) -> {
            log.debug("Отправка \"PING\" уведомления для поддержания сессии");
            broadcast(sseEmitter, userId, "PING");
        });
    }

    private void broadcast(SseEmitter sseEmitter, UUID userId, Object data) {
        try {
            sseEmitter.send(data, MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            notificationService.removeEmitter(userId);
        }
    }
}
