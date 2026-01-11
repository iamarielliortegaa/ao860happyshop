package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.User;
import ci553.shoppingcenter.utility.StorageLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

public class UserService {
    private final Map<String, UserRecord> users = new ConcurrentHashMap<>();

    public UserService() {
        loadFromCSV();
        // Ensure default users exist
        if (!users.containsKey("admin")) {
            users.put("admin", new UserRecord(new User("admin", "Administrator"), "adminpass", true));
        }
        if (!users.containsKey("customer")) {
            users.put("customer", new UserRecord(new User("customer", "Demo Customer"), "custpass", false));
        }
        persist();
    }

    private void loadFromCSV() {
        try {
            if (!Files.exists(StorageLocation.usersFilePath)) {
                return; // File doesn't exist yet
            }

            try (BufferedReader reader = Files.newBufferedReader(StorageLocation.usersFilePath, StandardCharsets.UTF_8)) {
                reader.readLine(); // Skip header
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4) {
                        String username = parts[0];
                        String displayName = parts[1];
                        String password = parts[2];
                        boolean admin = Boolean.parseBoolean(parts[3]);
                        users.put(username, new UserRecord(new User(username, displayName), password, admin));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public void persist() {
        try {
            Files.createDirectories(StorageLocation.dataPath);

            try (BufferedWriter writer = Files.newBufferedWriter(StorageLocation.usersFilePath, StandardCharsets.UTF_8)) {
                // Write header
                writer.write("username,displayName,password,admin");
                writer.newLine();

                // Write user data
                for (Map.Entry<String, UserRecord> entry : users.entrySet()) {
                    UserRecord r = entry.getValue();
                    writer.write(String.format("%s,%s,%s,%s",
                        entry.getKey(),
                        r.user.getName(),
                        r.password,
                        r.admin));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error persisting users: " + e.getMessage());
        }
    }

    public boolean register(String username, String displayName, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new UserRecord(new User(username, displayName), password, false));
        persist();
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
        persist();
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

