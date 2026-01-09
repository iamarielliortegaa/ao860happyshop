package ci553.happyshop.client;

import ci553.happyshop.client.shoppingcenter.ShoppingCenterView;
import ci553.happyshop.dbAccess.DatabaseRW;
import ci553.happyshop.dbAccess.DatabaseRWFactory;
import ci553.happyshop.middle.OrderHub;

import javax.swing.SwingUtilities;

/**
 * Main Application Launcher - Modernized Shopping Center
 * Launches ONLY the new unified Shopping Center interface
 * All old clients have been removed - this is a clean, modern system
 *
 * @version 4.0
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
            
            // Launch the modernized Shopping Center
            ShoppingCenterView shoppingCenter = new ShoppingCenterView(databaseRW, orderHub);
            shoppingCenter.setVisible(true);
        });
    }
}
