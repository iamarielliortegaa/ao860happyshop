package ci553.happyshop.client.shoppingcenter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * ShoppingCenterMain - Unified Shopping Center Application
 * This is the new main interface that combines all shopping center functionality
 * with a modern dark purple theme and sidebar navigation.
 */
public class ShoppingCenterMain extends Application {
    
    private BorderPane mainLayout;
    private StackPane contentArea;
    private VBox sidebar;
    private String currentView = "home";
    
    // View components
    private CustomerAuthView customerAuthView;
    private AdminAuthView adminAuthView;
    private CartView cartView;
    private ShippingView shippingView;
    private SendingsView sendingsView;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        mainLayout = new BorderPane();
        
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
        
        // Initialize views
        initializeViews();
        
        // Show home view by default
        showView("home");
        
        // Create scene
        Scene scene = new Scene(mainLayout, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        primaryStage.setTitle("Shopping Center");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(24, 20, 24, 20));
        
        // Brand section
        HBox brand = createBrand();
        
        // Navigation items
        VBox nav = new VBox(8);
        nav.getStyleClass().add("nav");
        
        nav.getChildren().addAll(
            createNavItem("🏠", "Home", "H", "home"),
            createNavItem("🛍️", "Shop", "Active", "shop"),
            createNavItem("👤", "Profile", "P", "profile"),
            createNavItem("⚙️", "Settings", "S", "settings")
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
        
        Label logo = new Label("SC");
        logo.getStyleClass().add("brand-logo");
        
        Label name = new Label("Shopping Center");
        name.getStyleClass().add("brand-name");
        
        brand.getChildren().addAll(logo, name);
        return brand;
    }
    
    private HBox createNavItem(String icon, String label, String badge, String viewId) {
        HBox navItem = new HBox(12);
        navItem.getStyleClass().add("nav-item");
        navItem.setAlignment(Pos.CENTER_LEFT);
        navItem.setPrefWidth(240);
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("nav-icon");
        iconLabel.setMinSize(38, 38);
        iconLabel.setAlignment(Pos.CENTER);
        
        // Label
        Label textLabel = new Label(label);
        textLabel.getStyleClass().add("nav-label");
        HBox.setHgrow(textLabel, Priority.ALWAYS);
        
        // Badge/Kbd
        Label badgeLabel = new Label(badge);
        if (badge.equals("Active")) {
            badgeLabel.getStyleClass().add("pill");
            navItem.getStyleClass().add("active");
        } else {
            badgeLabel.getStyleClass().add("nav-kbd");
        }
        
        navItem.getChildren().addAll(iconLabel, textLabel, badgeLabel);
        
        // Click handler
        navItem.setOnMouseClicked(e -> {
            updateActiveNavItem(navItem);
            showView(viewId);
        });
        
        return navItem;
    }
    
    private void updateActiveNavItem(HBox clickedItem) {
        VBox nav = (VBox) sidebar.getChildren().get(1);
        for (var node : nav.getChildren()) {
            if (node instanceof HBox) {
                HBox item = (HBox) node;
                item.getStyleClass().remove("active");
                
                // Update badge
                Label badge = (Label) item.getChildren().get(2);
                badge.getStyleClass().removeAll("pill", "nav-kbd");
                
                if (item == clickedItem) {
                    item.getStyleClass().add("active");
                    badge.setText("Active");
                    badge.getStyleClass().add("pill");
                } else {
                    badge.getStyleClass().add("nav-kbd");
                    // Restore original badge text based on label
                    Label lbl = (Label) item.getChildren().get(1);
                    badge.setText(lbl.getText().substring(0, 1).toUpperCase());
                }
            }
        }
    }
    
    private HBox createHeader() {
        HBox header = new HBox(16);
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(22, 24, 22, 24));
        
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
        
        Region avatar = new Region();
        avatar.getStyleClass().add("avatar");
        avatar.setMinSize(38, 38);
        avatar.setMaxSize(38, 38);
        
        profile.getChildren().addAll(profileName, avatar);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        
        header.getChildren().addAll(title, searchBox, spacer, profile);
        
        return header;
    }
    
    private void initializeViews() {
        customerAuthView = new CustomerAuthView();
        adminAuthView = new AdminAuthView();
        cartView = new CartView();
        shippingView = new ShippingView();
        sendingsView = new SendingsView();
    }
    
    private void showView(String viewId) {
        currentView = viewId;
        contentArea.getChildren().clear();
        
        switch (viewId) {
            case "home":
                contentArea.getChildren().add(createHomeView());
                break;
            case "shop":
                contentArea.getChildren().add(createShopFlowView());
                break;
            case "profile":
                contentArea.getChildren().add(createProfileView());
                break;
            case "settings":
                contentArea.getChildren().add(createSettingsView());
                break;
        }
    }
    
    private Pane createHomeView() {
        VBox home = new VBox(20);
        home.setAlignment(Pos.CENTER);
        
        Label welcome = new Label("Welcome to Shopping Center");
        welcome.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #eae6f7;");
        
        Label subtitle = new Label("Your one-stop shop for all your needs");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #bfb6dd;");
        
        home.getChildren().addAll(welcome, subtitle);
        
        return home;
    }
    
    private Pane createShopFlowView() {
        Pane canvas = new Pane();
        canvas.setPrefSize(1200, 600);
        
        // Create workflow nodes
        VBox customerAuth = createNodeCard("Customer authentication", 
            new String[]{"Registration", "Login", "Forgot password"}, 
            new String[]{"", "login", "danger"}, 40, 30);
        
        VBox adminAuth = createNodeCard("Admin authentication", 
            new String[]{"Login"}, 
            new String[]{"login"}, 420, 30);
        
        VBox cart = createNodeCard("Add to cart", 
            new String[]{"Login"}, 
            new String[]{"login"}, 800, 30);
        
        VBox shipping = createNodeCard("Shipping", 
            new String[]{"Delivery"}, 
            new String[]{"delivery"}, 420, 260);
        
        VBox sendings = createNodeCard("Sendings", 
            new String[]{"Delivery"}, 
            new String[]{"delivery"}, 800, 260);
        
        canvas.getChildren().addAll(customerAuth, adminAuth, cart, shipping, sendings);
        
        return canvas;
    }
    
    private VBox createNodeCard(String title, String[] chips, String[] chipTypes, double x, double y) {
        VBox node = new VBox(12);
        node.getStyleClass().add("node-card");
        node.setPrefWidth(320);
        node.setLayoutX(x);
        node.setLayoutY(y);
        
        // Header
        HBox head = new HBox(12);
        head.setAlignment(Pos.CENTER_LEFT);
        
        Region dot = new Region();
        dot.getStyleClass().add("node-dot");
        dot.setMinSize(12, 12);
        dot.setMaxSize(12, 12);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("node-title");
        
        head.getChildren().addAll(dot, titleLabel);
        
        // Chips
        FlowPane chipsContainer = new FlowPane(10, 10);
        
        for (int i = 0; i < chips.length; i++) {
            HBox chip = createChip(chips[i], chipTypes[i]);
            chipsContainer.getChildren().add(chip);
        }
        
        node.getChildren().addAll(head, chipsContainer);
        
        return node;
    }
    
    private HBox createChip(String text, String type) {
        HBox chip = new HBox(8);
        chip.getStyleClass().add("chip");
        chip.setAlignment(Pos.CENTER);
        
        Region mini = new Region();
        mini.getStyleClass().add("chip-mini");
        if (!type.isEmpty()) {
            mini.getStyleClass().add(type);
        }
        mini.setMinSize(8, 8);
        mini.setMaxSize(8, 8);
        
        Label label = new Label(text);
        
        chip.getChildren().addAll(mini, label);
        
        return chip;
    }
    
    private Pane createProfileView() {
        VBox profile = new VBox(20);
        profile.setAlignment(Pos.CENTER);
        
        Label title = new Label("Profile");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #eae6f7;");
        
        profile.getChildren().add(title);
        
        return profile;
    }
    
    private Pane createSettingsView() {
        VBox settings = new VBox(20);
        settings.setAlignment(Pos.CENTER);
        
        Label title = new Label("Settings");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #eae6f7;");
        
        settings.getChildren().add(title);
        
        return settings;
    }
}
