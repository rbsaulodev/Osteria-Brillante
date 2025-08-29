package com.rb.api.application.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateRecipeRequestDTO(
        @NotBlank(message = "As instruções não podem ser vazias.")
        String instructions,

        @NotNull(message = "O tempo de preparo é obrigatório.")
        @PositiveOrZero(message = "O tempo de preparo não pode ser negativo.")
        Integer prepTimeMinutes
) {}