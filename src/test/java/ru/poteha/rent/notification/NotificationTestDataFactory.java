package ru.poteha.rent.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.poteha.rent.notification.impl.jpa.Notification;
import ru.poteha.rent.notification.model.NotificationCreate;
import ru.poteha.rent.notification.model.NotificationType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class NotificationTestDataFactory {

    public static Notification notification() {
        var notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setRecipient("Test");
        notification.setType(NotificationType.EMAIL);
        notification.setBody("Test");
        notification.setModified(LocalDateTime.now());
        notification.setCreated(LocalDateTime.now());
        notification.setHeader("Test");
        return notification;
    }

    public static byte[] notificationCreate(boolean attr) throws JsonProcessingException {
        var notificationCreate = NotificationCreate.builder()
                .type(NotificationType.EMAIL)
                .attributes(Map.of(
                        "confirmCode", "",
                        "confirmExpires", "",
                        "confirmLink", ""
                ))
                .recipient("Test")
                .header("Test")
                .build();
        if (attr) {
            notificationCreate.setTemplate("confirmResend.ftl");
        } else {
            notificationCreate.setTemplate("");
        }
        return new ObjectMapper().writeValueAsBytes(notificationCreate);
    }
}