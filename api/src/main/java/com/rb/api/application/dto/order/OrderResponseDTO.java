package com.rb.api.application.dto.order;

import com.rb.api.application.dto.payment.PaymentResponseDTO;
import com.rb.api.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        int tableNumber,
        String waiterName,
        OrderStatus status,
        BigDecimal totalAmount,
        BigDecimal balanceDue,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items,
        List<PaymentResponseDTO> payments
) {}