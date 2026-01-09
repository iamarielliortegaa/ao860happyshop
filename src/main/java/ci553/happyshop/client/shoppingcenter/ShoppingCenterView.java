package ci553.happyshop.client.shoppingcenter;

import ci553.happyshop.dbAccess.DatabaseRW;
import ci553.happyshop.middle.OrderHub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * ShoppingCenterView - Main GUI for the Happy Shop Shopping Center
 * Dark purple theme with sidebar navigation and multiple panels
 */
public class ShoppingCenterView extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Color Theme - Dark Purple
    private static final Color DARK_PURPLE = new Color(75, 0, 130);
    private static final Color LIGHT_PURPLE = new Color(147, 112, 219);
    private static final Color VERY_LIGHT_PURPLE = new Color(230, 230, 250);
    private static final Color WHITE = Color.WHITE;
    
    // Main Components
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panels
    private JPanel welcomePanel;
    private JPanel customerLoginPanel;
    private JPanel adminLoginPanel;
    private JPanel browseProductsPanel;
    private JPanel shoppingCartPanel;
    private JPanel shippingPanel;
    private JPanel orderTrackingPanel;
    
    // Sidebar Buttons
    private JButton btnWelcome;
    private JButton btnCustomerLogin;
    private JButton btnAdminLogin;
    private JButton btnBrowseProducts;
    private JButton btnShoppingCart;
    private JButton btnShipping;
    private JButton btnOrderTracking;
    
    // Database and OrderHub references
    private DatabaseRW database;
    private OrderHub orderHub;
    
    /**
     * Constructor
     */
    public ShoppingCenterView(DatabaseRW database, OrderHub orderHub) {
        this.database = database;
        this.orderHub = orderHub;
        
        setTitle("Happy Shop - Shopping Center");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initializeComponents();
        layoutComponents();
    }
    
    /**
     * Initialize all components
     */
    private void initializeComponents() {
        // Initialize sidebar
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(DARK_PURPLE);
        sidebarPanel.setPreferredSize(new Dimension(250, 800));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        
        // Initialize content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Initialize panels
        welcomePanel = createWelcomePanel();
        customerLoginPanel = createCustomerLoginPanel();
        adminLoginPanel = createAdminLoginPanel();
        browseProductsPanel = createBrowseProductsPanel();
        shoppingCartPanel = createShoppingCartPanel();
        shippingPanel = createShippingPanel();
        orderTrackingPanel = createOrderTrackingPanel();
        
        // Initialize sidebar buttons
        btnWelcome = createSidebarButton("Welcome");
        btnCustomerLogin = createSidebarButton("Customer Login");
        btnAdminLogin = createSidebarButton("Admin Login");
        btnBrowseProducts = createSidebarButton("Browse Products");
        btnShoppingCart = createSidebarButton("Shopping Cart");
        btnShipping = createSidebarButton("Shipping");
        btnOrderTracking = createSidebarButton("Order Tracking");
        
        // Add action listeners
        btnWelcome.addActionListener(e -> showPanel("Welcome"));
        btnCustomerLogin.addActionListener(e -> showPanel("CustomerLogin"));
        btnAdminLogin.addActionListener(e -> showPanel("AdminLogin"));
        btnBrowseProducts.addActionListener(e -> showPanel("BrowseProducts"));
        btnShoppingCart.addActionListener(e -> showPanel("ShoppingCart"));
        btnShipping.addActionListener(e -> showPanel("Shipping"));
        btnOrderTracking.addActionListener(e -> showPanel("OrderTracking"));
    }
    
    /**
     * Layout all components
     */
    private void layoutComponents() {
        // Add title to sidebar
        JLabel titleLabel = new JLabel("Happy Shop");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        sidebarPanel.add(titleLabel);
        
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Add buttons to sidebar
        sidebarPanel.add(btnWelcome);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(btnCustomerLogin);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(btnAdminLogin);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(btnBrowseProducts);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(btnShoppingCart);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(btnShipping);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(btnOrderTracking);
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Add panels to content panel
        contentPanel.add(welcomePanel, "Welcome");
        contentPanel.add(customerLoginPanel, "CustomerLogin");
        contentPanel.add(adminLoginPanel, "AdminLogin");
        contentPanel.add(browseProductsPanel, "BrowseProducts");
        contentPanel.add(shoppingCartPanel, "ShoppingCart");
        contentPanel.add(shippingPanel, "Shipping");
        contentPanel.add(orderTrackingPanel, "OrderTracking");
        
        // Add components to frame
        setLayout(new BorderLayout());
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create sidebar button with consistent styling
     */
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(WHITE);
        button.setBackground(LIGHT_PURPLE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(230, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(VERY_LIGHT_PURPLE);
                button.setForeground(DARK_PURPLE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_PURPLE);
                button.setForeground(WHITE);
            }
        });
        
        return button;
    }
    
    /**
     * Create Welcome Panel with feature cards
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PURPLE);
        headerPanel.setPreferredSize(new Dimension(950, 100));
        
        JLabel headerLabel = new JLabel("Welcome to Happy Shop");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        headerLabel.setForeground(WHITE);
        headerPanel.add(headerLabel);
        
        // Feature Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setBackground(WHITE);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create feature cards
        cardsPanel.add(createFeatureCard("Customer Login", "Sign in to your account", "👤"));
        cardsPanel.add(createFeatureCard("Browse Products", "Explore our catalog", "🛍️"));
        cardsPanel.add(createFeatureCard("Shopping Cart", "View your cart", "🛒"));
        cardsPanel.add(createFeatureCard("Shipping", "Manage shipping", "📦"));
        cardsPanel.add(createFeatureCard("Order Tracking", "Track your orders", "📍"));
        cardsPanel.add(createFeatureCard("Admin Login", "Admin access", "🔐"));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(cardsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a feature card
     */
    private JPanel createFeatureCard(String title, String description, String emoji) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(VERY_LIGHT_PURPLE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_PURPLE, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(DARK_PURPLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(DARK_PURPLE);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(emojiLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        return card;
    }
    
    /**
     * Create Customer Login Panel
     */
    private JPanel createCustomerLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PURPLE);
        headerPanel.setPreferredSize(new Dimension(950, 80));
        
        JLabel headerLabel = new JLabel("Customer Login");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(WHITE);
        headerPanel.add(headerLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(DARK_PURPLE);
        loginButton.setForeground(WHITE);
        loginButton.setFocusPainted(false);
        
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setBackground(LIGHT_PURPLE);
        registerButton.setForeground(WHITE);
        registerButton.setFocusPainted(false);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);
        
        gbc.gridy = 3;
        formPanel.add(registerButton, gbc);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create Admin Login Panel
     */
    private JPanel createAdminLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PURPLE);
        headerPanel.setPreferredSize(new Dimension(950, 80));
        
        JLabel headerLabel = new JLabel("Admin Login");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(WHITE);
        headerPanel.add(headerLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel userLabel = new JLabel("Admin Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel passLabel = new JLabel("Admin Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JButton loginButton = new JButton("Admin Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(DARK_PURPLE);
        loginButton.setForeground(WHITE);
        loginButton.setFocusPainted(false);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create Browse Products Panel
     */
    private JPanel createBrowseProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PURPLE);
        headerPanel.setPreferredSize(new Dimension(950, 80));
        
        JLabel headerLabel = new JLabel("Browse Products");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(WHITE);
        headerPanel.add(headerLabel);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(VERY_LIGHT_PURPLE);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(LIGHT_PURPLE);
        searchButton.setForeground(WHITE);
        searchButton.setFocusPainted(false);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Products List
        JTextArea productsArea = new JTextArea();
        productsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        productsArea.setEditable(false);
        productsArea.setText("Product ID | Name | Price | Stock\n");
        productsArea.append("----------------------------------------\n");
        productsArea.append("Loading products...\n");
        
        JScrollPane scrollPane = new JScrollPane(productsArea);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create Shopping Cart Panel
     */
    private JPanel createShoppingCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PURPLE);
        headerPanel.setPreferredSize(new Dimension(950, 80));
        
        JLabel headerLabel = new JLabel("Shopping Cart");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(WHITE);
        headerPanel.add(headerLabel);
        
        // Cart Items
        JTextArea cartArea = new JTextArea();
        cartArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        cartArea.setEditable(false);
        cartArea.setText("Product | Quantity | Price | Subtotal\n");
        cartArea.append("----------------------------------------\n");
        cartArea.append("Your cart is empty\n");
        
        JScrollPane scrollPane = new JScrollPane(cartArea);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(VERY_LIGHT_PURPLE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(DARK_PURPLE);
        
        JButton checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setBackground(DARK_PURPLE);
        checkoutButton.setForeground(WHITE);
        checkoutButton.setFocusPainted(false);
        
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(checkoutButton, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create Shipping Panel
     */
    private JPanel createShippingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PURPLE);
        headerPanel.setPreferredSize(new Dimension(950, 80));
        
        JLabel headerLabel = new JLabel("Shipping Information");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(WHITE);
        headerPanel.add(headerLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        String[] labels = {"Full Name:", "Address Line 1:", "Address Line 2:", "City:", "Postal Code:", "Country:"};
        JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            fields[i] = new JTextField(25);
            fields[i].setFont(new Font("Arial", Font.PLAIN, 16));
            
            gbc.gridx = 0; gbc.gridy = i; gbc.anchor = GridBagConstraints.EAST;
            formPanel.add(label, gbc);
            gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(fields[i], gbc);
        }
        
        JButton saveButton = new JButton("Save Shipping Info");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(DARK_PURPLE);
        saveButton.setForeground(WHITE);
        saveButton.setFocusPainted(false);
        
        gbc.gridx = 1; gbc.gridy = labels.length; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(saveButton, gbc);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create Order Tracking Panel
     */
    private JPanel createOrderTrackingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_PURPLE);
        headerPanel.setPreferredSize(new Dimension(950, 80));
        
        JLabel headerLabel = new JLabel("Order Tracking");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(WHITE);
        headerPanel.add(headerLabel);
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(VERY_LIGHT_PURPLE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel orderLabel = new JLabel("Order Number:");
        orderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField orderField = new JTextField(15);
        orderField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton trackButton = new JButton("Track Order");
        trackButton.setBackground(DARK_PURPLE);
        trackButton.setForeground(WHITE);
        trackButton.setFocusPainted(false);
        
        searchPanel.add(orderLabel);
        searchPanel.add(orderField);
        searchPanel.add(trackButton);
        
        // Order Details Area
        JTextArea orderArea = new JTextArea();
        orderArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        orderArea.setEditable(false);
        orderArea.setText("Enter an order number to track your order\n");
        orderArea.append("----------------------------------------\n");
        
        JScrollPane scrollPane = new JScrollPane(orderArea);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Show specific panel
     */
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
    
    /**
     * Get database reference
     */
    public DatabaseRW getDatabase() {
        return database;
    }
    
    /**
     * Get OrderHub reference
     */
    public OrderHub getOrderHub() {
        return orderHub;
    }
}
