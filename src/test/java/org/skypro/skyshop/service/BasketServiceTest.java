package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.ProductNotFoundException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket basket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    @Test
    void addProductToBasket_shouldThrow_whenProductNotFound() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        when(storageService.getProductById(productId)).thenReturn(Optional.empty());

        // Действие и проверка
        assertThrows(ProductNotFoundException.class,
                () -> basketService.addProductToBasket(productId));
    }

    @Test
    void addProductToBasket_shouldAddProduct_whenProductExists() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // Действие
        basketService.addProductToBasket(productId);

        // Проверка
        verify(basket).addProduct(productId);
    }

    @Test
    void getUserBasket_shouldReturnEmptyBasket_whenNoProducts() {
        // Подготовка
        when(basket.getProducts()).thenReturn(Collections.emptyMap());

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        assertTrue(result.getItems().isEmpty());
        assertEquals(0, result.getTotal());
    }

    @Test
    void getUserBasket_shouldReturnCorrectBasket_whenProductsExist() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(100);

        Map<UUID, Integer> basketItems = Map.of(productId, 2);
        when(basket.getProducts()).thenReturn(basketItems);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        assertEquals(1, result.getItems().size());
        assertEquals(200, result.getTotal());

        BasketItem item = result.getItems().get(0);
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
    }
    
}