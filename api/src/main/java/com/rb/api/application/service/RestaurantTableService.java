package com.rb.api.application.service;

import com.rb.api.application.dto.table.CreateRestaurantTableRequestDTO;
import com.rb.api.application.dto.table.RestaurantTableResponseDTO;
import com.rb.api.application.dto.table.UpdateRestaurantTableRequestDTO;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.mapper.RestaurantTableMapper;
import com.rb.api.domain.model.RestaurantTable;
import com.rb.api.domain.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantTableMapper restaurantTableMapper;

    @Autowired
    public RestaurantTableService(RestaurantTableRepository restaurantTableRepository, RestaurantTableMapper restaurantTableMapper) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.restaurantTableMapper = restaurantTableMapper;
    }

    @Transactional
    public RestaurantTableResponseDTO occupyTable(UUID id) {
        RestaurantTable table = findEntityById(id);
        table.occupy();
        return restaurantTableMapper.toResponseDTO(table);
    }

    @Transactional
    public RestaurantTableResponseDTO releaseTable(UUID id) {
        RestaurantTable table = findEntityById(id);
        table.release();
        return restaurantTableMapper.toResponseDTO(table);
    }

    @Transactional
    public RestaurantTableResponseDTO reserveTable(UUID id) {
        RestaurantTable table = findEntityById(id);
        table.reserve();
        return restaurantTableMapper.toResponseDTO(table);
    }

    @Transactional
    public RestaurantTableResponseDTO cancelReservationTable(UUID id) {
        RestaurantTable table = findEntityById(id);
        table.cancelReservation();
        return restaurantTableMapper.toResponseDTO(table);
    }

    @Transactional
    public RestaurantTableResponseDTO create(CreateRestaurantTableRequestDTO dto){
        RestaurantTable newTable = restaurantTableMapper.toEntity(dto);
        RestaurantTable savedTable = restaurantTableRepository.save(newTable);
        return restaurantTableMapper.toResponseDTO(savedTable);
    }

    @Transactional(readOnly = true)
    public List<RestaurantTableResponseDTO> findAll(){
        return restaurantTableRepository.findAll()
                .stream()
                .map(this.restaurantTableMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RestaurantTableResponseDTO findById(UUID id){
        return restaurantTableRepository.findById(id)
                .map(this.restaurantTableMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com o ID: " + id));
    }

    @Transactional
    public RestaurantTableResponseDTO update(UUID id, UpdateRestaurantTableRequestDTO dto) {
        RestaurantTable tableToUpdate = findEntityById(id);
        if (dto.tableNumber() != null) {
            tableToUpdate.changeTableNumber(dto.tableNumber());
        }

        return restaurantTableMapper.toResponseDTO(tableToUpdate);
    }

    private RestaurantTable findEntityById(UUID id) {
        return restaurantTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com o ID: " + id));
    }
}
