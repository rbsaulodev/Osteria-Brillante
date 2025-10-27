package com.rb.api.application.dto.order;

import com.rb.api.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        UUID tableId,
        Integer tableNumber,
        UUID waiterId,
        String waiterName,
        OrderStatus status,
        BigDecimal totalAmount,
        BigDecimal balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderItemResponseDTO> items,
        int totalItems
) {
}