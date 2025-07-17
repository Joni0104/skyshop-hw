package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.ProductNotFoundException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket basket;
    private final StorageService storageService;

    public BasketService(ProductBasket basket, StorageService storageService) {
        this.basket = basket;
        this.storageService = storageService;
    }

    public void addProductToBasket(UUID productId) {
        storageService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        basket.addProduct(productId);
    }

    public UserBasket getUserBasket() {
        return new UserBasket(
                basket.getProducts().entrySet().stream()
                        .map(entry -> {
                            UUID productId = entry.getKey();
                            int quantity = entry.getValue();
                            Product product = storageService.getProductById(productId)
                                    .orElseThrow(() -> new IllegalStateException("Product not found: " + productId));
                            return new BasketItem(product, quantity);
                        })
                        .collect(Collectors.toList())
        );
    }
}