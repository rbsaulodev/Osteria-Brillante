package com.rb.api.application.dto.table;

import com.rb.api.domain.enums.TableStatus;
import jakarta.validation.constraints.Positive;

public record UpdateRestaurantTableRequestDTO(
        @Positive(message = "O número da mesa deve ser positivo.")
        Integer tableNumber
) {}