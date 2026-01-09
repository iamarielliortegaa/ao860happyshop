package ci553.happyshop;

import ci553.happyshop.ui.ShoppingCenterView;
import javax.swing.SwingUtilities;

/**
 * Modern Shopping Center Application
 * Standalone modernized shopping interface
 * 
 * @version 4.0
 * @author Arielli Ortega
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingCenterView shoppingCenter = new ShoppingCenterView();
            shoppingCenter.setVisible(true);
        });
    }
}
