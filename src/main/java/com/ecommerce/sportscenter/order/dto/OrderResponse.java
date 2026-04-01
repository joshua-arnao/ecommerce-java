package com.ecommerce.sportscenter.order.dto;

import com.ecommerce.sportscenter.order.entity.OrderStatus;
import com.ecommerce.sportscenter.order.entity.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer id;
    private String shoppingCartId;
    private ShippingAddress shippingAddress;
    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

}
