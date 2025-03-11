package com.store.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Automatically returns 409 Conflict
public class DuplicateUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateUserException(String message) {
        super(message);
    }

}