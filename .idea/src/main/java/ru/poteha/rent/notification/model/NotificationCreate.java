package ru.poteha.rent.notification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Builder
public class NotificationCreate {
    @NotNull
    @Schema(description = "Тип уведомления")
    private NotificationType type;
    @NotBlank
    @Schema(description = "Шаблон уведомления")
    private String template;
    @Schema(description = "Атрибуты")
    private Map<String, Object> attributes;
    @NotBlank
    @Schema(description = "Получатель")
    private String recipient;
    @Schema(description = "Заголовок")
    private String header;
}
