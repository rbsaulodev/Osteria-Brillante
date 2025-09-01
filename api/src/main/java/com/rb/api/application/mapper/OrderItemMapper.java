package com.rb.api.application.mapper;

import com.rb.api.application.dto.order.OrderItemResponseDTO;
import com.rb.api.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    @Mapping(source = "menuItem.id", target = "menuItemId")
    @Mapping(source = "menuItem.name", target = "name")
    @Mapping(source = "orderItem", target = "totalPrice")
    OrderItemResponseDTO toResponseDTO(OrderItem orderItem);

    default BigDecimal calculateTotalPrice(OrderItem orderItem) {
        if (orderItem == null || orderItem.getPriceAtOrder() == null) {
            return BigDecimal.ZERO;
        }
        return orderItem.getPriceAtOrder().multiply(new BigDecimal(orderItem.getQuantity()));
    }
}