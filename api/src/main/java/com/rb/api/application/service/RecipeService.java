package com.rb.api.application.service;

import com.rb.api.application.dto.recipe.CreateRecipeRequestDTO;
import com.rb.api.application.dto.recipe.RecipeResponseDTO;
import com.rb.api.application.dto.recipe.UpdateRecipeRequestDTO;
import com.rb.api.application.exception.RecipeAlreadyExistsException;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.mapper.RecipeMapper;
import com.rb.api.domain.model.MenuItem;
import com.rb.api.domain.model.Recipe;
import com.rb.api.domain.repository.MenuItemRepository;
import com.rb.api.domain.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final MenuItemRepository menuItemRepository;
    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, MenuItemRepository menuItemRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.menuItemRepository = menuItemRepository;
        this.recipeMapper = recipeMapper;
    }

    @Transactional
    public RecipeResponseDTO create(CreateRecipeRequestDTO dto) {
        MenuItem menuItem = menuItemRepository.findById(dto.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem não encontrado com o ID: " + dto.menuItemId()));

        recipeRepository.findByMenuItemId(dto.menuItemId()).ifPresent(recipe -> {
            throw new RecipeAlreadyExistsException("O MenuItem com ID " + dto.menuItemId() + " já possui uma receita.");
        });

        Recipe newRecipe = new Recipe(
                menuItem,
                dto.instructions(),
                dto.prepTimeMinutes()
        );

        Recipe savedRecipe = recipeRepository.save(newRecipe);
        return recipeMapper.toResponseDTO(savedRecipe);
    }

    @Transactional
    public RecipeResponseDTO update(UUID id, UpdateRecipeRequestDTO dto) {
        Recipe recipe = findEntityById(id);

        recipe.updateDetails(dto.instructions(), dto.prepTimeMinutes());

        return recipeMapper.toResponseDTO(recipe);
    }

    @Transactional(readOnly = true)
    public RecipeResponseDTO findById(UUID id) {
        return recipeRepository.findById(id)
                .map(recipeMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDTO> findAll() {
        return recipeRepository.findAll()
                .stream()
                .map(recipeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(UUID id) {
        Recipe recipe = findEntityById(id);
        recipeRepository.delete(recipe);
    }

    private Recipe findEntityById(UUID id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada com o ID: " + id));
    }
}