package com.rb.api.application.mapper;

import com.rb.api.application.dto.menuitem.MenuItemResponseDTO;
import com.rb.api.domain.model.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "available", target = "available")
    MenuItemResponseDTO toResponseDTO(MenuItem menuItem);

    List<MenuItemResponseDTO> toResponseDTOList(List<MenuItem> menuItems);
}