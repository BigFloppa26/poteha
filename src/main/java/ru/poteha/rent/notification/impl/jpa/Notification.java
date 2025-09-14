package ru.poteha.rent.notification.impl.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.poteha.rent.notification.model.NotificationStatus;
import ru.poteha.rent.notification.model.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "APP_NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String recipient;
    private String header;
    private String body;
}
