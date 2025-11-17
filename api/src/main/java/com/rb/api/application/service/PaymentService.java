package com.rb.api.application.service;

import com.rb.api.application.dto.payment.PaymentResponseDTO;
import com.rb.api.application.dto.payment.RegisterPaymentRequestDTO;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.mapper.PaymentMapper;
import com.rb.api.domain.model.Order;
import com.rb.api.domain.model.Payment;
import com.rb.api.domain.repository.OrderRepository;
import com.rb.api.domain.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentResponseDTO registerPayment(UUID orderId, RegisterPaymentRequestDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", orderId));

        Payment newPayment = Payment.of(
                order,
                dto.amount(),
                dto.method()
        );

        Payment savedPayment = paymentRepository.save(newPayment);
        return paymentMapper.toResponseDTO(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO findById(UUID id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> findAllByOrderId(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("O ID do pedido não pode ser nulo.");
        }
        var payments = paymentRepository.findByOrderId(orderId);
        return paymentMapper.toResponseDTOList(payments);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> findAllByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        var payments = paymentRepository.findByTransactionDateBetween(startOfDay, endOfDay);
        return paymentMapper.toResponseDTOList(payments);
    }
}