package com.rb.api.application.mapper;

import com.rb.api.application.dto.payment.PaymentResponseDTO;
import com.rb.api.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDTO toResponseDTO(Payment payment);

    List<PaymentResponseDTO> toResponseDTOList(List<Payment> payments);
}