package com.rb.api.application.mapper;

import com.rb.api.application.dto.payment.PaymentResponseDTO;
import com.rb.api.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
    PaymentResponseDTO toResponseDTO(Payment payment);
}