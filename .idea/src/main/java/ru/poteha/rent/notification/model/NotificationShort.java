package ru.poteha.rent.notification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class NotificationShort {
    @Schema(description = "Идентификатор уведомления")
    private UUID id;
    @Schema(description = "Заголовок")
    private String header;
    @Schema(description = "Получатель")
    private String recipient;
    @Schema(description = "Тип уведомления")
    private NotificationType type;
    @Schema(description = "Статус")
    private NotificationStatus status;
}
