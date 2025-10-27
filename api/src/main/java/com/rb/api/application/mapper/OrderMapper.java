package com.rb.api.application.mapper;

import com.rb.api.application.dto.order.OrderResponseDTO;
import com.rb.api.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "table.tableNumber", target = "tableNumber")
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "waiter.id", target = "waiterId")
    @Mapping(source = "waiter.fullName", target = "waiterName")
    @Mapping(source = "balance", target = "balance")
    @Mapping(target = "totalItems", ignore = true)
    OrderResponseDTO toResponseDTO(Order order);

    List<OrderResponseDTO> toResponseDTOList(List<Order> orders);

    @AfterMapping
    default void calculateTotalItems(Order order, @MappingTarget OrderResponseDTO dto) {
        int total = order.getItems().stream()
                .mapToInt(item -> item.getQuantity())
                .sum();

        dto = new OrderResponseDTO(
                dto.id(), dto.tableId(), dto.tableNumber(), dto.waiterId(), dto.waiterName(),
                dto.status(), dto.totalAmount(), dto.balance(), dto.createdAt(), dto.updatedAt(),
                dto.items(), total
        );
    }
}