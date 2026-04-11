package com.ecommerce.sportscenter.product.service;

import com.ecommerce.sportscenter.product.entity.Brand;
import com.ecommerce.sportscenter.product.dto.BrandResponse;
import com.ecommerce.sportscenter.product.repository.BrandRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        log.info("Fetching All Brands!!!");
        // FETCH BRANDS
        List<Brand> brandList = brandRepository.findAll();

        // NOW USE STREAM OPERATOR TO MAP WITH RESPONSE
        List<BrandResponse> brandResponses = brandList.stream()
                .map(this::convertToBrandResponse)
                .collect(Collectors.toList());
        log.info("Fetched All Brands!!!");
        return brandResponses;
    }

    private BrandResponse convertToBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getBrandId())
                .name(brand.getName())
                .build();
    }
}
