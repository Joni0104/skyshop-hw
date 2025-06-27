package org.skypro.skyshop.model.product;

import org.skypro.skyshop.model.search.Searchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public abstract class Product implements Searchable {
    private final UUID id;
    private final String name;

    protected Product(UUID id, String name) {
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
    @JsonIgnore
    public String getSearchTerm() {
        return getName();
    }

    @JsonIgnore
    public String getContentType() {
        return "product";
    }

    @Override
    public boolean match(String query) {
        return name.toLowerCase().contains(query.toLowerCase());
    }

    public abstract int getPrice();
    public abstract boolean isSpecial();
}
