package org.skypro.skyshop.model.search;

import org.skypro.skyshop.model.article.Article;

import java.util.Objects;

public class SearchResult {
    private final String id;
    private final String name;
    private final String contentType;

    public SearchResult(String id, String name, String contentType) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.contentType = Objects.requireNonNull(contentType, "Content type cannot be null");
    }

    public static SearchResult fromSearchable(Searchable searchable) {
        String contentType = (searchable instanceof Article) ? "article" : "product";
        return new SearchResult(
                searchable.getId().toString(),
                searchable.getName(),
                contentType
        );
    }

    // Геттеры
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    // equals и hashCode для корректной работы тестов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                contentType.equals(that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contentType);
    }
}