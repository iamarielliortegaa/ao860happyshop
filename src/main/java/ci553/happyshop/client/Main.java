package ci553.happyshop.client;

import ci553.happyshop.server.DatabaseRW;
import ci553.happyshop.server.OrderHub;
import ci553.happyshop.shoppingcenter.ShoppingCenterView;

/**
 * Main entry point for the Happy Shop Shopping Center application.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseRW database = new DatabaseRW();
        
        // Initialize order hub
        OrderHub orderHub = new OrderHub(database);
        
        // Launch Shopping Center View
        ShoppingCenterView shoppingCenter = new ShoppingCenterView(database, orderHub);
        shoppingCenter.setVisible(true);
    }
}
