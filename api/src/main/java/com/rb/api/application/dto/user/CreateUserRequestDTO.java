package com.rb.api.application.dto.user;

import com.rb.api.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDTO(
        @NotBlank
        @Size(min = 3, max = 255)
        String fullName,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @NotNull
        UserRole role
) {}