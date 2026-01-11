package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public final class NotificationService {
    private NotificationService() {}

    public static void notify(String message) {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            Path log = StorageLocation.dataPath.resolve("notifications.log");
            String entry = Instant.now().toString() + " - " + message + "\n";
            if (!Files.exists(log)) {
                Files.writeString(log, entry, StandardCharsets.UTF_8);
            } else {
                Files.writeString(log, entry, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            // ignore in this simple implementation
        }
    }

    public static String readAll() {
        try {
            Path log = StorageLocation.dataPath.resolve("notifications.log");
            if (!Files.exists(log)) return "";
            return Files.readString(log, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}

