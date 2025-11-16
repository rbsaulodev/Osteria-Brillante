package com.rb.api.application.dto.reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateReservationRequestDTO(
        @NotNull(message = "O horário da reserva não pode ser nulo.")
        @Future(message = "O horário da reserva deve ser futuro.")
        LocalDateTime reservationTime,

        @NotNull(message = "O tamanho da festa é obrigatório.")
        @Min(value = 1, message = "O tamanho da festa deve ser no mínimo 1.")
        @Max(value = 20, message = "O tamanho máximo da festa é 20.")
        Integer partySize
) {
}