package com.rb.api.application.dto.menuitem;

import jakarta.validation.constraints.NotNull;

public record UpdateMenuItemAvailabilityDTO(
        @NotNull
        Boolean isAvailable
) {
}