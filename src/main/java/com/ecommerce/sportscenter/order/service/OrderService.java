package com.ecommerce.sportscenter.order.service;

import com.ecommerce.sportscenter.order.dto.OrderRequest;
import com.ecommerce.sportscenter.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse getOrderById(Integer orderId);
    List<OrderResponse> getAllOrders(String userId);
    Integer createOrder(String userId, OrderRequest orderRequest);
    void deleteOrder(Integer orderId);
}
