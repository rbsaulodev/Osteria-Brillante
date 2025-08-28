package com.rb.api.domain.model;

import lombok.Getter;

@Getter
public enum OrderItemStatus {
    PENDING("Pendente"),
    PREPARING("Preparando"),
    READY("Pronto"),
    DELIVERD("Entregue");

    private final String displayName;

    OrderItemStatus(String displayName) {
        this.displayName = displayName;
    }
}
