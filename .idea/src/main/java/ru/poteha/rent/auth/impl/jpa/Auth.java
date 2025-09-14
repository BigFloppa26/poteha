package ru.poteha.rent.auth.impl.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.poteha.rent.user.impl.jpa.User;

import java.time.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "APP_AUTH")
public class Auth {

    @Id
    @GeneratedValue
    private UUID id;
    private String jti;

    @Version
    private LocalDateTime lastUse;
    @CreationTimestamp
    private LocalDateTime created;

    private String userAgent;

    @ManyToOne
    private User user;

}
