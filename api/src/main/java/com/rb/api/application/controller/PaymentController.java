package com.rb.api.application.controller;

import com.rb.api.application.dto.payment.PaymentResponseDTO;
import com.rb.api.application.dto.payment.RegisterPaymentRequestDTO;
import com.rb.api.application.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<PaymentResponseDTO> registerPayment(
            @PathVariable UUID orderId,
            @RequestBody @Valid RegisterPaymentRequestDTO dto
    ){
        PaymentResponseDTO payment = paymentService.registerPayment(orderId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable UUID id){
        PaymentResponseDTO payment = paymentService.findById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<List<PaymentResponseDTO>> findAllByOrderId(
            @RequestParam UUID orderId
    ){
        List<PaymentResponseDTO> payments = paymentService.findAllByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> findAllByDate(
            @RequestParam LocalDate date
    ){
        List<PaymentResponseDTO> payments = paymentService.findAllByDate(date);
        return ResponseEntity.ok(payments);
    }
}