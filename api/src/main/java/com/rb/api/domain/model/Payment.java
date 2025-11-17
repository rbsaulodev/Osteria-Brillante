package com.rb.api.domain.model;

import com.rb.api.domain.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payments")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    private Payment(Order order, BigDecimal amount, PaymentMethod paymentMethod) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("O método de pagamento é obrigatório.");
        }
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public static Payment of(Order order, BigDecimal amount, PaymentMethod paymentMethod) {
        if (order == null) {
            throw new IllegalArgumentException("O pedido (Order) é obrigatório.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("O método de pagamento é obrigatório.");
        }

        return new Payment(order, amount, paymentMethod);
    }
}