package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class ActionLogger {
    private static final Path LOG = StorageLocation.dataPath.resolve("actions.log");

    public static void log(String entry) {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            String line = Instant.now().toString() + " - " + entry + "\n";
            if (!Files.exists(LOG)) Files.writeString(LOG, line, StandardCharsets.UTF_8);
            else Files.writeString(LOG, line, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            // ignore
        }
    }
}

