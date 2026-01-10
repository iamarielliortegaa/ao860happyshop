package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @Test
    void registerLoginChangePassword() {
        UserService us = new UserService();
        boolean registered = us.register("john", "John", "secret");
        assertTrue(registered);
        assertTrue(us.login("john", "secret").isPresent());
        boolean changed = us.changePassword("john", "secret", "newpass");
        assertTrue(changed);
        assertTrue(us.login("john", "newpass").isPresent());
    }
}

