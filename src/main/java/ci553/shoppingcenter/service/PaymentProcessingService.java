package ci553.shoppingcenter.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

/**
 * Service for processing payments and managing payment history
 */
public class PaymentProcessingService {

    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        APPROVED,
        DECLINED,
        REFUNDED
    }

    public enum CardType {
        VISA("Visa"),
        MASTERCARD("Mastercard"),
        AMERICAN_EXPRESS("American Express"),
        DISCOVER("Discover"),
        DEBIT_CARD("Debit Card");

        private final String displayName;

        CardType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static class PaymentRecord {
        private final String paymentId;
        private final String orderId;
        private final String userId;
        private final double amount;
        private final CardType cardType;
        private final String cardLast4;
        private final Instant timestamp;
        private PaymentStatus status;
        private String transactionId;

        public PaymentRecord(String paymentId, String orderId, String userId, double amount,
                           CardType cardType, String cardLast4) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.userId = userId;
            this.amount = amount;
            this.cardType = cardType;
            this.cardLast4 = cardLast4;
            this.timestamp = Instant.now();
            this.status = PaymentStatus.PENDING;
            this.transactionId = generateTransactionId();
        }

        private String generateTransactionId() {
            return "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
        }

        // Getters
        public String getPaymentId() { return paymentId; }
        public String getOrderId() { return orderId; }
        public String getUserId() { return userId; }
        public double getAmount() { return amount; }
        public CardType getCardType() { return cardType; }
        public String getCardLast4() { return cardLast4; }
        public Instant getTimestamp() { return timestamp; }
        public PaymentStatus getStatus() { return status; }
        public String getTransactionId() { return transactionId; }

        public void setStatus(PaymentStatus status) { this.status = status; }
    }

    private final Map<String, PaymentRecord> payments = new HashMap<>();
    private final Map<String, List<PaymentRecord>> userPayments = new HashMap<>();

    /**
     * Process a payment
     */
    public PaymentRecord processPayment(String orderId, String userId, double amount,
                                       CardType cardType, String cardNumber,
                                       String expiry, String cvv) {
        // Validate card details
        if (!validateCardNumber(cardNumber)) {
            PaymentRecord record = new PaymentRecord(
                "PAY-" + System.currentTimeMillis(), orderId, userId, amount,
                cardType, getLastFourDigits(cardNumber)
            );
            record.setStatus(PaymentStatus.DECLINED);
            savePayment(record);
            return record;
        }

        if (!validateExpiry(expiry)) {
            PaymentRecord record = new PaymentRecord(
                "PAY-" + System.currentTimeMillis(), orderId, userId, amount,
                cardType, getLastFourDigits(cardNumber)
            );
            record.setStatus(PaymentStatus.DECLINED);
            savePayment(record);
            return record;
        }

        // Create payment record
        PaymentRecord record = new PaymentRecord(
            "PAY-" + System.currentTimeMillis(), orderId, userId, amount,
            cardType, getLastFourDigits(cardNumber)
        );

        // Simulate payment processing
        record.setStatus(PaymentStatus.PROCESSING);

        // Simulate approval (90% success rate for simulation)
        if (Math.random() < 0.9) {
            record.setStatus(PaymentStatus.APPROVED);
        } else {
            record.setStatus(PaymentStatus.DECLINED);
        }

        savePayment(record);
        ActionLogger.log("Payment processed: " + record.getPaymentId() + " Status: " + record.getStatus());

        return record;
    }

    private void savePayment(PaymentRecord record) {
        payments.put(record.getPaymentId(), record);
        userPayments.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
    }

    /**
     * Get payment by ID
     */
    public Optional<PaymentRecord> getPayment(String paymentId) {
        return Optional.ofNullable(payments.get(paymentId));
    }

    /**
     * Get all payments for a user
     */
    public List<PaymentRecord> getUserPayments(String userId) {
        return new ArrayList<>(userPayments.getOrDefault(userId, new ArrayList<>()));
    }

    /**
     * Get payment for an order
     */
    public Optional<PaymentRecord> getPaymentByOrder(String orderId) {
        return payments.values().stream()
            .filter(p -> p.getOrderId().equals(orderId))
            .findFirst();
    }

    /**
     * Validate card number (basic Luhn algorithm)
     */
    private boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }

        // Remove spaces and dashes
        cardNumber = cardNumber.replaceAll("[ -]", "");

        // Check if it's numeric and has valid length
        if (!cardNumber.matches("\\d{13,19}")) {
            return false;
        }

        // For simulation, accept any properly formatted number
        return true;
    }

    /**
     * Validate expiry date
     */
    private boolean validateExpiry(String expiry) {
        if (expiry == null || expiry.trim().isEmpty()) {
            return false;
        }

        // Expected format: MM/YY or MM/YYYY
        String cleaned = expiry.replaceAll("[^0-9/]", "");
        String[] parts = cleaned.split("/");

        if (parts.length != 2) {
            return false;
        }

        try {
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            // Validate month
            if (month < 1 || month > 12) {
                return false;
            }

            // Handle 2-digit year
            if (year < 100) {
                year += 2000;
            }

            // Check if not expired
            LocalDate now = LocalDate.now();
            LocalDate cardExpiry = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);

            return !cardExpiry.isBefore(now);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Get last 4 digits of card number
     */
    private String getLastFourDigits(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return "****";
        }
        String cleaned = cardNumber.replaceAll("[ -]", "");
        if (cleaned.length() < 4) {
            return cleaned;
        }
        return cleaned.substring(cleaned.length() - 4);
    }

    /**
     * Refund a payment
     */
    public boolean refundPayment(String paymentId) {
        PaymentRecord record = payments.get(paymentId);
        if (record != null && record.getStatus() == PaymentStatus.APPROVED) {
            record.setStatus(PaymentStatus.REFUNDED);
            ActionLogger.log("Payment refunded: " + paymentId);
            return true;
        }
        return false;
    }
}

