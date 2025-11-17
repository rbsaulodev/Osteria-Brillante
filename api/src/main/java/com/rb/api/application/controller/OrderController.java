package com.rb.api.application.controller;

import com.rb.api.application.dto.order.*;
import com.rb.api.application.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'CUSTOMER')")
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid CreateOrderRequestDTO dto){
        OrderResponseDTO order = orderService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> addItem(
            @PathVariable UUID orderId,
            @RequestBody @Valid AddOrderItemRequestDTO dto
    ){
        OrderResponseDTO order = orderService.addItem(orderId, dto);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> removeItem(
            @PathVariable UUID orderId,
            @PathVariable UUID orderItemId
    ){
        OrderResponseDTO order = orderService.removeItem(orderId, orderItemId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/items/{orderItemId}/quantity")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> updateItemQuantity(
            @PathVariable UUID orderId,
            @PathVariable UUID orderItemId,
            @RequestBody @Valid UpdateOrderItemQuantityDTO dto
    ){
        OrderResponseDTO order = orderService.updateItemQuantity(orderId, orderItemId, dto);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/items/{orderItemId}/note")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> addNoteToItem(
            @PathVariable UUID orderId,
            @PathVariable UUID orderItemId,
            @RequestBody @Valid AddOrderItemNoteDTO dto
    ){
        OrderResponseDTO order = orderService.addNoteToItem(orderId, orderItemId, dto);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> closeOrder(@PathVariable UUID orderId){
        OrderResponseDTO order = orderService.closeOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/reopen")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> reopenOrder(@PathVariable UUID orderId){
        OrderResponseDTO order = orderService.reopenOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> registerPayment(
            @PathVariable UUID orderId,
            @RequestBody @Valid RegisterPaymentRequestDTO dto
    ){
        OrderResponseDTO order = orderService.registerPayment(orderId, dto);
        return ResponseEntity.ok(order);
    }


    @PatchMapping("/{orderId}/items/{orderItemId}/preparing")
    @PreAuthorize("hasAnyRole('ADMIN', 'COOK')")
    public ResponseEntity<OrderResponseDTO> markItemAsPreparing(
            @PathVariable UUID orderId,
            @PathVariable UUID orderItemId
    ){
        OrderResponseDTO order = orderService.markItemAsPreparing(orderId, orderItemId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/items/{orderItemId}/ready")
    @PreAuthorize("hasAnyRole('ADMIN', 'COOK')")
    public ResponseEntity<OrderResponseDTO> markItemAsReady(
            @PathVariable UUID orderId,
            @PathVariable UUID orderItemId
    ){
        OrderResponseDTO order = orderService.markItemAsReady(orderId, orderItemId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{orderId}/items/{orderItemId}/delivered")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<OrderResponseDTO> markItemAsDelivered(
            @PathVariable UUID orderId,
            @PathVariable UUID orderItemId
    )
    {
        OrderResponseDTO order = orderService.markItemAsDelivered(orderId, orderItemId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/open")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER', 'COOK')")
    public ResponseEntity<List<OrderResponseDTO>> findAllOpenOrders(){
        List<OrderResponseDTO> orders = orderService.findAllOpenOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER') or @securityService.isOrderOwner(#orderId)")
    public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable UUID orderId){
        OrderResponseDTO order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}