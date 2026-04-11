package com.ecommerce.sportscenter.product.repository;

import com.ecommerce.sportscenter.product.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
    Specification<Product> searchByNameContaining(String keyword);
    Specification<Product> findByBrandBrandId(Integer brandId);
    Specification<Product> findByTypeTypeId(Integer typeId);
    Specification<Product> findByBrandBrandIdAfterAndTypeTypeId(Integer brandId, Integer typeId);

}
