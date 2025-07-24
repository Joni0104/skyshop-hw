package org.skypro.skyshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Collection;
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
    void search_shouldReturnEmptyList_whenNoItemsInStorage() {
        // Подготовка
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        // Действие
        var result = searchService.search("query");

        // Проверка
        assertTrue(result.isEmpty());
    }

    @Test
    void search_shouldReturnEmptyList_whenNoMatchesFound() {
        // Подготовка
        Searchable mockItem = mock(Searchable.class);
        when(mockItem.match("query")).thenReturn(false);
        when(storageService.getAllSearchables()).thenReturn(List.of(mockItem));

        // Действие
        var result = searchService.search("query");

        // Проверка
        assertTrue(result.isEmpty());
    }

    @Test
    void search_shouldReturnResults_whenMatchesFound() {
        // Подготовка
        Searchable matchingItem = mock(Searchable.class);
        when(matchingItem.match("query")).thenReturn(true);
        when(matchingItem.getName()).thenReturn("Test Item");
        when(matchingItem.getId()).thenReturn(UUID.randomUUID());

        when(storageService.getAllSearchables()).thenReturn(List.of(matchingItem));

        // Действие
        Collection<SearchResult> result = searchService.search("query");

        // Проверка
        assertEquals(1, result.size());

        // Получаем первый элемент через итератор
        SearchResult firstResult = result.iterator().next();
        assertEquals("Test Item", firstResult.getName());
    }
    }

