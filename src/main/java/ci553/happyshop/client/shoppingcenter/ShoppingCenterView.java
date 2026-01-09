package ci553.happyshop.client.shoppingcenter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * ShoppingCenterView - Main unified interface with sidebar navigation
 * Implements the dark purple theme shopping center design
 */
public class ShoppingCenterView {
    private Stage stage;
    private BorderPane root;
    private StackPane contentArea;
    private String currentUser = "Arielli";
    
    // Navigation pages
    private Pane homePage;
    private Pane shopPage;
    private Pane profilePage;
    private Pane settingsPage;

    public void start(Stage window) {
        this.stage = window;
        
        root = new BorderPane();
        root.getStyleClass().add("root");
        
        // Create sidebar
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);
        
        // Create header
        HBox header = createHeader();
        
        // Create content area
        contentArea = new StackPane();
        contentArea.getStyleClass().add("canvas");
        
        // Create pages
        createPages();
        
        // Show shop page by default
        showPage(shopPage);
        
        // Combine header and content
        VBox mainContent = new VBox(header, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setCenter(mainContent);
        
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        window.setScene(scene);
        window.setTitle("Shopping Center");
        window.show();
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(280);
        sidebar.setAlignment(Pos.TOP_LEFT);
        
        // Brand
        HBox brand = new HBox(12);
        brand.setAlignment(Pos.CENTER_LEFT);
        
        Label logo = new Label("SC");
        logo.getStyleClass().add("brand-logo");
        
        Label brandName = new Label("Shopping Center");
        brandName.getStyleClass().add("brand-name");
        
        brand.getChildren().addAll(logo, brandName);
        
        // Navigation items
        VBox nav = new VBox(8);
        nav.getStyleClass().add("nav");
        
        HBox homeNav = createNavItem("🏠", "Home", "H", false);
        HBox shopNav = createNavItem("🛍️", "Shop", null, true);
        HBox profileNav = createNavItem("👤", "Profile", "P", false);
        HBox settingsNav = createNavItem("⚙️", "Settings", "S", false);
        
        homeNav.setOnMouseClicked(e -> showPage(homePage));
        shopNav.setOnMouseClicked(e -> showPage(shopPage));
        profileNav.setOnMouseClicked(e -> showPage(profilePage));
        settingsNav.setOnMouseClicked(e -> showPage(settingsPage));
        
        nav.getChildren().addAll(homeNav, shopNav, profileNav, settingsNav);
        
        // Footer
        Label footer = new Label("© 2025 Shopping Center");
        footer.getStyleClass().add("sidebar-footer");
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        sidebar.getChildren().addAll(brand, nav, spacer, footer);
        
        return sidebar;
    }
    
    private HBox createNavItem(String icon, String label, String kbd, boolean active) {
        HBox item = new HBox(12);
        item.getStyleClass().add("nav-item");
        if (active) {
            item.getStyleClass().add("active");
        }
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPrefHeight(62);
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("nav-icon");
        iconLabel.setAlignment(Pos.CENTER);
        iconLabel.setMinSize(38, 38);
        
        // Label
        Label navLabel = new Label(label);
        navLabel.getStyleClass().add("nav-label");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        item.getChildren().addAll(iconLabel, navLabel, spacer);
        
        // Keyboard shortcut or active pill
        if (active) {
            Label pill = new Label("Active");
            pill.getStyleClass().add("pill");
            item.getChildren().add(pill);
        } else if (kbd != null) {
            Label kbdLabel = new Label(kbd);
            kbdLabel.getStyleClass().add("nav-kbd");
            item.getChildren().add(kbdLabel);
        }
        
        return item;
    }
    
    private HBox createHeader() {
        HBox header = new HBox(16);
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(66);
        
        // Title
        Label title = new Label("Shopping Center");
        title.getStyleClass().add("title");
        
        // Search box
        HBox searchBox = new HBox(10);
        searchBox.getStyleClass().add("search-box");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setMaxWidth(400);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        
        Label searchIcon = new Label("🔎");
        TextField searchField = new TextField();
        searchField.setPromptText("Search products, orders, users…");
        searchField.getStyleClass().add("text-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        searchBox.getChildren().addAll(searchIcon, searchField);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Profile
        HBox profile = new HBox(14);
        profile.setAlignment(Pos.CENTER);
        
        Label profileName = new Label(currentUser);
        profileName.getStyleClass().add("profile-name");
        
        Circle avatar = new Circle(19);
        avatar.getStyleClass().add("avatar");
        avatar.setFill(Color.web("#a07cff"));
        
        profile.getChildren().addAll(profileName, avatar);
        
        header.getChildren().addAll(title, searchBox, spacer, profile);
        
        return header;
    }
    
    private void createPages() {
        homePage = createHomePage();
        shopPage = createShopPage();
        profilePage = createProfilePage();
        settingsPage = createSettingsPage();
    }
    
    private Pane createHomePage() {
        VBox page = new VBox(20);
        page.setAlignment(Pos.CENTER);
        
        Label welcome = new Label("Welcome to Shopping Center");
        welcome.getStyleClass().add("form-title");
        
        page.getChildren().add(welcome);
        return page;
    }
    
    private Pane createShopPage() {
        Pane canvas = new Pane();
        canvas.setPrefSize(1200, 700);
        
        // Create node cards with positioning
        VBox customerAuth = createNodeCard("Customer authentication", 
            new String[]{"Registration", "Login", "Forgot password"},
            new String[]{"", "login", "danger"});
        customerAuth.setLayoutX(40);
        customerAuth.setLayoutY(30);
        
        VBox adminAuth = createNodeCard("Admin authentication",
            new String[]{"Login"},
            new String[]{"login"});
        adminAuth.setLayoutX(420);
        adminAuth.setLayoutY(30);
        
        VBox cart = createNodeCard("Add to cart",
            new String[]{"Login"},
            new String[]{"login"});
        cart.setLayoutX(800);
        cart.setLayoutY(30);
        
        VBox shipping = createNodeCard("Shipping",
            new String[]{"Delivery"},
            new String[]{"delivery"});
        shipping.setLayoutX(420);
        shipping.setLayoutY(260);
        
        VBox sendings = createNodeCard("Sendings",
            new String[]{"Delivery"},
            new String[]{"delivery"});
        sendings.setLayoutX(800);
        sendings.setLayoutY(260);
        
        canvas.getChildren().addAll(customerAuth, adminAuth, cart, shipping, sendings);
        
        return canvas;
    }
    
    private VBox createNodeCard(String title, String[] chipLabels, String[] chipTypes) {
        VBox card = new VBox(12);
        card.getStyleClass().add("node-card");
        card.setPrefWidth(320);
        
        // Header
        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        
        Circle dot = new Circle(6);
        dot.getStyleClass().add("node-dot");
        dot.setFill(Color.web("#7c4dff"));
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("node-title");
        
        head.getChildren().addAll(dot, titleLabel);
        
        // Chips
        FlowPane chips = new FlowPane(10, 10);
        
        for (int i = 0; i < chipLabels.length; i++) {
            HBox chip = new HBox(8);
            chip.getStyleClass().add("chip");
            chip.setAlignment(Pos.CENTER);
            
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
    
    private Pane createProfilePage() {
        VBox page = new VBox(20);
        page.setAlignment(Pos.CENTER);
        
        Label title = new Label("User Profile");
        title.getStyleClass().add("form-title");
        
        page.getChildren().add(title);
        return page;
    }
    
    private Pane createSettingsPage() {
        VBox page = new VBox(20);
        page.setAlignment(Pos.CENTER);
        
        Label title = new Label("Settings");
        title.getStyleClass().add("form-title");
        
        page.getChildren().add(title);
        return page;
    }
    
    private void showPage(Pane page) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(page);
    }
}