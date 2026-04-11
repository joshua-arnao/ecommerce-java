package com.ecommerce.sportscenter.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemResponse {
    private Integer shoppingCartItemId;
    private String name;
    private String description;
    private BigDecimal price;
    private String pictureUrl;
    private String productBrand;
    private String productType;
    private Integer quantity;
}
