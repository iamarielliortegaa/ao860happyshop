package ci553.happyshop.client.shoppingcenter;

import ci553.happyshop.catalogue.Basket;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.client.basket.BasketController;
import ci553.happyshop.client.basket.BasketModel;
import ci553.happyshop.client.basket.BasketView;
import ci553.happyshop.client.cashier.CashierClient;
import ci553.happyshop.client.customerlogin.CustomerLoginController;
import ci553.happyshop.client.customerlogin.CustomerLoginModel;
import ci553.happyshop.client.customerlogin.CustomerLoginView;
import ci553.happyshop.client.adminlogin.AdminLoginController;
import ci553.happyshop.client.adminlogin.AdminLoginModel;
import ci553.happyshop.client.adminlogin.AdminLoginView;
import ci553.happyshop.client.shipping.ShippingController;
import ci553.happyshop.client.shipping.ShippingModel;
import ci553.happyshop.client.shipping.ShippingView;
import ci553.happyshop.client.tracking.TrackingController;
import ci553.happyshop.client.tracking.TrackingModel;
import ci553.happyshop.client.tracking.TrackingView;
import ci553.happyshop.middle.MiddleFactory;
import ci553.happyshop.middle.StockReadWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * The ShoppingCenterView serves as the main container for the Happy Shop application.
 * It manages navigation between different panels: Welcome, Customer Auth, Admin Auth, Products, Cart, Shipping, and Tracking.
 */
public class ShoppingCenterView extends JFrame implements Observer {
    private static final long serialVersionUID = 1L;

    // Panel identifiers
    private static final String WELCOME = "WELCOME";
    private static final String CUSTOMER_AUTH = "CUSTOMER_AUTH";
    private static final String ADMIN_AUTH = "ADMIN_AUTH";
    private static final String PRODUCTS = "PRODUCTS";
    private static final String CART = "CART";
    private static final String SHIPPING = "SHIPPING";
    private static final String TRACKING = "TRACKING";

    // UI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    
    // Welcome panel
    private JPanel welcomePanel;
    private JButton btnWelcome;

    // Navigation buttons
    private JButton btnCustomerLogin;
    private JButton btnAdminLogin;
    private JButton btnProducts;
    private JButton btnCart;
    private JButton btnShipping;
    private JButton btnTracking;
    private JButton btnLogout;

    // Child views
    private CustomerLoginView customerLoginView;
    private AdminLoginView adminLoginView;
    private BasketView basketView;
    private ShippingView shippingView;
    private TrackingView trackingView;

    // Controllers
    private CustomerLoginController customerLoginController;
    private AdminLoginController adminLoginController;
    private BasketController basketController;
    private ShippingController shippingController;
    private TrackingController trackingController;

    // Models
    private ShoppingCenterModel model;
    private CustomerLoginModel customerLoginModel;
    private AdminLoginModel adminLoginModel;
    private BasketModel basketModel;
    private ShippingModel shippingModel;
    private TrackingModel trackingModel;

    // Middleware
    private MiddleFactory mlf;
    private StockReadWriter stockRW;

    // User state
    private String currentUser = null;
    private boolean isAdmin = false;

    // Colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private static final Color BUTTON_HOVER_COLOR = new Color(52, 73, 94);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;

    /**
     * Constructor
     */
    public ShoppingCenterView(StockReadWriter stockRW, MiddleFactory mlf) {
        this.stockRW = stockRW;
        this.mlf = mlf;
        
        setupModels();
        setupViews();
        setupControllers();
        setupUI();
        
        setTitle("Happy Shop - Shopping Center");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Initialize all models
     */
    private void setupModels() {
        model = new ShoppingCenterModel(stockRW);
        model.addObserver(this);
        
        customerLoginModel = new CustomerLoginModel(mlf);
        adminLoginModel = new AdminLoginModel(mlf);
        basketModel = new BasketModel(stockRW);
        shippingModel = new ShippingModel(mlf);
        trackingModel = new TrackingModel(mlf);
    }

    /**
     * Initialize all views
     */
    private void setupViews() {
        customerLoginView = new CustomerLoginView();
        adminLoginView = new AdminLoginView();
        basketView = new BasketView(stockRW, mlf);
        shippingView = new ShippingView();
        trackingView = new TrackingView();
    }

    /**
     * Initialize all controllers
     */
    private void setupControllers() {
        customerLoginController = new CustomerLoginController(customerLoginModel, customerLoginView);
        adminLoginController = new AdminLoginController(adminLoginModel, adminLoginView);
        basketController = new BasketController(basketModel, basketView);
        shippingController = new ShippingController(shippingModel, shippingView);
        trackingController = new TrackingController(trackingModel, trackingView);
        
        // Setup login callbacks
        customerLoginView.setLoginCallback(this::handleCustomerLogin);
        adminLoginView.setLoginCallback(this::handleAdminLogin);
        
        // Setup shipping callback
        shippingView.setShippingCallback(this::handleShippingSubmit);
    }

    /**
     * Setup the main UI
     */
    private void setupUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Create sidebar
        createSidebar();
        
        // Create content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Add panels
        contentPanel.add(createWelcomePanel(), WELCOME);
        contentPanel.add(wrapInScrollPane(customerLoginView), CUSTOMER_AUTH);
        contentPanel.add(wrapInScrollPane(adminLoginView), ADMIN_AUTH);
        contentPanel.add(wrapInScrollPane(basketView), PRODUCTS);
        contentPanel.add(wrapInScrollPane(basketView.getCartPanel()), CART);
        contentPanel.add(wrapInScrollPane(shippingView), SHIPPING);
        contentPanel.add(wrapInScrollPane(trackingView), TRACKING);
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Show welcome panel first
        cardLayout.show(contentPanel, WELCOME);
        updateSidebarButtons();
    }

    /**
     * Create the welcome panel with feature cards
     */
    private JPanel createWelcomePanel() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(BACKGROUND_COLOR);
        
        // Main content panel
        JPanel contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setBackground(BACKGROUND_COLOR);
        contentArea.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        // Title
        JLabel titleLabel = new JLabel("Welcome to Shopping Center");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(SIDEBAR_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Your modern shopping experience");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentArea.add(titleLabel);
        contentArea.add(Box.createRigidArea(new Dimension(0, 10)));
        contentArea.add(subtitleLabel);
        contentArea.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Feature cards panel
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(1, 3, 30, 0));
        cardsPanel.setBackground(BACKGROUND_COLOR);
        cardsPanel.setMaximumSize(new Dimension(900, 200));
        
        // Feature cards
        JPanel card1 = createFeatureCard(
            "🛍️",
            "Browse Products",
            "Explore our wide range of products with detailed descriptions and competitive prices"
        );
        
        JPanel card2 = createFeatureCard(
            "🛒",
            "Shopping Cart",
            "Easy-to-use cart management with real-time price calculation and instant updates"
        );
        
        JPanel card3 = createFeatureCard(
            "📦",
            "Order Tracking",
            "Track your orders in real-time and stay updated on delivery status"
        );
        
        cardsPanel.add(card1);
        cardsPanel.add(card2);
        cardsPanel.add(card3);
        
        contentArea.add(cardsPanel);
        contentArea.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Get Started button
        JButton btnGetStarted = new JButton("Get Started");
        btnGetStarted.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnGetStarted.setForeground(Color.WHITE);
        btnGetStarted.setBackground(PRIMARY_COLOR);
        btnGetStarted.setFocusPainted(false);
        btnGetStarted.setBorderPainted(false);
        btnGetStarted.setPreferredSize(new Dimension(200, 50));
        btnGetStarted.setMaximumSize(new Dimension(200, 50));
        btnGetStarted.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGetStarted.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGetStarted.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGetStarted.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGetStarted.setBackground(PRIMARY_COLOR);
            }
        });
        
        btnGetStarted.addActionListener(e -> {
            cardLayout.show(contentPanel, PRODUCTS);
            updateSidebarButtons();
        });
        
        contentArea.add(btnGetStarted);
        
        welcomePanel.add(contentArea, BorderLayout.CENTER);
        
        return welcomePanel;
    }

    /**
     * Create a feature card with icon, title, and description
     */
    private JPanel createFeatureCard(String icon, String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(25, 20, 25, 20)
        ));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(SIDEBAR_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setForeground(new Color(127, 140, 141));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        descArea.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descArea);
        
        return card;
    }

    /**
     * Create the sidebar navigation
     */
    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_COLOR);
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        // Logo/Title
        JLabel lblLogo = new JLabel("Happy Shop");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(lblLogo);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Welcome button
        btnWelcome = createSidebarButton("🏠 Welcome");
        btnWelcome.addActionListener(e -> {
            cardLayout.show(contentPanel, WELCOME);
            updateSidebarButtons();
        });
        sidebarPanel.add(btnWelcome);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Customer Login button
        btnCustomerLogin = createSidebarButton("👤 Customer Login");
        btnCustomerLogin.addActionListener(e -> {
            cardLayout.show(contentPanel, CUSTOMER_AUTH);
            updateSidebarButtons();
        });
        sidebarPanel.add(btnCustomerLogin);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Admin Login button
        btnAdminLogin = createSidebarButton("🔐 Admin Login");
        btnAdminLogin.addActionListener(e -> {
            cardLayout.show(contentPanel, ADMIN_AUTH);
            updateSidebarButtons();
        });
        sidebarPanel.add(btnAdminLogin);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Products button
        btnProducts = createSidebarButton("🛍️ Products");
        btnProducts.addActionListener(e -> {
            cardLayout.show(contentPanel, PRODUCTS);
            updateSidebarButtons();
        });
        sidebarPanel.add(btnProducts);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Cart button
        btnCart = createSidebarButton("🛒 Cart");
        btnCart.addActionListener(e -> {
            cardLayout.show(contentPanel, CART);
            updateSidebarButtons();
        });
        sidebarPanel.add(btnCart);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Shipping button
        btnShipping = createSidebarButton("📦 Shipping");
        btnShipping.addActionListener(e -> {
            cardLayout.show(contentPanel, SHIPPING);
            updateSidebarButtons();
        });
        sidebarPanel.add(btnShipping);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Tracking button
        btnTracking = createSidebarButton("📍 Tracking");
        btnTracking.addActionListener(e -> {
            cardLayout.show(contentPanel, TRACKING);
            updateSidebarButtons();
        });
        sidebarPanel.add(btnTracking);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Spacer
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Logout button
        btnLogout = createSidebarButton("🚪 Logout");
        btnLogout.addActionListener(e -> handleLogout());
        btnLogout.setVisible(false);
        sidebarPanel.add(btnLogout);
    }

    /**
     * Create a styled sidebar button
     */
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(SIDEBAR_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setOpaque(true);
                button.setContentAreaFilled(true);
                button.setBackground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
        });
        
        return button;
    }

    /**
     * Update sidebar button states
     */
    private void updateSidebarButtons() {
        // Reset all buttons
        resetButtonStyle(btnWelcome);
        resetButtonStyle(btnCustomerLogin);
        resetButtonStyle(btnAdminLogin);
        resetButtonStyle(btnProducts);
        resetButtonStyle(btnCart);
        resetButtonStyle(btnShipping);
        resetButtonStyle(btnTracking);
    }

    /**
     * Reset button style
     */
    private void resetButtonStyle(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBackground(SIDEBAR_COLOR);
    }

    /**
     * Wrap a component in a scroll pane
     */
    private JScrollPane wrapInScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    /**
     * Handle customer login
     */
    private void handleCustomerLogin(String username, String password) {
        customerLoginController.login(username, password);
        
        if (customerLoginModel.isAuthenticated()) {
            currentUser = username;
            isAdmin = false;
            
            JOptionPane.showMessageDialog(this, 
                "Welcome, " + username + "!", 
                "Login Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            
            btnLogout.setVisible(true);
            cardLayout.show(contentPanel, PRODUCTS);
            updateSidebarButtons();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid credentials. Please try again.", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handle admin login
     */
    private void handleAdminLogin(String username, String password) {
        adminLoginController.login(username, password);
        
        if (adminLoginModel.isAuthenticated()) {
            currentUser = username;
            isAdmin = true;
            
            JOptionPane.showMessageDialog(this, 
                "Welcome, Administrator " + username + "!", 
                "Login Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            
            btnLogout.setVisible(true);
            cardLayout.show(contentPanel, PRODUCTS);
            updateSidebarButtons();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid admin credentials. Please try again.", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handle logout
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            currentUser = null;
            isAdmin = false;
            
            customerLoginModel.logout();
            adminLoginModel.logout();
            basketModel.clearBasket();
            
            btnLogout.setVisible(false);
            cardLayout.show(contentPanel, WELCOME);
            updateSidebarButtons();
            
            JOptionPane.showMessageDialog(this,
                "You have been logged out successfully.",
                "Logout",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Handle shipping submission
     */
    private void handleShippingSubmit(String name, String address, String city, String zipCode, String phone) {
        if (basketView.getBasket().size() == 0) {
            JOptionPane.showMessageDialog(this,
                "Your cart is empty. Please add items before shipping.",
                "Empty Cart",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        shippingController.submitShipping(name, address, city, zipCode, phone, basketView.getBasket());
        
        if (shippingModel.getOrderNumber() != null) {
            String orderNum = shippingModel.getOrderNumber();
            
            JOptionPane.showMessageDialog(this,
                "Order placed successfully!\nYour order number is: " + orderNum,
                "Order Confirmed",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear basket after successful order
            basketModel.clearBasket();
            basketView.clearBasket();
            
            // Navigate to tracking
            trackingView.setOrderNumber(orderNum);
            cardLayout.show(contentPanel, TRACKING);
            updateSidebarButtons();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to process order. Please try again.",
                "Order Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Get current user
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if current user is admin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Get basket view
     */
    public BasketView getBasketView() {
        return basketView;
    }

    /**
     * Navigate to a specific panel
     */
    public void navigateTo(String panel) {
        cardLayout.show(contentPanel, panel);
        updateSidebarButtons();
    }

    /**
     * Observer update method
     */
    @Override
    public void update(Observable o, Object arg) {
        // Handle model updates if needed
    }

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Initialize middleware (replace with actual implementation)
            MiddleFactory mlf = new MiddleFactory();
            StockReadWriter stockRW = mlf.getStockReadWriter();
            
            ShoppingCenterView view = new ShoppingCenterView(stockRW, mlf);
            view.setVisible(true);
        });
    }
}
