package com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum TableStatus {
    AVAILABLE("Disponível"),
    OCCUPIED("Ocupada");

    private final String displayName;

    TableStatus(String displayName) {
        this.displayName = displayName;
    }
}
