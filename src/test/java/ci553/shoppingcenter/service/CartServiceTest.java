package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.Product;
import org.junit.jupiter.api.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    private CartService cartService;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
        testProduct1 = new Product("p001", "Test Product 1", 29.99, 100, "Electronics", "test1.jpg");
        testProduct2 = new Product("p002", "Test Product 2", 49.99, 50, "Clothing", "test2.jpg");
    }

    @AfterEach
    void tearDown() {
        cartService = null;
        testProduct1 = null;
        testProduct2 = null;
    }

    @Test
    @DisplayName("Should add product to cart successfully")
    void testAddToCart() {
        cartService.addToCart("user1", testProduct1, 2);
        Map<String, Integer> cart = cartService.getCart("user1");

        assertTrue(cart.containsKey("p001"), "Cart should contain the added product");
        assertEquals(2, cart.get("p001"), "Cart should have correct quantity");
    }

    @Test
    @DisplayName("Should add multiple different products to cart")
    void testAddMultipleProducts() {
        cartService.addToCart("user1", testProduct1, 1);
        cartService.addToCart("user1", testProduct2, 3);

        Map<String, Integer> cart = cartService.getCart("user1");
        assertEquals(2, cart.size(), "Cart should contain 2 different products");
        assertEquals(1, cart.get("p001"), "First product should have quantity 1");
        assertEquals(3, cart.get("p002"), "Second product should have quantity 3");
    }

    @Test
    @DisplayName("Should increment quantity when adding same product twice")
    void testAddSameProductTwice() {
        cartService.addToCart("user1", testProduct1, 2);
        cartService.addToCart("user1", testProduct1, 3);

        Map<String, Integer> cart = cartService.getCart("user1");
        assertEquals(5, cart.get("p001"), "Quantity should be sum of both additions");
    }

    @Test
    @DisplayName("Should handle multiple users with separate carts")
    void testMultipleUserCarts() {
        cartService.addToCart("user1", testProduct1, 1);
        cartService.addToCart("user2", testProduct2, 2);

        Map<String, Integer> cart1 = cartService.getCart("user1");
        Map<String, Integer> cart2 = cartService.getCart("user2");

        assertEquals(1, cart1.size(), "User1 cart should have 1 item");
        assertEquals(1, cart2.size(), "User2 cart should have 1 item");
        assertTrue(cart1.containsKey("p001"), "User1 should have product 1");
        assertTrue(cart2.containsKey("p002"), "User2 should have product 2");
    }

    @Test
    @DisplayName("Should remove product from cart")
    void testRemoveFromCart() {
        cartService.addToCart("user1", testProduct1, 2);
        cartService.addToCart("user1", testProduct2, 1);

        cartService.removeFromCart("user1", "p001");
        Map<String, Integer> cart = cartService.getCart("user1");

        assertFalse(cart.containsKey("p001"), "Removed product should not be in cart");
        assertTrue(cart.containsKey("p002"), "Other products should remain");
    }

    @Test
    @DisplayName("Should update product quantity in cart")
    void testUpdateCart() {
        cartService.addToCart("user1", testProduct1, 2);
        cartService.updateCart("user1", "p001", 5);

        Map<String, Integer> cart = cartService.getCart("user1");
        assertEquals(5, cart.get("p001"), "Quantity should be updated");
    }

    @Test
    @DisplayName("Should remove product when updating quantity to zero")
    void testUpdateCartToZero() {
        cartService.addToCart("user1", testProduct1, 2);
        cartService.updateCart("user1", "p001", 0);

        Map<String, Integer> cart = cartService.getCart("user1");
        assertFalse(cart.containsKey("p001"), "Product should be removed when quantity is 0");
    }

    @Test
    @DisplayName("Should remove product when updating quantity to negative")
    void testUpdateCartToNegative() {
        cartService.addToCart("user1", testProduct1, 2);
        cartService.updateCart("user1", "p001", -5);

        Map<String, Integer> cart = cartService.getCart("user1");
        assertFalse(cart.containsKey("p001"), "Product should be removed when quantity is negative");
    }

    @Test
    @DisplayName("Should clear entire cart")
    void testClearCart() {
        cartService.addToCart("user1", testProduct1, 2);
        cartService.addToCart("user1", testProduct2, 3);

        cartService.clearCart("user1");
        Map<String, Integer> cart = cartService.getCart("user1");

        assertTrue(cart.isEmpty(), "Cart should be empty after clearing");
    }

    @Test
    @DisplayName("Should return empty cart for user with no items")
    void testGetEmptyCart() {
        Map<String, Integer> cart = cartService.getCart("newuser");
        assertNotNull(cart, "Cart should not be null");
        assertTrue(cart.isEmpty(), "Cart should be empty for new user");
    }

    @Test
    @DisplayName("Should handle adding product with quantity less than 1 by defaulting to 1")
    void testAddWithZeroQuantity() {
        cartService.addToCart("user1", testProduct1, 0);
        Map<String, Integer> cart = cartService.getCart("user1");

        assertEquals(1, cart.get("p001"), "Quantity should default to at least 1");
    }

    @Test
    @DisplayName("Should handle guest user cart")
    void testGuestUserCart() {
        cartService.addToCart("guest", testProduct1, 1);
        Map<String, Integer> cart = cartService.getCart("guest");

        assertEquals(1, cart.size(), "Guest cart should work like regular cart");
        assertTrue(cart.containsKey("p001"), "Guest cart should contain added product");
    }

    @Test
    @DisplayName("Should handle removing non-existent product gracefully")
    void testRemoveNonExistentProduct() {
        assertDoesNotThrow(() -> cartService.removeFromCart("user1", "nonexistent"),
            "Should handle removing non-existent product without error");
    }

    @Test
    @DisplayName("Should handle updating non-existent product gracefully")
    void testUpdateNonExistentProduct() {
        assertDoesNotThrow(() -> cartService.updateCart("user1", "nonexistent", 5),
            "Should handle updating non-existent product without error");
    }
}

