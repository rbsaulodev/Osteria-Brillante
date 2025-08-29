package com.rb.api.application.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderItemDTO(
        @NotNull
        UUID menuItemId,

        @Min(1)
        int quantity
) {}