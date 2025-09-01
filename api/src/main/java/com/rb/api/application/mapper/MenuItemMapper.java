package com.rb.api.application.mapper;

import com.rb.api.application.dto.menu.CreateMenuItemRequestDTO;
import com.rb.api.application.dto.menu.MenuItemResponseDTO;
import com.rb.api.application.dto.menu.UpdateMenuItemRequestDTO;
import com.rb.api.domain.model.MenuItem;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { CategoryMapper.class, RecipeMapper.class }
)
public interface MenuItemMapper {

    @Mapping(target = "category", ignore = true)
    MenuItem toEntity(CreateMenuItemRequestDTO dto);
    MenuItemResponseDTO toResponseDTO(MenuItem menuItem);
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(UpdateMenuItemRequestDTO dto, @MappingTarget MenuItem menuItem);
}