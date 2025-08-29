package com.rb.api.domain.enums;

import lombok.Getter;

@Getter
public enum OrderItemStatus {
    PENDING("Pendente"),
    PREPARING("Preparando"),
    READY("Pronto"),
    DELIVERED("Entregue");

    private final String displayName;

    OrderItemStatus(String displayName) {
        this.displayName = displayName;
    }
}
