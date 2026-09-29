package com.enterprise.order_service.controller;

import com.enterprise.order_service.dto.OrderRequest;
import com.enterprise.order_service.dto.OrderResponse;
import com.enterprise.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping("/{orderNumber}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrder(@PathVariable String orderNumber) {
        return orderService.getOrderByNumber(orderNumber);
    }
}