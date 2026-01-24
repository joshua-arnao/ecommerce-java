package com.ecommerce.sportscenter.service;

import com.ecommerce.sportscenter.entity.Product;
import com.ecommerce.sportscenter.model.ProductResponse;
import com.ecommerce.sportscenter.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
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
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching All Products!!!");
        // FETCH BRANDS
        List<Product> productList = productRepository.findAll();

        // NOW USE STREAM OPERATOR TO MAP WITH RESPONSE
        List<ProductResponse> productResponses = productList.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        log.info("Fetched All Products!!!");
        return productResponses;
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
