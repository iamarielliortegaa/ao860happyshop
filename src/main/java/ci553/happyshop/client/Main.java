package ci553.happyshop.client;

import ci553.happyshop.client.shoppingcenter.ShoppingCenterView;
import ci553.happyshop.dbAccess.DatabaseRW;
import ci553.happyshop.dbAccess.DatabaseRWFactory;
import ci553.happyshop.middle.OrderHub;

import javax.swing.SwingUtilities;

/**
 * Main Application Launcher - Fully Modernized Shopping Center
 * Launches ONLY the unified Shopping Center interface with Welcome Screen
 * All old HappyShop clients removed - clean, modern system
 *
 * @version 5.0 - Complete Modernization
 * @author Arielli Ortega, University of Brighton
 */
public class Main {

    public static void main(String[] args) {
        // Launch Shopping Center using Swing
        SwingUtilities.invokeLater(() -> {
            // Initialize backend components
            DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();
            OrderHub orderHub = OrderHub.getOrderHub();
            orderHub.initializeOrderMap();
            
            // Launch the modernized Shopping Center with Welcome Screen
            ShoppingCenterView shoppingCenter = new ShoppingCenterView(databaseRW, orderHub);
            shoppingCenter.setVisible(true);
        });
    }
}
