package com.store.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // Automatically returns 403 Forbidden
public class UnauthorizedAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedAccessException(String message) {
        super(message);
    }

}
