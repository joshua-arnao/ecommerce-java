package com.ecommerce.sportscenter.cart.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@Getter
@Setter
@RedisHash("ShoppingCartItem")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class ShoppingCartItem {
    @Id
    @EqualsAndHashCode.Include
    private Integer shoppingCartIdItem;
    private String name;
    private String description;

    private BigDecimal price;

    private String pictureUrl;
    private String productBrand;
    private String productType;
    private Integer quantity;
}
