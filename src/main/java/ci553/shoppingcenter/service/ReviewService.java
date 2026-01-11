package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ReviewService {
    private final Map<String, List<Review>> reviews = new ConcurrentHashMap<>();

    public static class Review {
        public final String user;
        public final int rating;
        public final String comment;
        public final long timestamp;

        public Review(String user, int rating, String comment, long timestamp) {
            this.user = user; this.rating = rating; this.comment = comment; this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return user + "," + rating + "," + timestamp + "," + (comment == null ? "" : comment.replace('\n',' '));
        }
    }

    public ReviewService() {
        // lazy load
    }

    public List<Review> getReviews(String productId) {
        return Collections.unmodifiableList(reviews.computeIfAbsent(productId, this::loadProduct));
    }

    public void addReview(String productId, String user, int rating, String comment) {
        if (productId == null || rating <= 0) return;
        Review r = new Review(user == null ? "guest" : user, rating, comment, System.currentTimeMillis());
        reviews.computeIfAbsent(productId, k -> new ArrayList<>()).add(r);
        persistProduct(productId);
    }

    private List<Review> loadProduct(String productId) {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            java.nio.file.Path file = StorageLocation.dataPath.resolve("reviews_" + productId + ".csv");
            if (!Files.exists(file)) return new ArrayList<>();
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            List<Review> out = new ArrayList<>();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",",4);
                if (parts.length>=3) {
                    String user = parts[0];
                    int rating = Integer.parseInt(parts[1]);
                    long ts = Long.parseLong(parts[2]);
                    String comment = parts.length==4?parts[3]:"";
                    out.add(new Review(user,rating,comment,ts));
                }
            }
            return out;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void persistProduct(String productId) {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            java.nio.file.Path file = StorageLocation.dataPath.resolve("reviews_" + productId + ".csv");
            List<String> lines = reviews.getOrDefault(productId, Collections.emptyList()).stream().map(Review::toString).collect(Collectors.toList());
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // ignore
        }
    }
}

