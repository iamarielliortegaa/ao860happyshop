package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountServiceTest {
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
    }

    @Test
    void testAllRegisteredDiscountCodes() {
        // Test all valid codes with correct percentages
        assertEquals(10, discountService.validateCode("SAVE10"));
        assertEquals(5, discountService.validateCode("WELCOME5"));
        assertEquals(50, discountService.validateCode("BLACKFRIDAY"));
    }

    @Test
    void testCaseInsensitivity() {
        // Codes should work regardless of case
        assertEquals(10, discountService.validateCode("save10"));
        assertEquals(10, discountService.validateCode("SAVE10"));
        assertEquals(10, discountService.validateCode("SaVe10"));

        assertEquals(5, discountService.validateCode("welcome5"));
        assertEquals(5, discountService.validateCode("WELCOME5"));

        assertEquals(50, discountService.validateCode("blackfriday"));
        assertEquals(50, discountService.validateCode("BLACKFRIDAY"));
        assertEquals(50, discountService.validateCode("BlackFriday"));
    }

    @Test
    void testInvalidCodes() {
        // Invalid codes should return 0
        assertEquals(0, discountService.validateCode("not-a-code"));
        assertEquals(0, discountService.validateCode("INVALID"));
        assertEquals(0, discountService.validateCode(""));
        assertEquals(0, discountService.validateCode(null));
        assertEquals(0, discountService.validateCode("EXPIRED"));
    }

    @Test
    void testWhitespaceHandling() {
        // Codes should work with leading/trailing whitespace
        assertEquals(10, discountService.validateCode(" SAVE10 "));
        assertEquals(5, discountService.validateCode("  WELCOME5  "));
        assertEquals(50, discountService.validateCode(" BLACKFRIDAY "));
    }

    @Test
    void testListAllCodes() {
        Map<String, Integer> allCodes = discountService.listAll();

        // Verify all default codes are present
        assertTrue(allCodes.containsKey("SAVE10"));
        assertTrue(allCodes.containsKey("WELCOME5"));
        assertTrue(allCodes.containsKey("BLACKFRIDAY"));

        // Verify percentages
        assertEquals(10, allCodes.get("SAVE10"));
        assertEquals(5, allCodes.get("WELCOME5"));
        assertEquals(50, allCodes.get("BLACKFRIDAY"));
    }

    @Test
    void testAddAndRemoveDiscountCodes() {
        // Add a new code
        discountService.add("NEWCODE", 15);
        assertEquals(15, discountService.validateCode("NEWCODE"));

        // Remove the code
        discountService.remove("NEWCODE");
        assertEquals(0, discountService.validateCode("NEWCODE"));
    }

    @Test
    void testDiscountPercentageBounds() {
        // Test that percentages are clamped to 0-100
        discountService.add("OVER100", 150);
        assertEquals(100, discountService.validateCode("OVER100"));

        discountService.add("NEGATIVE", -10);
        assertEquals(0, discountService.validateCode("NEGATIVE"));
    }
}

