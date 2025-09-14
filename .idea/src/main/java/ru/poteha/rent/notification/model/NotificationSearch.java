package ru.poteha.rent.notification.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class NotificationSearch {
    private String header;
    private String recipient;
    private List<NotificationType> type;
    private List<NotificationStatus> status;

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
