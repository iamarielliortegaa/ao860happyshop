package ci553.shoppingcenter.service;

import ci553.shoppingcenter.utility.StorageLocation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DiscountService {
    private final Map<String, Integer> codes = new ConcurrentHashMap<>();

    public DiscountService() {
        // try load from disk
        try {
            if (Files.exists(StorageLocation.discountsFilePath)) {
                List<String> lines = Files.readAllLines(StorageLocation.discountsFilePath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        try {
                            String code = parts[0].trim().toUpperCase();
                            int pct = Integer.parseInt(parts[1].trim());
                            codes.put(code, pct);
                        } catch (NumberFormatException nfe) {
                            // skip
                        }
                    }
                }
            }
        } catch (IOException e) {
            // ignore and seed defaults
        }

        if (codes.isEmpty()) {
            // seed some discount codes (percentage values)
            codes.put("SAVE10", 10);
            codes.put("WELCOME5", 5);
            codes.put("BLACKFRIDAY", 50);
            persist();
        }
    }

    /**
     * Returns the discount percent for the code, or 0 if invalid.
     */
    public int validateCode(String code) {
        if (code == null) return 0;
        return codes.getOrDefault(code.trim().toUpperCase(), 0);
    }

    public Map<String,Integer> listAll() {
        return Map.copyOf(codes);
    }

    public synchronized void add(String code, int percent) {
        if (code == null || code.trim().isEmpty()) return;
        codes.put(code.trim().toUpperCase(), Math.max(0, Math.min(100, percent)));
        persist();
    }

    public synchronized void remove(String code) {
        if (code == null) return;
        codes.remove(code.trim().toUpperCase());
        persist();
    }

    private synchronized void persist() {
        try {
            Files.createDirectories(StorageLocation.dataPath);
            List<String> lines = codes.entrySet().stream().map(e -> e.getKey() + "," + e.getValue()).collect(Collectors.toList());
            Files.write(StorageLocation.discountsFilePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // ignore for now
        }
    }
}
