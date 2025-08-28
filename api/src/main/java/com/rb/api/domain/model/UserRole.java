package com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("Admin"),
    WAITER("Garçom"),
    COOK("Cozinheiro");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
}
