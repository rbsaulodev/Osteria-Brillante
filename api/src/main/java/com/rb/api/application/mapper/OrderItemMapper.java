package com.rb.api.application.mapper;

import com.rb.api.application.dto.order.OrderItemResponseDTO;
import com.rb.api.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "menuItem.id", target = "menuItemId")
    @Mapping(source = "menuItem.name", target = "menuItemName")
    OrderItemResponseDTO toResponseDTO(OrderItem orderItem);

    List<OrderItemResponseDTO> toResponseDTOList(List<OrderItem> orderItems);
}