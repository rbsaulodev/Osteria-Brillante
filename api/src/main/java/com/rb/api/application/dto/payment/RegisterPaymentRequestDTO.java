package com.rb.api.application.dto.payment;

import com.rb.api.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RegisterPaymentRequestDTO(
        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        PaymentMethod paymentMethod
) {}