package com.rb.api.application.mapper;

import com.rb.api.application.dto.category.CreateCategoryRequestDTO;
import com.rb.api.application.dto.category.CategoryResponseDTO;
import com.rb.api.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    Category toEntity(CreateCategoryRequestDTO dto);
    CategoryResponseDTO toResponseDTO(Category category);
}