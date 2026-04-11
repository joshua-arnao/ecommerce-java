package com.ecommerce.sportscenter.cart.service;

import com.ecommerce.sportscenter.cart.dto.ShoppingCartRequest;
import com.ecommerce.sportscenter.cart.entity.ShoppingCart;
import com.ecommerce.sportscenter.cart.entity.ShoppingCartItem;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartItemResponse;
import com.ecommerce.sportscenter.cart.dto.ShoppingCartResponse;
import com.ecommerce.sportscenter.cart.repository.ShoppingCartRepository;
import com.ecommerce.sportscenter.shared.exceptions.ShoppingCartNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;


    @Override
    public ShoppingCartResponse getShoppingCartById(String shoppingCartId) {
        return shoppingCartRepository.findById(shoppingCartId)
                .map(this::convertToShoppingCartResponse)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found"));
    }

    @Override
    public void deleteShoppingCartById(String shoppingCartId) {
        log.info("Deleting ShoppingCart by Id: {}", shoppingCartId);
        shoppingCartRepository.deleteById(shoppingCartId);
        log.info("Deleted shopping by Id: {}", shoppingCartId);
    }

    @Override
    public ShoppingCartResponse createOrUpdateCart(String userId, ShoppingCartRequest shoppingCartRequest) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(userId);

        List<ShoppingCartItem> items = shoppingCartRequest.items().stream()
                .map(item -> ShoppingCartItem.builder()
                        .shoppingCartIdItem(item.getShoppingCartItemId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .pictureUrl(item.getPictureUrl())
                        .productBrand(item.getProductBrand())
                        .productType(item.getProductType())
                        .quantity(item.getQuantity())
                        .build() )
                .toList();

        shoppingCart.setItems(items);

        ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);

        return new ShoppingCartResponse(
                savedCart.getId(),
                savedCart.getItems().stream()
                        .map(this::convertToShoppingCartItemResponse)
                        .toList()
        );
    }

    private ShoppingCartResponse convertToShoppingCartResponse(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            return null;
        }
        List<ShoppingCartItemResponse> itemResponses = shoppingCart.getItems().stream()
                .map(this::convertToShoppingCartItemResponse)
                .collect(Collectors.toList());
        return ShoppingCartResponse.builder()
                .shoppingCartId(shoppingCart.getId())
                .items(itemResponses)
                .build();
    }

    private ShoppingCartItemResponse convertToShoppingCartItemResponse(ShoppingCartItem shoppingCartItem) {
        return ShoppingCartItemResponse.builder()
                .shoppingCartItemId(shoppingCartItem.getShoppingCartIdItem())
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
