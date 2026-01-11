package ci553.shoppingcenter.service;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceServiceTest {
    @Test
    void writesTextFallbackWhenPdfboxMissing() throws Exception {
        Path tmp = Files.createTempDirectory("invtest");
        Path out = tmp.resolve("invoice_test.pdf");
        String content = "Line1\nLine2";
        InvoiceService.writePdf(out, content);
        Path txt = tmp.resolve("invoice_test.txt");
        assertTrue(Files.exists(txt), "Text invoice should exist");
        String got = Files.readString(txt);
        assertEquals(content, got);
    }
}

