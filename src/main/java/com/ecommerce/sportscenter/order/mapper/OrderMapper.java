package com.ecommerce.sportscenter.order.mapper;

import com.ecommerce.sportscenter.order.dto.OrderDto;
import com.ecommerce.sportscenter.order.dto.OrderResponse;
import com.ecommerce.sportscenter.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "total", expression = "java(order.getSubTotal().add(order.getDeliveryFee()))")
    OrderResponse orderToOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    Order orderDtoToOrder(OrderDto orderDto);

    List<OrderDto> ordersToOrderResponses(List<Order> orders);
}