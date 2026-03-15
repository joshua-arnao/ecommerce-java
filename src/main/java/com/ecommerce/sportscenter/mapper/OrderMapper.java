package com.ecommerce.sportscenter.mapper;

import com.ecommerce.sportscenter.entity.OrderAggregate.Order;
import com.ecommerce.sportscenter.model.OrderDto;
import com.ecommerce.sportscenter.model.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "shoppingCartId", target = "shoppingCartId")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    @Mapping(source = "subTotal", target = "subTotal")
    @Mapping(source = "deliveryFee", target = "deliveryFee")
    @Mapping(target = "total", expression = "java(order.getSubTotal() + order.getDeliveryFee())")
    @Mapping(target = "orderStatus", constant = "PENDING")
    OrderResponse OrderToOrderResponse(Order order);

    @Mapping(target = "orderDate", expression = "java(orderDto.getOrderDate())")
    @Mapping(target = "orderStatus", constant = "PENDING") // Reference enum constant directly
    Order orderResponseToOrder(OrderDto orderDto);

    List<OrderDto> ordersToOrderResponses(List<Order> orders);

    void updateOrderFromOrderResponse(OrderDto orderDto, @MappingTarget Order order);
}