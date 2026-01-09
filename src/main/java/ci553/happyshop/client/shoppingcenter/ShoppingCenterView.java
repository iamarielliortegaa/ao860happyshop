package ci553.happyshop.client.shoppingcenter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * ShoppingCenterView - Main unified view for the Shopping Center application
 * Features a sidebar navigation and dynamic content area
 */
public class ShoppingCenterView {
    
    private Stage primaryStage;
    private BorderPane mainLayout;
    private StackPane contentArea;
    private VBox sidebar;
    
    // Current active page
    private String activePage = "Shop";
    
    // Controllers for different sections
    private CustomerAuthController customerAuthController;
    private AdminAuthController adminAuthController;
    private CartController cartController;
    private ShippingController shippingController;
    
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        // Initialize controllers
        customerAuthController = new CustomerAuthController();
        adminAuthController = new AdminAuthController();
        cartController = new CartController();
        shippingController = new ShippingController();
        
        // Create main layout
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("root");
        
        // Create sidebar
        sidebar = createSidebar();
        mainLayout.setLeft(sidebar);
        
        // Create header
        HBox header = createHeader();
        mainLayout.setTop(header);
        
        // Create content area
        contentArea = new StackPane();
        contentArea.getStyleClass().add("canvas");
        mainLayout.setCenter(contentArea);
        
        // Load initial page (Shop page with flowchart)
        loadShopPage();
        
        // Create scene and load CSS
        Scene scene = new Scene(mainLayout, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        stage.setScene(scene);
        stage.setTitle("Shopping Center");
        stage.show();
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(280);
        sidebar.setSpacing(8);
        
        // Brand section
        HBox brand = createBrand();
        
        // Navigation items
        VBox nav = new VBox(8);
        nav.getStyleClass().add("nav");
        
        nav.getChildren().addAll(
            createNavItem("🏠", "Home", "H", false),
            createNavItem("🛍️", "Shop", "Active", true),
            createNavItem("👤", "Profile", "P", false),
            createNavItem("⚙️", "Settings", "S", false)
        );
        
        // Footer
        Label footer = new Label("© 2025 Shopping Center");
        footer.getStyleClass().add("sidebar-footer");
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        sidebar.getChildren().addAll(brand, nav, spacer, footer);
        return sidebar;
    }
    
    private HBox createBrand() {
        HBox brand = new HBox(12);
        brand.setAlignment(Pos.CENTER_LEFT);
        brand.setPadding(new Insets(0, 0, 28, 0));
        
        // Logo
        StackPane logo = new StackPane();
        logo.getStyleClass().add("brand-logo");
        Label logoText = new Label("SC");
        logo.getChildren().add(logoText);
        
        // Brand name
        Label brandName = new Label("Shopping Center");
        brandName.getStyleClass().add("brand-name");
        
        brand.getChildren().addAll(logo, brandName);
        return brand;
    }
    
    private HBox createNavItem(String icon, String label, String badge, boolean active) {
        HBox navItem = new HBox(12);
        navItem.getStyleClass().add("nav-item");
        if (active) {
            navItem.getStyleClass().add("active");
        }
        navItem.setAlignment(Pos.CENTER_LEFT);
        
        // Icon
        StackPane iconPane = new StackPane();
        iconPane.getStyleClass().add("nav-icon");
        Label iconLabel = new Label(icon);
        iconPane.getChildren().add(iconLabel);
        
        // Label
        Label navLabel = new Label(label);
        navLabel.getStyleClass().add("nav-label");
        HBox.setHgrow(navLabel, Priority.ALWAYS);
        
        // Badge/Kbd
        Label badgeLabel = new Label(badge);
        if (badge.equals("Active")) {
            badgeLabel.getStyleClass().add("pill");
        } else {
            badgeLabel.getStyleClass().add("nav-kbd");
        }
        
        navItem.getChildren().addAll(iconPane, navLabel, badgeLabel);
        
        // Click handler
        navItem.setOnMouseClicked(e -> handleNavigation(label));
        
        return navItem;
    }
    
    private HBox createHeader() {
        HBox header = new HBox(16);
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Title
        Label title = new Label("Shopping Center");
        title.getStyleClass().add("title");
        
        // Search box
        HBox searchBox = new HBox(10);
        searchBox.getStyleClass().add("search-box");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        
        Label searchIcon = new Label("🔎");
        TextField searchField = new TextField();
        searchField.setPromptText("Search products, orders, users…");
        searchField.getStyleClass().add("text-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        searchBox.getChildren().addAll(searchIcon, searchField);
        
        // Profile
        HBox profile = new HBox(14);
        profile.setAlignment(Pos.CENTER);
        
        Label profileName = new Label("Arielli");
        profileName.getStyleClass().add("profile-name");
        
        Circle avatar = new Circle(19);
        avatar.getStyleClass().add("avatar");
        
        profile.getChildren().addAll(profileName, avatar);
        
        header.getChildren().addAll(title, searchBox, profile);
        return header;
    }
    
    private void handleNavigation(String page) {
        activePage = page;
        
        switch (page) {
            case "Home":
                loadHomePage();
                break;
            case "Shop":
                loadShopPage();
                break;
            case "Profile":
                loadProfilePage();
                break;
            case "Settings":
                loadSettingsPage();
                break;
        }
        
        // Update active state in sidebar
        updateSidebarActiveState(page);
    }
    
    private void loadShopPage() {
        contentArea.getChildren().clear();
        
        // Create flowchart canvas with nodes
        Pane canvas = new Pane();
        canvas.setPrefSize(1200, 800);
        
        // Customer Authentication Node
        VBox customerAuthNode = createNode("Customer authentication", 40, 30,
            new String[]{"Registration", "Login", "Forgot password"},
            new String[]{"", "login", "danger"});
        
        // Admin Authentication Node
        VBox adminAuthNode = createNode("Admin authentication", 420, 30,
            new String[]{"Login"},
            new String[]{"login"});
        
        // Cart Node
        VBox cartNode = createNode("Add to cart", 800, 30,
            new String[]{"Login"},
            new String[]{"login"});
        
        // Shipping Node
        VBox shippingNode = createNode("Shipping", 420, 260,
            new String[]{"Delivery"},
            new String[]{"delivery"});
        
        // Sendings Node
        VBox sendingsNode = createNode("Sendings", 800, 260,
            new String[]{"Delivery"},
            new String[]{"delivery"});
        
        // Add click handlers to nodes
        customerAuthNode.setOnMouseClicked(e -> customerAuthController.showCustomerAuth(contentArea));
        adminAuthNode.setOnMouseClicked(e -> adminAuthController.showAdminAuth(contentArea));
        cartNode.setOnMouseClicked(e -> cartController.showCart(contentArea));
        shippingNode.setOnMouseClicked(e -> shippingController.showShipping(contentArea));
        sendingsNode.setOnMouseClicked(e -> shippingController.showSendings(contentArea));
        
        canvas.getChildren().addAll(customerAuthNode, adminAuthNode, cartNode, shippingNode, sendingsNode);
        
        ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        contentArea.getChildren().add(scrollPane);
    }
    
    private VBox createNode(String title, double x, double y, String[] chips, String[] chipStyles) {
        VBox node = new VBox(12);
        node.getStyleClass().add("node-card");
        node.setLayoutX(x);
        node.setLayoutY(y);
        node.setPrefWidth(320);
        
        // Header
        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        
        Circle dot = new Circle(6);
        dot.getStyleClass().add("node-dot");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("node-title");
        
        head.getChildren().addAll(dot, titleLabel);
        
        // Chips
        FlowPane chipsPane = new FlowPane(10, 10);
        
        for (int i = 0; i < chips.length; i++) {
            HBox chip = new HBox(8);
            chip.getStyleClass().add("chip");
            chip.setAlignment(Pos.CENTER);
            
            Circle mini = new Circle(4);
            mini.getStyleClass().add("chip-mini");
            if (i < chipStyles.length && !chipStyles[i].isEmpty()) {
                mini.getStyleClass().add(chipStyles[i]);
            }
            
            Label chipLabel = new Label(chips[i]);
            
            chip.getChildren().addAll(mini, chipLabel);
            chipsPane.getChildren().add(chip);
        }
        
        node.getChildren().addAll(head, chipsPane);
        return node;
    }
    
    private void loadHomePage() {
        contentArea.getChildren().clear();
        Label label = new Label("Home Page - Coming Soon");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #eae6f7;");
        contentArea.getChildren().add(label);
    }
    
    private void loadProfilePage() {
        contentArea.getChildren().clear();
        Label label = new Label("Profile Page - Coming Soon");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #eae6f7;");
        contentArea.getChildren().add(label);
    }
    
    private void loadSettingsPage() {
        contentArea.getChildren().clear();
        Label label = new Label("Settings Page - Coming Soon");
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #eae6f7;");
        contentArea.getChildren().add(label);
    }
    
    private void updateSidebarActiveState(String activePage) {
        // This would update the sidebar to reflect the active page
        // For simplicity, we'll recreate the sidebar
        sidebar.getChildren().clear();
        
        HBox brand = createBrand();
        
        VBox nav = new VBox(8);
        nav.getStyleClass().add("nav");
        
        nav.getChildren().addAll(
            createNavItem("🏠", "Home", "H", activePage.equals("Home")),
            createNavItem("🛍️", "Shop", "Active", activePage.equals("Shop")),
            createNavItem("👤", "Profile", "P", activePage.equals("Profile")),
            createNavItem("⚙️", "Settings", "S", activePage.equals("Settings"))
        );
        
        Label footer = new Label("© 2025 Shopping Center");
        footer.getStyleClass().add("sidebar-footer");
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        sidebar.getChildren().addAll(brand, nav, spacer, footer);
    }
}
