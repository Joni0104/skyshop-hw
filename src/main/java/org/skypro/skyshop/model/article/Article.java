package org.skypro.skyshop.model.article;

import org.skypro.skyshop.model.search.Searchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;


public class Article implements Searchable {
    private final UUID id;
    private final String name;

    public Article(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean match(String query) {
        return name.toLowerCase().contains(query.toLowerCase());
    }
    @JsonIgnore
    public String getContentType() {
        return "article";
    }
}