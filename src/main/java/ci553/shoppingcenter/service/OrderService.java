package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderService {
    private final AtomicInteger counter = new AtomicInteger(0);

    public OrderService() {
        // Initialize counter from file if present
        try {
            Path p = StorageLocation.orderCounterPath;
            if (Files.exists(p)) {
                String value = Files.readString(p, StandardCharsets.UTF_8).trim();
                if (!value.isEmpty()) {
                    counter.set(Integer.parseInt(value));
                }
            } else {
                ensureOrderDirExists();
                persistCounter();
            }
        } catch (Exception e) {
            // ignore and keep counter at 0
        }
    }

    public int nextOrderNumber() {
        int val = counter.incrementAndGet();
        persistCounter();
        return val;
    }

    /**
     * Place an order: initially save receipt into progressing folder so admin can approve/collect.
     * priceLookup returns unit price for productId; discountPercent is 0-100
     */
    public String placeOrder(String username, Map<String, Integer> cart, java.util.function.Function<String, Double> priceLookup, int discountPercent, java.util.function.Function<String,String> productNameLookup) {
        if (cart == null || cart.isEmpty()) return "";
        int orderNumber = nextOrderNumber();
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order #: ").append(orderNumber).append("\n");
        receipt.append("Customer: ").append(username).append("\n\n");
        double subtotal = 0.0;
        for (Map.Entry<String, Integer> e : cart.entrySet()) {
            String pid = e.getKey();
            int qty = e.getValue();
            double price = priceLookup.apply(pid);
            double line = price * qty;
            subtotal += line;
            String pname = productNameLookup.apply(pid);
            receipt.append(String.format("%s x%d @ $%.2f = $%.2f\n", pname, qty, price, line));
        }
        receipt.append(String.format("\nSubtotal: $%.2f\n", subtotal));
        double discountAmount = 0.0;
        if (discountPercent > 0) {
            discountAmount = subtotal * (discountPercent / 100.0);
            receipt.append(String.format("Discount (%d%%): -$%.2f\n", discountPercent, discountAmount));
        }
        double total = subtotal - discountAmount;
        if (total < 0) total = 0;
        receipt.append(String.format("Total: $%.2f\n\n", total));
        receipt.append("Thank you for your order!\n");

        // persist receipt into progressing so admin can promote
        try {
            ensureOrderDirExists();
            Path receiptPath = StorageLocation.progressingPath.resolve("order_" + orderNumber + ".txt");
            Files.writeString(receiptPath, receipt.toString(), StandardCharsets.UTF_8);
            return receipt.toString();
        } catch (IOException ex) {
            return receipt.toString();
        }
    }

    public List<Path> listProgressingOrders() {
        try {
            if (!Files.exists(StorageLocation.progressingPath)) return List.of();
            return Files.list(StorageLocation.progressingPath).collect(Collectors.toList());
        } catch (IOException ex) {
            return List.of();
        }
    }

    public List<Path> listOrderedOrders() {
        try {
            if (!Files.exists(StorageLocation.orderedPath)) return List.of();
            return Files.list(StorageLocation.orderedPath).collect(Collectors.toList());
        } catch (IOException ex) {
            return List.of();
        }
    }

    public boolean promoteToOrdered(Path progressingFile) {
        try {
            ensureOrderDirExists();
            Path dest = StorageLocation.orderedPath.resolve(progressingFile.getFileName());
            Files.move(progressingFile, dest);
            // notify
            NotificationService.notify("Order promoted to ordered: " + dest.getFileName().toString());
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public boolean promoteToCollected(Path orderedFile) {
        try {
            ensureOrderDirExists();
            Path dest = StorageLocation.collectedPath.resolve(orderedFile.getFileName());
            Files.move(orderedFile, dest);
            NotificationService.notify("Order collected: " + dest.getFileName().toString());
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void persistCounter() {
        try {
            ensureOrderDirExists();
            Files.writeString(StorageLocation.orderCounterPath, String.valueOf(counter.get()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // log in real app
        }
    }

    private void ensureOrderDirExists() throws IOException {
        Path orders = StorageLocation.ordersPath;
        if (!Files.exists(orders)) {
            Files.createDirectories(orders);
        }
        if (!Files.exists(StorageLocation.orderedPath)) {
            Files.createDirectories(StorageLocation.orderedPath);
        }
        if (!Files.exists(StorageLocation.progressingPath)) {
            Files.createDirectories(StorageLocation.progressingPath);
        }
        if (!Files.exists(StorageLocation.collectedPath)) {
            Files.createDirectories(StorageLocation.collectedPath);
        }
    }
}
