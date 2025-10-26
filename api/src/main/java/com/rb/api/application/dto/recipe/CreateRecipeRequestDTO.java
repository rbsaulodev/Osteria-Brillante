package com.rb.api.application.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreateRecipeRequestDTO(

        @NotNull(message = "O ID do item do cardápio é obrigatório")
        UUID menuItemId,

        @NotBlank(message = "As instruções são obrigatórias")
        String instructions,

        @NotNull(message = "O tempo de preparação é obrigatório")
        @PositiveOrZero(message = "O tempo de preparação não pode ser negativo")
        Integer prepTimeMinutes
) {
}