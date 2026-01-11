package ci553.shoppingcenter.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Order {
    private final String id;
    private final String userId;
    private final Instant createdAt;
    private String shippingAddress;
    private String postalCode;
    private String city;
    private String country;
    private OrderStatus status;
    private LocalDate estimatedDelivery;
    private LocalDate actualDelivery;

    public enum OrderStatus {
        ORDER_PLACED("Order Placed"),
        PROCESSING("Processing"),
        SHIPPED("Shipped"),
        IN_TRANSIT("In Transit"),
        OUT_FOR_DELIVERY("Out for Delivery"),
        DELIVERED("Delivered");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.createdAt = Instant.now();
        this.status = OrderStatus.ORDER_PLACED;
        this.estimatedDelivery = LocalDate.now().plusDays(5); // Default 5 day delivery
    }

    public Order(String id, String userId, String shippingAddress, String postalCode, String city, String country) {
        this.id = id;
        this.userId = userId;
        this.createdAt = Instant.now();
        this.shippingAddress = shippingAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.status = OrderStatus.ORDER_PLACED;
        this.estimatedDelivery = LocalDate.now().plusDays(5);
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(LocalDate estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public LocalDate getActualDelivery() {
        return actualDelivery;
    }

    public void setActualDelivery(LocalDate actualDelivery) {
        this.actualDelivery = actualDelivery;
    }

    public String getMaskedAddress() {
        if (shippingAddress == null || shippingAddress.isEmpty()) {
            return "N/A";
        }
        // Show first 20 chars and last 10 chars
        if (shippingAddress.length() <= 30) {
            return shippingAddress;
        }
        return shippingAddress.substring(0, 20) + "..." + shippingAddress.substring(shippingAddress.length() - 10);
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (shippingAddress != null) sb.append(shippingAddress);
        if (city != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        if (postalCode != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(postalCode);
        }
        if (country != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(country);
        }
        return sb.toString();
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
        return "Order{" + "id='" + id + '\'' + ", userId='" + userId + '\'' + ", createdAt=" + createdAt + ", status=" + status + '}';
    }
}

