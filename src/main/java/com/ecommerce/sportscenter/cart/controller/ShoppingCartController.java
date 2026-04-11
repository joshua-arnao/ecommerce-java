package com.ecommerce.sportscenter.cart.controller;

import com.ecommerce.sportscenter.cart.dto.ShoppingCartRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.sportscenter.cart.dto.ShoppingCartResponse;
import com.ecommerce.sportscenter.cart.service.ShoppingCartService;


@RestController
@RequestMapping("/api/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<ShoppingCartResponse> createOrUpdateCart(
            @RequestBody ShoppingCartRequest request,
            Authentication authentication) {
        System.out.println("LLEGÓ AL CONTROLLER");
        System.out.println("Authentication: " + authentication);
        String userId = authentication.getName();
        return ResponseEntity.ok(shoppingCartService.createOrUpdateCart(userId, request));
    }

    @GetMapping
    public ResponseEntity<ShoppingCartResponse> getCart(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(shoppingCartService.getShoppingCartById(userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart(Authentication authentication) {
        String userId = authentication.getName();
        shoppingCartService.deleteShoppingCartById(userId);
        return ResponseEntity.noContent().build();
    }
}
