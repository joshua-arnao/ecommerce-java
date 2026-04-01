package com.ecommerce.sportscenter.cart.service;

import com.ecommerce.sportscenter.cart.entity.ShoppingCart;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartResponse;

import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCartResponse> getAllShoppingCarts();
    ShoppingCartResponse getShoppingCartById(String shoppingId);
    void deleteShoppingCartById(String shoppingId);
    ShoppingCartResponse createShoppingCart(ShoppingCart shoppingCart);

}
