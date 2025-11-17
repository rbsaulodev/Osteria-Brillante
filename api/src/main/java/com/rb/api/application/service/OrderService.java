package com.rb.api.application.service;

import com.rb.api.application.dto.order.*;
import com.rb.api.application.exception.ResourceNotFoundException;
import com.rb.api.application.exception.TableAlreadyInUseException;
import com.rb.api.application.mapper.OrderMapper;
import com.rb.api.domain.enums.OrderStatus;
import com.rb.api.domain.model.MenuItem;
import com.rb.api.domain.model.Order;
import com.rb.api.domain.model.RestaurantTable;
import com.rb.api.domain.model.User;
import com.rb.api.domain.repository.MenuItemRepository;
import com.rb.api.domain.repository.OrderRepository;
import com.rb.api.domain.repository.RestaurantTableRepository;
import com.rb.api.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        RestaurantTableRepository restaurantTableRepository,
                        MenuItemRepository menuItemRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantTableRepository = restaurantTableRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponseDTO create(CreateOrderRequestDTO dto) {
        User waiter = findUserEntityById(dto.waiterId());
        RestaurantTable table = findTableEntityById(dto.tableId());

        orderRepository.findByTableIdAndStatus(dto.tableId(), OrderStatus.OPEN).ifPresent(order -> {
            throw new TableAlreadyInUseException("A mesa " + table.getTableNumber() + " já possui um pedido aberto.");
        });

        Order newOrder = new Order(table, waiter);
        Order savedOrder = orderRepository.save(newOrder);
        return orderMapper.toResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO addItem(UUID orderId, AddOrderItemRequestDTO dto) {
        Order order = findOrderEntityById(orderId);
        MenuItem menuItem = findMenuItemEntityById(dto.menuItemId());

        order.addItem(menuItem, dto.quantity());
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO removeItem(UUID orderId, UUID orderItemId) {
        Order order = findOrderEntityById(orderId);
        order.removeItem(orderItemId);
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO updateItemQuantity(UUID orderId, UUID orderItemId, UpdateOrderItemQuantityDTO dto) {
        Order order = findOrderEntityById(orderId);
        order.updateItemQuantity(orderItemId, dto.newQuantity());
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO addNoteToItem(UUID orderId, UUID orderItemId, AddOrderItemNoteDTO dto) {
        Order order = findOrderEntityById(orderId);
        order.addNoteToItem(orderItemId, dto.note());
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO registerPayment(UUID orderId, RegisterPaymentRequestDTO dto) {
        Order order = findOrderEntityById(orderId);
        order.registerPayment(dto.amount(), dto.method());
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO closeOrder(UUID orderId) {
        Order order = findOrderEntityById(orderId);
        order.closeOrder();
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO reopenOrder(UUID orderId) {
        Order order = findOrderEntityById(orderId);
        order.reopen();
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO markItemAsPreparing(UUID orderId, UUID orderItemId) {
        Order order = findOrderEntityById(orderId);
        order.markItemAsPreparing(orderItemId);
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO markItemAsReady(UUID orderId, UUID orderItemId) {
        Order order = findOrderEntityById(orderId);
        order.markItemAsReady(orderItemId);
        return orderMapper.toResponseDTO(order);
    }

    @Transactional
    public OrderResponseDTO markItemAsDelivered(UUID orderId, UUID orderItemId) {
        Order order = findOrderEntityById(orderId);
        order.markItemAsDelivered(orderItemId);
        return orderMapper.toResponseDTO(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO findOrderById(UUID orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + orderId));
        return orderMapper.toResponseDTO(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findAllOpenOrders() {
        List<Order> openOrders = orderRepository.findByStatus(OrderStatus.OPEN);
        return orderMapper.toResponseDTOList(openOrders);
    }

    private Order findOrderEntityById(UUID id) {
        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + id));
    }

    private User findUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (garçom) não encontrado com o ID: " + id));
    }

    private RestaurantTable findTableEntityById(UUID id) {
        return restaurantTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com o ID: " + id));
    }

    private MenuItem findMenuItemEntityById(UUID id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado com o ID: " + id));
    }
}