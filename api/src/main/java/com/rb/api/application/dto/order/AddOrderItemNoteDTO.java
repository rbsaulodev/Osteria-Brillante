package com.rb.api.application.dto.order;

import jakarta.validation.constraints.NotBlank;

public record AddOrderItemNoteDTO(
        @NotBlank(message = "A nota não pode ser vazia")
        String note
) {
}