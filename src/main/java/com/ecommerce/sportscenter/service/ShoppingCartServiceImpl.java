package com.ecommerce.sportscenter.service;

import com.ecommerce.sportscenter.entity.ShoppingCart;
import com.ecommerce.sportscenter.entity.ShoppingCartItem;
import com.ecommerce.sportscenter.model.ShoppingCartItemResponse;
import com.ecommerce.sportscenter.model.ShoppingCartResponse;
import com.ecommerce.sportscenter.repository.ShoppingCartRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public List<ShoppingCartResponse> getAllShoppingCarts() {
        log.info("Fetching All ShoppingCart");
        List<ShoppingCart> shoppingCartList = (List<ShoppingCart>) shoppingCartRepository.findAll();
        List<ShoppingCartResponse> shoppingCartResponses = shoppingCartList.stream()
                .map(this::convertToShoppingCartResponse)
                .collect(Collectors.toList());
        log.info("Fetched all ShoppingCart");
        return shoppingCartResponses;
    }


    @Override
    public ShoppingCartResponse getShoppingCartById(String shoppingCartId) {
        log.info("Fetching ShoppingCart by Id:{}", shoppingCartId);
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findById(shoppingCartId);
        if (shoppingCartOptional.isPresent()){
            ShoppingCart shoppingCart = shoppingCartOptional.get();
            log.info("Fetched Basket by Id: {}", shoppingCartId);

            return convertToShoppingCartResponse(shoppingCart);
        } else {
            log.info("ShoppingCart with Id: {} not found", shoppingCartId);
            return null;
        }
    }

    @Override
    public void deleteShoppingCartById(String shoppingCartId) {
        log.info("Deleting ShoppingCart by Id: {}", shoppingCartId);
        shoppingCartRepository.deleteById(shoppingCartId);
        log.info("Deleted shopping by Id: {}", shoppingCartId);
    }

    @Override
    public ShoppingCartResponse createShoppingCart(ShoppingCart shoppingCart) {
        log.info("Creating Shoppingcart");
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        log.info("ShoppingCart creted with Id: {}", savedShoppingCart.getId());
        return convertToShoppingCartResponse(savedShoppingCart);
    }

    private ShoppingCartResponse convertToShoppingCartResponse(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            return null;
        }
        List<ShoppingCartItemResponse> itemResponses = shoppingCart.getItems().stream()
                .map(this::convertToShoppingCartItemResponse)
                .collect(Collectors.toList());
        return ShoppingCartResponse.builder()
                .id(shoppingCart.getId())
                .items(itemResponses)
                .build();
    }

    private ShoppingCartItemResponse convertToShoppingCartItemResponse(ShoppingCartItem shoppingCartItem) {
        return ShoppingCartItemResponse.builder()
                .id(shoppingCartItem.getId())
                .name(shoppingCartItem.getName())
                .description(shoppingCartItem.getDescription())
                .price(shoppingCartItem.getPrice())
                .pictureUrl(shoppingCartItem.getPictureUrl())
                .productBrand(shoppingCartItem.getProductBrand())
                .productType(shoppingCartItem.getProductType())
                .quantity(shoppingCartItem.getQuantity())
                .build();
    }
}
