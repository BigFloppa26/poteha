package ru.poteha.rent.support.discuss.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class SupportDiscussShort {
    private UUID id;
    private boolean read;
    private String message;

    private UUID fromUserId;
    private String fromUsername;

    private LocalDateTime created;
}
