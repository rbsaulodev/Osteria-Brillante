package com.rb.api.application.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull
        UUID tableId,

        @NotNull
        UUID waiterId,

        @NotEmpty
        @Valid
        List<CreateOrderItemDTO> items
) {}