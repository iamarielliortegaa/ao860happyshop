package ci553.shoppingcenter.client;

import ci553.shoppingcenter.model.Product;
import ci553.shoppingcenter.service.CartService;
import ci553.shoppingcenter.service.DiscountService;
import ci553.shoppingcenter.service.OrderService;
import ci553.shoppingcenter.service.ProductService;
import ci553.shoppingcenter.service.UserService;
import ci553.shoppingcenter.service.WishlistService;
import ci553.shoppingcenter.service.ReviewService;
import ci553.shoppingcenter.utility.StorageLocation;
import ci553.shoppingcenter.service.ActionLogger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.*;

/**
 * ShoppingCenterView - Unified main interface for the shopping center application
 * Provides a modern dark-themed UI with sidebar navigation and dynamic content area
 */
public class ShoppingCenterView extends Application {

    private StackPane canvasArea;
    private String loggedInUser = null; // null means guest

    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final DiscountService discountService = new DiscountService();
    private final WishlistService wishlistService = new WishlistService();
    private final ReviewService reviewService = new ReviewService();

    // header controls that need to be refreshed
    private Label profileNameLabel;
    private Button loginButton;
    private Button cartButton;
    private Button adminButton;

    // UI state
    private boolean darkMode = true;

    @Override
    public void start(Stage stage) {
        BorderPane mainLayout = new BorderPane();
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

        // Load initial content (Shop page with product list)
        loadShopPage();

        mainContent.getChildren().addAll(header, canvasArea);
        VBox.setVgrow(canvasArea, Priority.ALWAYS);

        mainLayout.setCenter(mainContent);

        Scene scene = new Scene(mainLayout, 1200, 700);
        java.net.URL css = getClass().getResource("/shopping-center-styles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

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
        searchBox.setMaxWidth(600);

        Label searchIcon = new Label("🔎");
        TextField searchField = new TextField();
        searchField.setPromptText("Search products, orders, users…");
        searchField.getStyleClass().add("text-field");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        // Filters: category, price range, sort
        ComboBox<String> catFilter = new ComboBox<>();
        catFilter.getItems().add("All");
        catFilter.getItems().addAll(productService.listAll().stream().map(Product::getCategory).distinct().sorted().toList());
        catFilter.getSelectionModel().selectFirst();
        TextField minPrice = new TextField(); minPrice.setPromptText("min$"); minPrice.setPrefWidth(70);
        TextField maxPrice = new TextField(); maxPrice.setPromptText("max$"); maxPrice.setPrefWidth(70);
        ComboBox<String> sortBox = new ComboBox<>(); sortBox.getItems().addAll("Featured","Price ↑","Price ↓","Popularity"); sortBox.getSelectionModel().select(0);

        Button applyFilters = new Button("Apply");
        applyFilters.setOnAction(e -> loadShopPage(searchField.getText(), catFilter.getValue(), minPrice.getText(), maxPrice.getText(), sortBox.getValue()));
        searchField.setOnAction(e -> applyFilters.fire());

        searchBox.getChildren().addAll(searchIcon, searchField, catFilter, minPrice, maxPrice, sortBox, applyFilters);

        // Profile section
        HBox profile = new HBox(14);
        profile.setAlignment(Pos.CENTER_RIGHT);

        profileNameLabel = new Label(getDisplayName());
        profileNameLabel.getStyleClass().add("profile-name");

        Circle avatar = new Circle(19);
        avatar.getStyleClass().add("avatar");

        // Notifications button
        Button notifButton = new Button("🔔");
        notifButton.setOnAction(e -> showNotificationCenter());
        notifButton.setTooltip(new Tooltip("Notifications"));

        loginButton = new Button(loggedInUser == null ? "Login / Register" : "Logout");
        loginButton.setOnAction(e -> {
            if (loggedInUser == null) {
                showLoginDialog(profileNameLabel);
            } else {
                ActionLogger.log("Logout: " + loggedInUser);
                loggedInUser = null;
                profileNameLabel.setText(getDisplayName());
                loginButton.setText("Login / Register");
                refreshHeader();
            }
        });

        cartButton = new Button("Cart (" + cartSizeForDisplay() + ")");
        cartButton.setOnAction(e -> showCartDialog());

        adminButton = new Button("Order Tracker");
        adminButton.setOnAction(e -> showOrderTrackerDialog());
        adminButton.setVisible(false);

        // Dark/Light toggle
        ToggleButton themeToggle = new ToggleButton(darkMode ? "Dark" : "Light");
        themeToggle.setOnAction(e -> {
            darkMode = !darkMode;
            themeToggle.setText(darkMode ? "Dark" : "Light");
            // toggle style class on root
            Scene s = header.getScene();
            if (s != null) {
                if (darkMode) s.getRoot().getStyleClass().remove("light-mode");
                else s.getRoot().getStyleClass().add("light-mode");
            }
        });

        profile.getChildren().addAll(profileNameLabel, avatar, notifButton, loginButton, cartButton, adminButton, themeToggle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, searchBox, spacer, profile);

        return header;
    }

    private String getDisplayName() {
        return loggedInUser == null ? "Guest" : loggedInUser;
    }

    private int cartSizeForDisplay() {
        String user = loggedInUser == null ? "guest" : loggedInUser;
        return cartService.getCart(user).values().stream().mapToInt(Integer::intValue).sum();
    }

    private void refreshHeader() {
        // update profile text
        if (profileNameLabel != null) profileNameLabel.setText(getDisplayName());
        if (loginButton != null) loginButton.setText(loggedInUser == null ? "Login" : "Logout");
        if (cartButton != null) cartButton.setText("Cart (" + cartSizeForDisplay() + ")");
        if (adminButton != null) {
            boolean show = loggedInUser != null && userService.isAdmin(loggedInUser);
            adminButton.setVisible(show);
        }
    }

    private void showLoginDialog(Label profileName) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Login / Register");

        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        TabPane tabs = new TabPane();
        Tab loginTab = new Tab("Login");
        Tab regTab = new Tab("Register");
        tabs.getTabs().addAll(loginTab, regTab);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Login content
        VBox loginBox = new VBox(8);
        TextField username = new TextField(); username.setPromptText("Username");
        PasswordField password = new PasswordField(); password.setPromptText("Password");
        Button doLogin = new Button("Login");
        Label msg = new Label();
        doLogin.setOnAction(e -> {
            String u = username.getText().trim();
            String p = password.getText();
            Optional<ci553.shoppingcenter.model.User> user = userService.login(u, p);
            if (user.isPresent()) {
                loggedInUser = u;
                ActionLogger.log("Login: " + u);
                profileName.setText(getDisplayName());
                refreshHeader();
                dlg.close();
            } else {
                msg.setText("Invalid credentials");
            }
        });
        loginBox.getChildren().addAll(new Label("Login"), username, password, doLogin, msg);
        loginTab.setContent(loginBox);

        // Register content
        VBox regBox = new VBox(8);
        TextField rUser = new TextField(); rUser.setPromptText("Username");
        TextField rName = new TextField(); rName.setPromptText("Display name");
        PasswordField rPass = new PasswordField(); rPass.setPromptText("Password");
        Button doReg = new Button("Register");
        Label rMsg = new Label();
        doReg.setOnAction(e -> {
            String un = rUser.getText().trim();
            String dn = rName.getText().trim();
            String pw = rPass.getText();
            if (un.isEmpty() || pw.isEmpty()) { rMsg.setText("Username and password required"); return; }
            boolean ok = userService.register(un, dn.isEmpty() ? un : dn, pw);
            if (ok) { rMsg.setText("Registered — please login"); ActionLogger.log("Registered: " + un); }
            else rMsg.setText("User already exists");
        });
        regBox.getChildren().addAll(new Label("Register"), rUser, rName, rPass, doReg, rMsg);
        regTab.setContent(regBox);

        root.getChildren().addAll(tabs);
        Scene s = new Scene(root, 420, 320);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    private void showCartDialog() {
        String user = loggedInUser == null ? "guest" : loggedInUser;
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Your Cart");

        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        Map<String, Integer> cart = cartService.getCart(user);
        if (cart.isEmpty()) {
            root.getChildren().add(new Label("Your cart is empty."));
        } else {
            double subtotal = 0.0;
            for (Map.Entry<String, Integer> e : cart.entrySet()) {
                String pid = e.getKey();
                int qty = e.getValue();
                Product prod = productService.findById(pid).orElse(new Product(pid, pid, 0.0));
                double line = prod.getPrice() * qty;
                subtotal += line;

                HBox row = new HBox(10);
                Label name = new Label(prod.getName());
                Label price = new Label(String.format("$%.2f", prod.getPrice()));
                Label q = new Label("x" + qty);
                Label lineTotal = new Label(String.format("$%.2f", line));
                Button remove = new Button("Remove");
                remove.setOnAction(ev -> {
                    cartService.removeFromCart(user, pid);
                    dlg.close();
                    refreshHeader();
                    showCartDialog();
                });
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                row.getChildren().addAll(name, price, q, lineTotal, spacer, remove);
                root.getChildren().add(row);
            }

            final double orderSubtotal = subtotal;
            Label subtotalLabel = new Label(String.format("Subtotal: $%.2f", orderSubtotal));
            TextField discountCode = new TextField();
            discountCode.setPromptText("Discount code");
            Button applyDiscount = new Button("Apply");
            Label discountInfo = new Label();

            final int[] appliedDiscount = {0};
            applyDiscount.setOnAction(evt -> {
                String code = discountCode.getText();
                int pct = discountService.validateCode(code);
                appliedDiscount[0] = pct;
                discountInfo.setText(pct > 0 ? ("Applied " + pct + "%") : "Invalid code");
            });

            Button pay = new Button("Proceed to Payment");
            pay.setOnAction(ev -> {
                boolean ok = showPaymentDialog(orderSubtotal);
                if (!ok) return;
                // place order
                String buyer = user;
                if (loggedInUser == null) {
                    TextInputDialog req = new TextInputDialog();
                    req.setTitle("Guest checkout");
                    req.setHeaderText("Enter a name to associate with this guest order:");
                    Optional<String> nameOpt = req.showAndWait();
                    if (nameOpt.isEmpty() || nameOpt.get().trim().isEmpty()) {
                        return;
                    }
                    buyer = nameOpt.get().trim();
                }
                String receipt = orderService.placeOrder(buyer, cart, id -> productService.findById(id).map(Product::getPrice).orElse(0.0), appliedDiscount[0], id -> productService.findById(id).map(Product::getName).orElse(id));
                // decrease stock
                cart.forEach((pid, qtt) -> productService.findById(pid).ifPresent(p -> { p.setStock(p.getStock()-qtt); p.incrementPopularity(); }));
                productService.persist();
                cartService.clearCart(user);
                dlg.close();
                ActionLogger.log("Order placed by " + buyer);
                // export invoice
                try {
                    Files.createDirectories(StorageLocation.ordersPath.resolve("invoices"));
                    java.nio.file.Path inv = StorageLocation.ordersPath.resolve("invoices").resolve("invoice_" + System.currentTimeMillis() + ".txt");
                    Files.writeString(inv, receipt, StandardCharsets.UTF_8);
                } catch (Exception ex) { /*ignore*/ }
                refreshHeader();
                showReceiptDialog(receipt);
            });

            HBox discountRow = new HBox(8, discountCode, applyDiscount, discountInfo);
            discountRow.setAlignment(Pos.CENTER_LEFT);

            root.getChildren().addAll(subtotalLabel, discountRow, pay);
        }

        Scene s = new Scene(root, 600, 450);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    private boolean showPaymentDialog(double amount) {
        Stage dlg = new Stage(); dlg.initModality(Modality.APPLICATION_MODAL); dlg.setTitle("Payment");
        VBox root = new VBox(8); root.setPadding(new Insets(12));
        TextField card = new TextField(); card.setPromptText("Card number (fake)");
        TextField name = new TextField(); name.setPromptText("Name on card");
        TextField exp = new TextField(); exp.setPromptText("MM/YY"); exp.setPrefWidth(80);
        TextField cvv = new TextField(); cvv.setPromptText("CVV"); cvv.setPrefWidth(60);
        Label amt = new Label(String.format("Amount: $%.2f", amount));
        Label msg = new Label();
        Button pay = new Button("Pay");
        pay.setOnAction(e -> {
            if (card.getText().trim().length()<12) { msg.setText("Invalid card number"); return; }
            if (name.getText().trim().isEmpty()) { msg.setText("Name required"); return; }
            // simulate processing
            dlg.close();
        });
        root.getChildren().addAll(new Label("Payment (simulation)"), amt, card, name, new HBox(6, exp, cvv), pay, msg);
        Scene s = new Scene(root, 360, 300); dlg.setScene(s); dlg.showAndWait();
        return true;
    }

    private void showOrderTrackerDialog() {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Order Tracker");

        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        Label progLabel = new Label("Progressing orders");
        progLabel.getStyleClass().add("form-title");
        root.getChildren().add(progLabel);

        try {
            java.util.List<Path> progressing = orderService.listProgressingOrders();
            if (progressing.isEmpty()) {
                root.getChildren().add(new Label("No progressing orders."));
            } else {
                for (Path p : progressing) {
                    HBox row = new HBox(8);
                    Label name = new Label(p.getFileName().toString());
                    Button open = new Button("View");
                    open.setOnAction(e -> {
                        try {
                            String content = Files.readString(p, StandardCharsets.UTF_8);
                            showReceiptDialog(content);
                        } catch (IOException ex) {
                            showReceiptDialog("Unable to read receipt: " + ex.getMessage());
                        }
                    });
                    Button promote = new Button("Promote to Ordered");
                    promote.setOnAction(e -> {
                        boolean ok = orderService.promoteToOrdered(p);
                        if (ok) {
                            dlg.close();
                            showOrderTrackerDialog();
                        } else {
                            showReceiptDialog("Unable to promote order");
                        }
                    });
                    row.getChildren().addAll(name, open, promote);
                    root.getChildren().add(row);
                }
            }

            root.getChildren().add(new Separator());

            Label ordLabel = new Label("Ordered orders");
            ordLabel.getStyleClass().add("form-title");
            root.getChildren().add(ordLabel);

            java.util.List<Path> ordered = orderService.listOrderedOrders();
            if (ordered.isEmpty()) {
                root.getChildren().add(new Label("No ordered orders."));
            } else {
                for (Path p : ordered) {
                    HBox row = new HBox(8);
                    Label name = new Label(p.getFileName().toString());
                    Button open = new Button("View");
                    open.setOnAction(e -> {
                        try {
                            String content = Files.readString(p, StandardCharsets.UTF_8);
                            showReceiptDialog(content);
                        } catch (IOException ex) {
                            showReceiptDialog("Unable to read receipt: " + ex.getMessage());
                        }
                    });
                    Button collect = new Button("Mark Collected");
                    collect.setOnAction(e -> {
                        boolean ok = orderService.promoteToCollected(p);
                        if (ok) {
                            dlg.close();
                            showOrderTrackerDialog();
                        } else {
                            showReceiptDialog("Unable to mark collected");
                        }
                    });
                    row.getChildren().addAll(name, open, collect);
                    root.getChildren().add(row);
                }
            }
        } catch (Exception ex) {
            root.getChildren().add(new Label("Error: " + ex.getMessage()));
        }

        Button refreshAll = new Button("Refresh");
        refreshAll.setOnAction(e -> { dlg.close(); showOrderTrackerDialog(); });
        root.getChildren().addAll(0, java.util.List.of(refreshAll));

        Label notLabel = new Label("Notifications");
        notLabel.getStyleClass().add("form-title");
        root.getChildren().add(1, notLabel);

        TextArea notifArea = new TextArea();
        notifArea.setEditable(false);
        notifArea.setPrefRowCount(5);
        notifArea.setText(ci553.shoppingcenter.service.NotificationService.readAll());
        root.getChildren().add(2, notifArea);

        Scene s = new Scene(root, 600, 500);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    private void showReceiptDialog(String receipt) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Receipt");

        VBox root = new VBox(8);
        root.setPadding(new Insets(10));

        TextArea area = new TextArea(receipt);
        area.setEditable(false);
        area.setWrapText(true);

        Button close = new Button("Close");
        close.setOnAction(e -> dlg.close());

        Button export = new Button("Export PDF");
        export.setOnAction(e -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.setTitle("Save invoice as PDF");
            chooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF Files","*.pdf"));
            java.io.File dest = chooser.showSaveDialog(dlg);
            if (dest != null) {
                try {
                    ci553.shoppingcenter.service.InvoiceService.writePdf(dest.toPath(), receipt);
                    Alert ok = new Alert(Alert.AlertType.INFORMATION, "Invoice exported successfully:\n" + dest.getAbsolutePath(), ButtonType.OK);
                    ok.setTitle("Export successful");
                    ok.showAndWait();
                } catch (Exception ex) {
                    Alert err = new Alert(Alert.AlertType.ERROR, "Failed to export invoice: " + ex.getMessage(), ButtonType.OK);
                    err.setTitle("Export failed");
                    err.showAndWait();
                }
            }
        });

        root.getChildren().addAll(new Label("Order Receipt"), area, new HBox(8, export, close));
        Scene s = new Scene(root, 500, 400);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    private void switchPage(String page, Button... buttons) {
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

        Label productCount = new Label("Products available: " + productService.count());
        productCount.getStyleClass().add("muted");

        // Action buttons for quick navigation
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button toShop = new Button("Start Shopping");
        toShop.setOnAction(e -> switchPage("Shop"));

        Button viewCart = new Button("View Cart");
        viewCart.setOnAction(e -> showCartDialog());

        Button toProfile = new Button("Profile");
        toProfile.setOnAction(e -> switchPage("Profile"));

        Button tracker = new Button("Order Tracker");
        tracker.setOnAction(e -> showOrderTrackerDialog());
        tracker.setVisible(loggedInUser != null && userService.isAdmin(loggedInUser));

        actions.getChildren().addAll(toShop, viewCart, toProfile, tracker);

        VBox content = new VBox(12, message, productCount, actions);
        content.setAlignment(Pos.CENTER);

        canvasArea.getChildren().add(content);
    }

    private void loadShopPage() {
        loadShopPage("", "All", "", "", "Featured");
    }

    private void loadShopPage(String query, String category, String minP, String maxP, String sortMode) {
        canvasArea.getChildren().clear();

        VBox container = new VBox(8);
        container.setPadding(new Insets(12));

        // Build product list with filters
        List<Product> products = productService.listAll();
        // search
        if (query != null && !query.trim().isEmpty()) {
            String q = query.trim().toLowerCase();
            products = products.stream().filter(p -> p.getName().toLowerCase().contains(q)).toList();
        }
        // category
        if (category != null && !category.equals("All")) {
            products = products.stream().filter(p -> p.getCategory().equals(category)).toList();
        }
        // price range -> parse into temp variables then create final locals for lambda capture
        double tmpMin = -1, tmpMax = -1;
        try { if (minP != null && !minP.trim().isEmpty()) tmpMin = Double.parseDouble(minP); } catch (Exception ignored) {}
        try { if (maxP != null && !maxP.trim().isEmpty()) tmpMax = Double.parseDouble(maxP); } catch (Exception ignored) {}
        final double min = tmpMin;
        final double max = tmpMax;
        if (min >= 0) products = products.stream().filter(p -> p.getPrice() >= min).toList();
        if (max >= 0) products = products.stream().filter(p -> p.getPrice() <= max).toList();

        // sort
        switch (sortMode) {
            case "Price ↑" -> products = products.stream().sorted(Comparator.comparingDouble(Product::getPrice)).toList();
            case "Price ↓" -> products = products.stream().sorted(Comparator.comparingDouble(Product::getPrice).reversed()).toList();
            case "Popularity" -> products = products.stream().sorted(Comparator.comparingInt(Product::getPopularity).reversed()).toList();
            default -> {}
        }

        for (Product p : products) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);

            // Product image (use product image if present, otherwise resource placeholder)
            ImageView iv = new ImageView();
            try {
                String imgSrc = null;
                if (p.getImageFilename() != null && !p.getImageFilename().isBlank()) {
                    java.nio.file.Path fp = StorageLocation.imageFolderPath.resolve(p.getImageFilename());
                    if (java.nio.file.Files.exists(fp)) imgSrc = fp.toUri().toString();
                }
                if (imgSrc == null) {
                    java.net.URL res = getClass().getResource("/imageHolder.jpg");
                    imgSrc = res == null ? "" : res.toExternalForm();
                }
                iv.setImage(new Image(imgSrc, 120, 90, true, true));
            } catch (Exception ex) {
                // ignore image load errors
            }

            VBox infoBox = new VBox(4);
            Label name = new Label(p.getName());
            Label priceLabel = new Label(String.format("$%.2f", p.getPrice()));
            Label catLabel = new Label("Category: " + p.getCategory());
            Label stockLabel = new Label(p.getStock() > 0 ? ("In stock: " + p.getStock()) : "Out of stock");
            HBox ratingRow = new HBox(4);
            double avg = p.getAverageRating();
            int stars = (int)Math.round(avg);
            for (int i=0;i<5;i++) {
                Label star = new Label(i<stars?"★":"☆");
                ratingRow.getChildren().add(star);
            }
            ratingRow.getChildren().add(new Label(String.format(" (%.1f)", avg)));
            infoBox.getChildren().addAll(name, priceLabel, catLabel, stockLabel, ratingRow);

            Spinner<Integer> qty = new Spinner<>(1, Math.max(1, p.getStock()), 1);
            qty.getStyleClass().add("form-field");
            Button add = new Button("Add to Cart");
            add.getStyleClass().add("primary-button");
            add.setDisable(p.getStock() <= 0);
            add.setOnAction(e -> {
                String user = loggedInUser == null ? "guest" : loggedInUser;
                if (p.getStock() <= 0) {
                    showReceiptDialog("Cannot add: product out of stock");
                    return;
                }
                cartService.addToCart(user, p, qty.getValue());
                ActionLogger.log("AddToCart: " + user + " -> " + p.getId() + " x" + qty.getValue());
                refreshHeader();
                showToast("Added to cart: " + p.getName());
            });

            Button fav = new Button("♡");
            fav.getStyleClass().add("secondary-button");
            fav.setOnAction(e -> {
                String user = loggedInUser == null ? "guest" : loggedInUser;
                wishlistService.addToWishlist(user, p.getId());
                ActionLogger.log("Wishlist add: " + user + " -> " + p.getId());
                fav.setText("♥");
                showToast("Added to wishlist");
            });

            Button review = new Button("Write review");
            review.getStyleClass().add("secondary-button");
            review.setOnAction(e -> showReviewDialog(p));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            row.getChildren().addAll(iv, infoBox, qty, add, fav, review, spacer);
            container.getChildren().add(row);
        }

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        canvasArea.getChildren().add(sp);
    }

    private void showReviewDialog(Product p) {
        Stage dlg = new Stage(); dlg.initModality(Modality.APPLICATION_MODAL); dlg.setTitle("Write review for " + p.getName());
        VBox root = new VBox(8); root.setPadding(new Insets(12));
        TextField user = new TextField(); user.setPromptText("Your name (optional)");
        Spinner<Integer> rating = new Spinner<>(1,5,5);
        TextArea comment = new TextArea(); comment.setPromptText("Your comment"); comment.setPrefRowCount(4);
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            String uname = user.getText().trim().isEmpty() ? (loggedInUser==null?"guest":loggedInUser) : user.getText().trim();
            int r = rating.getValue();
            String c = comment.getText().trim();
            reviewService.addReview(p.getId(), uname, r, c);
            p.addRating(r);
            productService.persist();
            ActionLogger.log("Review: " + uname + " -> " + p.getId() + " rating:" + r);
            dlg.close();
            loadShopPage();
        });
        root.getChildren().addAll(new Label("Review " + p.getName()), user, rating, comment, submit);
        Scene s = new Scene(root, 400, 300); dlg.setScene(s); dlg.showAndWait();
    }

    @SuppressWarnings("unused")
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
        VBox root = new VBox(10); root.setPadding(new Insets(12));
        Label title = new Label("Profile"); title.getStyleClass().add("form-title");
        Label nameLabel = new Label("Signed in as: " + getDisplayName());

        Button viewOrders = new Button("Order History");
        viewOrders.getStyleClass().add("secondary-button");
        viewOrders.setOnAction(e -> showOrderHistory());

        // Wishlist
        Button viewWishlist = new Button("Wishlist");
        viewWishlist.getStyleClass().add("secondary-button");
        viewWishlist.setOnAction(e -> showWishlist());

        root.getChildren().addAll(title, nameLabel, viewOrders, viewWishlist);
        canvasArea.getChildren().add(root);
    }

    private void showOrderHistory() {
        Stage dlg = new Stage(); dlg.initModality(Modality.APPLICATION_MODAL); dlg.setTitle("Order History");
        VBox root = new VBox(8); root.setPadding(new Insets(12));
        String user = loggedInUser == null ? "guest" : loggedInUser;
        try {
            List<java.nio.file.Path> files = new ArrayList<>();
            if (Files.exists(StorageLocation.orderedPath)) {
                try (java.util.stream.Stream<Path> s = Files.list(StorageLocation.orderedPath)) {
                    files.addAll(s.toList());
                }
            }
            if (Files.exists(StorageLocation.collectedPath)) {
                try (java.util.stream.Stream<Path> s = Files.list(StorageLocation.collectedPath)) {
                    files.addAll(s.toList());
                }
            }
            for (java.nio.file.Path p : files) {
                String content = Files.readString(p, StandardCharsets.UTF_8);
                if (content.contains("Customer: " + user)) {
                    Button open = new Button(p.getFileName().toString());
                    open.setOnAction(e -> showReceiptDialog(content));
                    root.getChildren().add(open);
                }
            }
        } catch (Exception ex) { root.getChildren().add(new Label("Error reading history")); }
        Scene s = new Scene(root, 500, 400); dlg.setScene(s); dlg.showAndWait();
    }

    private void showWishlist() {
        Stage dlg = new Stage(); dlg.initModality(Modality.APPLICATION_MODAL); dlg.setTitle("Wishlist");
        VBox root = new VBox(8); root.setPadding(new Insets(12));
        String user = loggedInUser == null ? "guest" : loggedInUser;
        Set<String> items = wishlistService.getWishlist(user);
        for (String pid : items) {
            productService.findById(pid).ifPresent(p -> {
                HBox row = new HBox(8);
                row.getChildren().addAll(new Label(p.getName()), new Label("$"+p.getPrice()));
                root.getChildren().add(row);
            });
        }
        Scene s = new Scene(root, 400, 300); dlg.setScene(s); dlg.showAndWait();
    }

    private void loadSettingsPage() {
        canvasArea.getChildren().clear();

        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        Label title = new Label("Settings / Admin");
        title.getStyleClass().add("form-title");

        // Settings are accessible to all users; admin-only controls are hidden based on role where needed

        // Role switcher
        HBox roleRow = new HBox(8);
        roleRow.setAlignment(Pos.CENTER_LEFT);
        Label roleLabel = new Label("Current role:"); roleLabel.getStyleClass().add("form-label");
        ComboBox<String> roleBox = new ComboBox<>(); roleBox.getItems().addAll("Guest","Customer","Admin");
        roleBox.getSelectionModel().select(loggedInUser == null ? "Guest" : (userService.isAdmin(loggedInUser)?"Admin":"Customer"));
        Button applyRole = new Button("Switch Role"); applyRole.getStyleClass().add("secondary-button");
        applyRole.setOnAction(ev -> {
            String sel = roleBox.getValue();
            if (sel == null) return;
            if (sel.equals("Guest")) { loggedInUser = null; }
            else if (sel.equals("Admin")) { loggedInUser = "admin"; }
            else { loggedInUser = "customer"; }
            ActionLogger.log("Role switched to " + sel);
            refreshHeader();
            showToast("Role set to " + sel);
        });
        roleRow.getChildren().addAll(roleLabel, roleBox, applyRole);

        root.getChildren().addAll(title, roleRow);

        // Change password moved to Settings (only enabled when logged in)
        Label pwdLabel = new Label("Change password"); pwdLabel.getStyleClass().add("form-title");
        PasswordField current = new PasswordField(); current.setPromptText("Current password"); current.getStyleClass().add("form-field");
        PasswordField next = new PasswordField(); next.setPromptText("New password"); next.getStyleClass().add("form-field");
        PasswordField confirm = new PasswordField(); confirm.setPromptText("Confirm new password"); confirm.getStyleClass().add("form-field");
        Button change = new Button("Change Password"); change.getStyleClass().add("primary-button");
        Label msg = new Label();
        change.setOnAction(e -> {
            if (loggedInUser == null) { msg.setText("Please log in to change password."); return; }
            String cur = current.getText(); String n = next.getText(); String c = confirm.getText();
            if (n == null || n.trim().isEmpty()) { msg.setText("New password cannot be empty."); return; }
            if (!n.equals(c)) { msg.setText("New password and confirmation do not match."); return; }
            boolean ok = userService.changePassword(loggedInUser, cur, n);
            msg.setText(ok ? "Password changed." : "Current password incorrect.");
            if (ok) showToast("Password changed");
            current.clear(); next.clear(); confirm.clear();
        });
        root.getChildren().addAll(new Separator(), pwdLabel, current, next, confirm, change, msg);

        // Product management
        Label prodLabel = new Label("Products");
        prodLabel.getStyleClass().add("form-title");
        VBox prodBox = new VBox(6);
        for (Product p : productService.listAll()) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            Label info = new Label(p.getId() + ": " + p.getName() + " - $" + p.getPrice());

            Button edit = new Button("Edit");
            edit.setOnAction(ev -> {
                Stage dlg = new Stage(); dlg.initModality(Modality.APPLICATION_MODAL); dlg.setTitle("Edit product: " + p.getName());
                VBox r = new VBox(8); r.setPadding(new Insets(12));
                Label idLabel = new Label("ID: " + p.getId());
                TextField nameField = new TextField(p.getName());
                TextField priceField = new TextField(String.valueOf(p.getPrice()));
                TextField catField = new TextField(p.getCategory());
                TextField stockField = new TextField(String.valueOf(p.getStock()));
                TextField imgField = new TextField(p.getImageFilename() == null ? "" : p.getImageFilename());
                Button choose = new Button("Choose Image");
                choose.setOnAction(ch -> {
                    javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
                    chooser.setTitle("Select product image");
                    java.io.File sel = chooser.showOpenDialog(dlg);
                    if (sel != null) {
                        try {
                            java.nio.file.Path imagesDir = StorageLocation.imageFolderPath;
                            java.nio.file.Files.createDirectories(imagesDir);
                            java.nio.file.Path dest = imagesDir.resolve(sel.getName());
                            java.nio.file.Files.copy(sel.toPath(), dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                            imgField.setText(sel.getName());
                        } catch (Exception ex) { /* ignore */ }
                    }
                });
                Button save = new Button("Save");
                Label editMsg = new Label();
                save.setOnAction(sv -> {
                    try {
                        double pr = Double.parseDouble(priceField.getText().trim());
                        int st = Integer.parseInt(stockField.getText().trim());
                        String nm = nameField.getText().trim();
                        String cat = catField.getText().trim().isEmpty() ? "Uncategorized" : catField.getText().trim();
                        String img = imgField.getText().trim().isEmpty() ? null : imgField.getText().trim();
                        // preserve popularity and ratings
                        int pop = p.getPopularity();
                        long rsum = p.getRatingSum();
                        int rcount = p.getRatingCount();
                        Product updated = new Product(p.getId(), nm, pr, img, cat, st, pop, rsum, rcount);
                        productService.add(updated);
                        productService.persist();
                        ActionLogger.log("Product updated: " + p.getId());
                        dlg.close();
                        loadSettingsPage();
                    } catch (NumberFormatException nfe) {
                        editMsg.setText("Invalid number for price or stock");
                    }
                });
                r.getChildren().addAll(new Label("Edit product"), idLabel, new Label("Name"), nameField, new Label("Price"), priceField, new Label("Category"), catField, new Label("Stock"), stockField, new Label("Image filename"), imgField, choose, save, editMsg);
                Scene sc = new Scene(r, 420, 520); dlg.setScene(sc); dlg.showAndWait();
            });

            Button del = new Button("Remove");
            del.setOnAction(e -> {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Delete product " + p.getName() + "?", ButtonType.OK, ButtonType.CANCEL);
                conf.setTitle("Confirm remove");
                Optional<ButtonType> res = conf.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.OK) {
                    productService.remove(p.getId());
                    ActionLogger.log("Product removed: " + p.getId());
                    loadSettingsPage();
                }
            });

            row.getChildren().addAll(info, edit, del);
            prodBox.getChildren().add(row);
        }

        HBox addProdRow = new HBox(8);
        TextField newId = new TextField(); newId.setPromptText("id");
        TextField newName = new TextField(); newName.setPromptText("name");
        TextField newPrice = new TextField(); newPrice.setPromptText("price");
        TextField newImage = new TextField(); newImage.setPromptText("image filename (or choose file)");
        Button chooseImage = new Button("Choose Image");
        chooseImage.setOnAction(evt -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.setTitle("Select product image");
            // optional: set initial dir
            java.io.File selected = chooser.showOpenDialog(null);
            if (selected != null) {
                try {
                    // ensure images directory exists
                    java.nio.file.Path imagesDir = StorageLocation.imageFolderPath;
                    java.nio.file.Files.createDirectories(imagesDir);
                    java.nio.file.Path dest = imagesDir.resolve(selected.getName());
                    // copy file (overwrite existing)
                    java.nio.file.Files.copy(selected.toPath(), dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    newImage.setText(selected.getName());
                } catch (Exception ex) {
                    // ignore copy errors
                }
            }
        });
        Button addProd = new Button("Add Product");
        addProd.setOnAction(e -> {
            try {
                double price = Double.parseDouble(newPrice.getText());
                String img = newImage.getText().trim().isEmpty() ? null : newImage.getText().trim();
                productService.add(new Product(newId.getText().trim(), newName.getText().trim(), price, img));
                productService.persist();
                newId.clear(); newName.clear(); newPrice.clear(); newImage.clear();
                loadSettingsPage();
            } catch (NumberFormatException ex) {
                // ignore invalid price
            }
        });
        addProdRow.getChildren().addAll(newId, newName, newPrice, newImage, chooseImage, addProd);

        // Discount management
        Label discLabel = new Label("Discount codes");
        discLabel.getStyleClass().add("form-title");
        VBox discBox = new VBox(6);
        for (Map.Entry<String,Integer> e : discountService.listAll().entrySet()) {
            HBox row = new HBox(8);
            Label info = new Label(e.getKey() + " - " + e.getValue() + "%");
            Button del = new Button("Remove");
            del.setOnAction(ev -> {
                discountService.remove(e.getKey());
                loadSettingsPage();
            });
            row.getChildren().addAll(info, del);
            discBox.getChildren().add(row);
        }

        HBox addDiscRow = new HBox(8);
        TextField discCode = new TextField(); discCode.setPromptText("code");
        TextField discPct = new TextField(); discPct.setPromptText("percent");
        Button addDisc = new Button("Add Discount");
        addDisc.setOnAction(ev -> {
            try {
                int pct = Integer.parseInt(discPct.getText());
                discountService.add(discCode.getText().trim(), pct);
                discCode.clear(); discPct.clear();
                loadSettingsPage();
            } catch (NumberFormatException ex) {
                // ignore
            }
        });
        addDiscRow.getChildren().addAll(discCode, discPct, addDisc);

        root.getChildren().addAll(new Separator(), prodLabel, prodBox, addProdRow, new Separator(), discLabel, discBox, addDiscRow);

        canvasArea.getChildren().add(root);
    }

    // Display notification center dialog
    private void showNotificationCenter() {
        Stage dlg = new Stage(); dlg.initModality(Modality.APPLICATION_MODAL); dlg.setTitle("Notifications");
        VBox root = new VBox(8); root.setPadding(new Insets(12));
        TextArea area = new TextArea(); area.setEditable(false); area.setPrefRowCount(12);
        area.setText(ci553.shoppingcenter.service.NotificationService.readAll());
        Button close = new Button("Close"); close.setOnAction(e -> dlg.close());
        Button clear = new Button("Clear"); clear.setOnAction(e -> {
            try {
                java.nio.file.Path log = ci553.shoppingcenter.utility.StorageLocation.dataPath.resolve("notifications.log");
                if (java.nio.file.Files.exists(log)) java.nio.file.Files.delete(log);
            } catch (Exception ex) { /* ignore */ }
            area.setText("");
            showToast("Notifications cleared");
        });
        HBox actions = new HBox(8, clear, close);
        actions.setAlignment(Pos.CENTER_RIGHT);
        root.getChildren().addAll(new Label("Notifications"), area, actions);
        Scene s = new Scene(root, 520, 360); dlg.setScene(s); dlg.showAndWait();
    }

    // Simple transient toast-like notification
    private void showToast(String message) {
        Stage t = new Stage(); t.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        t.initOwner(canvasArea.getScene() == null ? null : canvasArea.getScene().getWindow());
        Label lab = new Label(message); lab.getStyleClass().add("toast"); lab.setPadding(new Insets(8));
        StackPane p = new StackPane(lab); p.setStyle("-fx-background-color: rgba(0,0,0,0.75); -fx-background-radius:6; -fx-text-fill: white;");
        Scene sc = new Scene(p); sc.setFill(null);
        t.setScene(sc); t.setWidth(300); t.setHeight(60);
        // position: bottom-right of main window
        if (canvasArea.getScene() != null && canvasArea.getScene().getWindow() != null) {
            javafx.stage.Window owner = canvasArea.getScene().getWindow();
            t.setX(owner.getX() + owner.getWidth() - 320);
            t.setY(owner.getY() + owner.getHeight() - 120);
        }
        t.show();
        // auto close
        new Thread(() -> {
            try { Thread.sleep(1600); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(t::close);
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
