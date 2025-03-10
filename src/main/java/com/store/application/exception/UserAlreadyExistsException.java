package com.store.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Automatically returns 409 Conflict
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super("User already exists.");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}