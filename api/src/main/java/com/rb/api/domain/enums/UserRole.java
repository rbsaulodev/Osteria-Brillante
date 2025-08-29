package com.rb.api.domain.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    CUSTOMER("Cliente"),
    ADMIN("Admin"),
    WAITER("Garçom"),
    COOK("Cozinheiro");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
}
