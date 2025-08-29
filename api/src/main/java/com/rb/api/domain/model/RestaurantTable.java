package com.rb.api.domain;

import com.rb.api.domain.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurant_tables")
@Entity
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private int tableNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status;

    public RestaurantTable(int tableNumber) {
        this.tableNumber = tableNumber;
        this.status = TableStatus.AVAILABLE;
    }

    // --- MÉTODOS DE NEGÓCIO ---

    public void occupy() {
        if (this.status != TableStatus.AVAILABLE && this.status != TableStatus.RESERVED) {
            throw new IllegalStateException("A mesa " + this.tableNumber + " não está disponível para ser ocupada.");
        }
        this.status = TableStatus.OCCUPIED;
    }

    public void release() {
        if (this.status != TableStatus.OCCUPIED) {
            throw new IllegalStateException("A mesa " + this.tableNumber + " não está ocupada para ser liberada.");
        }
        this.status = TableStatus.AVAILABLE;
    }

    public void reserve() {
        if (this.status != TableStatus.AVAILABLE) {
            throw new IllegalStateException("A mesa " + this.tableNumber + " não está disponível para reserva.");
        }
        this.status = TableStatus.RESERVED;
    }

    public void cancelReservation() {
        if (this.status != TableStatus.RESERVED) {
            throw new IllegalStateException("A mesa " + this.tableNumber + " não possui uma reserva para ser cancelada.");
        }
        this.status = TableStatus.AVAILABLE;
    }
}