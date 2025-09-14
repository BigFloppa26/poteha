package ru.poteha.rent.support.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.poteha.rent.support.discuss.model.SupportDiscussShort;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class SupportRequestShort {
    private UUID id;
    private String title;
    private LocalDateTime created;
    private SupportRequestStatus status;

    private SupportDiscussShort lastDiscuss;
}
