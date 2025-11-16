package com.rb.api.application.controller;

import com.rb.api.application.dto.reservation.CreateReservationsRequestDTO;
import com.rb.api.application.dto.reservation.ReservationResponseDTO;
import com.rb.api.application.dto.reservation.UpdateReservationRequestDTO;
import com.rb.api.application.service.ReservationService;
import com.rb.api.domain.enums.ReservationStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody @Valid CreateReservationsRequestDTO dto){
        ReservationResponseDTO reservation = reservationService.createReservation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') or @securityService.isReservationOwner(#id)")
    public ResponseEntity<ReservationResponseDTO> updateReservation(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateReservationRequestDTO dto
    ) {
        ReservationResponseDTO reservation = reservationService.updateReservation(id, dto);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'CUSTOMER')")
    public ResponseEntity<List<ReservationResponseDTO>> findAll(
            @RequestParam(required = false) ReservationStatus status
    ) {

        List<ReservationResponseDTO> reservations = reservationService.findAll(status);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER') or @securityService.isReservationOwner(#id)")
    public ResponseEntity<ReservationResponseDTO> findById(@PathVariable UUID id){
        ReservationResponseDTO reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ReservationResponseDTO> confirmReservation(@PathVariable UUID id){
        ReservationResponseDTO reservation = reservationService.confirmReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER') or @securityService.isReservationOwner(#id)")
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable UUID id){
        ReservationResponseDTO reservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ReservationResponseDTO> completeReservation(@PathVariable UUID id){
        ReservationResponseDTO reservation = reservationService.completeReservation(id);
        return ResponseEntity.ok(reservation);
    }
}