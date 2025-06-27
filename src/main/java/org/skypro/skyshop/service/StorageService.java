package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.*;
import org.springframework.stereotype.Service;
import java.util.*;
import org.skypro.skyshop.model.search.Searchable;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class StorageService {
    private final Map<UUID, Product> products = new HashMap<>();
    private final Map<UUID, Article> articles = new HashMap<>();

    public StorageService() {
        initializeTestData();
    }

    private void initializeTestData() {

        products.put(
                UUID.randomUUID(),
                new SimpleProduct(UUID.randomUUID(), "Ноутбук", 50000)
        );

        products.put(
                UUID.randomUUID(),
                new DiscountedProduct(UUID.randomUUID(), "Смартфон", 30000, 10)
        );

        products.put(
                UUID.randomUUID(),
                new FixPriceProduct(UUID.randomUUID(), "Зарядное устройство")
        );


        articles.put(
                UUID.randomUUID(),
                new Article(UUID.randomUUID(), "Обзор новейших ноутбуков")
        );

        articles.put(
                UUID.randomUUID(),
                new Article(UUID.randomUUID(), "Как выбрать смартфон")
        );
    }

    /**
     * Возвращает все продукты в хранилище
     * @return коллекция продуктов
     */
    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    /**
     * Возвращает все статьи в хранилище
     * @return коллекция статей
     */
    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articles.values());
    }
    public Collection<Searchable> getAllSearchables() {
        Collection<Searchable> result = new ArrayList<>();
        result.addAll(products.values());
        result.addAll(articles.values());
        return result;
    }
}
