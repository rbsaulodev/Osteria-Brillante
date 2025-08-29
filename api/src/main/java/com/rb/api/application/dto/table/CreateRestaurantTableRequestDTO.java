package com.rb.api.application.dto.table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRestaurantTableRequestDTO(
        @NotNull(message = "O número da mesa é obrigatório.")
        @Positive(message = "O número da mesa deve ser positivo.")
        Integer tableNumber
) {}