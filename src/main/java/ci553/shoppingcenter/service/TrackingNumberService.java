package ci553.shoppingcenter.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Service for generating and managing unique tracking numbers for orders and products
 */
public class TrackingNumberService {
    private static final Random random = new Random();
    private final Map<String, String> orderTrackingNumbers = new HashMap<>();
    private final Map<String, String> productTrackingNumbers = new HashMap<>();

    /**
     * Generate a unique tracking number for an order
     * Format: TRK-YYYYMMDD-XXXXXX (where X is random alphanumeric)
     */
    public String generateOrderTrackingNumber(String orderId) {
        if (orderTrackingNumbers.containsKey(orderId)) {
            return orderTrackingNumbers.get(orderId);
        }

        String dateCode = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomCode = generateRandomCode(6);
        String trackingNumber = "TRK-" + dateCode + "-" + randomCode;

        orderTrackingNumbers.put(orderId, trackingNumber);
        return trackingNumber;
    }

    /**
     * Generate a unique tracking number for a product
     * Format: PRD-XXXXX-YYY (where X is category code, Y is random)
     */
    public String generateProductTrackingNumber(String productId, String category) {
        if (productTrackingNumbers.containsKey(productId)) {
            return productTrackingNumbers.get(productId);
        }

        String categoryCode = getCategoryCode(category);
        String randomCode = generateRandomCode(3);
        String trackingNumber = "PRD-" + categoryCode + "-" + randomCode;

        productTrackingNumbers.put(productId, trackingNumber);
        return trackingNumber;
    }

    /**
     * Get tracking number for an existing order
     */
    public String getOrderTrackingNumber(String orderId) {
        return orderTrackingNumbers.getOrDefault(orderId, "N/A");
    }

    /**
     * Get tracking number for an existing product
     */
    public String getProductTrackingNumber(String productId) {
        return productTrackingNumbers.getOrDefault(productId, "N/A");
    }

    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    private String getCategoryCode(String category) {
        return switch (category.toLowerCase()) {
            case "electronics" -> "ELECT";
            case "clothing" -> "CLOTH";
            case "home & garden" -> "HOMEG";
            case "books" -> "BOOKS";
            case "sports" -> "SPORT";
            case "toys" -> "TOYS";
            default -> "GENER";
        };
    }
}

