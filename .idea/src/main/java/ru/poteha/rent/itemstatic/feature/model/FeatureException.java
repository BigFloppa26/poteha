package ru.poteha.rent.itemstatic.feature.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class FeatureException extends ErrorResponseException {
    private FeatureException(HttpStatusCode status) {
        super(status);
    }

    public static class NotFound extends FeatureException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND);
        }
    }

    public static class Required extends FeatureException {
        public Required() {
            super(HttpStatus.BAD_REQUEST);
        }
    }
}
