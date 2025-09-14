package ru.poteha.rent.support.request.impl.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.poteha.rent.support.discuss.impl.jpa.SupportDiscuss;
import ru.poteha.rent.support.request.impl.SupportRequestListener;
import ru.poteha.rent.support.request.model.SupportRequestStatus;
import ru.poteha.rent.user.impl.jpa.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@EntityListeners(SupportRequestListener.class)
@Table(name = "APP_SUPPORT_REQUEST")
public class SupportRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;

    @Enumerated(EnumType.STRING)
    private SupportRequestStatus status;

    @ManyToOne
    private User author;
    private String title;

    @OneToMany(mappedBy = "request")
    private List<SupportDiscuss> discusses = new ArrayList<>();

    private String description;
}
