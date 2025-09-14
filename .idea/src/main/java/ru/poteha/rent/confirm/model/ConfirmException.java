package ru.poteha.rent.confirm.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class ConfirmException extends ErrorResponseException {
    private ConfirmException(HttpStatusCode status) {
        super(status);
    }

    public static class ExpiredAt extends ConfirmException {
        public ExpiredAt() {
            super(HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoAttempts extends ConfirmException {
        public NoAttempts() {
            super(HttpStatus.BAD_REQUEST);
        }
    }

    public static class IllegalCode extends ConfirmException {
        public IllegalCode() {
            super(HttpStatus.BAD_REQUEST);
        }
    }

    public static class NotConfirm extends ConfirmException {
        public NotConfirm() {
            super(HttpStatus.BAD_REQUEST);
        }
    }
}
