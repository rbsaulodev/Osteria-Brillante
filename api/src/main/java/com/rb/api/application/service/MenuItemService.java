package com.rb.api.application.service;

import com.rb.api.application.dto.menuitem.*;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.mapper.MenuItemMapper;
import com.rb.api.domain.model.Category;
import com.rb.api.domain.model.MenuItem;
import com.rb.api.domain.repository.CategoryRepository;
import com.rb.api.domain.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemMapper menuItemMapper;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository,
                           CategoryRepository categoryRepository,
                           MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Transactional
    public MenuItemResponseDTO create(CreateMenuItemRequestDTO dto) {
        Category category = findCategoryEntityById(dto.categoryId());

        MenuItem newItem = new MenuItem(
                dto.name(),
                dto.description(),
                dto.price(),
                category
        );

        MenuItem savedItem = menuItemRepository.save(newItem);
        return menuItemMapper.toResponseDTO(savedItem);
    }

    @Transactional
    public MenuItemResponseDTO updateDetails(UUID id, UpdateMenuItemDetailsDTO dto) {
        MenuItem menuItem = findMenuItemEntityById(id);

        Category newCategory = findCategoryEntityById(dto.categoryId());

        menuItem.updateDetails(dto.name(), dto.description(), newCategory);

        return menuItemMapper.toResponseDTO(menuItem);
    }

    @Transactional
    public MenuItemResponseDTO updatePrice(UUID id, UpdateMenuItemPriceDTO dto) {
        MenuItem menuItem = findMenuItemEntityById(id);
        menuItem.updatePrice(dto.price());
        return menuItemMapper.toResponseDTO(menuItem);
    }

    @Transactional
    public MenuItemResponseDTO setAvailability(UUID id, UpdateMenuItemAvailabilityDTO dto) {
        MenuItem menuItem = findMenuItemEntityById(id);

        if (dto.isAvailable()) {
            menuItem.makeAvailable();
        } else {
            menuItem.makeUnavailable();
        }

        return menuItemMapper.toResponseDTO(menuItem);
    }

    @Transactional(readOnly = true)
    public MenuItemResponseDTO findById(UUID id) {
        return menuItemRepository.findById(id)
                .map(menuItemMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> findAll() {
        List<MenuItem> items = menuItemRepository.findAll();
        return menuItemMapper.toResponseDTOList(items);
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> findAllAvailable() {
        List<MenuItem> items = menuItemRepository.findByIsAvailableTrue();
        return menuItemMapper.toResponseDTOList(items);
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDTO> findAllByCategoryId(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Categoria não encontrada com o ID: " + categoryId);
        }
        List<MenuItem> items = menuItemRepository.findByCategoryId(categoryId);
        return menuItemMapper.toResponseDTOList(items);
    }

    @Transactional
    public void deleteById(UUID id) {
        MenuItem menuItem = findMenuItemEntityById(id);
        menuItemRepository.delete(menuItem);
    }

    private MenuItem findMenuItemEntityById(UUID id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado com o ID: " + id));
    }

    private Category findCategoryEntityById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }
}