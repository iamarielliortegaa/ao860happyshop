package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test guest user shopping permissions and flow
 */
public class GuestUserTest {
    private ProductService productService;
    private CartService cartService;
    private DiscountService discountService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        cartService = new CartService();
        discountService = new DiscountService();
        orderService = new OrderService();
    }

    @Test
    void testGuestCanAddToCart() {
        // Guest user identifier
        String guestUser = "guest";

        // Get a product
        Product product = productService.listAll().get(0);

        // Add to cart
        cartService.addToCart(guestUser, product, 2);

        // Verify cart
        Map<String, Integer> cart = cartService.getCart(guestUser);
        assertTrue(cart.containsKey(product.getId()));
        assertEquals(2, cart.get(product.getId()));
    }

    @Test
    void testGuestCanUpdateCart() {
        String guestUser = "guest";
        Product product = productService.listAll().get(0);

        // Add to cart
        cartService.addToCart(guestUser, product, 1);

        // Update quantity
        cartService.updateCart(guestUser, product.getId(), 5);

        // Verify
        Map<String, Integer> cart = cartService.getCart(guestUser);
        assertEquals(5, cart.get(product.getId()));
    }

    @Test
    void testGuestCanRemoveFromCart() {
        String guestUser = "guest";
        Product product = productService.listAll().get(0);

        // Add to cart
        cartService.addToCart(guestUser, product, 3);

        // Remove from cart
        cartService.removeFromCart(guestUser, product.getId());

        // Verify removed
        Map<String, Integer> cart = cartService.getCart(guestUser);
        assertFalse(cart.containsKey(product.getId()));
    }

    @Test
    void testGuestCanUseDiscountCodes() {
        String guestUser = "guest";

        // All discount codes should work for guests
        assertEquals(10, discountService.validateCode("SAVE10"));
        assertEquals(5, discountService.validateCode("WELCOME5"));
        assertEquals(50, discountService.validateCode("BLACKFRIDAY"));
    }

    @Test
    void testGuestCanPlaceOrder() {
        String guestUser = "guest-buyer-test";

        // Create a cart
        Map<String, Integer> cart = new HashMap<>();
        Product product = productService.listAll().get(0);
        cart.put(product.getId(), 2);

        // Place order with discount
        String receipt = orderService.placeOrder(
            guestUser,
            cart,
            id -> productService.findById(id).map(Product::getPrice).orElse(0.0),
            10, // 10% discount
            id -> productService.findById(id).map(Product::getName).orElse(id)
        );

        // Verify receipt was generated
        assertNotNull(receipt);
        assertFalse(receipt.isEmpty());
        assertTrue(receipt.contains(guestUser));
        assertTrue(receipt.contains("Discount (10%)"));
    }

    @Test
    void testGuestAndCustomerHaveSameShoppingPermissions() {
        String guestUser = "guest";
        String customerUser = "customer-test";

        Product product = productService.listAll().get(0);

        // Both can add to cart
        cartService.addToCart(guestUser, product, 1);
        cartService.addToCart(customerUser, product, 1);

        // Both have items in cart
        assertFalse(cartService.getCart(guestUser).isEmpty());
        assertFalse(cartService.getCart(customerUser).isEmpty());

        // Both can use discount codes
        int guestDiscount = discountService.validateCode("SAVE10");
        int customerDiscount = discountService.validateCode("SAVE10");
        assertEquals(guestDiscount, customerDiscount);
    }

    @Test
    void testGuestCanClearCart() {
        String guestUser = "guest";
        Product product = productService.listAll().get(0);

        // Add items
        cartService.addToCart(guestUser, product, 5);
        assertFalse(cartService.getCart(guestUser).isEmpty());

        // Clear cart
        cartService.clearCart(guestUser);

        // Verify empty
        assertTrue(cartService.getCart(guestUser).isEmpty());
    }

    @Test
    void testMultipleGuestUsers() {
        // Each guest should have independent carts
        String guest1 = "guest1";
        String guest2 = "guest2";

        Product product1 = productService.listAll().get(0);
        Product product2 = productService.listAll().get(1);

        cartService.addToCart(guest1, product1, 2);
        cartService.addToCart(guest2, product2, 3);

        // Verify independent carts
        Map<String, Integer> cart1 = cartService.getCart(guest1);
        Map<String, Integer> cart2 = cartService.getCart(guest2);

        assertTrue(cart1.containsKey(product1.getId()));
        assertFalse(cart1.containsKey(product2.getId()));

        assertFalse(cart2.containsKey(product1.getId()));
        assertTrue(cart2.containsKey(product2.getId()));
    }
}

