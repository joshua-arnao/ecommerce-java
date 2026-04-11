package com.ecommerce.sportscenter.cart.dto;

import java.util.List;

public record ShoppingCartRequest(
        List<ShoppingCartItemResponse> items
) {
}
