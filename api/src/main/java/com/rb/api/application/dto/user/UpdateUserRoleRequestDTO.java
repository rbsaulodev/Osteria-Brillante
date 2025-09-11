package com.rb.api.application.dto.user;

import com.rb.api.domain.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequestDTO(
        @NotNull
        UserRole newRole
) {}