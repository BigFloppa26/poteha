package ru.poteha.rent.user.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class UserException extends ErrorResponseException {

    private UserException(HttpStatusCode status) {
        super(status);
    }

    public static class NotFound extends UserException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND);
        }
    }

    public static class AlreadyExists extends UserException {
        public AlreadyExists() {
            super(HttpStatus.BAD_REQUEST);
        }
    }
}
