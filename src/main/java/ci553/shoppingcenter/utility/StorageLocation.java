package ci553.shoppingcenter.utility;

import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageLocation {
    public static final String imageFolder = "images/";
    public static final Path imageFolderPath = Paths.get(imageFolder);

    public static final String imageResetFolder = "images_resetDB";
    public static final Path imageResetFolderPath = Paths.get(imageResetFolder);

    public static final String ordersFolder = "orders";
    public static final Path ordersPath = Paths.get(ordersFolder);
    public static final Path orderedPath = ordersPath.resolve("ordered");
    public static final Path progressingPath = ordersPath.resolve("progressing");
    public static final Path collectedPath = ordersPath.resolve("collected");

    public static final String orderCounterFile = "orderCounter.txt";
    public static final Path orderCounterPath = ordersPath.resolve(orderCounterFile);

    // Data folder for all CSV persistence
    public static final String dataFolder = "data";
    public static final Path dataPath = Paths.get(dataFolder);

    // Products persistence
    public static final String productsFileName = "products.csv";
    public static final Path productsFilePath = dataPath.resolve(productsFileName);

    // Discounts persistence
    public static final String discountsFileName = "discounts.csv";
    public static final Path discountsFilePath = dataPath.resolve(discountsFileName);

    // Users persistence
    public static final String usersFileName = "users.csv";
    public static final Path usersFilePath = dataPath.resolve(usersFileName);

    // Cart persistence
    public static final String cartsFileName = "carts.csv";
    public static final Path cartsFilePath = dataPath.resolve(cartsFileName);

    // Wishlist persistence
    public static final String wishlistFileName = "wishlists.csv";
    public static final Path wishlistFilePath = dataPath.resolve(wishlistFileName);

    // Reviews persistence
    public static final String reviewsFileName = "reviews.csv";
    public static final Path reviewsFilePath = dataPath.resolve(reviewsFileName);

    // Order tracking persistence
    public static final String orderTrackingFileName = "order_tracking.csv";
    public static final Path orderTrackingFilePath = dataPath.resolve(orderTrackingFileName);

    // Activity logs persistence
    public static final String activityLogFileName = "activity_log.csv";
    public static final Path activityLogFilePath = dataPath.resolve(activityLogFileName);

    // Inventory changes log
    public static final String inventoryLogFileName = "inventory_log.csv";
    public static final Path inventoryLogFilePath = dataPath.resolve(inventoryLogFileName);
}


