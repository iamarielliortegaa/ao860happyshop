package ci553.happyshop.client.shoppingcenter;

import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.utility.ProductListFormatter;
import ci553.happyshop.catalogue.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Shopping Center View - GUI for customer shopping interface
 * Fixed compilation errors with correct imports and method calls
 */
public class ShoppingCenterView extends JFrame {
    private JTextArea displayArea;
    private JTextField productIdField;
    private JTextField quantityField;
    private JButton searchButton;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private JButton checkoutButton;
    
    private OrderHub orderHub;
    private DatabaseRW database;
    
    public ShoppingCenterView() {
        setTitle("Happy Shop - Shopping Center");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Initialize components
        initializeComponents();
        
        // Initialize database and order hub
        try {
            database = new DatabaseRW();
            orderHub = new OrderHub(database);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error initializing system: " + e.getMessage(),
                "Initialization Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeComponents() {
        // Top panel for product search
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField(10);
        topPanel.add(productIdField);
        
        topPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        topPanel.add(quantityField);
        
        searchButton = new JButton("Search Product");
        searchButton.addActionListener(new SearchButtonListener());
        topPanel.add(searchButton);
        
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new AddToCartListener());
        topPanel.add(addToCartButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for display
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for cart operations
        JPanel bottomPanel = new JPanel(new FlowLayout());
        viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(new ViewCartListener());
        bottomPanel.add(viewCartButton);
        
        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new CheckoutListener());
        bottomPanel.add(checkoutButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Display sample data
        displaySampleProducts();
    }
    
    private void displaySampleProducts() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Welcome to Happy Shop ===\n\n");
        sb.append("Sample Products Available:\n");
        sb.append("---------------------------\n");
        sb.append("Product ID | Description           | Price\n");
        sb.append("--------------------------------------------------\n");
        sb.append("0001       | Laptop Computer       | £799.99\n");
        sb.append("0002       | Wireless Mouse        | £24.99\n");
        sb.append("0003       | USB Keyboard          | £34.99\n");
        sb.append("0004       | Monitor 24\"           | £189.99\n");
        sb.append("0005       | HDMI Cable            | £12.99\n");
        sb.append("\nEnter a Product ID to search for details.\n");
        displayArea.setText(sb.toString());
    }
    
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String productId = productIdField.getText().trim();
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(ShoppingCenterView.this,
                    "Please enter a product ID",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Use searchByProductId() instead of getProductDetails()
                Product product = database.searchByProductId(productId);
                if (product != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Product Details:\n");
                    sb.append("---------------------------\n");
                    // Use getProductId(), getProductDescription(), getUnitPrice()
                    // instead of getProductNum(), getDescription(), getPrice()
                    sb.append("Product ID: ").append(product.getProductId()).append("\n");
                    sb.append("Description: ").append(product.getProductDescription()).append("\n");
                    sb.append("Price: £").append(String.format("%.2f", product.getUnitPrice())).append("\n");
                    sb.append("Stock Available: ").append(product.getQuantity()).append("\n");
                    displayArea.setText(sb.toString());
                } else {
                    displayArea.setText("Product not found: " + productId);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ShoppingCenterView.this,
                    "Error searching for product: " + ex.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class AddToCartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String productId = productIdField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            
            if (productId.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(ShoppingCenterView.this,
                    "Please enter both Product ID and Quantity",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    throw new NumberFormatException("Quantity must be positive");
                }
                
                // Add to cart logic would go here
                displayArea.append("\nAdded " + quantity + " x Product " + productId + " to cart.\n");
                
                // Clear fields
                productIdField.setText("");
                quantityField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ShoppingCenterView.this,
                    "Please enter a valid quantity",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ShoppingCenterView.this,
                    "Error adding to cart: " + ex.getMessage(),
                    "Cart Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class ViewCartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            displayArea.setText("Shopping Cart:\n---------------------------\n(Cart contents would be displayed here)\n");
        }
    }
    
    private class CheckoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(ShoppingCenterView.this,
                "Proceed to checkout?",
                "Checkout Confirmation",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                displayArea.setText("Processing checkout...\n");
                // Checkout logic would go here
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingCenterView view = new ShoppingCenterView();
            view.setVisible(true);
        });
    }
}
