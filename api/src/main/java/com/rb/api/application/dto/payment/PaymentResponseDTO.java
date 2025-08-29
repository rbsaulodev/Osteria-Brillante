package com.rb.api.application.dto.payment;

import com.rb.api.domain.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        LocalDateTime transactionDate
) {}