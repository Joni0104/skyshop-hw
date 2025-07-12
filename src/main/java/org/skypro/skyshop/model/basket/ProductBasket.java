package org.skypro.skyshop.model.basket;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductBasket {
    private final Map<UUID, Integer> items = new HashMap<>();

    public void addProduct(UUID productId) {
        items.merge(productId, 1, Integer::sum);
    }

    public Map<UUID, Integer> getProducts() {
        return Collections.unmodifiableMap(items);
    }
}