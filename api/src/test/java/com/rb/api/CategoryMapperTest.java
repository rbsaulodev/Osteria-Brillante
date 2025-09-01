package com.rb.api;

import com.rb.api.application.dto.category.CreateCategoryRequestDTO;
import com.rb.api.application.dto.category.CategoryResponseDTO;
import com.rb.api.application.dto.category.UpdateCategoryRequestDTO;
import com.rb.api.application.mapper.CategoryMapper;
import com.rb.api.domain.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Deve mapear corretamente um CreateCategoryRequestDTO para uma entidade Category")
    void deveMapearCreateDtoParaEntidade() {
        var dto = new CreateCategoryRequestDTO("Sobremesas", "Doces e bolos");

        Category category = categoryMapper.toEntity(dto);

        assertNotNull(category);
        assertEquals("Sobremesas", category.getName());
        assertEquals("Doces e bolos", category.getDescription());
    }

    @Test
    @DisplayName("Deve mapear corretamente uma entidade Category para um CategoryResponseDTO")
    void deveMapearEntidadeParaResponseDto() {
        // Arrange
        var category = new Category("Bebidas", "Refrigerantes e sucos");

        // Act
        CategoryResponseDTO dto = categoryMapper.toResponseDTO(category);

        // Assert
        assertNotNull(dto);
        assertEquals("Bebidas", dto.name());
        assertEquals("Refrigerantes e sucos", dto.description());
    }

    @Test
    @DisplayName("Deve atualizar uma entidade Category a partir de um UpdateCategoryRequestDTO")
    void deveAtualizarEntidadeComUpdateDto() {
        // Arrange
        var existingCategory = new Category("Entradas", "Petiscos para abrir o apetite");
        var dto = new UpdateCategoryRequestDTO("Entradas Especiais", null);

        // Act
        categoryMapper.updateEntityFromDto(dto, existingCategory);

        // Assert
        assertEquals("Entradas Especiais", existingCategory.getName());
        assertEquals("Petiscos para abrir o apetite", existingCategory.getDescription());
    }
}