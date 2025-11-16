package com.rb.api.application.exception;

import java.util.UUID;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resourceName, UUID id) {
        super(resourceName + " com ID " + id + " não encontrado(a).");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}