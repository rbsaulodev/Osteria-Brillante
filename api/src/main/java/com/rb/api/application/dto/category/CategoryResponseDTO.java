package com.rb.api.application.dto.category;

import java.util.UUID;

public record CategoryResponseDTO(
        UUID id,
        String name,
        String description
) {
}