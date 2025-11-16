package com.rb.api.domain.repository;

import com.rb.api.domain.enums.ReservationStatus;
import com.rb.api.domain.model.Reservation;
import com.rb.api.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.table.id = :tableId AND r.status IN ('PENDING', 'CONFIRMED') AND " +
            "(:requestedTime < r.reservationTime + 1 HOUR) AND " +
            "(:requestedTime + 1 HOUR > r.reservationTime)")

    List<Reservation> findOverlappingReservations(
            @Param("tableId") UUID tableId,
            @Param("requestedTime") LocalDateTime requestedTime);

    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByCustomerId(UUID customerId);
    List<Reservation> findByCustomerIdAndStatus(UUID customerId, ReservationStatus status);
}