package ru.poteha.rent.notification.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class NotificationException extends ErrorResponseException {
    public NotificationException(HttpStatusCode status) {
        super(status);
    }

    public static class NotFound extends NotificationException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND);
        }
    }
}
