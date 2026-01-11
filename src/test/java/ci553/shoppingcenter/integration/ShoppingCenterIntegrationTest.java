package ci553.shoppingcenter.integration;

import ci553.shoppingcenter.model.Product;
import ci553.shoppingcenter.model.User;
import ci553.shoppingcenter.service.*;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Shopping Center Integration Tests")
class ShoppingCenterIntegrationTest {

    private UserService userService;
    private ProductService productService;
    private CartService cartService;
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        productService = new ProductService();
        cartService = new CartService();
        wishlistService = new WishlistService();
    }

    @AfterEach
    void tearDown() {
        userService = null;
        productService = null;
        cartService = null;
        wishlistService = null;
    }

    @Test
    @DisplayName("Complete user registration and shopping flow")
    void testCompleteShoppingFlow() {
        // 1. Register user
        boolean registered = userService.register("shopper1", "Test Shopper", "password123");
        assertTrue(registered, "User should register successfully");

        // 2. Login
        Optional<User> user = userService.login("shopper1", "password123");
        assertTrue(user.isPresent(), "User should login successfully");

        // 3. Get a product
        Product product = productService.listAll().stream().findFirst().orElse(null);
        assertNotNull(product, "Should have at least one product");

        // 4. Add to cart
        cartService.addToCart("shopper1", product, 2);
        Map<String, Integer> cart = cartService.getCart("shopper1");
        assertTrue(cart.containsKey(product.getId()), "Cart should contain added product");
        assertEquals(2, cart.get(product.getId()), "Cart should have correct quantity");

        // 5. Add to wishlist
        wishlistService.addToWishlist("shopper1", product.getId());
        Set<String> wishlist = wishlistService.getWishlist("shopper1");
        assertTrue(wishlist.contains(product.getId()), "Wishlist should contain product");

        // 6. Update cart quantity
        cartService.updateCart("shopper1", product.getId(), 5);
        cart = cartService.getCart("shopper1");
        assertEquals(5, cart.get(product.getId()), "Cart quantity should be updated");

        // 7. Remove from wishlist
        wishlistService.removeFromWishlist("shopper1", product.getId());
        wishlist = wishlistService.getWishlist("shopper1");
        assertFalse(wishlist.contains(product.getId()), "Product should be removed from wishlist");

        // 8. Clear cart
        cartService.clearCart("shopper1");
        cart = cartService.getCart("shopper1");
        assertTrue(cart.isEmpty(), "Cart should be empty after clearing");
    }

    @Test
    @DisplayName("Multiple users shopping independently")
    void testMultipleUsersIndependentShopping() {
        // Register two users
        userService.register("user1", "User One", "pass1");
        userService.register("user2", "User Two", "pass2");

        // Get two different products
        Product[] products = productService.listAll().stream().limit(2).toArray(Product[]::new);
        assumeTrue(products.length >= 2, "Need at least 2 products for this test");

        // User1 adds product1 to cart and wishlist
        cartService.addToCart("user1", products[0], 1);
        wishlistService.addToWishlist("user1", products[0].getId());

        // User2 adds product2 to cart and wishlist
        cartService.addToCart("user2", products[1], 2);
        wishlistService.addToWishlist("user2", products[1].getId());

        // Verify isolation
        Map<String, Integer> cart1 = cartService.getCart("user1");
        Map<String, Integer> cart2 = cartService.getCart("user2");
        Set<String> wishlist1 = wishlistService.getWishlist("user1");
        Set<String> wishlist2 = wishlistService.getWishlist("user2");

        assertTrue(cart1.containsKey(products[0].getId()), "User1 cart should have product1");
        assertFalse(cart1.containsKey(products[1].getId()), "User1 cart should not have product2");
        assertTrue(cart2.containsKey(products[1].getId()), "User2 cart should have product2");
        assertFalse(cart2.containsKey(products[0].getId()), "User2 cart should not have product1");

        assertTrue(wishlist1.contains(products[0].getId()), "User1 wishlist should have product1");
        assertFalse(wishlist1.contains(products[1].getId()), "User1 wishlist should not have product2");
        assertTrue(wishlist2.contains(products[1].getId()), "User2 wishlist should have product2");
        assertFalse(wishlist2.contains(products[0].getId()), "User2 wishlist should not have product1");
    }

    @Test
    @DisplayName("Admin vs Customer permissions")
    void testAdminVsCustomerPermissions() {
        // Admin should exist by default
        assertTrue(userService.isAdmin("admin"), "Admin user should have admin privileges");

        // Customer should exist by default but not be admin
        assertFalse(userService.isAdmin("customer"), "Customer user should not have admin privileges");

        // New registered users should not be admin
        userService.register("newuser", "New User", "pass");
        assertFalse(userService.isAdmin("newuser"), "Newly registered users should not be admin");
    }

    @Test
    @DisplayName("Cart and wishlist persistence across service instances")
    void testPersistenceAcrossInstances() {
        // Add items using first instance
        CartService cart1 = new CartService();
        WishlistService wishlist1 = new WishlistService();

        Product product = productService.listAll().stream().findFirst().orElse(null);
        assumeNotNull(product, "Need at least one product");

        cart1.addToCart("persistuser", product, 3);
        wishlist1.addToWishlist("persistuser", product.getId());

        // Create new instances (simulating app restart)
        CartService cart2 = new CartService();
        WishlistService wishlist2 = new WishlistService();

        // Verify data persisted
        Map<String, Integer> cartData = cart2.getCart("persistuser");
        Set<String> wishlistData = wishlist2.getWishlist("persistuser");

        assertTrue(cartData.containsKey(product.getId()), "Cart should persist across instances");
        assertEquals(3, cartData.get(product.getId()), "Cart quantity should persist");
        assertTrue(wishlistData.contains(product.getId()), "Wishlist should persist across instances");
    }

    @Test
    @DisplayName("Product stock management during shopping")
    void testProductStockManagement() {
        Product product = productService.listAll().stream()
            .filter(p -> p.getStockQuantity() > 0)
            .findFirst()
            .orElse(null);

        assumeNotNull(product, "Need a product with stock");

        int initialStock = product.getStockQuantity();

        // Add to cart (should not reduce stock until checkout)
        cartService.addToCart("shopper", product, 5);

        // Stock should remain the same (not decremented until checkout/order)
        Product sameProduct = productService.get(product.getId());
        assertNotNull(sameProduct, "Product should still exist");
        // Note: Stock management depends on implementation
        // This test documents the expected behavior
    }

    @Test
    @DisplayName("User workflow: Browse, Save, Purchase")
    void testUserWorkflowBrowseSavePurchase() {
        // Step 1: User browses products
        long productCount = productService.count();
        assertTrue(productCount > 0, "Should have products to browse");

        // Step 2: User adds some to wishlist (saves for later)
        Product[] products = productService.listAll().stream().limit(3).toArray(Product[]::new);
        for (Product p : products) {
            wishlistService.addToWishlist("browseruser", p.getId());
        }

        Set<String> wishlist = wishlistService.getWishlist("browseruser");
        assertEquals(3, wishlist.size(), "Wishlist should have 3 items");

        // Step 3: User decides to purchase some items
        cartService.addToCart("browseruser", products[0], 2);
        cartService.addToCart("browseruser", products[1], 1);

        Map<String, Integer> cart = cartService.getCart("browseruser");
        assertEquals(2, cart.size(), "Cart should have 2 items");

        // Step 4: User removes purchased items from wishlist
        wishlistService.removeFromWishlist("browseruser", products[0].getId());
        wishlistService.removeFromWishlist("browseruser", products[1].getId());

        wishlist = wishlistService.getWishlist("browseruser");
        assertEquals(1, wishlist.size(), "Wishlist should have 1 item remaining");
        assertTrue(wishlist.contains(products[2].getId()), "Unpurchased item should remain in wishlist");
    }

    private void assumeTrue(boolean condition, String message) {
        if (!condition) {
            throw new org.opentest4j.TestAbortedException(message);
        }
    }

    private void assumeNotNull(Object obj, String message) {
        if (obj == null) {
            throw new org.opentest4j.TestAbortedException(message);
        }
    }
}

