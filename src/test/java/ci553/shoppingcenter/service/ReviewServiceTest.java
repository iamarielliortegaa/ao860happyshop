package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewServiceTest {
    @Test
    void addReviewPersistsFile() throws Exception {
        ReviewService rs = new ReviewService();
        String pid = "testprod";
        rs.addReview(pid, "bob", 5, "Great!");
        Path file = ci553.shoppingcenter.utility.StorageLocation.dataPath.resolve("reviews_" + pid + ".csv");
        assertTrue(Files.exists(file));
        List<String> lines = Files.readAllLines(file);
        assertFalse(lines.isEmpty());
    }
}

