package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderLifecycleTest {
    @Test
    void orderProgressToOrderedToCollected() throws Exception {
        OrderService os = new OrderService();
        Map<String,Integer> cart = Map.of("p1",1);
        String receipt = os.placeOrder("lifecycleTester", cart, id -> 1.0, 0, id -> "p1");
        assertNotNull(receipt);
        // find a progressing file
        boolean found;
        try (java.util.stream.Stream<java.nio.file.Path> s = Files.list(ci553.shoppingcenter.utility.StorageLocation.progressingPath)) {
            found = s.anyMatch(p -> {
                try { return Files.readString(p).contains("Customer: lifecycleTester"); } catch (Exception e) { return false; }
            });
        }
        assertTrue(found, "Progressing order file should exist");
    }
}
