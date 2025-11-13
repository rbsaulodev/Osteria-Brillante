package com.rb.api.application.controller;

import com.rb.api.application.dto.menuitem.*;
import com.rb.api.application.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuItemService menuItemService;

    public MenuController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    @PreAuthorize("permitAll() or hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<List<MenuItemResponseDTO>> findAll(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Boolean available,
            @Valid SearchMenuItemDTO searchDto
    ) {
        List<MenuItemResponseDTO> items = menuItemService.searchAndFilter(searchDto, categoryId, available);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll() or hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<MenuItemResponseDTO> findById(@PathVariable UUID id) {
        MenuItemResponseDTO item = menuItemService.findById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponseDTO> create(@RequestBody @Valid CreateMenuItemRequestDTO dto) {
        MenuItemResponseDTO item = menuItemService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponseDTO> updateDetails(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMenuItemDetailsDTO dto
    ) {
        MenuItemResponseDTO item = menuItemService.updateDetails(id, dto);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponseDTO> updatePrice(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMenuItemPriceDTO dto
    ) {
        MenuItemResponseDTO item = menuItemService.updatePrice(id, dto);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COOK')")
    public ResponseEntity<MenuItemResponseDTO> setAvailability(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMenuItemAvailabilityDTO dto
    ) {
        MenuItemResponseDTO item = menuItemService.setAvailability(id, dto);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        menuItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}