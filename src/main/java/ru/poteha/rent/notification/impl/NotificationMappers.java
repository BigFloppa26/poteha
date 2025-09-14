package ru.poteha.rent.notification.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.poteha.rent.notification.NotificationMapper;
import ru.poteha.rent.notification.impl.jpa.Notification;
import ru.poteha.rent.notification.model.*;

import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
@Component
@RequiredArgsConstructor
class NotificationMappers implements NotificationMapper {
    @Override
    public NotificationShort toShort(Notification notification) {
        var target = new NotificationShort();
        target.setId(notification.getId());
        target.setType(notification.getType());
        target.setHeader(notification.getHeader());
        target.setStatus(notification.getStatus());
        target.setRecipient(notification.getRecipient());
        return target;
    }

    @Override
    public NotificationDetail toDetail(Notification notification) {
        var target = new NotificationDetail();
        target.setId(notification.getId());
        target.setType(notification.getType());
        target.setBody(notification.getBody());
        target.setHeader(notification.getHeader());
        target.setStatus(notification.getStatus());
        target.setRecipient(notification.getRecipient());
        return target;
    }

    Notification fromCreate(NotificationCreate model, BiFunction<String, Map<String, Object>, String> render) {
        var notification = new Notification();
        notification.setBody(render.apply(model.getTemplate(), model.getAttributes()));
        notification.setStatus(NotificationStatus.SEND);
        notification.setType(model.getType());
        notification.setRecipient(model.getRecipient());
        notification.setHeader(model.getHeader());
        return notification;
    }
}
