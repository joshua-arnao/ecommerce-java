package com.ecommerce.sportscenter.order.service;

import com.ecommerce.sportscenter.order.entity.Order;
import com.ecommerce.sportscenter.order.entity.OrderItem;
import com.ecommerce.sportscenter.order.entity.ProductItemOrdered;
import com.ecommerce.sportscenter.order.mapper.OrderMapper;
import com.ecommerce.sportscenter.order.dto.OrderRequest;
import com.ecommerce.sportscenter.order.dto.OrderResponse;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartItemResponse;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartResponse;
import com.ecommerce.sportscenter.order.repository.OrderRepository;
import com.ecommerce.sportscenter.cart.service.ShoppingCartService;
import com.ecommerce.sportscenter.shared.exceptions.ProductNotFoundException;
import com.ecommerce.sportscenter.shared.exceptions.ShoppingCartNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;


    @Override
    public OrderResponse getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::orderToOrderResponse)
                .orElseThrow(() -> new ProductNotFoundException("Order not found"));
    }

    @Override
    public List<OrderResponse> getAllOrders(String userId) {
        return orderRepository.findByShoppingCartId(userId)
                .stream()
                .map(orderMapper::orderToOrderResponse)
                .toList();
    }

    @Override
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    @Transactional
    @Override
    public Integer createOrder(String userId, OrderRequest orderRequest) {
        ShoppingCartResponse cart = shoppingCartService.getShoppingCartById(userId);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for user: " + userId);
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::mapShoppingCartToOrderItem)
                .toList();

        BigDecimal subtotal = cart.getItems().stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderMapper.orderRequestToOrder(orderRequest);

        order.setShoppingCartId(userId);
        order.setOrderItems(orderItems);
        order.setSubTotal(subtotal);

        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        shoppingCartService.deleteShoppingCartById(userId);

        return savedOrder.getId();
    }

    private OrderItem mapShoppingCartToOrderItem(ShoppingCartItemResponse item) {
        if (item == null) return null;
        OrderItem orderItem = new OrderItem();
        orderItem.setItemOrdered(mapShoppingCartItemToProduct(item));
        orderItem.setQuantity(item.getQuantity());
        orderItem.setPrice(item.getPrice());
        return orderItem;
    }

    private ProductItemOrdered mapShoppingCartItemToProduct(ShoppingCartItemResponse shoppingCartItemResponse) {
        ProductItemOrdered productItemOrdered = new ProductItemOrdered();
        productItemOrdered.setName(shoppingCartItemResponse.getName());
        productItemOrdered.setPictureUrl(shoppingCartItemResponse.getPictureUrl());
        productItemOrdered.setProductId(shoppingCartItemResponse.getShoppingCartItemId());
        return productItemOrdered;
    }
}
