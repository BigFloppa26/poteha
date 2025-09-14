package ru.poteha.rent.support.discuss.impl.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.poteha.rent.support.request.impl.jpa.SupportRequest;
import ru.poteha.rent.user.impl.jpa.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "APP_SUPPORT_DISCUSS")
public class SupportDiscuss {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;

    @ManyToOne
    private SupportRequest request;

    private String message;
    @ManyToOne
    private User fromUser;
    private boolean read;
}
