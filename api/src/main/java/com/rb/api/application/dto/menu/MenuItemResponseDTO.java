package com.rb.api.application.dto.menu;

import com.rb.api.application.dto.category.CategoryResponseDTO;
import com.rb.api.application.dto.recipe.RecipeResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MenuItemResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        boolean isAvailable,
        CategoryResponseDTO category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        RecipeResponseDTO recipe
) {}