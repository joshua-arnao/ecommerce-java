package com.ecommerce.sportscenter.order.mapper;

import com.ecommerce.sportscenter.order.dto.OrderRequest;
import com.ecommerce.sportscenter.order.dto.OrderResponse;
import com.ecommerce.sportscenter.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "total", expression = "java(order.getSubTotal().add(order.getDeliveryFee()))")
    OrderResponse orderToOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    Order orderRequestToOrder(OrderRequest orderRequest);

    List<OrderRequest> ordersToOrderResponses(List<Order> orders);
}