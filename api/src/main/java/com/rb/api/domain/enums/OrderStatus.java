package com.rb.api.domain.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    OPEN("Aberto"),
    CLOSED("Fechada"),
    PAID("Paga"),
    CANCELLED("Cancelada");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
