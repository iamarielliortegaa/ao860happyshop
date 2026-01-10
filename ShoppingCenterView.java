import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShoppingCenterView extends JFrame {
    
    // CardLayout for navigation
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Page constants
    private static final String SHOP_PAGE = "shop";
    private static final String CUSTOMER_AUTH_PAGE = "customerAuth";
    private static final String ADMIN_AUTH_PAGE = "adminAuth";
    private static final String CART_PAGE = "cart";
    private static final String SHIPPING_PAGE = "shipping";
    private static final String SENDINGS_PAGE = "sendings";
    
    // Page panels
    private JPanel shopPage;
    private JPanel customerAuthPage;
    private JPanel adminAuthPage;
    private JPanel cartPage;
    private JPanel shippingPage;
    private JPanel sendingsPage;
    
    public ShoppingCenterView() {
        setTitle("Happy Shop - Shopping Center");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Initialize CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create all pages
        createPages();
        
        // Add all pages to main panel
        mainPanel.add(shopPage, SHOP_PAGE);
        mainPanel.add(customerAuthPage, CUSTOMER_AUTH_PAGE);
        mainPanel.add(adminAuthPage, ADMIN_AUTH_PAGE);
        mainPanel.add(cartPage, CART_PAGE);
        mainPanel.add(shippingPage, SHIPPING_PAGE);
        mainPanel.add(sendingsPage, SENDINGS_PAGE);
        
        // Set shop page as default
        cardLayout.show(mainPanel, SHOP_PAGE);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void createPages() {
        // Create shop page with navigation cards
        shopPage = createShopPage();
        
        // Create customer authentication page
        customerAuthPage = createCustomerAuthPage();
        
        // Create admin authentication page
        adminAuthPage = createAdminAuthPage();
        
        // Create cart page
        cartPage = createCartPage();
        
        // Create shipping page
        shippingPage = createShippingPage();
        
        // Create sendings page
        sendingsPage = createSendingsPage();
    }
    
    private JPanel createShopPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        JLabel titleLabel = new JLabel("Happy Shop - Shopping Center");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Main content with cards
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(createNavigationCard("Customer Authentication", 
            "Login or register as a customer", 
            new Color(76, 175, 80),
            "Login",
            CUSTOMER_AUTH_PAGE), gbc);
        
        gbc.gridx = 1;
        contentPanel.add(createNavigationCard("Admin Authentication", 
            "Admin login portal", 
            new Color(255, 152, 0),
            "Admin Login",
            ADMIN_AUTH_PAGE), gbc);
        
        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(createNavigationCard("Shopping Cart", 
            "View and manage your cart", 
            new Color(233, 30, 99),
            "View Cart",
            CART_PAGE), gbc);
        
        gbc.gridx = 1;
        contentPanel.add(createNavigationCard("Shipping Options", 
            "Choose shipping method", 
            new Color(156, 39, 176),
            "Shipping",
            SHIPPING_PAGE), gbc);
        
        // Row 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        contentPanel.add(createNavigationCard("Track Sendings", 
            "Track your orders", 
            new Color(63, 81, 181),
            "Track Orders",
            SENDINGS_PAGE), gbc);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createNavigationCard(String title, String description, Color color, String buttonText, String targetPage) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Clickable chip/button
        JButton chipButton = new JButton(buttonText);
        chipButton.setFont(new Font("Arial", Font.BOLD, 14));
        chipButton.setBackground(color);
        chipButton.setForeground(Color.WHITE);
        chipButton.setFocusPainted(false);
        chipButton.setBorderPainted(false);
        chipButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chipButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chipButton.setMaximumSize(new Dimension(200, 40));
        
        // Add click listener to navigate
        chipButton.addActionListener(e -> navigateToPage(targetPage));
        
        // Add hover effect
        chipButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                chipButton.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                chipButton.setBackground(color);
            }
        });
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(chipButton);
        
        return card;
    }
    
    private JPanel createCustomerAuthPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Header with back button
        JPanel header = createPageHeader("Customer Authentication", new Color(76, 175, 80));
        
        // Content (integrated from CustomerAuthView)
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(20);
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(33, 150, 243));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        
        formPanel.add(loginButton);
        formPanel.add(registerButton);
        
        content.add(formPanel, gbc);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAdminAuthPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Header with back button
        JPanel header = createPageHeader("Admin Authentication", new Color(255, 152, 0));
        
        // Content (integrated from AdminAuthView)
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Admin ID:"));
        JTextField adminIdField = new JTextField(20);
        formPanel.add(adminIdField);
        
        formPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField);
        
        JButton loginButton = new JButton("Admin Login");
        loginButton.setBackground(new Color(255, 152, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        formPanel.add(new JLabel(""));
        formPanel.add(loginButton);
        
        content.add(formPanel, gbc);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCartPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Header with back button
        JPanel header = createPageHeader("Shopping Cart", new Color(233, 30, 99));
        
        // Content (integrated from CartView)
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Cart items list
        DefaultListModel<String> cartModel = new DefaultListModel<>();
        cartModel.addElement("Sample Item 1 - $10.00");
        cartModel.addElement("Sample Item 2 - $15.00");
        cartModel.addElement("Sample Item 3 - $20.00");
        
        JList<String> cartList = new JList<>(cartModel);
        cartList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(cartList);
        
        // Bottom panel with total and checkout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel totalLabel = new JLabel("Total: $45.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.setBackground(new Color(233, 30, 99));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> navigateToPage(SHIPPING_PAGE));
        
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(checkoutButton, BorderLayout.EAST);
        
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createShippingPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Header with back button
        JPanel header = createPageHeader("Shipping Options", new Color(156, 39, 176));
        
        // Content (integrated from ShippingView)
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        ButtonGroup shippingGroup = new ButtonGroup();
        
        JRadioButton standardShipping = new JRadioButton("Standard Shipping (5-7 days) - $5.00");
        standardShipping.setBackground(Color.WHITE);
        standardShipping.setFont(new Font("Arial", Font.PLAIN, 14));
        shippingGroup.add(standardShipping);
        formPanel.add(standardShipping);
        
        JRadioButton expressShipping = new JRadioButton("Express Shipping (2-3 days) - $15.00");
        expressShipping.setBackground(Color.WHITE);
        expressShipping.setFont(new Font("Arial", Font.PLAIN, 14));
        shippingGroup.add(expressShipping);
        formPanel.add(expressShipping);
        
        JRadioButton overnightShipping = new JRadioButton("Overnight Shipping (1 day) - $30.00");
        overnightShipping.setBackground(Color.WHITE);
        overnightShipping.setFont(new Font("Arial", Font.PLAIN, 14));
        shippingGroup.add(overnightShipping);
        formPanel.add(overnightShipping);
        
        standardShipping.setSelected(true);
        
        JButton confirmButton = new JButton("Confirm Shipping");
        confirmButton.setBackground(new Color(156, 39, 176));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> navigateToPage(SENDINGS_PAGE));
        
        formPanel.add(confirmButton);
        
        content.add(formPanel, gbc);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSendingsPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Header with back button
        JPanel header = createPageHeader("Track Sendings", new Color(63, 81, 181));
        
        // Content (integrated from SendingsView)
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Sendings list
        DefaultListModel<String> sendingsModel = new DefaultListModel<>();
        sendingsModel.addElement("Order #12345 - In Transit - Expected: Jan 15, 2026");
        sendingsModel.addElement("Order #12346 - Processing - Expected: Jan 17, 2026");
        sendingsModel.addElement("Order #12347 - Delivered - Delivered: Jan 05, 2026");
        
        JList<String> sendingsList = new JList<>(sendingsModel);
        sendingsList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(sendingsList);
        
        // Refresh button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(63, 81, 181));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        
        bottomPanel.add(refreshButton);
        
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPageHeader(String title, Color color) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(color);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Back button
        JButton backButton = new JButton("← Back to Shop");
        backButton.setBackground(color.darker());
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> navigateToPage(SHOP_PAGE));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        header.add(backButton, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        
        return header;
    }
    
    private void navigateToPage(String pageName) {
        cardLayout.show(mainPanel, pageName);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShoppingCenterView());
    }
}
