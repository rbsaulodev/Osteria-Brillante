package com.rb.api.application.dto.menu;

import com.rb.api.application.dto.recipe.CreateRecipeRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateMenuItemRequestDTO(
        @NotBlank
        @Size(max = 100)
        String name,

        @Size(max = 255)
        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        UUID categoryId,

        @Valid
        CreateRecipeRequestDTO recipe
) {}