package com.rb.api.application.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateOrderItemDTO(
        @Min(1)
        Integer quantity,

        @Size(max = 255)
        String notes
) {}