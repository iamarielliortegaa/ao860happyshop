package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WishlistServiceTest {
    @Test
    void addAndRemoveWishlistPersists() {
        try {
            WishlistService ws = new WishlistService();
            String user = "testuser";
            ws.addToWishlist(user, "p1");
            Set<String> s = ws.getWishlist(user);
            assertTrue(s.contains("p1"));
            ws.removeFromWishlist(user, "p1");
            Set<String> s2 = ws.getWishlist(user);
            assertFalse(s2.contains("p1"));
            Path file = ci553.shoppingcenter.utility.StorageLocation.dataPath.resolve("wishlist_" + user + ".csv");
            assertTrue(Files.exists(file));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
