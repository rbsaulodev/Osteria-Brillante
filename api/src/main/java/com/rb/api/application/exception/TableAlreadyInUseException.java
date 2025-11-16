package com.rb.api.application.exception;

public class TableAlreadyInUseException extends BusinessException {
    public TableAlreadyInUseException(String message) {
        super(message);
    }
}