package com.rb.api.application.dto.order;

import com.rb.api.domain.enums.OrderItemStatus;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID id,
        String name,
        int quantity,
        BigDecimal priceAtOrder,
        OrderItemStatus status
) {}