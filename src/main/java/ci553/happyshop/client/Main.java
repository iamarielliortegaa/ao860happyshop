package ci553.happyshop.client;

import ci553.happyshop.middle.OrderHub;
import ci553.happyshop.dbAccess.DatabaseRW;
import ci553.happyshop.dbAccess.DatabaseRWFactory;
import ci553.happyshop.client.shoppingcenter.ShoppingCenterView;

/**
 * Main class for the Shopping Center application
 * Launches the modern Shopping Center interface
 */
public class Main {
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    /**
     * Initializes the OrderHub and launches the Shopping Center view
     */
    public void start() {
        try {
            // Initialize database connection
            DatabaseRW db = DatabaseRWFactory.getDatabase();
            
            // Initialize OrderHub
            OrderHub orderHub = new OrderHub(db);
            
            // Launch Shopping Center view
            ShoppingCenterView shoppingCenter = new ShoppingCenterView(orderHub);
            shoppingCenter.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Error starting Shopping Center application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
