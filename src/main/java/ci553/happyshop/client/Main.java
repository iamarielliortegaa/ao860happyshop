package ci553.happyshop.client;

import ci553.happyshop.client.shoppingcenter.ShoppingCenterView;
import javax.swing.SwingUtilities;

/**
 * Main Application Launcher - Modernized Shopping Center
 * Launches ONLY the unified Shopping Center interface
 *
 * @version 4.0 - Fully Modernized
 * @author Arielli Ortega, University of Brighton
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Launch the modernized Shopping Center
            ShoppingCenterView shoppingCenter = new ShoppingCenterView(null, null);
            shoppingCenter.setVisible(true);
        });
    }
}