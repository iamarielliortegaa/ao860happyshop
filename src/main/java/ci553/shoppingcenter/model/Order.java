package ci553.shoppingcenter.model;

import java.time.Instant;
import java.util.Objects;

public class Order {
    private final String id;
    private final String userId;
    private final Instant createdAt;

    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" + "id='" + id + '\'' + ", userId='" + userId + '\'' + ", createdAt=" + createdAt + '}';
    }
}

