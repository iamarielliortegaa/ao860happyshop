package ci553.shoppingcenter.service;

import org.junit.jupiter.api.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WishlistServiceTest {

    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        wishlistService = new WishlistService();
    }

    @AfterEach
    void tearDown() {
        wishlistService = null;
    }

    @Test
    @DisplayName("Should add product to wishlist successfully")
    void testAddToWishlist() {
        wishlistService.addToWishlist("user1", "p001");
        Set<String> wishlist = wishlistService.getWishlist("user1");

        assertTrue(wishlist.contains("p001"), "Wishlist should contain added product");
        assertEquals(1, wishlist.size(), "Wishlist should have 1 item");
    }

    @Test
    @DisplayName("Should add multiple products to wishlist")
    void testAddMultipleProducts() {
        wishlistService.addToWishlist("user1", "p001");
        wishlistService.addToWishlist("user1", "p002");
        wishlistService.addToWishlist("user1", "p003");

        Set<String> wishlist = wishlistService.getWishlist("user1");
        assertEquals(3, wishlist.size(), "Wishlist should contain 3 products");
        assertTrue(wishlist.contains("p001"), "Should contain first product");
        assertTrue(wishlist.contains("p002"), "Should contain second product");
        assertTrue(wishlist.contains("p003"), "Should contain third product");
    }

    @Test
    @DisplayName("Should not add duplicate products to wishlist")
    void testAddDuplicateProduct() {
        wishlistService.addToWishlist("user1", "p001");
        wishlistService.addToWishlist("user1", "p001");

        Set<String> wishlist = wishlistService.getWishlist("user1");
        assertEquals(1, wishlist.size(), "Wishlist should not contain duplicates");
    }

    @Test
    @DisplayName("Should remove product from wishlist")
    void testRemoveFromWishlist() {
        wishlistService.addToWishlist("user1", "p001");
        wishlistService.addToWishlist("user1", "p002");

        wishlistService.removeFromWishlist("user1", "p001");
        Set<String> wishlist = wishlistService.getWishlist("user1");

        assertFalse(wishlist.contains("p001"), "Removed product should not be in wishlist");
        assertTrue(wishlist.contains("p002"), "Other products should remain");
        assertEquals(1, wishlist.size(), "Wishlist should have 1 item remaining");
    }

    @Test
    @DisplayName("Should handle removing non-existent product")
    void testRemoveNonExistentProduct() {
        wishlistService.addToWishlist("user1", "p001");
        assertDoesNotThrow(() -> wishlistService.removeFromWishlist("user1", "p999"),
            "Should handle removing non-existent product gracefully");

        Set<String> wishlist = wishlistService.getWishlist("user1");
        assertEquals(1, wishlist.size(), "Wishlist size should remain unchanged");
    }

    @Test
    @DisplayName("Should maintain separate wishlists for different users")
    void testMultipleUserWishlists() {
        wishlistService.addToWishlist("user1", "p001");
        wishlistService.addToWishlist("user2", "p002");

        Set<String> wishlist1 = wishlistService.getWishlist("user1");
        Set<String> wishlist2 = wishlistService.getWishlist("user2");

        assertTrue(wishlist1.contains("p001"), "User1 should have their product");
        assertFalse(wishlist1.contains("p002"), "User1 should not have user2's product");
        assertTrue(wishlist2.contains("p002"), "User2 should have their product");
        assertFalse(wishlist2.contains("p001"), "User2 should not have user1's product");
    }

    @Test
    @DisplayName("Should return empty wishlist for new user")
    void testGetEmptyWishlist() {
        Set<String> wishlist = wishlistService.getWishlist("newuser");

        assertNotNull(wishlist, "Wishlist should not be null");
        assertTrue(wishlist.isEmpty(), "Wishlist should be empty for new user");
    }

    @Test
    @DisplayName("Should handle guest user wishlist")
    void testGuestUserWishlist() {
        wishlistService.addToWishlist("guest", "p001");
        Set<String> wishlist = wishlistService.getWishlist("guest");

        assertEquals(1, wishlist.size(), "Guest wishlist should work");
        assertTrue(wishlist.contains("p001"), "Guest wishlist should contain added product");
    }

    @Test
    @DisplayName("Should handle null username by converting to guest")
    void testNullUsername() {
        wishlistService.addToWishlist(null, "p001");
        Set<String> wishlist = wishlistService.getWishlist(null);

        assertTrue(wishlist.contains("p001"), "Null username should be treated as guest");
    }

    @Test
    @DisplayName("Should return unmodifiable wishlist")
    void testUnmodifiableWishlist() {
        wishlistService.addToWishlist("user1", "p001");
        Set<String> wishlist = wishlistService.getWishlist("user1");

        assertThrows(UnsupportedOperationException.class, () -> wishlist.add("p002"),
            "Returned wishlist should be unmodifiable");
    }

    @Test
    @DisplayName("Should preserve insertion order in wishlist")
    void testWishlistOrder() {
        wishlistService.addToWishlist("user1", "p001");
        wishlistService.addToWishlist("user1", "p002");
        wishlistService.addToWishlist("user1", "p003");

        Set<String> wishlist = wishlistService.getWishlist("user1");
        String[] items = wishlist.toArray(new String[0]);

        assertEquals("p001", items[0], "First added should be first in order");
        assertEquals("p002", items[1], "Second added should be second in order");
        assertEquals("p003", items[2], "Third added should be third in order");
    }
}

