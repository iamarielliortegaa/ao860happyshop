package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WishlistService {
    private final Map<String, Set<String>> wishlists = new ConcurrentHashMap<>();

    public WishlistService() {
        // load existing wishlists from data directory (optional)
    }

    public synchronized void addToWishlist(String username, String productId) {
        String user = username == null ? "guest" : username;
        wishlists.computeIfAbsent(user, k -> new LinkedHashSet<>()).add(productId);
        persistUser(user);
    }

    public synchronized void removeFromWishlist(String username, String productId) {
        String user = username == null ? "guest" : username;
        Set<String> set = wishlists.get(user);
        if (set != null) {
            set.remove(productId);
            persistUser(user);
        }
    }

    public Set<String> getWishlist(String username) {
        String user = username == null ? "guest" : username;
        return Collections.unmodifiableSet(wishlists.computeIfAbsent(user, k -> loadUser(user)));
    }

    private Set<String> loadUser(String user) {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            java.nio.file.Path file = StorageLocation.dataPath.resolve("wishlist_" + user + ".csv");
            if (!Files.exists(file)) return new LinkedHashSet<>();
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            return lines.stream().filter(l -> !l.trim().isEmpty()).collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            return new LinkedHashSet<>();
        }
    }

    private void persistUser(String user) {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            java.nio.file.Path file = StorageLocation.dataPath.resolve("wishlist_" + user + ".csv");
            Set<String> set = wishlists.getOrDefault(user, Collections.emptySet());
            Files.write(file, set, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // ignore
        }
    }
}

