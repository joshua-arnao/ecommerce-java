package com.ecommerce.sportscenter.product.service;

import com.ecommerce.sportscenter.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    ProductResponse getProductById(Integer productId);
    Page<ProductResponse> getAllProducts(Pageable pegable, Integer brandId, Integer typeId, String keyword);


}
