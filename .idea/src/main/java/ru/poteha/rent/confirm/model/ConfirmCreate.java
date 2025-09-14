package ru.poteha.rent.confirm.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import ru.poteha.rent.notification.model.NotificationCreate;

@Setter
@Getter
@RequiredArgsConstructor
public class ConfirmCreate {
    @URL
    private final String url;
    @NotNull
    private final Object payload;

    private NotificationCreate notification;
}
