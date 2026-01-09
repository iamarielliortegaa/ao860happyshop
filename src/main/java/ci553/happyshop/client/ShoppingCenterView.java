package ci553.happyshop.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * ShoppingCenterView - Unified main interface for the shopping center application
 * Provides a modern dark-themed UI with sidebar navigation and dynamic content area
 * 
 * Features:
 * - Sidebar navigation with Home, Shop, Profile, Settings
 * - Header with search and user profile
 * - Dynamic canvas area that displays different modules based on navigation
 * - Integration with authentication, cart, shipping, and delivery modules
 */
public class ShoppingCenterView {
    
    private Stage primaryStage;
    private BorderPane mainLayout;
    private StackPane canvasArea;
    private String currentUser = "Arielli";
    
    // Navigation state
    private String currentPage = "Shop";
    
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("root");
        
        // Create sidebar
        VBox sidebar = createSidebar();
        mainLayout.setLeft(sidebar);
        
        // Create main content area
        VBox mainContent = new VBox();
        
        // Create header
        HBox header = createHeader();
        
        // Create canvas
        canvasArea = new StackPane();
        canvasArea.getStyleClass().add("canvas");
        canvasArea.setPrefSize(800, 600);
        
        // Load initial content (Shop page with flowchart)
        loadShopPage();
        
        mainContent.getChildren().addAll(header, canvasArea);
        VBox.setVgrow(canvasArea, Priority.ALWAYS);
        
        mainLayout.setCenter(mainContent);
        
        Scene scene = new Scene(mainLayout, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        stage.setScene(scene);
        stage.setTitle("Shopping Center");
        stage.show();
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(24, 20, 24, 20));
        
        // Brand section
        HBox brand = new HBox(12);
        brand.setAlignment(Pos.CENTER_LEFT);
        
        Label brandLogo = new Label("SC");
        brandLogo.getStyleClass().add("brand-logo");
        brandLogo.setMinSize(36, 36);
        brandLogo.setAlignment(Pos.CENTER);
        
        Label brandName = new Label("Shopping Center");
        brandName.getStyleClass().add("brand-name");
        
        brand.getChildren().addAll(brandLogo, brandName);
        
        // Navigation items
        VBox nav = new VBox(8);
        
        Button homeBtn = createNavButton("🏠", "Home", "H", false);
        Button shopBtn = createNavButton("🛍️", "Shop", "Active", true);
        Button profileBtn = createNavButton("👤", "Profile", "P", false);
        Button settingsBtn = createNavButton("⚙️", "Settings", "S", false);
        
        homeBtn.setOnAction(e -> switchPage("Home", homeBtn, shopBtn, profileBtn, settingsBtn));
        shopBtn.setOnAction(e -> switchPage("Shop", homeBtn, shopBtn, profileBtn, settingsBtn));
        profileBtn.setOnAction(e -> switchPage("Profile", homeBtn, shopBtn, profileBtn, settingsBtn));
        settingsBtn.setOnAction(e -> switchPage("Settings", homeBtn, shopBtn, profileBtn, settingsBtn));
        
        nav.getChildren().addAll(homeBtn, shopBtn, profileBtn, settingsBtn);
        
        // Footer
        Label footer = new Label("© 2025 Shopping Center");
        footer.getStyleClass().add("sidebar-footer");
        VBox.setMargin(footer, new Insets(350, 0, 0, 0));
        
        sidebar.getChildren().addAll(brand, nav, footer);
        
        return sidebar;
    }
    
    private Button createNavButton(String icon, String label, String badge, boolean active) {
        Button btn = new Button();
        btn.getStyleClass().add("nav-item");
        if (active) {
            btn.getStyleClass().add("active");
        }
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(62);
        
        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("nav-icon");
        iconLabel.setMinSize(38, 38);
        iconLabel.setAlignment(Pos.CENTER);
        
        Label textLabel = new Label(label);
        textLabel.getStyleClass().add("nav-label");
        HBox.setHgrow(textLabel, Priority.ALWAYS);
        
        Label badgeLabel = new Label(badge);
        if (badge.equals("Active")) {
            badgeLabel.getStyleClass().add("pill");
        } else {
            badgeLabel.getStyleClass().add("nav-kbd");
        }
        
        content.getChildren().addAll(iconLabel, textLabel, badgeLabel);
        btn.setGraphic(content);
        
        return btn;
    }
    
    private HBox createHeader() {
        HBox header = new HBox(16);
        header.getStyleClass().add("header");
        header.setPadding(new Insets(22, 24, 22, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Shopping Center");
        title.getStyleClass().add("title");
        
        // Search box
        HBox searchBox = new HBox(10);
        searchBox.getStyleClass().add("search-box");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.setMaxWidth(400);
        
        Label searchIcon = new Label("🔎");
        TextField searchField = new TextField();
        searchField.setPromptText("Search products, orders, users…");
        searchField.getStyleClass().add("text-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        searchBox.getChildren().addAll(searchIcon, searchField);
        
        // Profile section
        HBox profile = new HBox(14);
        profile.setAlignment(Pos.CENTER_RIGHT);
        
        Label profileName = new Label(currentUser);
        profileName.getStyleClass().add("profile-name");
        
        Circle avatar = new Circle(19);
        avatar.getStyleClass().add("avatar");
        
        profile.getChildren().addAll(profileName, avatar);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        header.getChildren().addAll(title, searchBox, spacer, profile);
        
        return header;
    }
    
    private void switchPage(String page, Button... buttons) {
        currentPage = page;
        
        // Update button states
        for (Button btn : buttons) {
            btn.getStyleClass().remove("active");
        }
        
        // Find and activate the correct button
        for (Button btn : buttons) {
            HBox content = (HBox) btn.getGraphic();
            Label label = (Label) content.getChildren().get(1);
            if (label.getText().equals(page)) {
                btn.getStyleClass().add("active");
                break;
            }
        }
        
        // Load page content
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
    }
    
    private void loadHomePage() {
        canvasArea.getChildren().clear();
        Label message = new Label("Welcome to Shopping Center!");
        message.getStyleClass().add("form-title");
        canvasArea.getChildren().add(message);
    }
    
    private void loadShopPage() {
        canvasArea.getChildren().clear();
        
        // Create flowchart nodes
        Pane canvas = new Pane();
        canvas.setPrefSize(1200, 600);
        
        // Customer Authentication node
        VBox customerAuth = createNodeCard("Customer authentication", 
            new String[]{"Registration", "Login", "Forgot password"},
            new String[]{"", "login", "danger"});
        customerAuth.setLayoutX(40);
        customerAuth.setLayoutY(30);
        
        // Admin Authentication node
        VBox adminAuth = createNodeCard("Admin authentication",
            new String[]{"Login"},
            new String[]{"login"});
        adminAuth.setLayoutX(420);
        adminAuth.setLayoutY(30);
        
        // Cart node
        VBox cart = createNodeCard("Add to cart",
            new String[]{"Login"},
            new String[]{"login"});
        cart.setLayoutX(800);
        cart.setLayoutY(30);
        
        // Shipping node
        VBox shipping = createNodeCard("Shipping",
            new String[]{"Delivery"},
            new String[]{"delivery"});
        shipping.setLayoutX(420);
        shipping.setLayoutY(260);
        
        // Sendings node
        VBox sendings = createNodeCard("Sendings",
            new String[]{"Delivery"},
            new String[]{"delivery"});
        sendings.setLayoutX(800);
        sendings.setLayoutY(260);
        
        canvas.getChildren().addAll(customerAuth, adminAuth, cart, shipping, sendings);
        
        canvasArea.getChildren().add(canvas);
    }
    
    private VBox createNodeCard(String title, String[] chipLabels, String[] chipTypes) {
        VBox card = new VBox(12);
        card.getStyleClass().add("node-card");
        card.setPrefWidth(320);
        card.setPadding(new Insets(16));
        
        // Header
        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        
        Circle dot = new Circle(6);
        dot.getStyleClass().add("node-dot");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("node-title");
        
        head.getChildren().addAll(dot, titleLabel);
        
        // Chips
        FlowPane chips = new FlowPane(10, 10);
        
        for (int i = 0; i < chipLabels.length; i++) {
            HBox chip = new HBox(8);
            chip.getStyleClass().add("chip");
            chip.setAlignment(Pos.CENTER_LEFT);
            
            Circle mini = new Circle(4);
            mini.getStyleClass().add("chip-mini");
            if (i < chipTypes.length && !chipTypes[i].isEmpty()) {
                mini.getStyleClass().add(chipTypes[i]);
            }
            
            Label chipLabel = new Label(chipLabels[i]);
            
            chip.getChildren().addAll(mini, chipLabel);
            chips.getChildren().add(chip);
        }
        
        card.getChildren().addAll(head, chips);
        
        return card;
    }
    
    private void loadProfilePage() {
        canvasArea.getChildren().clear();
        Label message = new Label("Profile Page - Coming Soon");
        message.getStyleClass().add("form-title");
        canvasArea.getChildren().add(message);
    }
    
    private void loadSettingsPage() {
        canvasArea.getChildren().clear();
        Label message = new Label("Settings Page - Coming Soon");
        message.getStyleClass().add("form-title");
        canvasArea.getChildren().add(message);
    }
}