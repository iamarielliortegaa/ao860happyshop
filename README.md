# Shopping Center (Modernized)

This project is a modernized shopping center JavaFX application derived from the legacy HappyShop.

Quick run (with Maven):

```powershell
mvn -B clean verify
mvn javafx:run
```

Demo accounts
- admin / adminpass  (admin)
- customer / custpass (customer)

Paths used by the app (relative to project root):
- data/ : product CSV, reviews, wishlist, notifications
- orders/ : progressing/ ordered/ collected/ invoices
- images/ : product images

Notes
- PDF export uses Apache PDFBox if available. If PDFBox isn't present or fails, a `.txt` invoice fallback is written.
- If you see missing `mvn` command, install Maven or run from an IDE (IntelliJ) and run the `Main` class.

If you run into any build/test failures, paste the Maven output and I'll fix them immediately.
