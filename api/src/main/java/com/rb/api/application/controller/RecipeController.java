package com.rb.api.application.controller;

import com.rb.api.application.dto.recipe.CreateRecipeRequestDTO;
import com.rb.api.application.dto.recipe.RecipeResponseDTO;
import com.rb.api.application.dto.recipe.UpdateRecipeRequestDTO;
import com.rb.api.application.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
@PreAuthorize("hasAnyRole('ADMIN', 'COOK')")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<RecipeResponseDTO> create(@RequestBody @Valid CreateRecipeRequestDTO dto){
        RecipeResponseDTO recipe = recipeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateRecipeRequestDTO dto
    ){
        RecipeResponseDTO recipe = recipeService.update(id, dto);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> findById(@PathVariable UUID id){
        RecipeResponseDTO recipe = recipeService.findById(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponseDTO>> listAll(){
        List<RecipeResponseDTO> recipes = recipeService.findAll();
        return ResponseEntity.ok(recipes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}