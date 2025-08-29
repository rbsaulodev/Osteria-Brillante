package com.rb.api.domain.enums;

import lombok.Getter;

@Getter
public enum TableStatus {
    RESERVED("Reservada"),
    AVAILABLE("Disponível"),
    OCCUPIED("Ocupada");

    private final String displayName;

    TableStatus(String displayName) {
        this.displayName = displayName;
    }
}
