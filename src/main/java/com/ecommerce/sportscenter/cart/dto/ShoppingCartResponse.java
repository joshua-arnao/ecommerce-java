package com.ecommerce.sportscenter.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartResponse {
    private String shoppingCartId;
    private List<ShoppingCartItemResponse> items;
}
