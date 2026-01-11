package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.Product;
import ci553.shoppingcenter.utility.StorageLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CartService {
    // map of username -> map of productId -> quantity
    private final Map<String, Map<String, Integer>> carts = new ConcurrentHashMap<>();

    public CartService() {
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            if (!Files.exists(StorageLocation.cartsFilePath)) {
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(StorageLocation.cartsFilePath, StandardCharsets.UTF_8)) {
                String line = reader.readLine(); // Skip header
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 3) {
                        String username = parts[0];
                        String productId = parts[1];
                        int quantity = Integer.parseInt(parts[2]);

                        carts.computeIfAbsent(username, u -> new ConcurrentHashMap<>());
                        carts.get(username).put(productId, quantity);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading carts: " + e.getMessage());
        }
    }

    public void persist() {
        try {
            Files.createDirectories(StorageLocation.dataPath);

            try (BufferedWriter writer = Files.newBufferedWriter(StorageLocation.cartsFilePath, StandardCharsets.UTF_8)) {
                writer.write("username,productId,quantity");
                writer.newLine();

                for (Map.Entry<String, Map<String, Integer>> userCart : carts.entrySet()) {
                    String username = userCart.getKey();
                    for (Map.Entry<String, Integer> item : userCart.getValue().entrySet()) {
                        writer.write(String.format("%s,%s,%d",
                            username,
                            item.getKey(),
                            item.getValue()));
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error persisting carts: " + e.getMessage());
        }
    }

    public void addToCart(String username, Product product, int qty) {
        carts.computeIfAbsent(username, u -> new ConcurrentHashMap<>());
        Map<String, Integer> cart = carts.get(username);
        cart.put(product.getId(), cart.getOrDefault(product.getId(), 0) + Math.max(1, qty));
        persist();
    }

    public Map<String, Integer> getCart(String username) {
        return carts.getOrDefault(username, Collections.emptyMap());
    }

    public void removeFromCart(String username, String productId) {
        Map<String, Integer> cart = carts.get(username);
        if (cart != null) {
            cart.remove(productId);
            if (cart.isEmpty()) carts.remove(username);
            persist();
        }
    }

    public void updateCart(String username, String productId, int newQty) {
        if (newQty <= 0) {
            removeFromCart(username, productId);
            return;
        }
        Map<String, Integer> cart = carts.get(username);
        if (cart != null && cart.containsKey(productId)) {
            cart.put(productId, newQty);
            persist();
        }
    }

    public void clearCart(String username) {
        carts.remove(username);
        persist();
    }
}

