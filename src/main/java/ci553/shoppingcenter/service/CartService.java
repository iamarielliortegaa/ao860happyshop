package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CartService {
    // map of username -> map of productId -> quantity
    private final Map<String, Map<String, Integer>> carts = new ConcurrentHashMap<>();

    public void addToCart(String username, Product product, int qty) {
        carts.computeIfAbsent(username, u -> new ConcurrentHashMap<>());
        Map<String, Integer> cart = carts.get(username);
        cart.put(product.getId(), cart.getOrDefault(product.getId(), 0) + Math.max(1, qty));
    }

    public Map<String, Integer> getCart(String username) {
        return carts.getOrDefault(username, Collections.emptyMap());
    }

    public void removeFromCart(String username, String productId) {
        Map<String, Integer> cart = carts.get(username);
        if (cart != null) {
            cart.remove(productId);
            if (cart.isEmpty()) carts.remove(username);
        }
    }

    public void clearCart(String username) {
        carts.remove(username);
    }
}

