package com.rb.api.domain.model;

import com.rb.api.domain.enums.OrderItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_items")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Min(1)
    @Column(nullable = false)
    private int quantity;

    @NotNull
    @Column(nullable = false)
    private BigDecimal priceAtOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderItemStatus status;

    private String notes;

    protected OrderItem(Order order, MenuItem menuItem, int quantity) {
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.priceAtOrder = menuItem.getPrice();
        this.status = OrderItemStatus.PENDING;
    }

    public void changeQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser positiva.");
        }
        this.quantity = newQuantity;
    }

    public void markAsPreparing() {
        if (this.status != OrderItemStatus.PENDING) {
            throw new IllegalStateException("O item só pode ser preparado se estiver pendente.");
        }
        this.status = OrderItemStatus.PREPARING;
    }

    public void markAsReady() {
        if (this.status != OrderItemStatus.PREPARING) {
            throw new IllegalStateException("O item só pode ficar pronto se estiver em preparação.");
        }
        this.status = OrderItemStatus.READY;
    }

    public void markAsDelivered() {
        if (this.status != OrderItemStatus.READY) {
            throw new IllegalStateException("O item só pode ser entregue se estiver pronto.");
        }
        this.status = OrderItemStatus.DELIVERED;
    }

    public void addNote(String note) {
        this.notes = note;
    }
}