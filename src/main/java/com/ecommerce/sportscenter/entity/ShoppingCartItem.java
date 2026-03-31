package com.ecommerce.sportscenter.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@Getter
@Setter
@RedisHash("ShoppingCartItem")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShoppingCartItem {
    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String description;

    private BigDecimal price;

    private String pictureUrl;
    private String productBrand;
    private String productType;
    private Integer quantity;
}
