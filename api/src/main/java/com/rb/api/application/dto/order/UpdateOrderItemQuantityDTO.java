package com.rb.api.application.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderItemQuantityDTO(
        @NotNull
        @Min(value = 1, message = "A nova quantidade deve ser de no mínimo 1")
        int newQuantity
) {
}