package com.rb.api.application.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddOrderItemRequestDTO(
        @NotNull(message = "O ID do item de cardápio é obrigatório")
        UUID menuItemId,

        @NotNull
        @Min(value = 1, message = "A quantidade deve ser de no mínimo 1")
        int quantity
) {
}