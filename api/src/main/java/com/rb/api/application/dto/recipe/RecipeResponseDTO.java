package com.rb.api.application.dto.recipe;

import java.util.UUID;

public record RecipeResponseDTO(
        UUID id,
        String instructions,
        int prepTimeMinutes
) {}