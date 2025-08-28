package com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    OPEN("Aberto"),
    CLOSED("Fechada"),
    PAID("Paga");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
