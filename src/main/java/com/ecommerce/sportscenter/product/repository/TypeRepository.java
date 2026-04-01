package com.ecommerce.sportscenter.product.repository;

import com.ecommerce.sportscenter.product.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
}
