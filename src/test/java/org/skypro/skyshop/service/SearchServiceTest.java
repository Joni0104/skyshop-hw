package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    @Test
    void search_shouldReturnEmptyList_whenNoItems() {
        // Подготовка
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        // Действие
        List<SearchResult> results = (List<SearchResult>) searchService.search("query");

        // Проверка
        assertTrue(results.isEmpty());
    }

    @Test
    void search_shouldReturnEmptyList_whenNoMatches() {
        // Подготовка
        Product product = createTestProduct("Ноутбук");
        Article article = createTestArticle("Обзор смартфонов");

        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(product, article));

        // Действие
        List<SearchResult> results = (List<SearchResult>) searchService.search("телевизор");

        // Проверка
        assertTrue(results.isEmpty());
    }

    @Test
    void search_shouldReturnSingleResult_whenOneMatch() {
        // Подготовка
        Product laptop = createTestProduct("Мощный игровой ноутбук");
        Product phone = createTestProduct("Смартфон");
        Article article = createTestArticle("Обзор ноутбуков");

        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(laptop, phone, article));

        // Действие
        List<SearchResult> results = (List<SearchResult>) searchService.search("игровой");

        // Проверка
        assertEquals(1, results.size());
        assertEquals("Мощный игровой ноутбук", results.get(0).getName());
        assertEquals("product", results.get(0).getContentType());
    }

    @Test
    void search_shouldReturnMultipleResults_whenMultipleMatches() {
        // Подготовка
        Product laptop = createTestProduct("Игровой ноутбук");
        Product phone = createTestProduct("Смартфон");
        Article laptopReview = createTestArticle("Обзор игровых ноутбуков");

        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(laptop, phone, laptopReview));

        // Действие
        List<SearchResult> results = (List<SearchResult>) searchService.search("игров");

        // Проверка
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.getName().contains("ноутбук")));
        assertTrue(results.stream().anyMatch(r -> r.getName().contains("Обзор")));
    }

    @Test
    void search_shouldBeCaseInsensitive() {
        // Подготовка
        Product product = createTestProduct("Ноутбук Dell");

        when(storageService.getAllSearchables())
                .thenReturn(Collections.singletonList(product));

        // Действие
        List<SearchResult> results = (List<SearchResult>) searchService.search("DELL");

        // Проверка
        assertEquals(1, results.size());
        assertEquals("Ноутбук Dell", results.get(0).getName());
    }

    private Product createTestProduct(String name) {
        return new Product(UUID.randomUUID(), name) {
            @Override
            public int getPrice() {
                return 0;
            }

            @Override
            public boolean isSpecial() {
                return false;
            }
        };
    }

    private Article createTestArticle(String title) {
        return new Article(UUID.randomUUID(), title);
    }
}