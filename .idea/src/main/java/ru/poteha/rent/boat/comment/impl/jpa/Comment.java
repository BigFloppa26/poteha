package ru.poteha.rent.boat.comment.impl.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.poteha.rent.boat.boat.impl.jpa.Boat;
import ru.poteha.rent.user.impl.jpa.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "APP_COMMENT")
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;

    private String comment;

    @ManyToOne
    private Boat boat;

    @ManyToOne
    private User user;
}
