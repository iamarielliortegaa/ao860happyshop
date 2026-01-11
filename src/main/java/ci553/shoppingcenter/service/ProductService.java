package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.Product;
import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProductService {
    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public ProductService() {
        try {
            if (Files.exists(StorageLocation.productsFilePath)) {
                List<String> lines = Files.readAllLines(StorageLocation.productsFilePath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    // CSV format: id,name,price,imageFilename,category,stock,popularity,ratingSum,ratingCount
                    String[] parts = line.split(",", 9);
                    if (parts.length >= 3) {
                        try {
                            String id = parts[0];
                            String name = parts[1];
                            double price = Double.parseDouble(parts[2]);
                            String image = parts.length>3?parts[3]:null;
                            String category = parts.length>4?parts[4]:"Uncategorized";
                            int stock = parts.length>5?Integer.parseInt(parts[5]):0;
                            int popularity = parts.length>6?Integer.parseInt(parts[6]):0;
                            long ratingSum = parts.length>7?Long.parseLong(parts[7]):0L;
                            int ratingCount = parts.length>8?Integer.parseInt(parts[8]):0;
                            products.put(id, new Product(id, name, price, image, category, stock, popularity, ratingSum, ratingCount));
                        } catch (NumberFormatException nfe) {
                            // skip invalid line
                        }
                    }
                }
            }
        } catch (IOException e) {
            // ignore and seed defaults
        }

        if (products.isEmpty()) {
            // seed default products with diverse categories and stock
            add(new Product("p001", "Wireless Headphones", 79.99, "0001.jpg", "Electronics", 45, 120, 245L, 52));
            add(new Product("p002", "Smart Watch", 199.99, "0002.jpg", "Electronics", 30, 95, 380L, 78));
            add(new Product("p003", "USB-C Cable 2m", 12.99, "0003.jpg", "Electronics", 150, 200, 430L, 89));
            add(new Product("p006", "Cotton T-Shirt", 19.99, "0006.jpg", "Clothing", 80, 150, 425L, 87));
            add(new Product("p007", "Denim Jeans", 49.99, "0007.jpg", "Clothing", 55, 110, 340L, 70));
            add(new Product("p011", "Coffee Maker", 89.99, "0011.jpg", "Home & Garden", 35, 120, 475L, 98));
            add(new Product("p016", "Programming Book", 45.99, "0005.jpg", "Books", 40, 90, 340L, 70));
            add(new Product("p021", "Yoga Mat", 29.99, "0010.jpg", "Sports", 75, 125, 385L, 79));
            add(new Product("p026", "Building Blocks Set", 34.99, "0004.jpg", "Toys", 60, 140, 430L, 88));
            persist();
        }
    }

    public void add(Product p) {
        products.put(p.getId(), p);
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> listAll() {
        return new ArrayList<>(products.values());
    }

    public int count() {
        return products.size();
    }

    public synchronized void persist() {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            List<String> lines = products.values().stream().map(p -> String.join(",",
                    p.getId(),
                    p.getName(),
                    String.valueOf(p.getPrice()),
                    p.getImageFilename() == null ? "" : p.getImageFilename(),
                    p.getCategory() == null ? "Uncategorized" : p.getCategory(),
                    String.valueOf(p.getStock()),
                    String.valueOf(p.getPopularity()),
                    String.valueOf(p.getRatingSum()),
                    String.valueOf(p.getRatingCount())
            )).collect(Collectors.toList());
            Files.write(StorageLocation.productsFilePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // ignore for now
        }
    }

    public synchronized void remove(String id) {
        if (id == null) return;
        products.remove(id);
        persist();
    }
}
