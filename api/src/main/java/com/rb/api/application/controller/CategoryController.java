package com.rb.api.application.controller;

import com.rb.api.application.dto.category.CategoryResponseDTO;
import com.rb.api.application.dto.category.CreateCategoryRequestDTO;
import com.rb.api.application.dto.category.UpdateCategoryRequestDTO;
import com.rb.api.application.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CreateCategoryRequestDTO dto){
        CategoryResponseDTO category = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    @PreAuthorize("permitAll() or hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<List<CategoryResponseDTO>> findAll(){
        List<CategoryResponseDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll() or hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable UUID id){
        CategoryResponseDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateCategoryRequestDTO dto
    ){
        CategoryResponseDTO category = categoryService.update(id, dto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}