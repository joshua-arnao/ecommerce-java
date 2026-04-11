package com.ecommerce.sportscenter.order.dto;

import com.ecommerce.sportscenter.order.entity.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private ShippingAddress shippingAddress;
    private BigDecimal deliveryFee;
}
