package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

public class UserService {
    private final Map<String, UserRecord> users = new ConcurrentHashMap<>();

    public UserService() {
        // seed an admin user and a demo customer
        users.put("admin", new UserRecord(new User("admin", "Administrator"), "adminpass", true));
        users.put("customer", new UserRecord(new User("customer", "Demo Customer"), "custpass", false));
    }

    public boolean register(String username, String displayName, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new UserRecord(new User(username, displayName), password, false));
        return true;
    }

    public Optional<User> login(String username, String password) {
        UserRecord r = users.get(username);
        if (r == null) return Optional.empty();
        if (r.password.equals(password)) return Optional.of(r.user);
        return Optional.empty();
    }

    public boolean isAdmin(String username) {
        UserRecord r = users.get(username);
        return r != null && r.admin;
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        UserRecord r = users.get(username);
        if (r == null) return false;
        if (!r.password.equals(currentPassword)) return false;
        r.password = newPassword;
        return true;
    }

    private static class UserRecord {
        final User user;
        String password;
        final boolean admin;

        UserRecord(User user, String password, boolean admin) {
            this.user = user;
            this.password = password;
            this.admin = admin;
        }
    }
}
