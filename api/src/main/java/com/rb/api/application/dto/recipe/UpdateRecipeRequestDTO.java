package com.rb.api.application.dto.recipe;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdateRecipeRequestDTO(
        String instructions,

        @PositiveOrZero(message = "O tempo de preparo não pode ser negativo.")
        Integer prepTimeMinutes
) {}