package com.rb.api.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDTO(
        @Size(min = 3, max = 255)
        String fullName,

        @Email
        String email
) {}