package ci553.shoppingcenter.model;

import java.util.Objects;

public class Product {
    private final String id;
    private String name;
    private double price;
    private final String imageFilename; // optional, may be null
    private String category;
    private int stock;
    private int popularity; // number of times viewed/ordered
    private long ratingSum; // sum of ratings
    private int ratingCount; // number of ratings

    public Product(String id, String name, double price) {
        this(id, name, price, null, "Uncategorized", 0, 0, 0L, 0);
    }

    public Product(String id, String name, double price, String imageFilename) {
        this(id, name, price, imageFilename, "Uncategorized", 0, 0, 0L, 0);
    }

    public Product(String id, String name, double price, String imageFilename, String category, int stock, int popularity, long ratingSum, int ratingCount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageFilename = imageFilename;
        this.category = category == null ? "Uncategorized" : category;
        this.stock = Math.max(0, stock);
        this.popularity = Math.max(0, popularity);
        this.ratingSum = Math.max(0L, ratingSum);
        this.ratingCount = Math.max(0, ratingCount);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) { this.price = price; }

    public String getImageFilename() {
        return imageFilename;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category == null ? "Uncategorized" : category; }

    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = Math.max(0, stock); }

    public int getPopularity() { return popularity; }

    public void setPopularity(int popularity) { this.popularity = Math.max(0, popularity); }

    public void incrementPopularity() { this.popularity++; }

    public void addRating(int rating) {
        if (rating <= 0) return;
        this.ratingSum += rating;
        this.ratingCount++;
    }

    public double getAverageRating() {
        return ratingCount == 0 ? 0.0 : ((double) ratingSum) / ratingCount;
    }

    public long getRatingSum() { return ratingSum; }
    public int getRatingCount() { return ratingCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", price=" + price + ',' + " image='" + imageFilename + '\'' + '}';
    }
}
