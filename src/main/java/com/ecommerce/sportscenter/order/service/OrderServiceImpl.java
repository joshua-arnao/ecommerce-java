package com.ecommerce.sportscenter.order.service;

import com.ecommerce.sportscenter.order.entity.Order;
import com.ecommerce.sportscenter.order.entity.OrderItem;
import com.ecommerce.sportscenter.order.entity.ProductItemOrdered;
import com.ecommerce.sportscenter.order.mapper.OrderMapper;
import com.ecommerce.sportscenter.order.dto.OrderDto;
import com.ecommerce.sportscenter.order.dto.OrderResponse;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartItemResponse;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartResponse;
import com.ecommerce.sportscenter.product.repository.BrandRepository;
import com.ecommerce.sportscenter.order.repository.OrderRepository;
import com.ecommerce.sportscenter.product.repository.TypeRepository;
import com.ecommerce.sportscenter.cart.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, BrandRepository brandRepository, TypeRepository typeRepository, ShoppingCartService shoppingCartService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.brandRepository = brandRepository;
        this.typeRepository = typeRepository;
        this.shoppingCartService = shoppingCartService;
        this.orderMapper = orderMapper;
    }


    @Override
    public OrderResponse getOrderById(Integer orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        return optionalOrder.map(orderMapper::orderToOrderResponse).orElse(null);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(orderMapper::orderToOrderResponse).collect(Collectors.toList());

    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::orderToOrderResponse);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    @Transactional
    @Override
    public Integer createOrder(OrderDto orderDto) {
        ShoppingCartResponse shoppingCartResponse = shoppingCartService.getShoppingCartById(orderDto.getShoppingCartId());

        if(shoppingCartResponse == null) {
            log.error("Shopping Cart with ID {} not found", orderDto.getShoppingCartId());
            return null;
        }

        List<OrderItem> orderItems = shoppingCartResponse.getItems().stream()
                .map(this::mapShoppingCartToOrderItem)
                .collect(Collectors.toList());

        BigDecimal subtotal = shoppingCartResponse.getItems().stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderMapper.orderDtoToOrder(orderDto);
        order.setOrderItems(orderItems);
        order.setSubTotal(subtotal);

        Order savedOrder = orderRepository.save(order);
        shoppingCartService.deleteShoppingCartById(orderDto.getShoppingCartId());

        return savedOrder.getId();
    }

    private OrderItem mapShoppingCartToOrderItem(ShoppingCartItemResponse shoppingCartItemResponse) {
        if(shoppingCartItemResponse!=null){
            OrderItem orderItem = new OrderItem();
            orderItem.setItemOrdered(mapShoppingCartItemToProduct(shoppingCartItemResponse));
            orderItem.setQuantity(shoppingCartItemResponse.getQuantity());
            return orderItem;
        }else{
            return null;
        }
    }

    private ProductItemOrdered mapShoppingCartItemToProduct(ShoppingCartItemResponse shoppingCartItemResponse) {
        ProductItemOrdered productItemOrdered = new ProductItemOrdered();
        productItemOrdered.setName(shoppingCartItemResponse.getName());
        productItemOrdered.setPictureUrl(shoppingCartItemResponse.getPictureUrl());
        productItemOrdered.setProductId(shoppingCartItemResponse.getId());
        return productItemOrdered;
    }

}
