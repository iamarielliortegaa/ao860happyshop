package ci553.happyshop.client.shoppingcenter;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.dbAccess.DatabaseRW;
import ci553.happyshop.middle.OrderHub;
import ci553.happyshop.util.ProductListFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Integrated Shopping Center View with sidebar navigation
 * Provides Welcome Screen, Customer Auth, Admin Auth, Shopping Cart, Shipping, and Order Tracking
 * Dark Purple Theme
 */
public class ShoppingCenterView extends JFrame implements Observer {
    private static final long serialVersionUID = 1L;
    
    // Color scheme - Dark Purple Theme
    private static final Color DARK_PURPLE = new Color(45, 20, 70);
    private static final Color MEDIUM_PURPLE = new Color(75, 40, 110);
    private static final Color LIGHT_PURPLE = new Color(140, 100, 180);
    private static final Color ACCENT_PURPLE = new Color(180, 140, 220);
    private static final Color TEXT_WHITE = new Color(240, 240, 250);
    private static final Color HOVER_PURPLE = new Color(95, 60, 130);
    
    // Components
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Navigation buttons
    private JButton btnWelcome;
    private JButton btnCustomerAuth;
    private JButton btnAdminAuth;
    private JButton btnShoppingCart;
    private JButton btnShipping;
    private JButton btnOrderTracking;
    private JButton btnProducts;
    
    // Content panels
    private JPanel welcomePanel;
    private JPanel customerAuthPanel;
    private JPanel adminAuthPanel;
    private JPanel shoppingCartPanel;
    private JPanel shippingPanel;
    private JPanel orderTrackingPanel;
    private JPanel productsPanel;
    
    // Data components
    private DatabaseRW databaseRW;
    private OrderHub orderHub;
    private ProductListFormatter productListFormatter;
    
    // User session
    private String currentUser = null;
    private boolean isAdmin = false;
    
    // Shopping cart
    private List<Product> shoppingCart;
    private JTextArea cartDisplayArea;
    private JLabel cartTotalLabel;
    
    // Product display
    private JTextArea productDisplayArea;
    
    // Order tracking
    private JTextArea orderTrackingArea;
    
    /**
     * Constructor
     */
    public ShoppingCenterView(DatabaseRW db, OrderHub hub) {
        this.databaseRW = db;
        this.orderHub = hub;
        this.productListFormatter = new ProductListFormatter();
        this.shoppingCart = new ArrayList<>();
        
        setupUI();
        setTitle("Happy Shop - Shopping Center");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    /**
     * Setup the main UI
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_PURPLE);
        
        // Create sidebar
        createSidebar();
        
        // Create content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(DARK_PURPLE);
        
        // Create all panels
        createWelcomePanel();
        createCustomerAuthPanel();
        createAdminAuthPanel();
        createProductsPanel();
        createShoppingCartPanel();
        createShippingPanel();
        createOrderTrackingPanel();
        
        // Add panels to content area
        contentPanel.add(welcomePanel, "WELCOME");
        contentPanel.add(customerAuthPanel, "CUSTOMER_AUTH");
        contentPanel.add(adminAuthPanel, "ADMIN_AUTH");
        contentPanel.add(productsPanel, "PRODUCTS");
        contentPanel.add(shoppingCartPanel, "SHOPPING_CART");
        contentPanel.add(shippingPanel, "SHIPPING");
        contentPanel.add(orderTrackingPanel, "ORDER_TRACKING");
        
        // Add components to frame
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        // Show welcome screen first
        cardLayout.show(contentPanel, "WELCOME");
    }
    
    /**
     * Create sidebar navigation
     */
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(MEDIUM_PURPLE);
        sidebarPanel.setPreferredSize(new Dimension(220, 800));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        // Logo/Title
        JLabel titleLabel = new JLabel("Happy Shop");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(titleLabel);
        
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Navigation buttons
        btnWelcome = createNavButton("🏠 Welcome", "WELCOME");
        btnCustomerAuth = createNavButton("Customer Login", "CUSTOMER_AUTH");
        btnAdminAuth = createNavButton("Admin Login", "ADMIN_AUTH");
        btnProducts = createNavButton("Browse Products", "PRODUCTS");
        btnShoppingCart = createNavButton("Shopping Cart", "SHOPPING_CART");
        btnShipping = createNavButton("Shipping", "SHIPPING");
        btnOrderTracking = createNavButton("Order Tracking", "ORDER_TRACKING");
        
        sidebarPanel.add(btnWelcome);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnCustomerAuth);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnAdminAuth);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnProducts);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnShoppingCart);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnShipping);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnOrderTracking);
        
        sidebarPanel.add(Box.createVerticalGlue());
    }
    
    /**
     * Create a navigation button
     */
    private JButton createNavButton(String text, String panelName) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBackground(LIGHT_PURPLE);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HOVER_PURPLE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(LIGHT_PURPLE);
            }
        });
        
        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, panelName);
            if (panelName.equals("PRODUCTS")) {
                loadProducts();
            } else if (panelName.equals("SHOPPING_CART")) {
                updateCartDisplay();
            }
        });
        
        return btn;
    }
    
    /**
     * Create Welcome Panel
     */
    private void createWelcomePanel() {
        welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(DARK_PURPLE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Welcome title
        JLabel welcomeTitle = new JLabel("Welcome to Shopping Center");
        welcomeTitle.setFont(new Font("Arial", Font.BOLD, 42));
        welcomeTitle.setForeground(ACCENT_PURPLE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        welcomePanel.add(welcomeTitle, gbc);
        
        // Subtitle
        JLabel subtitle = new JLabel("Your modern shopping experience");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(TEXT_WHITE);
        gbc.gridy = 1;
        welcomePanel.add(subtitle, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(40, 20, 20, 20);
        welcomePanel.add(Box.createVerticalStrut(20), gbc);
        
        // Feature cards
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Browse Products card
        JPanel browseCard = createFeatureCard("🛍️", "Browse Products", "Discover our catalog");
        gbc.gridx = 0;
        welcomePanel.add(browseCard, gbc);
        
        // Shopping Cart card
        JPanel cartCard = createFeatureCard("🛒", "Shopping Cart", "Manage your orders");
        gbc.gridx = 1;
        welcomePanel.add(cartCard, gbc);
        
        // Order Tracking card
        JPanel trackCard = createFeatureCard("📦", "Order Tracking", "Track delivery");
        gbc.gridx = 2;
        welcomePanel.add(trackCard, gbc);
        
        // Get Started button
        JButton getStartedBtn = new JButton("Get Started →");
        styleButton(getStartedBtn);
        getStartedBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.insets = new Insets(40, 20, 20, 20);
        welcomePanel.add(getStartedBtn, gbc);
        
        getStartedBtn.addActionListener(e -> {
            loadProducts();
            cardLayout.show(contentPanel, "PRODUCTS");
        });
    }
    
    /**
     * Create a feature card for the welcome screen
     */
    private JPanel createFeatureCard(String icon, String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(MEDIUM_PURPLE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_PURPLE, 2),
            new EmptyBorder(30, 30, 30, 30)));
        card.setPreferredSize(new Dimension(220, 200));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        
        card.add(Box.createVerticalStrut(15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(ACCENT_PURPLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        
        card.add(Box.createVerticalStrut(10));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_WHITE);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(descLabel);
        
        return card;
    }
    
    /**
     * Create Customer Authentication Panel
     */
    private void createCustomerAuthPanel() {
        customerAuthPanel = new JPanel(new GridBagLayout());
        customerAuthPanel.setBackground(DARK_PURPLE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Customer Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_PURPLE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        customerAuthPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 1;
        customerAuthPanel.add(userLabel, gbc);
        
        JTextField userField = new JTextField(20);
        styleTextField(userField);
        gbc.gridx = 1; gbc.gridy = 1;
        customerAuthPanel.add(userField, gbc);
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        customerAuthPanel.add(passLabel, gbc);
        
        JPasswordField passField = new JPasswordField(20);
        styleTextField(passField);
        gbc.gridx = 1; gbc.gridy = 2;
        customerAuthPanel.add(passField, gbc);
        
        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        customerAuthPanel.add(loginBtn, gbc);
        
        JLabel statusLabel = new JLabel("");
        statusLabel.setForeground(ACCENT_PURPLE);
        gbc.gridy = 4;
        customerAuthPanel.add(statusLabel, gbc);
        
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            if (!username.isEmpty() && !password.isEmpty()) {
                currentUser = username;
                isAdmin = false;
                statusLabel.setText("Welcome, " + username + "!");
                statusLabel.setForeground(Color.GREEN);
                userField.setText("");
                passField.setText("");
            } else {
                statusLabel.setText("Please enter username and password");
                statusLabel.setForeground(Color.RED);
            }
        });
    }
    
    /**
     * Create Admin Authentication Panel
     */
    private void createAdminAuthPanel() {
        adminAuthPanel = new JPanel(new GridBagLayout());
        adminAuthPanel.setBackground(DARK_PURPLE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_PURPLE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        adminAuthPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Admin ID:");
        userLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 1;
        adminAuthPanel.add(userLabel, gbc);
        
        JTextField userField = new JTextField(20);
        styleTextField(userField);
        gbc.gridx = 1; gbc.gridy = 1;
        adminAuthPanel.add(userField, gbc);
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        adminAuthPanel.add(passLabel, gbc);
        
        JPasswordField passField = new JPasswordField(20);
        styleTextField(passField);
        gbc.gridx = 1; gbc.gridy = 2;
        adminAuthPanel.add(passField, gbc);
        
        JButton loginBtn = new JButton("Admin Login");
        styleButton(loginBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        adminAuthPanel.add(loginBtn, gbc);
        
        JLabel statusLabel = new JLabel("");
        statusLabel.setForeground(ACCENT_PURPLE);
        gbc.gridy = 4;
        adminAuthPanel.add(statusLabel, gbc);
        
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            if (!username.isEmpty() && !password.isEmpty()) {
                currentUser = username;
                isAdmin = true;
                statusLabel.setText("Admin access granted: " + username);
                statusLabel.setForeground(Color.GREEN);
                userField.setText("");
                passField.setText("");
            } else {
                statusLabel.setText("Please enter admin credentials");
                statusLabel.setForeground(Color.RED);
            }
        });
    }
    
    /**
     * Create Products Panel
     */
    private void createProductsPanel() {
        productsPanel = new JPanel(new BorderLayout(10, 10));
        productsPanel.setBackground(DARK_PURPLE);
        productsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Browse Products");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_PURPLE);
        productsPanel.add(titleLabel, BorderLayout.NORTH);
        
        productDisplayArea = new JTextArea();
        productDisplayArea.setEditable(false);
        productDisplayArea.setBackground(MEDIUM_PURPLE);
        productDisplayArea.setForeground(TEXT_WHITE);
        productDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(productDisplayArea);
        productsPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(DARK_PURPLE);
        
        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setForeground(TEXT_WHITE);
        controlPanel.add(productIdLabel);
        
        JTextField productIdField = new JTextField(10);
        styleTextField(productIdField);
        controlPanel.add(productIdField);
        
        JButton addToCartBtn = new JButton("Add to Cart");
        styleButton(addToCartBtn);
        controlPanel.add(addToCartBtn);
        
        JButton refreshBtn = new JButton("Refresh Products");
        styleButton(refreshBtn);
        controlPanel.add(refreshBtn);
        
        productsPanel.add(controlPanel, BorderLayout.SOUTH);
        
        addToCartBtn.addActionListener(e -> {
            try {
                String productId = productIdField.getText().trim();
                if (!productId.isEmpty() && databaseRW != null) {
                    Product product = databaseRW.getProductDetails(productId);
                    if (product != null) {
                        shoppingCart.add(product);
                        JOptionPane.showMessageDialog(this, 
                            "Added to cart: " + product.getDescription(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        productIdField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error adding product: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        refreshBtn.addActionListener(e -> loadProducts());
    }
    
    /**
     * Create Shopping Cart Panel
     */
    private void createShoppingCartPanel() {
        shoppingCartPanel = new JPanel(new BorderLayout(10, 10));
        shoppingCartPanel.setBackground(DARK_PURPLE);
        shoppingCartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_PURPLE);
        shoppingCartPanel.add(titleLabel, BorderLayout.NORTH);
        
        cartDisplayArea = new JTextArea();
        cartDisplayArea.setEditable(false);
        cartDisplayArea.setBackground(MEDIUM_PURPLE);
        cartDisplayArea.setForeground(TEXT_WHITE);
        cartDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(cartDisplayArea);
        shoppingCartPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(DARK_PURPLE);
        
        cartTotalLabel = new JLabel("Total: £0.00");
        cartTotalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        cartTotalLabel.setForeground(ACCENT_PURPLE);
        bottomPanel.add(cartTotalLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(DARK_PURPLE);
        
        JButton clearCartBtn = new JButton("Clear Cart");
        styleButton(clearCartBtn);
        buttonPanel.add(clearCartBtn);
        
        JButton checkoutBtn = new JButton("Checkout");
        styleButton(checkoutBtn);
        buttonPanel.add(checkoutBtn);
        
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        shoppingCartPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        clearCartBtn.addActionListener(e -> {
            shoppingCart.clear();
            updateCartDisplay();
            JOptionPane.showMessageDialog(this, "Cart cleared!", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        checkoutBtn.addActionListener(e -> {
            if (shoppingCart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please login first!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                cardLayout.show(contentPanel, "SHIPPING");
            }
        });
    }
    
    /**
     * Create Shipping Panel
     */
    private void createShippingPanel() {
        shippingPanel = new JPanel(new GridBagLayout());
        shippingPanel.setBackground(DARK_PURPLE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Shipping Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_PURPLE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        shippingPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 1;
        shippingPanel.add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(30);
        styleTextField(nameField);
        gbc.gridx = 1; gbc.gridy = 1;
        shippingPanel.add(nameField, gbc);
        
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 2;
        shippingPanel.add(addressLabel, gbc);
        
        JTextArea addressArea = new JTextArea(3, 30);
        addressArea.setBackground(MEDIUM_PURPLE);
        addressArea.setForeground(TEXT_WHITE);
        addressArea.setCaretColor(TEXT_WHITE);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        gbc.gridx = 1; gbc.gridy = 2;
        shippingPanel.add(addressScroll, gbc);
        
        JLabel cityLabel = new JLabel("City:");
        cityLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 3;
        shippingPanel.add(cityLabel, gbc);
        
        JTextField cityField = new JTextField(30);
        styleTextField(cityField);
        gbc.gridx = 1; gbc.gridy = 3;
        shippingPanel.add(cityField, gbc);
        
        JLabel postcodeLabel = new JLabel("Postcode:");
        postcodeLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0; gbc.gridy = 4;
        shippingPanel.add(postcodeLabel, gbc);
        
        JTextField postcodeField = new JTextField(30);
        styleTextField(postcodeField);
        gbc.gridx = 1; gbc.gridy = 4;
        shippingPanel.add(postcodeField, gbc);
        
        JButton placeOrderBtn = new JButton("Place Order");
        styleButton(placeOrderBtn);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        shippingPanel.add(placeOrderBtn, gbc);
        
        placeOrderBtn.addActionListener(e -> {
            if (nameField.getText().isEmpty() || addressArea.getText().isEmpty() ||
                cityField.getText().isEmpty() || postcodeField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all shipping information!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String orderNum = "ORD" + System.currentTimeMillis();
                JOptionPane.showMessageDialog(this, 
                    "Order placed successfully!\nOrder Number: " + orderNum,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear cart after order
                shoppingCart.clear();
                updateCartDisplay();
                
                // Clear shipping form
                nameField.setText("");
                addressArea.setText("");
                cityField.setText("");
                postcodeField.setText("");
                
                // Show order tracking
                cardLayout.show(contentPanel, "ORDER_TRACKING");
            }
        });
    }
    
    /**
     * Create Order Tracking Panel
     */
    private void createOrderTrackingPanel() {
        orderTrackingPanel = new JPanel(new BorderLayout(10, 10));
        orderTrackingPanel.setBackground(DARK_PURPLE);
        orderTrackingPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Order Tracking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_PURPLE);
        orderTrackingPanel.add(titleLabel, BorderLayout.NORTH);
        
        orderTrackingArea = new JTextArea();
        orderTrackingArea.setEditable(false);
        orderTrackingArea.setBackground(MEDIUM_PURPLE);
        orderTrackingArea.setForeground(TEXT_WHITE);
        orderTrackingArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        orderTrackingArea.setText("No orders to track.\n\nPlace an order to see it here!");
        JScrollPane scrollPane = new JScrollPane(orderTrackingArea);
        orderTrackingPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(DARK_PURPLE);
        
        JLabel orderIdLabel = new JLabel("Order Number:");
        orderIdLabel.setForeground(TEXT_WHITE);
        controlPanel.add(orderIdLabel);
        
        JTextField orderIdField = new JTextField(15);
        styleTextField(orderIdField);
        controlPanel.add(orderIdField);
        
        JButton trackBtn = new JButton("Track Order");
        styleButton(trackBtn);
        controlPanel.add(trackBtn);
        
        orderTrackingPanel.add(controlPanel, BorderLayout.SOUTH);
        
        trackBtn.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            if (!orderId.isEmpty()) {
                orderTrackingArea.setText("Tracking Order: " + orderId + "\n\n" +
                    "Order Status: Processing\n" +
                    "Estimated Delivery: 3-5 Business Days\n" +
                    "Shipping Address: Customer Address\n\n" +
                    "Timeline:\n" +
                    "✓ Order Received\n" +
                    "✓ Payment Confirmed\n" +
                    "→ Preparing for Shipment\n" +
                    "  In Transit\n" +
                    "  Delivered");
            }
        });
    }
    
    /**
     * Load products from database
     */
    private void loadProducts() {
        if (databaseRW != null) {
            try {
                List<Product> products = databaseRW.getAllProducts();
                if (products != null && !products.isEmpty()) {
                    String formattedProducts = productListFormatter.formatProductList(products);
                    productDisplayArea.setText(formattedProducts);
                } else {
                    productDisplayArea.setText("No products available.");
                }
            } catch (Exception e) {
                productDisplayArea.setText("Error loading products: " + e.getMessage());
            }
        } else {
            productDisplayArea.setText("Database not connected. Showing sample products:\n\n" +
                "ID    Description                Price   Stock\n" +
                "====  =========================  ======  =====\n" +
                "0001  Premium Headphones         £49.99    15\n" +
                "0002  Wireless Mouse             £19.99    30\n" +
                "0003  Mechanical Keyboard        £79.99    10\n" +
                "0004  USB-C Cable (2m)           £9.99     50\n" +
                "0005  Laptop Stand               £29.99    20\n" +
                "0006  Webcam HD 1080p            £59.99    12\n" +
                "0007  Desk Lamp LED              £24.99    25\n" +
                "0008  External SSD 500GB         £89.99     8\n");
        }
    }
    
    /**
     * Update shopping cart display
     */
    private void updateCartDisplay() {
        if (shoppingCart.isEmpty()) {
            cartDisplayArea.setText("Your cart is empty.");
            cartTotalLabel.setText("Total: £0.00");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Shopping Cart Items:\n\n");
            sb.append(String.format("%-6s %-30s %10s %8s\n", 
                "ID", "Description", "Price", "Qty"));
            sb.append("=".repeat(60)).append("\n");
            
            double total = 0.0;
            for (Product product : shoppingCart) {
                sb.append(String.format("%-6s %-30s £%9.2f %8d\n",
                    product.getProductNum(),
                    product.getDescription(),
                    product.getPrice(),
                    1));
                total += product.getPrice();
            }
            sb.append("\n").append("=".repeat(60)).append("\n");
            
            cartDisplayArea.setText(sb.toString());
            cartTotalLabel.setText(String.format("Total: £%.2f", total));
        }
    }
    
    /**
     * Style a text field
     */
    private void styleTextField(JTextField field) {
        field.setBackground(MEDIUM_PURPLE);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_PURPLE, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
    
    /**
     * Style a button
     */
    private void styleButton(JButton button) {
        button.setBackground(LIGHT_PURPLE);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_PURPLE, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_PURPLE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(LIGHT_PURPLE);
            }
        });
    }
    
    @Override
    public void update(Observable o, Object arg) {
        // Handle updates from Observable objects
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingCenterView view = new ShoppingCenterView(null, null);
            view.setVisible(true);
        });
    }
}
