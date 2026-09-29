package com.enterprise.order_service.service;

import com.enterprise.order_service.dto.OrderRequest;
import com.enterprise.order_service.dto.OrderResponse;
import com.enterprise.order_service.event.OrderPlacedEvent;
import com.enterprise.order_service.model.Order;
import com.enterprise.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .skuCode(request.getSkuCode())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .status("PLACED")
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order {} successfully persisted to database.", savedOrder.getOrderNumber());

        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getOrderNumber(),
                savedOrder.getSkuCode(),
                savedOrder.getQuantity(),
                savedOrder.getPrice()
        );

        kafkaTemplate.send("order-events", savedOrder.getOrderNumber(), event);
        log.info("OrderPlacedEvent emitted to Kafka for order: {}", savedOrder.getOrderNumber());

        return mapToResponse(savedOrder);
    }

    public OrderResponse getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Order not found with order number: " + orderNumber));
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .skuCode(order.getSkuCode())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .build();
    }
}