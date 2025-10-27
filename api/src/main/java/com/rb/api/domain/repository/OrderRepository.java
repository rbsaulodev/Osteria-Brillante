package com.rb.api.domain.repository;

import com.rb.api.domain.enums.OrderStatus;
import com.rb.api.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByTableIdAndStatus(UUID tableId, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.menuItem WHERE o.id = :id")
    Optional<Order> findByIdWithItems(UUID id);
}