package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActionLogger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        initializeLogFile();
    }

    private static void initializeLogFile() {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            if (!Files.exists(StorageLocation.activityLogFilePath)) {
                try (BufferedWriter writer = Files.newBufferedWriter(StorageLocation.activityLogFilePath, StandardCharsets.UTF_8)) {
                    writer.write("timestamp,action,details");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing activity log: " + e.getMessage());
        }
    }

    public static void log(String entry) {
        try {
            // Parse entry to extract action and details
            String[] parts = entry.split(":", 2);
            String action = parts.length > 0 ? parts[0].trim() : "UNKNOWN";
            String details = parts.length > 1 ? parts[1].trim().replace(",", ";") : "";

            String timestamp = LocalDateTime.now().format(formatter);
            String logLine = String.format("%s,%s,%s", timestamp, action, details);

            Files.writeString(StorageLocation.activityLogFilePath,
                logLine + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error logging activity: " + e.getMessage());
        }
    }

    public static String readAll() {
        try {
            if (Files.exists(StorageLocation.activityLogFilePath)) {
                return Files.readString(StorageLocation.activityLogFilePath, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.err.println("Error reading activity log: " + e.getMessage());
        }
        return "No activity logs available.";
    }
}

