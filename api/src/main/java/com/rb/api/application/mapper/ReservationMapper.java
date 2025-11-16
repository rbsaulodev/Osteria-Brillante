package com.rb.api.application.mapper;

import com.rb.api.application.dto.reservation.ReservationResponseDTO;
import com.rb.api.domain.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "table.name", target = "tableName")
    ReservationResponseDTO toResponseDTO(Reservation reservation);

    List<ReservationResponseDTO> toResponseDTOList(List<Reservation> entities);
}
