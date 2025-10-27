package com.rb.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TableAlreadyInUseException extends RuntimeException {
    public TableAlreadyInUseException(String message) {
        super(message);
    }
}