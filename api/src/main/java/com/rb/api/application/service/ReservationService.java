package com.rb.api.application.service;

import com.rb.api.application.dto.reservation.CreateReservationsRequestDTO;
import com.rb.api.application.dto.reservation.ReservationResponseDTO;
import com.rb.api.application.dto.reservation.UpdateReservationRequestDTO;
import com.rb.api.application.mapper.ReservationMapper;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.exception.TableAlreadyInUseException;
import com.rb.api.domain.enums.ReservationStatus;
import com.rb.api.domain.model.Reservation;
import com.rb.api.domain.model.RestaurantTable;
import com.rb.api.domain.model.User;
import com.rb.api.domain.repository.ReservationRepository;
import com.rb.api.domain.repository.RestaurantTableRepository;
import com.rb.api.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RestaurantTableRepository tableRepository;
    private final ReservationMapper mapper;
    private final SecurityService securityService;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            RestaurantTableRepository tableRepository,
            ReservationMapper mapper, SecurityService securityService) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.tableRepository = tableRepository;
        this.mapper = mapper;
        this.securityService = securityService;
    }

    @Transactional
    public ReservationResponseDTO createReservation(CreateReservationsRequestDTO dto) {

        User customer = userRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", dto.customerId()));

        RestaurantTable table = tableRepository.findById(dto.tableId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", dto.tableId()));

        LocalDateTime requestedTime = dto.reservationTime();
        if (!reservationRepository.findOverlappingReservations(table.getId(), requestedTime).isEmpty()) {
            throw new TableAlreadyInUseException("A mesa está ocupada no horário solicitado (" + requestedTime + ").");
        }

        Reservation reservation = Reservation.of(
                customer,
                table,
                requestedTime,
                dto.partySize()
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return mapper.toResponseDTO(savedReservation);
    }

    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        return mapper.toResponseDTO(reservation);
    }

    @Transactional
    public ReservationResponseDTO confirmReservation(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        reservation.makeConfirmed();

        Reservation updatedReservation = reservationRepository.save(reservation);

        return mapper.toResponseDTO(updatedReservation);
    }

    @Transactional
    public ReservationResponseDTO cancelReservation(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        reservation.makeCanceled();

        Reservation updatedReservation = reservationRepository.save(reservation);

        return mapper.toResponseDTO(updatedReservation);
    }

    @Transactional
    public ReservationResponseDTO completeReservation(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        reservation.markCompleted();

        Reservation updatedReservation = reservationRepository.save(reservation);

        return mapper.toResponseDTO(updatedReservation);
    }

    @Transactional
    public ReservationResponseDTO updateReservation(UUID id, UpdateReservationRequestDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        if (reservation.getStatus() != ReservationStatus.PENDING && reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Só é possível atualizar reservas PENDENTES ou CONFIRMADAS.");
        }

        reservation.updateDetails(dto.reservationTime(), dto.partySize());

        return mapper.toResponseDTO(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll(ReservationStatus status) {
        List<Reservation> reservations;

        if (securityService.hasRole("ADMIN") || securityService.hasRole("WAITER")) {
            reservations = findAllByStatusAndRole(status);
        } else if (securityService.hasRole("CUSTOMER")) {
            UUID customerId = securityService.getCurrentUserId();
            reservations = findAllByCustomer(customerId, status);
        } else {
            return Collections.emptyList();
        }

        return mapper.toResponseDTOList(reservations);
    }

    public List<Reservation> findAllByStatusAndRole(ReservationStatus status) {
        if (status != null) {
            return reservationRepository.findByStatus(status);
        }
        return reservationRepository.findAll();
    }

    public List<Reservation> findAllByCustomer(UUID customerId, ReservationStatus status) {
        if (status != null) {
            return reservationRepository.findByCustomerIdAndStatus(customerId, status);
        }
        return reservationRepository.findByCustomerId(customerId);
    }
}