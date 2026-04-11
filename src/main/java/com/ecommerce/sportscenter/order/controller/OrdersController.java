package com.ecommerce.sportscenter.order.controller;

import com.ecommerce.sportscenter.order.dto.OrderRequest;
import com.ecommerce.sportscenter.order.dto.OrderResponse;
import com.ecommerce.sportscenter.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer orderId) {
        OrderResponse order = orderService.getOrderById(orderId);

        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getAllOrders(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            Authentication authentication) {
        String userId = authentication.getName();
        Integer orderId = orderService.createOrder(userId, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
