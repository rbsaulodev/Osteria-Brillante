package com.rb.api.application.mapper;

import com.rb.api.application.dto.recipe.CreateRecipeRequestDTO;
import com.rb.api.application.dto.recipe.RecipeResponseDTO;
import com.rb.api.application.dto.recipe.UpdateRecipeRequestDTO;
import com.rb.api.domain.model.MenuItem;
import com.rb.api.domain.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeMapper {

    @Mapping(source = "menuItem.id", target = "menuItemId")
    RecipeResponseDTO toResponseDTO(Recipe recipe);
    Recipe toEntity(CreateRecipeRequestDTO dto, MenuItem menuItem);
    void updateEntityFromDto(UpdateRecipeRequestDTO dto, @MappingTarget Recipe recipe);

}