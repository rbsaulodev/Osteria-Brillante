package com.rb.api.application.dto.menuitem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MenuItemResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        boolean isAvailable,
        UUID categoryId,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}