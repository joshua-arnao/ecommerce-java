package com.ecommerce.sportscenter.cart.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RedisHash("ShoppingCartRepository")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShoppingCart {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private List<ShoppingCartItem> items = new ArrayList<>();

    public ShoppingCart(String id) {
        this.id = id;
    }
}
