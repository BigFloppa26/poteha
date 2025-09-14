package ru.poteha.rent.notification;

import ru.poteha.rent.notification.impl.jpa.Notification;
import ru.poteha.rent.notification.model.NotificationDetail;
import ru.poteha.rent.notification.model.NotificationShort;

public interface NotificationMapper {
    NotificationShort toShort(Notification notification);

    NotificationDetail toDetail(Notification notification);
}
