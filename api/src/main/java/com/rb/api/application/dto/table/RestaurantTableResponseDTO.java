package com.rb.api.application.dto.table;

import com.rb.api.domain.enums.TableStatus;
import java.util.UUID;

public record RestaurantTableResponseDTO(
        UUID id,
        int tableNumber,
        TableStatus status
) {}