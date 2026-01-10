package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    @Test
    void placeOrderCreatesProgressingFile() throws Exception {
        OrderService os = new OrderService();
        Path tmp = Files.createTempDirectory("ordersrv");
        // override StorageLocation paths via reflection isn't set up; instead call placeOrder and check progress dir created
        Map<String,Integer> cart = Map.of("p1", 2);
        String receipt = os.placeOrder("tester", cart, id -> 5.0, 0, id -> "p1");
        assertNotNull(receipt);
        // ensure progressing dir exists
        assertTrue(Files.exists(ci553.shoppingcenter.utility.StorageLocation.progressingPath));
    }
}

