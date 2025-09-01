package com.rb.api.application.mapper;

import com.rb.api.application.dto.table.CreateRestaurantTableRequestDTO;
import com.rb.api.application.dto.table.RestaurantTableResponseDTO;
import com.rb.api.application.dto.table.UpdateRestaurantTableRequestDTO;
import com.rb.api.domain.model.RestaurantTable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantTableMapper {
    RestaurantTable toEntity(CreateRestaurantTableRequestDTO dto);
    RestaurantTableResponseDTO toResponseDTO(RestaurantTable table);
    void updateEntityFromDto(UpdateRestaurantTableRequestDTO dto, @MappingTarget RestaurantTable table);
}