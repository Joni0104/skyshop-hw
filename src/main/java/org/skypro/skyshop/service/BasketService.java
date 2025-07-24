package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.ProductNotFoundException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BasketService {

    private final ProductBasket basket;


    private final StorageService storageService;


    public BasketService(ProductBasket basket, StorageService storageService) {
        this.basket = basket;
        this.storageService = storageService;
    }


    public void addProductToBasket(UUID productId) {

        Product product = storageService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));


        basket.addProduct(productId);
    }


    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketItems = basket.getProducts();


        List<BasketItem> items = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : basketItems.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();

            // Получаем товар из хранилища
            Product product = storageService.getProductById(productId)
                    .orElseThrow(() -> new IllegalStateException(
                            "Продукт не найден: " + productId
                    ));

            items.add(new BasketItem(product, quantity));
        }

        return new UserBasket(items);
    }
}