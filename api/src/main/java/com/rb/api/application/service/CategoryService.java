package com.rb.api.application.service;

import com.rb.api.application.dto.category.CategoryResponseDTO;
import com.rb.api.application.dto.category.CreateCategoryRequestDTO;
import com.rb.api.application.dto.category.UpdateCategoryRequestDTO;
import com.rb.api.application.exception.BusinessException;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.mapper.CategoryMapper;
import com.rb.api.domain.model.Category;
import com.rb.api.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryResponseDTO create(CreateCategoryRequestDTO dto) {
        categoryRepository.findByNameIgnoreCase(dto.name())
                .ifPresent(c -> {
                    throw new BusinessException("Já existe uma categoria cadastrada com o nome: " + dto.name());
                });

        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this.categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(UUID id) {
        return categoryRepository.findById(id)
                .map(this.categoryMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    @Transactional
    public CategoryResponseDTO update(UUID id, UpdateCategoryRequestDTO dto){
        Category categoryToUpdate = findEntityById(id);
        categoryToUpdate.updateDetails(dto.name(), dto.description());
        return categoryMapper.toResponseDTO(categoryToUpdate);
    }

    @Transactional
    public void deleteById(UUID id){
        Category categoryToDelete = findEntityById(id);
        categoryRepository.delete(categoryToDelete);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Category findEntityById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }
}