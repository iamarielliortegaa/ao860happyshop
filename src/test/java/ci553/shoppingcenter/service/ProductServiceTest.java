package ci553.shoppingcenter.service;

import ci553.shoppingcenter.model.Product;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    @Test
    void persistAndLoadProducts() throws Exception {
        ProductService ps = new ProductService();
        Product p = new Product("tx1","Tst",1.23);
        ps.add(p);
        ps.persist();
        Path file = ci553.shoppingcenter.utility.StorageLocation.productsFilePath;
        assertTrue(Files.exists(file));
        List<String> lines = Files.readAllLines(file);
        assertTrue(lines.stream().anyMatch(l -> l.contains("tx1")));
    }
}

