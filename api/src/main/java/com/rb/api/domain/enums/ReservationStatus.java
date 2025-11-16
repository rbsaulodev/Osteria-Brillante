package com.rb.api.domain.enums;

public enum ReservationStatus {
    PENDING("Pendente"),
    CONFIRMED("Confirmada"),
    CANCELED("Cancelada"),
    COMPLETED("Concluída");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }
}
