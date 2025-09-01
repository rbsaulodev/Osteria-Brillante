package com.rb.api.domain.model;

import com.rb.api.domain.enums.OrderItemStatus;
import com.rb.api.domain.enums.OrderStatus;
import com.rb.api.domain.enums.PaymentMethod;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<Payment> payments = new ArrayList<>();

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
            throw new IllegalStateException("O pedido precisa estar aberto para adicionar novos itens.");
        }
        OrderItem newItem = new OrderItem(this, menuItem, quantity);
        this.items.add(newItem);
        recalculateTotal();
    }

    public void registerPayment(BigDecimal amount, PaymentMethod method) {
        if (this.status == OrderStatus.PAID) {
            throw new IllegalStateException("Este pedido já está totalmente pago.");
        }
        if (amount.compareTo(getBalance()) > 0) {
            throw new IllegalArgumentException("O valor do pagamento não pode ser maior que o saldo devedor.");
        }

        this.payments.add(new Payment(amount, method));

        if (getBalance().compareTo(BigDecimal.ZERO) == 0) {
            this.status = OrderStatus.PAID;
        }
    }

    public BigDecimal getBalance() {
        BigDecimal totalPaid = this.payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.totalAmount.subtract(totalPaid);
    }

    public void closeOrder() {
        if (this.status != OrderStatus.OPEN) {
            throw new IllegalStateException("Apenas pedidos abertos podem ser fechados.");
        }
        this.status = OrderStatus.CLOSED;
    }

    public void reopen() {
        if (this.status != OrderStatus.CLOSED && this.status != OrderStatus.PAID) {
            throw new IllegalStateException("Apenas pedidos fechados ou pagos podem ser reabertos.");
        }
        this.status = OrderStatus.OPEN;
    }

    public void cancel() {

        if (this.status == OrderStatus.PAID) {
            throw new IllegalStateException("Não é possível cancelar um pedido que já foi pago.");
        }

        boolean hasPreparingItems = this.items.stream()
           .anyMatch(item -> item.getStatus() == OrderItemStatus.PREPARING);

        if (hasPreparingItems) {
            throw new IllegalStateException("Não é possível cancelar o pedido, pois itens já estão sendo preparados.");
        }
        this.status = OrderStatus.CANCELLED;
    }

    private void recalculateTotal() {
        this.totalAmount = this.items.stream()
                .map(item -> item.getPriceAtOrder().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void removeItem(UUID orderItemId) {
        if (this.status != OrderStatus.OPEN) {
            throw new IllegalStateException("Não é possível remover itens de um pedido que não está aberto.");
        }
        this.items.removeIf(item -> item.getId().equals(orderItemId));
        recalculateTotal();
    }

    public void updateItemQuantity(UUID orderItemId, int newQuantity) {
        if (this.status != OrderStatus.OPEN) {
            throw new IllegalStateException("Não é possível alterar a quantidade de itens de um pedido que não está aberto.");
        }
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("A nova quantidade deve ser positiva. Para remover, use o método removeItem.");
        }

        OrderItem itemToUpdate = findItemOrFail(orderItemId);
        itemToUpdate.changeQuantity(newQuantity);
        recalculateTotal();
    }

    private OrderItem findItemOrFail(UUID orderItemId) {
        return this.items.stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no pedido."));
    }

    public void markItemAsPreparing(UUID orderItemId) {
        OrderItem item = findItemOrFail(orderItemId);
        item.markAsPreparing();
    }

    public void markItemAsReady(UUID orderItemId) {
        OrderItem item = findItemOrFail(orderItemId);
        item.markAsReady();
    }

    public void markItemAsDelivered(UUID orderItemId) {
        OrderItem item = findItemOrFail(orderItemId);
        item.markAsDelivered();
    }

    public void addNoteToItem(UUID orderItemId, String note) {
        OrderItem item = findItemOrFail(orderItemId);
        item.addNote(note);
    }
}