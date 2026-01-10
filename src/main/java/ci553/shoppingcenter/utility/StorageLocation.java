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

    // Products persistence
    public static final String dataFolder = "data";
    public static final Path dataPath = Paths.get(dataFolder);
    public static final String productsFileName = "products.csv";
    public static final Path productsFilePath = dataPath.resolve(productsFileName);

    // Discounts persistence
    public static final String discountsFileName = "discounts.csv";
    public static final Path discountsFilePath = dataPath.resolve(discountsFileName);
}
