package com.ecommerce.sportscenter.cart.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.sportscenter.cart.entity.ShoppingCart;
import com.ecommerce.sportscenter.cart.entity.ShoppingCartItem;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartItemResponse;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartResponse;
import com.ecommerce.sportscenter.cart.service.ShoppingCartService;


@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public List<ShoppingCartResponse> getAllShoppingCarts(){
        return  shoppingCartService.getAllShoppingCarts();
    }

    @GetMapping("/{shoppingId}")
    public ShoppingCartResponse getShoppingCartById(@PathVariable String shoppingId){
        return shoppingCartService.getShoppingCartById(shoppingId);
    }

    @DeleteMapping("/{shoppingId}")
    public void deleteShoppingCartById(@PathVariable String shoppingId){
        shoppingCartService.deleteShoppingCartById(shoppingId);
    }

    @PostMapping
    public ResponseEntity<ShoppingCartResponse> createShopping(@RequestBody ShoppingCartResponse shoppingCartResponse) {
        System.out.println("shoppingCartResponse: " + shoppingCartResponse);
        ShoppingCart shoppingCart = convertToShoppingCart(shoppingCartResponse);
        System.out.println("shoppingCart" + shoppingCart);
        ShoppingCartResponse createShoppingCart = shoppingCartService.createShoppingCart(shoppingCart);
        System.out.println("createShoppingCart" + createShoppingCart);
        return new ResponseEntity<>(createShoppingCart, HttpStatus.CREATED);
    }

    private ShoppingCart convertToShoppingCart(ShoppingCartResponse shoppingCartResponse) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartResponse.getId());
        shoppingCart.setItems(mapShoppingCartItemResponsesToEntities(shoppingCartResponse.getItems()));
        return shoppingCart;
    }

    private List<ShoppingCartItem> mapShoppingCartItemResponsesToEntities(List<ShoppingCartItemResponse> itemResponses) {
        return itemResponses.stream()
                .map(this::convertToShoppingCartEntity)
                .collect(Collectors.toList());
    }

    private ShoppingCartItem convertToShoppingCartEntity(ShoppingCartItemResponse itemResponse) {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setId(itemResponse.getId());
        shoppingCartItem.setName(itemResponse.getName());
        shoppingCartItem.setDescription(itemResponse.getDescription());
        shoppingCartItem.setPrice(itemResponse.getPrice());
        shoppingCartItem.setPictureUrl(itemResponse.getPictureUrl());
        shoppingCartItem.setProductBrand(itemResponse.getProductBrand());
        shoppingCartItem.setProductType(itemResponse.getProductType());
        shoppingCartItem.setQuantity(itemResponse.getQuantity());
        return shoppingCartItem;
    }

}
