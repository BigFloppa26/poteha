package ru.poteha.rent.support.request.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class SupportRequestException extends ErrorResponseException {
    private SupportRequestException(HttpStatusCode status) {
        super(status);
    }

    public static class NotFound extends SupportRequestException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND);
        }
    }
}
