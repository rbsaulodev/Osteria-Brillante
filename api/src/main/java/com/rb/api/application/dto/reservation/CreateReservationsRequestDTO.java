package com.rb.api.application.dto.reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateReservationsRequestDTO(
        @NotNull(message = "O ID do cliente não pode ser nulo.")
        UUID customerId,

        @NotNull(message = "O ID da mesa não pode ser nulo.")
        UUID tableId,

        @NotNull(message = "O horário da reserva não pode ser nulo.")
        @Future(message = "O horário da reserva deve ser futuro.")
        LocalDateTime reservationTime,

        @Min(value = 1, message = "O tamanho da festa deve ser de pelo menos 1.")
        int partySize
) {
}