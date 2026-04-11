package com.ecommerce.sportscenter.cart.service;

import com.ecommerce.sportscenter.cart.dto.ShoppingCartRequest;
import com.ecommerce.sportscenter.cart.entity.ShoppingCart;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartResponse;

public interface ShoppingCartService {
    ShoppingCartResponse getShoppingCartById(String shoppingId);
    void deleteShoppingCartById(String shoppingId);
    ShoppingCartResponse createOrUpdateCart(String userId, ShoppingCartRequest shoppingCartRequest);
}
