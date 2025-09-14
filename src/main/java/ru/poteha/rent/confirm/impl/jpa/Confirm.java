package ru.poteha.rent.confirm.impl.jpa;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import ru.poteha.rent.notification.impl.jpa.Notification;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "APP_CONFIRM")
public class Confirm {
    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;

    private String url;
    private String code;
    private int attempts;
    private long expiresIn;
    private boolean confirmed;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode payload;

    @ManyToOne
    private Notification notification;
}