package com.rb.api.domain.repository;

import com.rb.api.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrderId(UUID orderId);
    List<Payment> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}