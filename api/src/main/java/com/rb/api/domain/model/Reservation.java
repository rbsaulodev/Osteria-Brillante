package com.rb.api.domain.model;

import com.rb.api.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reservations")
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    private int partySize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Reservation(User customer, RestaurantTable table, LocalDateTime reservationTime, int partySize) {
        this.customer = customer;
        this.table = table;
        this.reservationTime = reservationTime;
        this.partySize = partySize;
        this.status = ReservationStatus.PENDING;
    }

    public static Reservation of(User customer, RestaurantTable table, LocalDateTime reservationTime, int partySize) {
        if (partySize <= 0) {
            throw new IllegalArgumentException("O tamanho da festa deve ser maior que zero.");
        }
        if (reservationTime == null || reservationTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data da reserva deve ser futura.");
        }

        return new Reservation(customer, table, reservationTime, partySize);
    }

    public void updateDetails(LocalDateTime newReservationTime, int newPartySize) {
        if (newPartySize <= 0) {
            throw new IllegalArgumentException("O novo tamanho da festa deve ser maior que zero.");
        }
        if (newReservationTime == null || newReservationTime.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("A nova data da reserva deve ser pelo menos 1 hora futura.");
        }

        if (this.status != ReservationStatus.PENDING && this.status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Só é possível alterar detalhes de reservas PENDENTES ou CONFIRMADAS.");
        }

        this.reservationTime = newReservationTime;
        this.partySize = newPartySize;
    }

    public void makeConfirmed(){
        if(this.status != ReservationStatus.PENDING && this.status != ReservationStatus.CANCELED){
            throw new IllegalStateException("A reserva não pode ser marcada como confirmada neste status.");
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void makeCanceled(){
        if(this.status != ReservationStatus.COMPLETED && this.status != ReservationStatus.CANCELED){
            throw new IllegalStateException("A reserva não pode ser marcada como cancelada neste status.");
        }
        this.status = ReservationStatus.CANCELED;
    }

    public void markCompleted() {
        if (this.status != ReservationStatus.CONFIRMED && this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("A reserva não pode ser marcada como concluída neste status.");
        }
        this.status = ReservationStatus.COMPLETED;
    }
}