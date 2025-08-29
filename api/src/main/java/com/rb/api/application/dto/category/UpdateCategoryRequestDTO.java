package com.rb.api.application.dto.category;

import jakarta.validation.constraints.Size;

public record UpdateCategoryRequestDTO(
        @Size(min = 5, max = 100)
        String name,

        @Size(max = 250)
        String description
) {}