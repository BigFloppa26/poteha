package ru.poteha.rent.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.poteha.rent.notification.model.*;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface NotificationService {
    Page<NotificationShort> searchBy(NotificationSearch search, Pageable page);

    NotificationDetail getDetailById(UUID id);

    void sendAsync(NotificationCreate model, Consumer<UUID> id);

    void sendAsync(NotificationCreate model);

    //
    //
    //
    SseEmitter registerEmitter(UUID userId, SseEmitter emitter);

    void broadcastEmitters(BiConsumer<UUID, SseEmitter> consume);

    void removeEmitter(UUID userId);
}
