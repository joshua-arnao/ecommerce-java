package com.ecommerce.sportscenter.service;

import com.ecommerce.sportscenter.entity.Product;
import com.ecommerce.sportscenter.model.ProductResponse;
import com.ecommerce.sportscenter.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private ProductServiceImpl( ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse getProductById(Integer productId) {
        log.info("Fetching Product by Id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product doesn't exist"));

        // CONVERT THE PRODUCT TO PRODUCT RESPONSE
        ProductResponse productResponse = convertToProductResponse(product);
        log.info("Fetched Product by Prodcut ID: ", productId);

        return productResponse;
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable, Integer brandId, Integer typeId, String keyword) {
        log.info("Fetching All Products!!!");
        Specification<Product> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        // Specification<Product> spec = Specification.where((Specification<Product>) null);
        if (brandId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), brandId));
        }
        if (typeId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type").get("id"), typeId));
        }
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%")));
        }
        log.info("Fetched All Products!!!");

        return productRepository.findAll(spec, pageable).map(this::convertToProductResponse);
    }

    private ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .Price(product.getPrice())
                .pictureUrl(product.getPictureUrl())
                .productBrand(product.getBrand().getName())
                .producType(product.getType().getName())
                .build();
    }
}
