package com.rb.api.application.dto.user;

import com.rb.api.domain.enums.UserRole;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String fullName,
        String email,
        UserRole role
) {}