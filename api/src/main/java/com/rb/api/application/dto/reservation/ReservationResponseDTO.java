package com.rb.api.application.dto.reservation;

import com.rb.api.domain.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponseDTO(
        UUID id,
        UUID customerId,
        UUID tableId,
        Integer tableNumber,
        LocalDateTime reservationTime,
        int partySize,
        ReservationStatus status,
        LocalDateTime createdAt
) {
}