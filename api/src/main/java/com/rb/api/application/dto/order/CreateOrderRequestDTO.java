package com.rb.api.application.dto.order;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull(message = "O ID da mesa é obrigatório")
        UUID tableId,

        @NotNull(message = "O ID do garçom é obrigatório")
        UUID waiterId
) {
}