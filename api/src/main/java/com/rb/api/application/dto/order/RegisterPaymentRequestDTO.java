package com.rb.api.application.dto.order;

import com.rb.api.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RegisterPaymentRequestDTO(
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal amount,

        @NotNull(message = "O método de pagamento é obrigatório")
        PaymentMethod method
) {
}