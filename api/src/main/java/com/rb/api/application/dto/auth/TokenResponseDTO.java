package com.rb.api.application.dto.auth;

import com.rb.api.domain.enums.UserRole;
import java.util.UUID;

public record TokenResponseDTO(
        String token,
        UUID userId,
        String email,
        UserRole role
) {
}