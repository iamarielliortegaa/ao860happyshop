package ci553.shoppingcenter.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;

public class InvoiceService {
    /**
     * Write an invoice as PDF if PDFBox is on the classpath; otherwise write a plain text file.
     * Also always write a text invoice alongside the PDF so both formats are available.
     */
    public static void writePdf(Path outFile, String content) throws IOException {
        // Ensure parent directory exists for both PDF and fallback text
        Path parent = outFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        boolean pdfWritten = false;
        try {
            // Try to detect PDFBox on classpath
            Class<?> pdDocClass = Class.forName("org.apache.pdfbox.pdmodel.PDDocument");

            // If found, attempt to use it via reflection
            Object doc = pdDocClass.getDeclaredConstructor().newInstance();

            Class<?> pdPageClass = Class.forName("org.apache.pdfbox.pdmodel.PDPage");
            Class<?> pdRectClass = Class.forName("org.apache.pdfbox.pdmodel.common.PDRectangle");
            Object letter = pdRectClass.getField("LETTER").get(null);
            Object page = pdPageClass.getDeclaredConstructor(pdRectClass).newInstance(letter);

            pdDocClass.getMethod("addPage", pdPageClass).invoke(doc, page);

            Class<?> csClass = Class.forName("org.apache.pdfbox.pdmodel.PDPageContentStream");
            // PDPageContentStream(PDDocument document, PDPage page)
            Object cs = csClass.getDeclaredConstructor(pdDocClass, pdPageClass).newInstance(doc, page);

            Class<?> pdTypeFont = Class.forName("org.apache.pdfbox.pdmodel.font.PDType1Font");
            Object helvetica = pdTypeFont.getField("HELVETICA").get(null);

            // begin text
            csClass.getMethod("beginText").invoke(cs);
            csClass.getMethod("setFont", Class.forName("org.apache.pdfbox.pdmodel.font.PDFont"), float.class).invoke(cs, helvetica, 12f);
            csClass.getMethod("newLineAtOffset", float.class, float.class).invoke(cs, 50f, 700f);

            String[] lines = content.split("\n");
            for (String line : lines) {
                csClass.getMethod("showText", String.class).invoke(cs, line.replaceAll("\r", ""));
                csClass.getMethod("newLineAtOffset", float.class, float.class).invoke(cs, 0f, -14f);
            }

            csClass.getMethod("endText").invoke(cs);
            csClass.getMethod("close").invoke(cs);

            pdDocClass.getMethod("save", File.class).invoke(doc, outFile.toFile());
            pdDocClass.getMethod("close").invoke(doc);
            pdfWritten = true;
        } catch (ClassNotFoundException cnfe) {
            // PDFBox not available; fall through to text fallback
        } catch (Exception e) {
            // Reflection or PDFBox runtime error — fall back to text invoice
        }

        // Always write a plain text invoice alongside the PDF
        try {
            String filename = outFile.getFileName().toString();
            String base = filename;
            int idx = filename.lastIndexOf('.');
            if (idx > 0) base = filename.substring(0, idx);
            Path txt = (parent == null) ? Path.of(base + ".txt") : parent.resolve(base + ".txt");
            Files.writeString(txt, content, StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            // If writing text fails and PDF wasn't written, rethrow; otherwise ignore
            if (!pdfWritten) throw ioe;
        }
    }
}
