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
    private ProductBasket productBasket;

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
        assertThrows(ProductNotFoundException.class, () -> {
            basketService.addProductToBasket(productId);
        });

        // Проверка, что метод корзины не вызывался
        verify(productBasket, never()).addProduct(any());
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
        verify(productBasket).addProduct(productId);
    }

    @Test
    void getUserBasket_shouldReturnEmptyBasket_whenNoItems() {
        // Подготовка
        when(productBasket.getProducts()).thenReturn(Collections.emptyMap());

        // Действие
        UserBasket basket = basketService.getUserBasket();

        // Проверка
        assertTrue(basket.getItems().isEmpty());
        assertEquals(0.0, basket.getTotal());
    }

    @Test
    void getUserBasket_shouldReturnCorrectBasket_whenItemsExist() {
        // Подготовка
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        Product product1 = createProduct(productId1, "Ноутбук", 50000);
        Product product2 = createProduct(productId2, "Мышь", 1000);

        Map<UUID, Integer> basketItems = new HashMap<>();
        basketItems.put(productId1, 1);
        basketItems.put(productId2, 2);

        when(productBasket.getProducts()).thenReturn(basketItems);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(productId2)).thenReturn(Optional.of(product2));

        // Действие
        UserBasket basket = basketService.getUserBasket();

        // Проверка
        List<BasketItem> items = basket.getItems();
        assertEquals(2, items.size());

        // Проверка первого товара
        BasketItem item1 = items.get(0);
        assertEquals("Ноутбук", item1.getProduct().getName());
        assertEquals(1, item1.getQuantity());

        // Проверка второго товара
        BasketItem item2 = items.get(1);
        assertEquals("Мышь", item2.getProduct().getName());
        assertEquals(2, item2.getQuantity());

        // Проверка общей суммы
        double expectedTotal = 50000 * 1 + 1000 * 2;
        assertEquals(expectedTotal, basket.getTotal());
    }

    @Test
    void getUserBasket_shouldThrow_whenProductInBasketNotFound() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        Map<UUID, Integer> basketItems = Collections.singletonMap(productId, 1);

        when(productBasket.getProducts()).thenReturn(basketItems);
        when(storageService.getProductById(productId)).thenReturn(Optional.empty());

        // Действие и проверка
        assertThrows(IllegalStateException.class, () -> {
            basketService.getUserBasket();
        });
    }

    @Test
    void getUserBasket_shouldCalculateTotalCorrectly() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        Product product = createProduct(productId, "Тест", 100);

        Map<UUID, Integer> basketItems = Collections.singletonMap(productId, 3);

        when(productBasket.getProducts()).thenReturn(basketItems);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // Действие
        UserBasket basket = basketService.getUserBasket();

        // Проверка
        assertEquals(300.0, basket.getTotal());
    }

    private Product createProduct(UUID id, String name, int price) {
        return new Product(id, name) {
            @Override
            public int getPrice() {
                return price;
            }

            @Override
            public boolean isSpecial() {
                return false;
            }
        };
    }
}