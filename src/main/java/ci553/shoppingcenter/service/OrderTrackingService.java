package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.Order;
import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrderTrackingService {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public OrderTrackingService() {
        loadOrders();
    }

    public void createOrder(String orderId, String userId, String shippingAddress, String postalCode, String city, String country) {
        Order order = new Order(orderId, userId, shippingAddress, postalCode, city, country);
        orders.put(orderId, order);
        persist();
        ActionLogger.log("Order created: " + orderId + " for user: " + userId);
    }

    public Optional<Order> getOrder(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public List<Order> getOrdersByUser(String userId) {
        return orders.values().stream()
                .filter(o -> o.getUserId().equals(userId))
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .toList();
    }

    public void updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(newStatus);

            // Auto-update estimated delivery based on status
            switch (newStatus) {
                case PROCESSING -> order.setEstimatedDelivery(LocalDate.now().plusDays(4));
                case SHIPPED -> order.setEstimatedDelivery(LocalDate.now().plusDays(3));
                case IN_TRANSIT -> order.setEstimatedDelivery(LocalDate.now().plusDays(2));
                case OUT_FOR_DELIVERY -> order.setEstimatedDelivery(LocalDate.now().plusDays(1));
                case DELIVERED -> {
                    order.setActualDelivery(LocalDate.now());
                    order.setEstimatedDelivery(LocalDate.now());
                }
            }

            persist();
            ActionLogger.log("Order " + orderId + " status updated to: " + newStatus.getDisplayName());
        }
    }

    public void advanceOrderStatus(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            Order.OrderStatus currentStatus = order.getStatus();
            Order.OrderStatus nextStatus = getNextStatus(currentStatus);
            if (nextStatus != null) {
                updateOrderStatus(orderId, nextStatus);
            }
        }
    }

    private Order.OrderStatus getNextStatus(Order.OrderStatus current) {
        return switch (current) {
            case ORDER_PLACED -> Order.OrderStatus.PROCESSING;
            case PROCESSING -> Order.OrderStatus.SHIPPED;
            case SHIPPED -> Order.OrderStatus.IN_TRANSIT;
            case IN_TRANSIT -> Order.OrderStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> Order.OrderStatus.DELIVERED;
            case DELIVERED -> null; // Already at final status
        };
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    private void loadOrders() {
        try {
            Path ordersFile = StorageLocation.dataPath.resolve("orders.csv");
            if (!Files.exists(ordersFile)) {
                return;
            }

            List<String> lines = Files.readAllLines(ordersFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                // CSV format: id,userId,address,postalCode,city,country,status,estimatedDelivery,actualDelivery
                String[] parts = line.split(",", 9);
                if (parts.length >= 7) {
                    try {
                        String id = parts[0];
                        String userId = parts[1];
                        String address = parts.length > 2 ? parts[2] : "";
                        String postalCode = parts.length > 3 ? parts[3] : "";
                        String city = parts.length > 4 ? parts[4] : "";
                        String country = parts.length > 5 ? parts[5] : "";
                        Order.OrderStatus status = parts.length > 6 ? Order.OrderStatus.valueOf(parts[6]) : Order.OrderStatus.ORDER_PLACED;

                        Order order = new Order(id, userId, address, postalCode, city, country);
                        order.setStatus(status);

                        if (parts.length > 7 && !parts[7].isEmpty()) {
                            order.setEstimatedDelivery(LocalDate.parse(parts[7], DATE_FORMATTER));
                        }
                        if (parts.length > 8 && !parts[8].isEmpty()) {
                            order.setActualDelivery(LocalDate.parse(parts[8], DATE_FORMATTER));
                        }

                        orders.put(id, order);
                    } catch (Exception e) {
                        // Skip invalid line
                    }
                }
            }
        } catch (IOException e) {
            // Ignore and start fresh
        }
    }

    private void persist() {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            List<String> lines = orders.values().stream().map(o -> String.join(",",
                    o.getId(),
                    o.getUserId(),
                    o.getShippingAddress() != null ? o.getShippingAddress() : "",
                    o.getPostalCode() != null ? o.getPostalCode() : "",
                    o.getCity() != null ? o.getCity() : "",
                    o.getCountry() != null ? o.getCountry() : "",
                    o.getStatus().name(),
                    o.getEstimatedDelivery() != null ? o.getEstimatedDelivery().format(DATE_FORMATTER) : "",
                    o.getActualDelivery() != null ? o.getActualDelivery().format(DATE_FORMATTER) : ""
            )).toList();
            Files.write(StorageLocation.dataPath.resolve("orders.csv"), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Ignore for now
        }
    }
}

