package com.store.application.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStockException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public InvalidStockException(String message) {
        super(message);
    }

}