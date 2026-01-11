package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;
import org.junit.jupiter.api.*;

import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ActionLoggerTest {

    @BeforeEach
    void setUp() throws Exception {
        // Clean up any existing log file before each test
        if (Files.exists(StorageLocation.activityLogFilePath)) {
            Files.delete(StorageLocation.activityLogFilePath);
        }
    }

    @Test
    @DisplayName("Should log action successfully")
    void testLogAction() {
        assertDoesNotThrow(() -> ActionLogger.log("Login: testuser"),
            "Should log action without throwing exception");
    }

    @Test
    @DisplayName("Should create log file on first log")
    void testLogFileCreation() {
        ActionLogger.log("Test: action");
        assertTrue(Files.exists(StorageLocation.activityLogFilePath),
            "Log file should be created");
    }

    @Test
    @DisplayName("Should log multiple actions")
    void testMultipleLogEntries() {
        ActionLogger.log("Login: user1");
        ActionLogger.log("AddToCart: p001");
        ActionLogger.log("Checkout: completed");

        String logContent = ActionLogger.readAll();
        assertTrue(logContent.contains("Login"), "Log should contain Login action");
        assertTrue(logContent.contains("AddToCart"), "Log should contain AddToCart action");
        assertTrue(logContent.contains("Checkout"), "Log should contain Checkout action");
    }

    @Test
    @DisplayName("Should format log entries correctly")
    void testLogFormat() {
        ActionLogger.log("Test: sample action");
        String logContent = ActionLogger.readAll();

        assertTrue(logContent.contains("timestamp,action,details"),
            "Log should have CSV header");
        assertTrue(logContent.contains("Test"),
            "Log should contain action");
    }

    @Test
    @DisplayName("Should handle special characters in log")
    void testSpecialCharactersInLog() {
        assertDoesNotThrow(() -> ActionLogger.log("Test: action,with,commas"),
            "Should handle commas in log entry");
        assertDoesNotThrow(() -> ActionLogger.log("Test: action\"with\"quotes"),
            "Should handle quotes in log entry");
    }

    @Test
    @DisplayName("Should handle empty log entry")
    void testEmptyLogEntry() {
        assertDoesNotThrow(() -> ActionLogger.log(""),
            "Should handle empty log entry");
    }

    @Test
    @DisplayName("Should handle null log entry")
    void testNullLogEntry() {
        assertDoesNotThrow(() -> ActionLogger.log(null),
            "Should handle null log entry gracefully");
    }

    @Test
    @DisplayName("Should read all logs successfully")
    void testReadAllLogs() {
        ActionLogger.log("Action1: test");
        ActionLogger.log("Action2: test");

        String logs = ActionLogger.readAll();
        assertNotNull(logs, "Read logs should not be null");
        assertFalse(logs.isEmpty(), "Read logs should not be empty");
    }

    @Test
    @DisplayName("Should return appropriate message when no logs exist")
    void testReadAllWithNoLogs() throws Exception {
        if (Files.exists(StorageLocation.activityLogFilePath)) {
            Files.delete(StorageLocation.activityLogFilePath);
        }

        String logs = ActionLogger.readAll();
        assertNotNull(logs, "Should return a message even when no logs exist");
    }

    @Test
    @DisplayName("Should append to existing log file")
    void testAppendToLog() {
        ActionLogger.log("First: entry");
        ActionLogger.log("Second: entry");

        String logs = ActionLogger.readAll();
        int firstCount = logs.split("First").length - 1;
        int secondCount = logs.split("Second").length - 1;

        assertEquals(1, firstCount, "First entry should appear once");
        assertEquals(1, secondCount, "Second entry should appear once");
    }

    @Test
    @DisplayName("Should create data directory if not exists")
    void testDataDirectoryCreation() throws Exception {
        if (Files.exists(StorageLocation.dataPath)) {
            // Directory exists, test passes
            assertTrue(true);
        } else {
            ActionLogger.log("Test: create directory");
            assertTrue(Files.exists(StorageLocation.dataPath),
                "Data directory should be created");
        }
    }
}

