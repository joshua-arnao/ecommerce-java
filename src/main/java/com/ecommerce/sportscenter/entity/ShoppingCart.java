package com.ecommerce.sportscenter.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RedisHash("ShoppingCartRepository")
public class ShoppingCart {
    @Id
    private String id;
    private List<ShoppingCartItem> items = new ArrayList<>();

    public ShoppingCart(String id) {
        this.id = id;
    }



}
