# Running the Shopping Center (modernized) UI

Prerequisites
- JDK 21 (or matching project JDK).
- Maven installed (recommended) OR open in an IDE with JavaFX configured.

Quick commands

Build and compile:
```powershell
mvn -U -DskipTests clean compile
```

Run tests:
```powershell
mvn test
```

Run the JavaFX app (development):
```powershell
mvn javafx:run
```

Build an executable fat JAR (packaging):
```powershell
mvn -B clean package
# produced artifact: target/HappyShop-1.0-SNAPSHOT-jar-with-dependencies.jar
# Run the jar (ensure JavaFX dependencies are on the module path or use a JRE that includes JavaFX):
java -jar target/HappyShop-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Behavior and notes

- Admin / demo accounts:
  - admin / adminpass  (administrator — sees Settings and Order Tracker)
  - customer / custpass (demo customer)
  - Guests can checkout by entering a display name at checkout.
- Orders:
  - Orders are saved initially to `orders/progressing/` as text receipts. Admin can promote orders to `orders/ordered/` and then `orders/collected/`.
- Discounts:
  - Discount codes persisted in `data/discounts.csv`. Seeded codes: SAVE10, WELCOME5, BLACKFRIDAY.
- Products:
  - Products persisted in `data/products.csv`. Admins can add/remove products via Settings.
- Notifications:
  - Basic notification log is saved to `data/notifications.log`. Admin Order Tracker shows notifications and has a Refresh button to re-read logs.

Troubleshooting
- If `mvn` is not found, install Maven or run the project from IntelliJ (set JavaFX SDK in run configuration and run `ci553.shoppingcenter.client.Main`).

Next steps
- I can implement richer UX, product images, emails, or packaging for specific platforms (Windows EXE, Mac app) — tell me which you prefer next.
