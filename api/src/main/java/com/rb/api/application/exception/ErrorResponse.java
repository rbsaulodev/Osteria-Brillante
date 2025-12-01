package com.rb.api.application.exception;

import java.time.LocalDateTime;

record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}