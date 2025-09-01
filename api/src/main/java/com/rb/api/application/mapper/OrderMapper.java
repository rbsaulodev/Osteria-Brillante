package com.rb.api.application.mapper;

import com.rb.api.application.dto.order.OrderResponseDTO;
import com.rb.api.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { OrderItemMapper.class, PaymentMapper.class }
)
public interface OrderMapper {
    @Mapping(source = "table.tableNumber", target = "tableNumber")
    @Mapping(source = "waiter.fullName", target = "waiterName")
    @Mapping(source = "balance", target = "balanceDue")
    OrderResponseDTO toResponseDTO(Order order);
}