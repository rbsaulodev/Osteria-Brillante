package com.rb.api.application.dto.menu;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateMenuItemRequestDTO(
        @Size(max = 100)
        String name,

        @Size(max = 255)
        String description,

        @Positive
        BigDecimal price,

        UUID categoryId,

        Boolean isAvailable
) {}