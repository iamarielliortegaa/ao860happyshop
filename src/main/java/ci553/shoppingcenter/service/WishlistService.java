package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WishlistService {
    private final Map<String, Set<String>> wishlists = new ConcurrentHashMap<>();

    public WishlistService() {
        loadFromCSV();
    }

    private void loadFromCSV() {
        try {
            if (!Files.exists(StorageLocation.wishlistFilePath)) {
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(StorageLocation.wishlistFilePath, StandardCharsets.UTF_8)) {
                reader.readLine(); // Skip header
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 2) {
                        String username = parts[0];
                        String productId = parts[1];

                        wishlists.computeIfAbsent(username, k -> new LinkedHashSet<>()).add(productId);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading wishlists: " + e.getMessage());
        }
    }

    public void persist() {
        try {
            Files.createDirectories(StorageLocation.dataPath);

            try (BufferedWriter writer = Files.newBufferedWriter(StorageLocation.wishlistFilePath, StandardCharsets.UTF_8)) {
                writer.write("username,productId");
                writer.newLine();

                for (Map.Entry<String, Set<String>> entry : wishlists.entrySet()) {
                    String username = entry.getKey();
                    for (String productId : entry.getValue()) {
                        writer.write(String.format("%s,%s", username, productId));
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error persisting wishlists: " + e.getMessage());
        }
    }

    public synchronized void addToWishlist(String username, String productId) {
        String user = username == null ? "guest" : username;
        wishlists.computeIfAbsent(user, k -> new LinkedHashSet<>()).add(productId);
        persist();
    }

    public synchronized void removeFromWishlist(String username, String productId) {
        String user = username == null ? "guest" : username;
        Set<String> set = wishlists.get(user);
        if (set != null) {
            set.remove(productId);
            persist();
        }
    }

    public Set<String> getWishlist(String username) {
        String user = username == null ? "guest" : username;
        return Collections.unmodifiableSet(wishlists.getOrDefault(user, Collections.emptySet()));
    }
}

