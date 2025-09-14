package ru.poteha.rent.support.discuss.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class SupportDiscussException extends ErrorResponseException {
    private SupportDiscussException(HttpStatusCode status) {
        super(status);
    }

    public static class IsActualDiscuss extends SupportDiscussException {
        public IsActualDiscuss() {
            super(HttpStatus.BAD_REQUEST);
        }
    }
}
