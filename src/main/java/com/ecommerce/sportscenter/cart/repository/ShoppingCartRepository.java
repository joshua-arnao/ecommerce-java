package com.ecommerce.sportscenter.cart.repository;

import com.ecommerce.sportscenter.cart.entity.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, String> {

}
