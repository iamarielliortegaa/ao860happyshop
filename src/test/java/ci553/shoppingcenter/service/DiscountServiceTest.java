package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscountServiceTest {
    @Test
    void testDiscountCodes() {
        DiscountService ds = new DiscountService();
        assertEquals(10, ds.validateCode("save10"));
        assertEquals(5, ds.validateCode("WELCOME5"));
        assertEquals(0, ds.validateCode("not-a-code"));
    }
}

