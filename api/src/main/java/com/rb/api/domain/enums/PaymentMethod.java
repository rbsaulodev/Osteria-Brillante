package com.rb.api.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    PIX("Pix"),
    CASH("Dinheiro"),
    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}