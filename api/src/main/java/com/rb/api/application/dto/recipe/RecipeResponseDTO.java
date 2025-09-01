package com.rb.api.application.dto.recipe;

import java.util.UUID;

public record RecipeResponseDTO(
        UUID id,
        UUID menuItemId,
        String instructions,
        int prepTimeMinutes
) {}