package com.rb.api.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_table_id", nullable = false)
    private RestaurantTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User waiter;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Order(RestaurantTable table, User waiter) {
        this.table = table;
        this.waiter = waiter;
        this.status = OrderStatus.OPEN;
        this.totalAmount = BigDecimal.ZERO;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        if (this.status != OrderStatus.OPEN) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido que não está aberto.");
        }
        OrderItem newItem = new OrderItem(this, menuItem, quantity);
        this.items.add(newItem);
        recalculateTotal();

        if(this.status == OrderStatus.PAID){
            this.status = OrderStatus.OPEN;
        }
    }

    public void closeOrder() {
        if (this.status != OrderStatus.OPEN) {
            throw new IllegalStateException("Apenas pedidos abertos podem ser fechados.");
        }
        this.status = OrderStatus.CLOSED;
    }

    public void markAsPaid() {
        if (this.status != OrderStatus.CLOSED) {
            throw new IllegalStateException("Apenas pedidos fechados podem ser marcados como pagos.");
        }
        this.status = OrderStatus.PAID;
    }

    public void reopen() {
        if (this.status != OrderStatus.CLOSED && this.status != OrderStatus.PAID) {
            throw new IllegalStateException("Apenas pedidos fechados ou pagos podem ser reabertos.");
        }
        this.status = OrderStatus.OPEN;
    }

    private void recalculateTotal() {
        this.totalAmount = this.items.stream()
                .map(item -> item.getPriceAtOrder().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}