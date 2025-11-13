package com.rb.api.application.controller;

import com.rb.api.application.dto.table.CreateRestaurantTableRequestDTO;
import com.rb.api.application.dto.table.RestaurantTableResponseDTO;
import com.rb.api.application.dto.table.UpdateRestaurantTableRequestDTO;
import com.rb.api.application.service.RestaurantTableService;
import com.rb.api.domain.enums.TableStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tables")
public class TableController {
    private final RestaurantTableService tableService;

    public TableController(RestaurantTableService tableService) {
        this.tableService = tableService;
    }
    
    @PatchMapping("/{id}/occupy")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<RestaurantTableResponseDTO> occupyTable(@PathVariable UUID id){
        RestaurantTableResponseDTO table = tableService.occupyTable(id);
        return ResponseEntity.ok(table);
    }

    @PatchMapping("/{id}/release")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<RestaurantTableResponseDTO> releaseTable(@PathVariable UUID id){
        RestaurantTableResponseDTO table = tableService.releaseTable(id);
        return ResponseEntity.ok(table);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantTableResponseDTO> create(
            @RequestBody @Valid CreateRestaurantTableRequestDTO dto
    ){
        RestaurantTableResponseDTO table = tableService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(table);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantTableResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateRestaurantTableRequestDTO dto
    ){
        RestaurantTableResponseDTO table = tableService.update(id, dto);
        return ResponseEntity.ok(table);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        tableService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RestaurantTableResponseDTO>> findAll(
            @RequestParam(required = false) TableStatus status
    ){
        List<RestaurantTableResponseDTO> tables = tableService.findAll(status);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTableResponseDTO> findById(@PathVariable UUID id){
        RestaurantTableResponseDTO table = tableService.findById(id);
        return ResponseEntity.ok(table);
    }
}