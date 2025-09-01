package com.rb.api.application.dto.order;

import com.rb.api.domain.enums.OrderItemStatus;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID id,
        UUID menuItemId,
        String name,
        int quantity,
        BigDecimal priceAtOrder,
        BigDecimal totalPrice,
        OrderItemStatus status,
        String notes
) {}