package ci553.shoppingcenter.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileStorage {
    private FileStorage() {}

    public static void ensureDir(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public static String safeReadString(Path path) throws IOException {
        if (!Files.exists(path)) return "";
        return Files.readString(path);
    }

    public static void safeWriteString(Path path, String content) throws IOException {
        ensureDir(path.getParent());
        Files.writeString(path, content);
    }
}

