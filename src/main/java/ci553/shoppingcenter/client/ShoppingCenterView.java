package ci553.shoppingcenter.client;

import ci553.shoppingcenter.model.Product;
import ci553.shoppingcenter.model.Order;
import ci553.shoppingcenter.service.CartService;
import ci553.shoppingcenter.service.DiscountService;
import ci553.shoppingcenter.service.OrderService;
import ci553.shoppingcenter.service.ProductService;
import ci553.shoppingcenter.service.UserService;
import ci553.shoppingcenter.service.WishlistService;
import ci553.shoppingcenter.service.ReviewService;
import ci553.shoppingcenter.service.OrderTrackingService;
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
    private final OrderTrackingService orderTrackingService = new OrderTrackingService();

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

        // Load initial content (Home page - main dashboard)
        loadHomePage();

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

        Button homeBtn = createNavButton("🏠", "Home", "Active", true);
        Button shopBtn = createNavButton("🛍️", "Shop", "S", false);
        Button profileBtn = createNavButton("👤", "Profile", "P", false);
        Button settingsBtn = createNavButton("⚙️", "Settings", "ST", false);
        Button warehouseBtn = createNavButton("📦", "Warehouse", "W", false);

        homeBtn.setOnAction(e -> switchPage("Home", homeBtn, shopBtn, profileBtn, settingsBtn, warehouseBtn));
        shopBtn.setOnAction(e -> switchPage("Shop", homeBtn, shopBtn, profileBtn, settingsBtn, warehouseBtn));
        profileBtn.setOnAction(e -> switchPage("Profile", homeBtn, shopBtn, profileBtn, settingsBtn, warehouseBtn));
        settingsBtn.setOnAction(e -> switchPage("Settings", homeBtn, shopBtn, profileBtn, settingsBtn, warehouseBtn));
        warehouseBtn.setOnAction(e -> switchPage("Warehouse", homeBtn, shopBtn, profileBtn, settingsBtn, warehouseBtn));

        // Warehouse is permanently visible for all users
        nav.getChildren().addAll(homeBtn, shopBtn, profileBtn, settingsBtn, warehouseBtn);

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
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setAlignment(Pos.CENTER_LEFT);

        // Brand/Logo
        Label title = new Label("🛍️ Shopping Center");
        title.getStyleClass().add("title");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        // Spacer to push profile section to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User info and actions
        HBox userSection = new HBox(12);
        userSection.setAlignment(Pos.CENTER_RIGHT);

        // Current user label
        profileNameLabel = new Label("👤 " + getDisplayName());
        profileNameLabel.getStyleClass().add("profile-name");
        profileNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #7c4dff;");

        // Login/Logout button
        loginButton = new Button(loggedInUser == null ? "🔑 Login" : "🚪 Logout");
        loginButton.getStyleClass().add("header-button");
        loginButton.setStyle("-fx-text-fill: #7c4dff; -fx-font-weight: bold; -fx-font-size: 13px;");
        loginButton.setMinWidth(90);
        loginButton.setOnAction(e -> {
            if (loggedInUser == null) {
                showLoginDialog(profileNameLabel);
            } else {
                ActionLogger.log("Logout: " + loggedInUser);
                loggedInUser = null;
                profileNameLabel.setText("👤 " + getDisplayName());
                loginButton.setText("🔑 Login");
                refreshHeader();
                loadHomePage(); // Refresh home to update user-specific data
            }
        });

        // Cart button
        cartButton = new Button("🛒 Cart (" + cartSizeForDisplay() + ")");
        cartButton.getStyleClass().add("header-button");
        cartButton.setStyle("-fx-text-fill: #7c4dff; -fx-font-weight: bold; -fx-font-size: 13px;");
        cartButton.setMinWidth(100);
        cartButton.setOnAction(e -> showCartDialog());

        // Theme toggle
        ToggleButton themeToggle = new ToggleButton(darkMode ? "🌙 Dark" : "☀️ Light");
        themeToggle.getStyleClass().add("header-button");
        themeToggle.setStyle("-fx-text-fill: #7c4dff; -fx-font-weight: bold; -fx-font-size: 13px;");
        themeToggle.setMinWidth(90);
        themeToggle.setOnAction(e -> {
            darkMode = !darkMode;
            themeToggle.setText(darkMode ? "🌙 Dark" : "☀️ Light");
            Scene s = header.getScene();
            if (s != null) {
                if (darkMode) s.getRoot().getStyleClass().remove("light-mode");
                else s.getRoot().getStyleClass().add("light-mode");
            }
        });

        // Admin button (hidden by default, shown for admins)
        adminButton = new Button("📦 Orders");
        adminButton.getStyleClass().add("header-button");
        adminButton.setStyle("-fx-text-fill: #7c4dff; -fx-font-weight: bold; -fx-font-size: 13px;");
        adminButton.setMinWidth(100);
        adminButton.setOnAction(e -> showOrderTrackerDialog());
        adminButton.setVisible(false);
        adminButton.setManaged(false);

        userSection.getChildren().addAll(profileNameLabel, loginButton, cartButton, themeToggle, adminButton);

        header.getChildren().addAll(title, spacer, userSection);

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
        if (profileNameLabel != null) profileNameLabel.setText("👤 " + getDisplayName());
        if (loginButton != null) loginButton.setText(loggedInUser == null ? "🔑 Login" : "🚪 Logout");
        if (cartButton != null) cartButton.setText("🛒 Cart (" + cartSizeForDisplay() + ")");
        if (adminButton != null) {
            boolean show = loggedInUser != null && userService.isAdmin(loggedInUser);
            adminButton.setVisible(show);
            adminButton.setManaged(show);
        }
    }

    private void showLoginDialog(Label profileName) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Login / Register");

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.getStyleClass().add("form-container");

        TabPane tabs = new TabPane();
        Tab loginTab = new Tab("Login");
        Tab regTab = new Tab("Register");
        tabs.getTabs().addAll(loginTab, regTab);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Login content
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(12));
        Label loginTitle = new Label("Login to Your Account");
        loginTitle.getStyleClass().add("form-title");
        TextField username = new TextField();
        username.setPromptText("Username");
        username.getStyleClass().add("form-field");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.getStyleClass().add("form-field");
        Button doLogin = new Button("Login");
        doLogin.getStyleClass().add("primary-button");
        doLogin.setMaxWidth(Double.MAX_VALUE);
        Label msg = new Label();
        msg.getStyleClass().add("muted");
        msg.setStyle("-fx-text-fill: #ff5a8a;");

        doLogin.setOnAction(e -> {
            String u = username.getText().trim();
            String p = password.getText();

            // Validation
            if (u.isEmpty()) {
                msg.setText("Username is required");
                return;
            }
            if (p.isEmpty()) {
                msg.setText("Password is required");
                return;
            }

            Optional<ci553.shoppingcenter.model.User> user = userService.login(u, p);
            if (user.isPresent()) {
                loggedInUser = u;
                ActionLogger.log("Login: " + u);
                profileName.setText(getDisplayName());
                refreshHeader();
                dlg.close();
                showToast("Welcome back, " + getDisplayName() + "!");
            } else {
                msg.setText("Invalid username or password");
                password.clear();
            }
        });

        // Enter key to login
        password.setOnAction(e -> doLogin.fire());

        loginBox.getChildren().addAll(loginTitle, new Label("Username:"), username, new Label("Password:"), password, doLogin, msg);
        loginTab.setContent(loginBox);

        // Register content
        VBox regBox = new VBox(10);
        regBox.setPadding(new Insets(12));
        Label regTitle = new Label("Create New Account");
        regTitle.getStyleClass().add("form-title");
        TextField rUser = new TextField();
        rUser.setPromptText("Username (min 3 characters)");
        rUser.getStyleClass().add("form-field");
        TextField rName = new TextField();
        rName.setPromptText("Display name");
        rName.getStyleClass().add("form-field");
        PasswordField rPass = new PasswordField();
        rPass.setPromptText("Password (min 6 characters)");
        rPass.getStyleClass().add("form-field");
        PasswordField rPassConfirm = new PasswordField();
        rPassConfirm.setPromptText("Confirm password");
        rPassConfirm.getStyleClass().add("form-field");
        Button doReg = new Button("Register");
        doReg.getStyleClass().add("primary-button");
        doReg.setMaxWidth(Double.MAX_VALUE);
        Label rMsg = new Label();
        rMsg.getStyleClass().add("muted");
        rMsg.setStyle("-fx-text-fill: #ff5a8a;");

        doReg.setOnAction(e -> {
            String un = rUser.getText().trim();
            String dn = rName.getText().trim();
            String pw = rPass.getText();
            String pwConfirm = rPassConfirm.getText();

            // Comprehensive validation
            if (un.isEmpty()) {
                rMsg.setText("Username is required");
                return;
            }
            if (un.length() < 3) {
                rMsg.setText("Username must be at least 3 characters");
                return;
            }
            if (pw.isEmpty()) {
                rMsg.setText("Password is required");
                return;
            }
            if (pw.length() < 6) {
                rMsg.setText("Password must be at least 6 characters");
                return;
            }
            if (!pw.equals(pwConfirm)) {
                rMsg.setText("Passwords do not match");
                rPassConfirm.clear();
                return;
            }

            boolean ok = userService.register(un, dn.isEmpty() ? un : dn, pw);
            if (ok) {
                rMsg.setText("Registration successful!");
                rMsg.setStyle("-fx-text-fill: #5ce6b8;");
                ActionLogger.log("Registered: " + un);
                showToast("Account created! Please login.");
                // Switch to login tab
                tabs.getSelectionModel().select(loginTab);
                username.setText(un);
                password.requestFocus();
            } else {
                rMsg.setText("Username already exists");
            }
        });

        regBox.getChildren().addAll(regTitle, new Label("Username:"), rUser, new Label("Display Name:"), rName, new Label("Password:"), rPass, new Label("Confirm Password:"), rPassConfirm, doReg, rMsg);
        regTab.setContent(regBox);

        root.getChildren().addAll(tabs);
        Scene s = new Scene(root, 480, 520);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    private void showCartDialog() {
        String user = loggedInUser == null ? "guest" : loggedInUser;
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Your Cart");

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.getStyleClass().add("form-container");

        Label cartTitle = new Label("Shopping Cart");
        cartTitle.getStyleClass().add("form-title");
        root.getChildren().add(cartTitle);

        Map<String, Integer> cart = cartService.getCart(user);
        if (cart.isEmpty()) {
            Label emptyMsg = new Label("Your cart is empty.");
            emptyMsg.getStyleClass().add("muted");
            root.getChildren().add(emptyMsg);
        } else {
            VBox itemsList = new VBox(8);
            final double[] subtotal = {0.0};

            for (Map.Entry<String, Integer> e : cart.entrySet()) {
                String pid = e.getKey();
                int qty = e.getValue();
                Optional<Product> prodOpt = productService.findById(pid);
                if (prodOpt.isEmpty()) continue;
                Product prod = prodOpt.get();
                double line = prod.getPrice() * qty;
                subtotal[0] += line;

                HBox row = new HBox(12);
                row.setAlignment(Pos.CENTER_LEFT);
                row.getStyleClass().add("product-card");
                row.setPadding(new Insets(12));

                // Product image
                ImageView iv = new ImageView();
                try {
                    String imgSrc = null;
                    if (prod.getImageFilename() != null && !prod.getImageFilename().isBlank()) {
                        java.nio.file.Path fp = StorageLocation.imageFolderPath.resolve(prod.getImageFilename());
                        if (java.nio.file.Files.exists(fp)) imgSrc = fp.toUri().toString();
                    }
                    if (imgSrc == null) {
                        java.net.URL res = getClass().getResource("/imageHolder.jpg");
                        imgSrc = res == null ? "" : res.toExternalForm();
                    }
                    iv.setImage(new Image(imgSrc, 80, 60, true, true));
                } catch (Exception ex) {
                    // ignore image errors
                }

                VBox info = new VBox(4);
                Label name = new Label(prod.getName());
                name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                Label price = new Label(String.format("$%.2f each", prod.getPrice()));
                price.getStyleClass().add("muted");
                Label stockInfo = new Label("Available: " + prod.getStock());
                stockInfo.getStyleClass().add("muted");
                stockInfo.setStyle("-fx-font-size: 11px;");
                info.getChildren().addAll(name, price, stockInfo);

                // Quantity spinner with validation
                Spinner<Integer> qtySpinner = new Spinner<>(1, Math.max(1, prod.getStock()), qty);
                qtySpinner.setEditable(true);
                qtySpinner.setPrefWidth(80);
                qtySpinner.getStyleClass().add("form-field");

                Label lineTotal = new Label(String.format("$%.2f", line));
                lineTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                lineTotal.setMinWidth(80);

                Button update = new Button("Update");
                update.getStyleClass().add("secondary-button");
                update.setOnAction(ev -> {
                    try {
                        int newQty = qtySpinner.getValue();
                        if (newQty > prod.getStock()) {
                            showAlert("Stock Limit", "Only " + prod.getStock() + " units available.");
                            return;
                        }
                        cartService.updateCart(user, pid, newQty);
                        ActionLogger.log("Cart update: " + user + " -> " + pid + " qty:" + newQty);
                        dlg.close();
                        refreshHeader();
                        showCartDialog();
                    } catch (Exception ex) {
                        showAlert("Error", "Invalid quantity");
                    }
                });

                Button remove = new Button("Remove");
                remove.getStyleClass().add("secondary-button");
                remove.setOnAction(ev -> {
                    cartService.removeFromCart(user, pid);
                    ActionLogger.log("Cart remove: " + user + " -> " + pid);
                    dlg.close();
                    refreshHeader();
                    showCartDialog();
                });

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                row.getChildren().addAll(iv, info, spacer, qtySpinner, update, lineTotal, remove);
                itemsList.getChildren().add(row);
            }

            ScrollPane itemsScroll = new ScrollPane(itemsList);
            itemsScroll.setFitToWidth(true);
            itemsScroll.setPrefHeight(300);
            itemsScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            root.getChildren().add(itemsScroll);

            Separator sep = new Separator();
            root.getChildren().add(sep);

            final double orderSubtotal = subtotal[0];
            Label subtotalLabel = new Label(String.format("Subtotal: $%.2f", orderSubtotal));
            subtotalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            // Discount section with available codes hint
            VBox discountSection = new VBox(8);
            Label discountHeader = new Label("💰 Discount Code");
            discountHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // Show available codes
            Map<String, Integer> availableCodes = discountService.listAll();
            StringBuilder codesHint = new StringBuilder("Available codes: ");
            availableCodes.forEach((code, pct) -> codesHint.append(code).append(" (").append(pct).append("%), "));
            String codesHintText = codesHint.substring(0, codesHint.length() - 2); // Remove trailing ", "

            Label codesLabel = new Label(codesHintText);
            codesLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7c4dff; -fx-font-style: italic;");
            codesLabel.setWrapText(true);

            TextField discountCode = new TextField();
            discountCode.setPromptText("Enter discount code");
            discountCode.getStyleClass().add("form-field");
            discountCode.setPrefWidth(200);

            Button applyDiscount = new Button("Apply Code");
            applyDiscount.getStyleClass().add("secondary-button");

            Label discountInfo = new Label();
            discountInfo.getStyleClass().add("muted");

            final int[] appliedDiscount = {0};
            final double[] finalAmount = {orderSubtotal}; // Track final amount after discount

            applyDiscount.setOnAction(evt -> {
                String code = discountCode.getText().trim();
                if (code.isEmpty()) {
                    discountInfo.setText("⚠️ Please enter a discount code");
                    discountInfo.setStyle("-fx-text-fill: #ff9800;");
                    return;
                }
                int pct = discountService.validateCode(code);
                appliedDiscount[0] = pct;
                if (pct > 0) {
                    double discounted = orderSubtotal * (1.0 - pct / 100.0);
                    finalAmount[0] = discounted;
                    discountInfo.setText("✅ Successfully applied " + pct + "% discount!");
                    discountInfo.setStyle("-fx-text-fill: #5ce6b8; -fx-font-weight: bold;");
                    subtotalLabel.setText(String.format("Subtotal: $%.2f\nDiscount (-%d%%): -$%.2f\nTotal: $%.2f",
                        orderSubtotal, pct, orderSubtotal - discounted, discounted));
                    subtotalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");
                } else {
                    discountInfo.setText("❌ Invalid code. Please check available codes above.");
                    discountInfo.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                    finalAmount[0] = orderSubtotal;
                }
            });

            HBox discountInputRow = new HBox(8);
            discountInputRow.setAlignment(Pos.CENTER_LEFT);
            discountInputRow.getChildren().addAll(discountCode, applyDiscount);

            discountSection.getChildren().addAll(discountHeader, codesLabel, discountInputRow, discountInfo);

            Button pay = new Button("Proceed to Checkout");
            pay.getStyleClass().add("primary-button");
            pay.setOnAction(ev -> {
                // Validate stock before payment
                boolean stockIssue = false;
                for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                    Optional<Product> pOpt = productService.findById(entry.getKey());
                    if (pOpt.isEmpty() || pOpt.get().getStock() < entry.getValue()) {
                        stockIssue = true;
                        break;
                    }
                }
                if (stockIssue) {
                    showAlert("Stock Issue", "Some items in your cart are no longer available in the requested quantity. Please update your cart.");
                    return;
                }

                // Get user identifier
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

                // Collect shipping information
                Map<String, String> shippingInfo = showShippingDialog();
                if (shippingInfo == null || shippingInfo.isEmpty()) {
                    return; // User cancelled
                }

                // Show payment dialog with final discounted amount
                boolean paymentOk = showPaymentDialog(finalAmount[0]);
                if (!paymentOk) return;

                // Create order ID
                String orderId = "ORD-" + System.currentTimeMillis();

                // Create tracked order
                orderTrackingService.createOrder(
                        orderId,
                        buyer,
                        shippingInfo.get("address"),
                        shippingInfo.get("postalCode"),
                        shippingInfo.get("city"),
                        shippingInfo.get("country")
                );

                // Place order (legacy system)
                String receipt = orderService.placeOrder(buyer, cart, id -> productService.findById(id).map(Product::getPrice).orElse(0.0), appliedDiscount[0], id -> productService.findById(id).map(Product::getName).orElse(id));

                // Add shipping info to receipt
                receipt += "\n\n--- SHIPPING INFORMATION ---\n";
                receipt += "Order ID: " + orderId + "\n";
                receipt += "Delivery Address: " + shippingInfo.get("address") + "\n";
                receipt += "City: " + shippingInfo.get("city") + "\n";
                receipt += "Postal Code: " + shippingInfo.get("postalCode") + "\n";
                receipt += "Country: " + shippingInfo.get("country") + "\n";
                receipt += "Status: Order Placed\n";
                receipt += "Estimated Delivery: " + java.time.LocalDate.now().plusDays(5).format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")) + "\n";

                // decrease stock
                cart.forEach((pid, qtt) -> productService.findById(pid).ifPresent(p -> { p.setStock(p.getStock()-qtt); p.incrementPopularity(); }));
                productService.persist();
                cartService.clearCart(user);
                dlg.close();
                ActionLogger.log("Order placed by " + buyer + " - Order ID: " + orderId);

                // export invoice
                try {
                    Files.createDirectories(StorageLocation.ordersPath.resolve("invoices"));
                    java.nio.file.Path inv = StorageLocation.ordersPath.resolve("invoices").resolve("invoice_" + orderId + ".txt");
                    Files.writeString(inv, receipt, StandardCharsets.UTF_8);
                } catch (Exception ex) { /*ignore*/ }
                refreshHeader();
                showReceiptDialog(receipt);
                showToast("Order placed successfully! Order ID: " + orderId);
            });

            root.getChildren().addAll(subtotalLabel, discountSection, pay);
        }

        Scene s = new Scene(root, 750, 600);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    private Map<String, String> showShippingDialog() {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Shipping Information");

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.getStyleClass().add("form-container");

        Label title = new Label("Enter Delivery Address");
        title.getStyleClass().add("form-title");

        Label desc = new Label("Please provide your shipping address for delivery:");
        desc.getStyleClass().add("muted");

        // Address fields
        TextField addressField = new TextField();
        addressField.setPromptText("Street address, Apt/Unit number");
        addressField.getStyleClass().add("form-field");

        TextField cityField = new TextField();
        cityField.setPromptText("City");
        cityField.getStyleClass().add("form-field");

        TextField postalCodeField = new TextField();
        postalCodeField.setPromptText("Postal / ZIP Code");
        postalCodeField.getStyleClass().add("form-field");

        ComboBox<String> countryDropdown = new ComboBox<>();
        countryDropdown.getItems().addAll(
            "United Kingdom", "United States", "Canada", "Australia", "Germany",
            "France", "Italy", "Spain", "Netherlands", "Belgium", "Sweden",
            "Norway", "Denmark", "Finland", "Ireland", "Poland", "Switzerland",
            "Austria", "Portugal", "Greece", "Japan", "South Korea", "Singapore",
            "New Zealand", "India", "Brazil", "Mexico", "Argentina", "China"
        );
        countryDropdown.getSelectionModel().selectFirst(); // Default to United Kingdom
        countryDropdown.getStyleClass().add("form-field");
        countryDropdown.setPrefWidth(Double.MAX_VALUE);
        countryDropdown.setStyle("-fx-font-size: 13px;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");

        Button continueBtn = new Button("Continue to Payment");
        continueBtn.getStyleClass().add("primary-button");
        continueBtn.setMaxWidth(Double.MAX_VALUE);

        final Map<String, String>[] result = new Map[]{null};

        continueBtn.setOnAction(e -> {
            // Validation
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String postalCode = postalCodeField.getText().trim();
            String country = countryDropdown.getValue();

            if (address.isEmpty()) {
                errorLabel.setText("Street address is required");
                return;
            }
            if (city.isEmpty()) {
                errorLabel.setText("City is required");
                return;
            }
            if (postalCode.isEmpty()) {
                errorLabel.setText("Postal/ZIP code is required");
                return;
            }
            if (country == null || country.isEmpty()) {
                errorLabel.setText("Please select a country");
                return;
            }

            // Store shipping info
            result[0] = new HashMap<>();
            result[0].put("address", address);
            result[0].put("city", city);
            result[0].put("postalCode", postalCode);
            result[0].put("country", country);

            dlg.close();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("secondary-button");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setOnAction(e -> dlg.close());

        root.getChildren().addAll(
                title,
                desc,
                new Label("Street Address:"),
                addressField,
                new Label("City:"),
                cityField,
                new Label("Postal / ZIP Code:"),
                postalCodeField,
                new Label("Country:"),
                countryDropdown,
                errorLabel,
                continueBtn,
                cancelBtn
        );

        Scene s = new Scene(root, 480, 550);
        dlg.setScene(s);
        dlg.showAndWait();

        return result[0];
    }

    private boolean showPaymentDialog(double amount) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Payment Information");

        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("form-container");

        Label title = new Label("Enter Payment Details");
        title.getStyleClass().add("form-title");

        Label amtLabel = new Label(String.format("Amount to Pay: $%.2f", amount));
        amtLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        // Card Type Selection
        Label cardTypeLabel = new Label("Card Type:");
        cardTypeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        ComboBox<String> cardTypeBox = new ComboBox<>();
        cardTypeBox.getItems().addAll("Visa", "Mastercard", "American Express", "Discover", "Debit Card");
        cardTypeBox.getSelectionModel().selectFirst();
        cardTypeBox.setPrefWidth(Double.MAX_VALUE);
        cardTypeBox.setStyle("-fx-font-size: 13px;");

        // Card Number
        Label cardNumLabel = new Label("Card Number:");
        cardNumLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        TextField cardField = new TextField();
        cardField.setPromptText("1234 5678 9012 3456");
        cardField.getStyleClass().add("form-field");

        // Auto-format card number with spaces
        cardField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !newVal.equals(old)) {
                String cleaned = newVal.replaceAll("[^\\d]", "");
                if (cleaned.length() > 16) {
                    cleaned = cleaned.substring(0, 16);
                }
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < cleaned.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ");
                    }
                    formatted.append(cleaned.charAt(i));
                }
                if (!formatted.toString().equals(newVal)) {
                    cardField.setText(formatted.toString());
                    cardField.positionCaret(formatted.length());
                }
            }
        });

        // Cardholder Name
        Label nameLabel = new Label("Cardholder Name:");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");
        nameField.getStyleClass().add("form-field");

        // Expiry and CVV Row
        HBox detailsRow = new HBox(12);

        VBox expiryBox = new VBox(6);
        Label expLabel = new Label("Expiry Date:");
        expLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        TextField expField = new TextField();
        expField.setPromptText("MM/YY");
        expField.setPrefWidth(100);
        expField.getStyleClass().add("form-field");

        // Auto-format expiry with automatic slash insertion
        expField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !newVal.equals(old)) {
                String cleaned = newVal.replaceAll("[^\\d]", "");
                if (cleaned.length() > 4) {
                    cleaned = cleaned.substring(0, 4);
                }
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < cleaned.length(); i++) {
                    if (i == 2) {
                        formatted.append("/");
                    }
                    formatted.append(cleaned.charAt(i));
                }
                if (!formatted.toString().equals(newVal)) {
                    expField.setText(formatted.toString());
                    expField.positionCaret(formatted.length());
                }
            }
        });

        expiryBox.getChildren().addAll(expLabel, expField);

        VBox cvvBox = new VBox(6);
        Label cvvLabel = new Label("CVV:");
        cvvLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        TextField cvvField = new TextField();
        cvvField.setPromptText("123");
        cvvField.setPrefWidth(80);
        cvvField.getStyleClass().add("form-field");

        // Limit CVV to 3-4 digits
        cvvField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !newVal.matches("\\d{0,4}")) {
                cvvField.setText(old);
            }
        });

        cvvBox.getChildren().addAll(cvvLabel, cvvField);

        detailsRow.getChildren().addAll(expiryBox, cvvBox);

        // Error/Success Message
        Label msgLabel = new Label();
        msgLabel.setStyle("-fx-font-weight: bold;");

        // Pay Button
        Button payButton = new Button("Process Payment");
        payButton.getStyleClass().add("primary-button");
        payButton.setMaxWidth(Double.MAX_VALUE);
        payButton.setStyle("-fx-font-size: 15px; -fx-padding: 12;");

        final boolean[] paymentSuccess = {false};

        payButton.setOnAction(e -> {
            // Validation
            String cardNum = cardField.getText().replaceAll("[ -]", "");
            String name = nameField.getText().trim();
            String expiry = expField.getText().trim();
            String cvv = cvvField.getText().trim();

            if (cardNum.length() < 13) {
                msgLabel.setText("❌ Invalid card number");
                msgLabel.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                return;
            }
            if (name.isEmpty()) {
                msgLabel.setText("❌ Cardholder name is required");
                msgLabel.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                return;
            }
            if (!expiry.matches("\\d{2}/\\d{2}")) {
                msgLabel.setText("❌ Invalid expiry date (use MM/YY)");
                msgLabel.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                return;
            }
            if (cvv.length() < 3) {
                msgLabel.setText("❌ Invalid CVV");
                msgLabel.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                return;
            }

            // Simulate payment processing
            msgLabel.setText("⏳ Processing payment...");
            msgLabel.setStyle("-fx-text-fill: #7c4dff; -fx-font-weight: bold;");

            // Simulate delay
            new Thread(() -> {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(() -> {
                    msgLabel.setText("✅ Payment Approved!");
                    msgLabel.setStyle("-fx-text-fill: #5ce6b8; -fx-font-weight: bold;");
                    paymentSuccess[0] = true;

                    // Auto-close after showing success
                    new Thread(() -> {
                        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                        javafx.application.Platform.runLater(dlg::close);
                    }).start();
                });
            }).start();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("secondary-button");
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setOnAction(e -> dlg.close());

        root.getChildren().addAll(
                title,
                amtLabel,
                cardTypeLabel,
                cardTypeBox,
                cardNumLabel,
                cardField,
                nameLabel,
                nameField,
                detailsRow,
                msgLabel,
                payButton,
                cancelButton
        );

        Scene s = new Scene(root, 450, 580);
        dlg.setScene(s);
        dlg.showAndWait();

        return paymentSuccess[0];
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
        dlg.setTitle("Order Receipt");

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));

        Label title = new Label("📄 Order Receipt");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        TextArea area = new TextArea(receipt);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefRowCount(20);
        area.setPrefColumnCount(50);
        area.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 12px; " +
                     "-fx-text-fill: #2d3748; -fx-control-inner-background: #f7fafc;");

        // Wrap in ScrollPane for better control
        ScrollPane scrollPane = new ScrollPane(area);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button close = new Button("✖ Close");
        close.getStyleClass().add("secondary-button");
        close.setPrefWidth(120);
        close.setOnAction(e -> dlg.close());

        Button export = new Button("📥 Export PDF");
        export.getStyleClass().add("primary-button");
        export.setPrefWidth(140);
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

        buttonBox.getChildren().addAll(export, close);

        root.getChildren().addAll(title, scrollPane, buttonBox);
        Scene s = new Scene(root, 650, 550);
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
            case "Warehouse":
                loadWarehousePage();
                break;
        }
    }

    private void loadHomePage() {
        canvasArea.getChildren().clear();

        VBox homeContainer = new VBox(32);
        homeContainer.setPadding(new Insets(0));
        homeContainer.setMaxWidth(1400); // Max width for large screens
        homeContainer.setAlignment(Pos.TOP_CENTER);

        // Hero Section with Gradient Background
        VBox heroSection = new VBox(16);
        heroSection.setPadding(new Insets(48, 32, 48, 32));
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setMaxWidth(Double.MAX_VALUE);
        heroSection.setStyle("-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" +
                            "-fx-background-radius: 0 0 24 24;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);");

        Label welcomeTitle = new Label("Welcome to Shopping Center");
        welcomeTitle.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: white;");
        welcomeTitle.setWrapText(true);
        welcomeTitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label welcomeSubtitle = new Label("Discover amazing products at unbeatable prices");
        welcomeSubtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: rgba(255, 255, 255, 0.9);");
        welcomeSubtitle.setWrapText(true);
        welcomeSubtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label userGreeting = new Label("Hello, " + getDisplayName() + "! 👋");
        userGreeting.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255, 255, 255, 0.95); -fx-font-weight: 600;");

        heroSection.getChildren().addAll(welcomeTitle, welcomeSubtitle, userGreeting);

        // Main Content Container
        VBox mainContent = new VBox(32);
        mainContent.setPadding(new Insets(32, 24, 24, 24));
        mainContent.setMaxWidth(1200);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Statistics Cards Section
        Label statsTitle = new Label("📊 Platform Overview");
        statsTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setMaxWidth(Double.MAX_VALUE);

        // Stat Cards
        VBox prodCard = createModernStatCard("🛍️", String.valueOf(productService.count()),
            "Products Available", "#7c4dff", "Browse our extensive catalog");

        long categoryCount = productService.listAll().stream().map(Product::getCategory).distinct().count();
        VBox catCard = createModernStatCard("📁", String.valueOf(categoryCount),
            "Categories", "#5ce6b8", "Explore different categories");

        VBox cartCard = createModernStatCard("🛒", String.valueOf(cartSizeForDisplay()),
            "Items in Cart", "#ff8db8", "Ready for checkout");

        String user = loggedInUser == null ? "guest" : loggedInUser;
        int orderCount = orderTrackingService.getOrdersByUser(user).size();
        VBox orderCard = createModernStatCard("📦", String.valueOf(orderCount),
            "Active Orders", "#ffa500", "Track your deliveries");

        statsGrid.add(prodCard, 0, 0);
        statsGrid.add(catCard, 1, 0);
        statsGrid.add(cartCard, 0, 1);
        statsGrid.add(orderCard, 1, 1);

        // Quick Actions Section
        Label actionsTitle = new Label("⚡ Quick Actions");
        actionsTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        GridPane actionsGrid = new GridPane();
        actionsGrid.setHgap(16);
        actionsGrid.setVgap(16);
        actionsGrid.setAlignment(Pos.CENTER);
        actionsGrid.setMaxWidth(Double.MAX_VALUE);

        VBox browseAction = createActionCard("🛍️", "Browse Products",
            "Explore our full catalog", "#7c4dff", e -> switchPage("Shop"));

        VBox cartAction = createActionCard("🛒", "View Cart",
            "Review your items", "#5ce6b8", e -> showCartDialog());

        VBox profileAction = createActionCard("👤", "My Profile",
            "Manage your account", "#ff8db8", e -> switchPage("Profile"));

        VBox ordersAction = createActionCard("📦", "My Orders",
            "Track deliveries", "#ffa500", e -> switchPage("Profile"));

        actionsGrid.add(browseAction, 0, 0);
        actionsGrid.add(cartAction, 1, 0);
        actionsGrid.add(profileAction, 0, 1);
        actionsGrid.add(ordersAction, 1, 1);

        if (loggedInUser != null && userService.isAdmin(loggedInUser)) {
            VBox warehouseAction = createActionCard("📦", "Warehouse",
                "Manage inventory", "#9f7aea", e -> switchPage("Warehouse"));
            actionsGrid.add(warehouseAction, 0, 2);
            GridPane.setColumnSpan(warehouseAction, 2);
        }

        // Categories Section
        Label categoriesTitle = new Label("🏷️ Shop by Category");
        categoriesTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        FlowPane categoriesFlow = new FlowPane(12, 12);
        categoriesFlow.setAlignment(Pos.CENTER);
        categoriesFlow.setMaxWidth(Double.MAX_VALUE);

        List<String> categories = productService.listAll().stream()
            .map(Product::getCategory)
            .distinct()
            .sorted()
            .toList();

        for (String category : categories) {
            long count = productService.listAll().stream()
                .filter(p -> p.getCategory().equals(category))
                .count();

            Button categoryBtn = createCategoryButton(category, (int) count);
            categoriesFlow.getChildren().add(categoryBtn);
        }

        // Featured Products Section
        Label featuredTitle = new Label("⭐ Featured Products");
        featuredTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        FlowPane featuredFlow = new FlowPane(16, 16);
        featuredFlow.setAlignment(Pos.CENTER);
        featuredFlow.setMaxWidth(Double.MAX_VALUE);

        List<Product> featuredProducts = productService.listAll().stream()
            .sorted(Comparator.comparingInt(Product::getPopularity).reversed())
            .limit(4)
            .toList();

        for (Product p : featuredProducts) {
            VBox productCard = createCompactProductCard(p);
            featuredFlow.getChildren().add(productCard);
        }

        // Add all sections to main content
        mainContent.getChildren().addAll(
            statsTitle, statsGrid,
            new Separator(),
            actionsTitle, actionsGrid,
            new Separator(),
            categoriesTitle, categoriesFlow,
            new Separator(),
            featuredTitle, featuredFlow
        );

        homeContainer.getChildren().addAll(heroSection, mainContent);

        // Center the container in the canvas
        StackPane centerWrapper = new StackPane(homeContainer);
        centerWrapper.setAlignment(Pos.TOP_CENTER);

        ScrollPane homeScroll = new ScrollPane(centerWrapper);
        homeScroll.setFitToWidth(true);
        homeScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        homeScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        homeScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        canvasArea.getChildren().add(homeScroll);
    }

    // Helper method for modern stat cards
    private VBox createModernStatCard(String icon, String value, String label, String color, String subtitle) {
        VBox card = new VBox(8);
        card.getStyleClass().add("node-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(240);
        card.setPrefHeight(140);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-radius: 16; -fx-background-radius: 16; " +
                     "-fx-effect: dropshadow(gaussian, rgba(124, 77, 255, 0.15), 15, 0, 0, 5); " +
                     "-fx-border-color: " + color + "; -fx-border-width: 2;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 32px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label mainLabel = new Label(label);
        mainLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        Label subLabel = new Label(subtitle);
        subLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096;");
        subLabel.setWrapText(true);
        subLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(iconLabel, valueLabel, mainLabel, subLabel);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-scale-x: 1.02; -fx-scale-y: 1.02;", "")));

        return card;
    }

    // Helper method for action cards
    private VBox createActionCard(String icon, String title, String description, String color, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(220);
        card.setPrefHeight(160);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: linear-gradient(135deg, " + color + " 0%, " + adjustColorBrightness(color, -20) + " 100%); " +
                     "-fx-border-radius: 16; -fx-background-radius: 16; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 12, 0, 0, 4); -fx-cursor: hand;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 40px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255, 255, 255, 0.9);");
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(iconLabel, titleLabel, descLabel);

        // Click action
        card.setOnMouseClicked(e -> {
            if (action != null) {
                action.handle(new javafx.event.ActionEvent());
            }
        });

        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setScaleX(1.05);
            card.setScaleY(1.05);
        });
        card.setOnMouseExited(e -> {
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });

        return card;
    }

    // Helper method for category buttons
    private Button createCategoryButton(String category, int count) {
        Button btn = new Button(category + " (" + count + ")");
        btn.setStyle("-fx-background-color: linear-gradient(to bottom, #7c4dff, #6b46c1); " +
                    "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                    "-fx-padding: 12 24; -fx-border-radius: 20; -fx-background-radius: 20; " +
                    "-fx-cursor: hand;");
        btn.setOnAction(e -> {
            // Navigate to shop with category filter
            loadShopPage("", category, "", "", "Featured");
            switchPage("Shop");
        });

        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", "")));

        return btn;
    }

    // Helper method for compact product cards
    private VBox createCompactProductCard(Product p) {
        VBox card = new VBox(10);
        card.getStyleClass().add("product-card");
        card.setPrefWidth(200);
        card.setMaxWidth(200);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-cursor: hand;");

        // Product image
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
            iv.setImage(new Image(imgSrc, 180, 120, true, true));
        } catch (Exception ex) {
            // ignore
        }

        Label name = new Label(p.getName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #2d3748;");
        name.setWrapText(true);
        name.setMaxWidth(180);

        Label price = new Label(String.format("$%.2f", p.getPrice()));
        price.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        Button addBtn = new Button("Add to Cart");
        addBtn.getStyleClass().add("primary-button");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setDisable(p.getStock() <= 0);
        addBtn.setOnAction(e -> {
            String user = loggedInUser == null ? "guest" : loggedInUser;
            if (p.getStock() > 0) {
                cartService.addToCart(user, p, 1);
                refreshHeader();
                showToast("Added to cart: " + p.getName());
            }
        });

        card.getChildren().addAll(iv, name, price, addBtn);

        // Hover effect
        card.setOnMouseEntered(e -> card.setScaleX(1.03));
        card.setOnMouseExited(e -> card.setScaleX(1.0));

        return card;
    }

    // Utility method to adjust color brightness
    private String adjustColorBrightness(String hexColor, int percent) {
        // Simple color adjustment - in production, use proper color manipulation
        return hexColor; // For now, return same color
    }

    private void loadShopPage() {
        loadShopPage("", "All", "", "", "Featured");
    }

    private void loadShopPage(String query, String category, String minP, String maxP, String sortMode) {
        canvasArea.getChildren().clear();

        VBox container = new VBox(16);
        container.setPadding(new Insets(16));

        // Results header
        HBox resultsHeader = new HBox(12);
        resultsHeader.setAlignment(Pos.CENTER_LEFT);
        Label resultsTitle = new Label("Products");
        resultsTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        resultsHeader.getChildren().add(resultsTitle);

        // Build product list with filters
        List<Product> products = productService.listAll();
        int totalProducts = products.size();

        // search
        if (query != null && !query.trim().isEmpty()) {
            String q = query.trim().toLowerCase();
            products = products.stream().filter(p -> p.getName().toLowerCase().contains(q) || p.getCategory().toLowerCase().contains(q)).toList();
        }
        // category
        if (category != null && !category.equals("All")) {
            products = products.stream().filter(p -> p.getCategory().equals(category)).toList();
        }
        // price range
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

        Label resultCount = new Label(products.size() + " of " + totalProducts + " products");
        resultCount.getStyleClass().add("muted");
        resultsHeader.getChildren().add(resultCount);
        container.getChildren().add(resultsHeader);

        // Product grid using FlowPane for responsive layout
        FlowPane productGrid = new FlowPane(16, 16);
        productGrid.setPrefWrapLength(1000);

        for (Product p : products) {
            VBox productCard = new VBox(12);
            productCard.getStyleClass().add("product-card");
            productCard.setPrefWidth(280);
            productCard.setMaxWidth(280);
            productCard.setPadding(new Insets(16));

            // Product image
            StackPane imageContainer = new StackPane();
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
                iv.setImage(new Image(imgSrc, 250, 180, true, true));
                iv.setPreserveRatio(true);
            } catch (Exception ex) {
                // ignore image load errors
            }
            imageContainer.getChildren().add(iv);

            // Out of stock badge overlay
            if (p.getStock() <= 0) {
                Label outOfStock = new Label("OUT OF STOCK");
                outOfStock.getStyleClass().add("out-of-stock");
                outOfStock.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
                StackPane.setAlignment(outOfStock, Pos.TOP_RIGHT);
                StackPane.setMargin(outOfStock, new Insets(8));
                imageContainer.getChildren().add(outOfStock);
            }

            VBox infoBox = new VBox(6);
            Label name = new Label(p.getName());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
            name.setWrapText(true);

            Label priceLabel = new Label(String.format("$%.2f", p.getPrice()));
            priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

            Label catLabel = new Label(p.getCategory());
            catLabel.getStyleClass().add("pill");
            catLabel.setStyle("-fx-font-size: 10px;");

            HBox stockRatingRow = new HBox(8);
            stockRatingRow.setAlignment(Pos.CENTER_LEFT);
            Label stockLabel = new Label(p.getStock() > 0 ? ("Stock: " + p.getStock()) : "Out of stock");
            stockLabel.getStyleClass().add("muted");
            stockLabel.setStyle("-fx-font-size: 11px;");

            HBox ratingBox = new HBox(2);
            double avg = p.getAverageRating();
            int stars = (int)Math.round(avg);
            for (int i=0;i<5;i++) {
                Label star = new Label(i<stars?"★":"☆");
                star.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 12px;");
                ratingBox.getChildren().add(star);
            }
            Label ratingText = new Label(String.format("(%.1f)", avg));
            ratingText.getStyleClass().add("muted");
            ratingText.setStyle("-fx-font-size: 11px;");
            ratingBox.getChildren().add(ratingText);
            stockRatingRow.getChildren().addAll(stockLabel, new Region(), ratingBox);
            HBox.setHgrow(stockRatingRow.getChildren().get(1), Priority.ALWAYS);

            infoBox.getChildren().addAll(name, priceLabel, catLabel, stockRatingRow);

            // Action buttons
            HBox qtyRow = new HBox(8);
            qtyRow.setAlignment(Pos.CENTER_LEFT);
            Label qtyLabel = new Label("Qty:");
            qtyLabel.getStyleClass().add("muted");
            Spinner<Integer> qty = new Spinner<>(1, Math.max(1, p.getStock()), 1);
            qty.setPrefWidth(70);
            qty.setDisable(p.getStock() <= 0);
            qtyRow.getChildren().addAll(qtyLabel, qty);

            HBox btnRow = new HBox(8);
            btnRow.setAlignment(Pos.CENTER);

            Button add = new Button("Add to Cart");
            add.getStyleClass().add("primary-button");
            add.setMaxWidth(Double.MAX_VALUE);
            add.setDisable(p.getStock() <= 0);
            HBox.setHgrow(add, Priority.ALWAYS);
            add.setOnAction(e -> {
                String user = loggedInUser == null ? "guest" : loggedInUser;
                if (p.getStock() <= 0) {
                    showAlert("Out of Stock", "This product is currently out of stock.");
                    return;
                }
                int qtyVal = qty.getValue();
                if (qtyVal > p.getStock()) {
                    showAlert("Stock Limit", "Only " + p.getStock() + " units available.");
                    return;
                }
                cartService.addToCart(user, p, qtyVal);
                ActionLogger.log("AddToCart: " + user + " -> " + p.getId() + " x" + qtyVal);
                refreshHeader();
                showToast("Added to cart: " + p.getName());
            });

            Button fav = new Button("♡");
            fav.getStyleClass().add("secondary-button");
            fav.setTooltip(new Tooltip("Add to wishlist"));
            fav.setOnAction(e -> {
                String user = loggedInUser == null ? "guest" : loggedInUser;
                wishlistService.addToWishlist(user, p.getId());
                ActionLogger.log("Wishlist add: " + user + " -> " + p.getId());
                fav.setText("♥");
                fav.setStyle("-fx-text-fill: #ff5a8a;");
                showToast("Added to wishlist");
            });

            btnRow.getChildren().addAll(add, fav);

            Button review = new Button("Write Review");
            review.getStyleClass().add("secondary-button");
            review.setMaxWidth(Double.MAX_VALUE);
            review.setOnAction(e -> showReviewDialog(p));

            productCard.getChildren().addAll(imageContainer, infoBox, qtyRow, btnRow, review);
            productGrid.getChildren().add(productCard);
        }

        if (products.isEmpty()) {
            Label noResults = new Label("No products found matching your criteria.");
            noResults.getStyleClass().add("muted");
            noResults.setStyle("-fx-font-size: 16px;");
            container.getChildren().add(noResults);
        } else {
            container.getChildren().add(productGrid);
        }

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
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

    // Enhanced section with gradient border and better visibility
    private VBox createEnhancedSection(String title, String accentColor) {
        VBox section = new VBox(16);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; " +
                        "-fx-border-radius: 12; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: " + accentColor + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(124, 77, 255, 0.15), 10, 0, 0, 3);");

        HBox titleBox = new HBox(12);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(0, 0, 12, 0));
        titleBox.setStyle("-fx-border-color: " + accentColor + "; -fx-border-width: 0 0 2 0;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");
        titleBox.getChildren().add(titleLabel);

        section.getChildren().add(titleBox);
        return section;
    }


    private void loadProfilePage() {
        canvasArea.getChildren().clear();
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Enhanced Header with gradient background
        VBox headerBox = new VBox(8);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: linear-gradient(to right, #7c4dff, #9f7aea); " +
                          "-fx-background-radius: 12; " +
                          "-fx-effect: dropshadow(gaussian, rgba(124, 77, 255, 0.4), 15, 0, 0, 5);");

        Label title = new Label("👤 My Profile & Account");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Manage your personal information and account settings");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255, 255, 255, 0.9);");

        headerBox.getChildren().addAll(title, subtitle);

        // Create TabPane for profile sections
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(650);

        // Tab 1: Profile & Account Info
        Tab accountTab = new Tab("👤 Profile & Account");
        accountTab.setContent(createProfileAccountTab());

        // Tab 2: My Orders
        Tab ordersTab = new Tab("📦 My Orders");
        ordersTab.setContent(createMyOrdersTab());

        // Tab 3: Wishlist
        Tab wishlistTab = new Tab("♥ Wishlist");
        wishlistTab.setContent(createWishlistTab());

        tabPane.getTabs().addAll(accountTab, ordersTab, wishlistTab);

        root.getChildren().addAll(headerBox, tabPane);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        canvasArea.getChildren().add(sp);
    }

    private ScrollPane createMyOrdersTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));

        Label ordersTitle = new Label("📦 My Orders - Track Delivery");
        ordersTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        VBox ordersContainer = new VBox(12);
        ordersContainer.setPadding(new Insets(12));

        String user = loggedInUser == null ? "guest" : loggedInUser;
        List<Order> userOrders = orderTrackingService.getOrdersByUser(user);

        if (userOrders.isEmpty()) {
            Label noOrders = new Label("No orders yet. Start shopping to see your order tracking here!");
            noOrders.getStyleClass().add("muted");
            noOrders.setStyle("-fx-font-size: 14px;");
            ordersContainer.getChildren().add(noOrders);
        } else {
            for (Order order : userOrders) {
                VBox orderCard = createOrderTrackingCard(order);
                ordersContainer.getChildren().add(orderCard);
            }
        }

        // Legacy orders button
        Button viewLegacy = new Button("📄 View Legacy Order History");
        viewLegacy.getStyleClass().add("secondary-button");
        viewLegacy.setOnAction(e -> showOrderHistory());

        content.getChildren().addAll(ordersTitle, ordersContainer, viewLegacy);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private ScrollPane createWishlistTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));

        Label wishlistTitle = new Label("♥ My Wishlist");
        wishlistTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        VBox wishlistContainer = new VBox(12);
        String user = loggedInUser == null ? "guest" : loggedInUser;
        Set<String> items = wishlistService.getWishlist(user);

        if (items.isEmpty()) {
            Label noItems = new Label("Your wishlist is empty. Start adding products you love!");
            noItems.getStyleClass().add("muted");
            noItems.setStyle("-fx-font-size: 14px;");
            wishlistContainer.getChildren().add(noItems);
        } else {
            for (String pid : items) {
                productService.findById(pid).ifPresent(p -> {
                    HBox itemCard = new HBox(16);
                    itemCard.getStyleClass().add("product-card");
                    itemCard.setPadding(new Insets(12));
                    itemCard.setAlignment(Pos.CENTER_LEFT);

                    // Product image
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
                        iv.setImage(new Image(imgSrc, 80, 60, true, true));
                    } catch (Exception ex) {
                        // ignore
                    }

                    VBox info = new VBox(4);
                    Label nameLabel = new Label(p.getName());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                    Label priceLabel = new Label(String.format("$%.2f", p.getPrice()));
                    priceLabel.setStyle("-fx-text-fill: #7c4dff; -fx-font-weight: bold;");
                    Label stockLabel = new Label("Stock: " + p.getStock());
                    stockLabel.getStyleClass().add("muted");
                    info.getChildren().addAll(nameLabel, priceLabel, stockLabel);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button addToCart = new Button("🛒 Add to Cart");
                    addToCart.getStyleClass().add("primary-button");
                    addToCart.setDisable(p.getStock() <= 0);
                    addToCart.setOnAction(e -> {
                        if (p.getStock() > 0) {
                            cartService.addToCart(user, p, 1);
                            refreshHeader();
                            showToast("Added to cart: " + p.getName());
                        }
                    });

                    Button remove = new Button("🗑️");
                    remove.getStyleClass().add("secondary-button");
                    remove.setOnAction(e -> {
                        wishlistService.removeFromWishlist(user, pid);
                        loadProfilePage();
                        showToast("Removed from wishlist");
                    });

                    itemCard.getChildren().addAll(iv, info, spacer, addToCart, remove);
                    wishlistContainer.getChildren().add(itemCard);
                });
            }
        }

        content.getChildren().addAll(wishlistTitle, wishlistContainer);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private VBox createOrderTrackingCard(Order order) {
        VBox card = new VBox(12);
        card.getStyleClass().add("product-card");
        card.setPadding(new Insets(16));

        // Order Header
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label orderIdLabel = new Label("Order #" + order.getId());
        orderIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Label statusLabel = new Label(order.getStatus().getDisplayName());
        statusLabel.getStyleClass().add("pill");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        // Color code based on status
        switch (order.getStatus()) {
            case ORDER_PLACED -> statusLabel.setStyle(statusLabel.getStyle() + " -fx-background-color: rgba(124, 77, 255, 0.18); -fx-text-fill: #7c4dff;");
            case PROCESSING -> statusLabel.setStyle(statusLabel.getStyle() + " -fx-background-color: rgba(255, 193, 7, 0.18); -fx-text-fill: #ff9800;");
            case SHIPPED, IN_TRANSIT -> statusLabel.setStyle(statusLabel.getStyle() + " -fx-background-color: rgba(33, 150, 243, 0.18); -fx-text-fill: #2196f3;");
            case OUT_FOR_DELIVERY -> statusLabel.setStyle(statusLabel.getStyle() + " -fx-background-color: rgba(255, 152, 0, 0.18); -fx-text-fill: #ff9800;");
            case DELIVERED -> statusLabel.setStyle(statusLabel.getStyle() + " -fx-background-color: rgba(76, 175, 80, 0.18); -fx-text-fill: #4caf50;");
        }

        header.getChildren().addAll(orderIdLabel, spacer1, statusLabel);

        // Delivery Info
        VBox infoBox = new VBox(6);
        Label addressLabel = new Label("📍 " + order.getMaskedAddress());
        addressLabel.getStyleClass().add("muted");

        Label deliveryLabel = new Label("📅 Estimated Delivery: " +
            (order.getEstimatedDelivery() != null ?
                order.getEstimatedDelivery().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")) :
                "TBD"));
        deliveryLabel.getStyleClass().add("muted");

        if (order.getActualDelivery() != null) {
            Label actualLabel = new Label("✅ Delivered on: " +
                order.getActualDelivery().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            actualLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold;");
            infoBox.getChildren().add(actualLabel);
        }

        infoBox.getChildren().addAll(addressLabel, deliveryLabel);

        // Progress Tracker
        HBox progressTracker = createProgressTracker(order.getStatus());

        // Action Buttons
        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button viewDetails = new Button("View Details");
        viewDetails.getStyleClass().add("secondary-button");
        viewDetails.setOnAction(e -> showOrderDetails(order));

        // Admin: Advance status button
        if (loggedInUser != null && userService.isAdmin(loggedInUser) && order.getStatus() != Order.OrderStatus.DELIVERED) {
            Button advanceBtn = new Button("⏭️ Advance Status");
            advanceBtn.getStyleClass().add("primary-button");
            advanceBtn.setOnAction(e -> {
                orderTrackingService.advanceOrderStatus(order.getId());
                loadProfilePage(); // Refresh
                showToast("Order status updated!");
            });
            actions.getChildren().add(advanceBtn);
        }

        actions.getChildren().add(viewDetails);

        card.getChildren().addAll(header, infoBox, progressTracker, actions);

        return card;
    }

    private HBox createProgressTracker(Order.OrderStatus currentStatus) {
        HBox tracker = new HBox(8);
        tracker.setAlignment(Pos.CENTER_LEFT);
        tracker.setPadding(new Insets(12, 0, 12, 0));

        Order.OrderStatus[] statuses = Order.OrderStatus.values();
        int currentIndex = currentStatus.ordinal();

        for (int i = 0; i < statuses.length; i++) {
            VBox statusNode = new VBox(4);
            statusNode.setAlignment(Pos.CENTER);
            statusNode.setPrefWidth(100);

            // Status icon/circle
            Circle circle = new Circle(12);
            if (i <= currentIndex) {
                circle.setStyle("-fx-fill: #7c4dff;");
            } else {
                circle.setStyle("-fx-fill: #444; -fx-stroke: #777; -fx-stroke-width: 2;");
            }

            // Status label
            Label label = new Label(statuses[i].getDisplayName());
            label.setStyle("-fx-font-size: 10px; -fx-text-align: center;");
            label.setWrapText(true);
            label.setMaxWidth(90);
            if (i <= currentIndex) {
                label.setStyle(label.getStyle() + " -fx-font-weight: bold; -fx-text-fill: #7c4dff;");
            } else {
                label.getStyleClass().add("muted");
            }

            statusNode.getChildren().addAll(circle, label);
            tracker.getChildren().add(statusNode);

            // Connector line
            if (i < statuses.length - 1) {
                Region line = new Region();
                line.setPrefHeight(2);
                line.setPrefWidth(20);
                if (i < currentIndex) {
                    line.setStyle("-fx-background-color: #7c4dff;");
                } else {
                    line.setStyle("-fx-background-color: #444;");
                }
                HBox.setMargin(line, new Insets(12, 0, 0, 0));
                tracker.getChildren().add(line);
            }
        }

        return tracker;
    }

    private void showOrderDetails(Order order) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Order Details - " + order.getId());

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.getStyleClass().add("form-container");

        Label title = new Label("Order Details");
        title.getStyleClass().add("form-title");

        VBox details = new VBox(8);
        details.getChildren().addAll(
                new Label("Order ID: " + order.getId()),
                new Label("Status: " + order.getStatus().getDisplayName()),
                new Label(""),
                new Label("Delivery Address:"),
                new Label("  " + (order.getShippingAddress() != null ? order.getShippingAddress() : "N/A")),
                new Label("  " + (order.getCity() != null ? order.getCity() : "")),
                new Label("  " + (order.getPostalCode() != null ? order.getPostalCode() : "")),
                new Label("  " + (order.getCountry() != null ? order.getCountry() : "")),
                new Label(""),
                new Label("Estimated Delivery: " +
                    (order.getEstimatedDelivery() != null ?
                        order.getEstimatedDelivery().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")) :
                        "TBD"))
        );

        if (order.getActualDelivery() != null) {
            Label delivered = new Label("✅ Delivered on: " +
                order.getActualDelivery().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            delivered.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold; -fx-font-size: 14px;");
            details.getChildren().add(delivered);
        }

        Button close = new Button("Close");
        close.getStyleClass().add("secondary-button");
        close.setMaxWidth(Double.MAX_VALUE);
        close.setOnAction(e -> dlg.close());

        root.getChildren().addAll(title, details, close);

        Scene s = new Scene(root, 450, 450);
        dlg.setScene(s);
        dlg.showAndWait();
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


    private VBox createStatCard(String label, String value, String color) {
        VBox card = new VBox(8);
        card.getStyleClass().add("node-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(180);
        card.setPadding(new Insets(20));

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label titleLabel = new Label(label);
        titleLabel.setStyle("-fx-text-fill: #718096; -fx-font-size: 13px;");
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    private void showEditProductDialog(Product product) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Edit Product - " + product.getName());

        VBox root = new VBox(14);
        root.setPadding(new Insets(20));

        Label title = new Label("✏️ Edit Product");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);

        TextField nameField = new TextField(product.getName());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Electronics", "Clothing", "Home & Garden", "Books", "Sports", "Toys");
        categoryBox.setValue(product.getCategory());
        TextField stockField = new TextField(String.valueOf(product.getStock()));
        TextField imageField = new TextField(product.getImageFilename() != null ? product.getImageFilename() : "");
        imageField.setEditable(false); // Image is final and cannot be changed
        imageField.setStyle("-fx-background-color: #f0f0f0;");

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Price:"), 0, 1);
        form.add(priceField, 1, 1);
        form.add(new Label("Category:"), 0, 2);
        form.add(categoryBox, 1, 2);
        form.add(new Label("Stock:"), 0, 3);
        form.add(stockField, 1, 3);
        form.add(new Label("Image (read-only):"), 0, 4);
        form.add(imageField, 1, 4);

        Label msg = new Label();
        msg.setStyle("-fx-font-weight: bold;");

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                String category = categoryBox.getValue();
                int stock = Integer.parseInt(stockField.getText().trim());

                if (name.isEmpty()) {
                    msg.setText("❌ Name is required");
                    msg.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                    return;
                }

                product.setName(name);
                product.setPrice(price);
                product.setCategory(category);
                product.setStock(stock);
                // Note: Image filename is final and cannot be changed after product creation

                productService.persist();
                ActionLogger.log("Product updated: " + product.getId());
                showToast("Product updated: " + name);
                dlg.close();
                loadWarehousePage();
            } catch (NumberFormatException ex) {
                msg.setText("❌ Invalid price or stock format");
                msg.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("secondary-button");
        cancelBtn.setOnAction(e -> dlg.close());

        buttons.getChildren().addAll(saveBtn, cancelBtn);

        root.getChildren().addAll(title, form, msg, buttons);

        Scene s = new Scene(root, 400, 400);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    private void loadSettingsPage() {
        canvasArea.getChildren().clear();
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Enhanced Header with gradient background
        VBox headerBox = new VBox(8);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: linear-gradient(to right, #7c4dff, #9f7aea); " +
                          "-fx-background-radius: 12; " +
                          "-fx-effect: dropshadow(gaussian, rgba(124, 77, 255, 0.4), 15, 0, 0, 5);");

        Label title = new Label("⚙️ Settings & Preferences");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Manage security, permissions, sessions, and system preferences");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255, 255, 255, 0.9);");

        headerBox.getChildren().addAll(title, subtitle);

        // Create TabPane with enhanced styling
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(650);
        tabPane.getStyleClass().add("settings-tab-pane");

        // Tab 1: Security & Privacy
        Tab securityTab = new Tab("🔒 Security & Privacy");
        securityTab.setContent(createSecurityPrivacyTab());

        // Tab 2: Roles & Access Control
        Tab rolesTab = new Tab("👥 Roles & Permissions");
        rolesTab.setContent(createRolesAccessTab());

        // Tab 3: Session Management
        Tab sessionsTab = new Tab("💻 Active Sessions");
        sessionsTab.setContent(createSessionManagementTab());

        // Tab 4: Activity & Audit
        Tab activityTab = new Tab("📊 Activity & Audit");
        activityTab.setContent(createActivityAuditTab());

        tabPane.getTabs().addAll(securityTab, rolesTab, sessionsTab, activityTab);

        root.getChildren().addAll(headerBox, tabPane);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        canvasArea.getChildren().add(sp);
    }

    // Tab 1: Profile & Account (Reorganized)
    private ScrollPane createProfileAccountTab() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(24));

        // Section 1: Profile Information
        VBox profileInfoSection = createEnhancedSection("👤 Profile Information", "#7c4dff");

        HBox profileRow = new HBox(24);
        profileRow.setAlignment(Pos.CENTER_LEFT);

        // Profile picture with larger preview
        VBox picColumn = new VBox(12);
        picColumn.setAlignment(Pos.CENTER);

        Circle profilePic = new Circle(60);
        profilePic.setStyle("-fx-fill: linear-gradient(to bottom right, #7c4dff, #a07cff); " +
                           "-fx-stroke: white; -fx-stroke-width: 3;");
        Label initials = new Label(loggedInUser != null ? loggedInUser.substring(0, Math.min(2, loggedInUser.length())).toUpperCase() : "G");
        initials.setStyle("-fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold;");
        StackPane picContainer = new StackPane(profilePic, initials);

        HBox picButtons = new HBox(8);
        picButtons.setAlignment(Pos.CENTER);
        Button uploadPic = new Button("📁 Upload");
        uploadPic.getStyleClass().add("action-button-primary");
        uploadPic.setStyle("-fx-font-size: 12px; -fx-padding: 8 16;");
        uploadPic.setOnAction(e -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.setTitle("Select Profile Picture");
            chooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            if (chooser.showOpenDialog(null) != null) {
                showToast("✅ Profile picture updated!");
            }
        });

        Button removePic = new Button("🗑️");
        removePic.getStyleClass().add("action-button-secondary");
        removePic.setStyle("-fx-font-size: 12px; -fx-padding: 8 12;");
        removePic.setOnAction(e -> showToast("Picture removed"));
        picButtons.getChildren().addAll(uploadPic, removePic);

        picColumn.getChildren().addAll(picContainer, picButtons);

        // Profile details
        VBox detailsColumn = new VBox(16);
        HBox.setHgrow(detailsColumn, Priority.ALWAYS);

        // Username
        VBox usernameBox = new VBox(6);
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748; -fx-font-size: 13px;");
        Label currentUsername = new Label(loggedInUser != null ? loggedInUser : "Guest User");
        currentUsername.setStyle("-fx-font-size: 16px; -fx-text-fill: #7c4dff; -fx-font-weight: bold;");
        usernameBox.getChildren().addAll(usernameLabel, currentUsername);

        // Email
        VBox emailBox = new VBox(6);
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748; -fx-font-size: 13px;");
        Label currentEmail = new Label((loggedInUser != null ? loggedInUser : "guest") + "@shoppingcenter.com");
        currentEmail.setStyle("-fx-font-size: 14px; -fx-text-fill: #718096;");
        emailBox.getChildren().addAll(emailLabel, currentEmail);

        // Member since
        VBox memberBox = new VBox(6);
        Label memberLabel = new Label("Member Since");
        memberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748; -fx-font-size: 13px;");
        Label memberDate = new Label("January 2026");
        memberDate.setStyle("-fx-font-size: 14px; -fx-text-fill: #718096;");
        memberBox.getChildren().addAll(memberLabel, memberDate);

        detailsColumn.getChildren().addAll(usernameBox, emailBox, memberBox);
        profileRow.getChildren().addAll(picColumn, detailsColumn);
        profileInfoSection.getChildren().add(profileRow);

        // Section 2: Edit Profile
        VBox editSection = createEnhancedSection("✏️ Edit Profile Details", "#6b46c1");

        GridPane editGrid = new GridPane();
        editGrid.setHgap(16);
        editGrid.setVgap(14);

        // Row 1: Username
        Label newUsernameLabel = new Label("New Username:");
        newUsernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField newUsername = new TextField();
        newUsername.setPromptText("Enter new username");
        newUsername.setPrefWidth(280);

        // Row 2: Display Name
        Label displayNameLabel = new Label("Display Name:");
        displayNameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField displayName = new TextField();
        displayName.setPromptText("Your display name");
        displayName.setPrefWidth(280);

        // Row 3: Email
        Label newEmailLabel = new Label("New Email:");
        newEmailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField newEmail = new TextField();
        newEmail.setPromptText("your.email@example.com");
        newEmail.setPrefWidth(280);

        editGrid.add(newUsernameLabel, 0, 0);
        editGrid.add(newUsername, 1, 0);
        editGrid.add(displayNameLabel, 0, 1);
        editGrid.add(displayName, 1, 1);
        editGrid.add(newEmailLabel, 0, 2);
        editGrid.add(newEmail, 1, 2);

        HBox editButtons = new HBox(12);
        Button saveChanges = new Button("✅ Save All Changes");
        saveChanges.getStyleClass().add("primary-button");
        saveChanges.setOnAction(e -> {
            if (loggedInUser == null) {
                showAlert("Login Required", "Please login to update profile");
                return;
            }
            String name = newUsername.getText().trim();
            if (!name.isEmpty()) {
                loggedInUser = name;
                refreshHeader();
                showToast("✅ Profile updated successfully!");
                loadSettingsPage();
            }
        });

        Button cancel = new Button("↶ Reset");
        cancel.getStyleClass().add("secondary-button");
        cancel.setOnAction(e -> {
            newUsername.clear();
            displayName.clear();
            newEmail.clear();
        });

        editButtons.getChildren().addAll(saveChanges, cancel);
        editSection.getChildren().addAll(editGrid, editButtons);

        content.getChildren().addAll(profileInfoSection, editSection);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    // Tab 2: Security & Privacy (Reorganized)
    private ScrollPane createSecurityPrivacyTab() {
        // Just return the security tab content
        return createSecurityTab();
    }

    // Tab 3: Roles & Access Control
    private ScrollPane createRolesAccessTab() {
        return createRolePermissionsTab();
    }

    // Tab 4: Session Management
    private ScrollPane createSessionManagementTab() {
        return createSessionsTab();
    }

    // Tab 5: Activity & Audit
    private ScrollPane createActivityAuditTab() {
        return createLoginHistoryTab();
    }

    // Account Settings Tab
    private ScrollPane createAccountSettingsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Profile Picture Section
        VBox profileSection = createSectionBox("📸 Profile Picture");

        HBox profilePicRow = new HBox(20);
        profilePicRow.setAlignment(Pos.CENTER_LEFT);

        // Profile picture preview
        Circle profilePic = new Circle(50);
        profilePic.setStyle("-fx-fill: linear-gradient(to bottom right, #7c4dff, #a07cff);");
        Label initials = new Label(loggedInUser != null ? loggedInUser.substring(0, Math.min(2, loggedInUser.length())).toUpperCase() : "G");
        initials.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        StackPane picContainer = new StackPane(profilePic, initials);

        VBox picActions = new VBox(8);
        Button uploadPic = new Button("📁 Upload Picture");
        uploadPic.getStyleClass().add("action-button-primary");
        uploadPic.setOnAction(e -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.setTitle("Select Profile Picture");
            chooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            java.io.File file = chooser.showOpenDialog(null);
            if (file != null) {
                showToast("Profile picture updated!");
            }
        });

        Button removePic = new Button("🗑️ Remove");
        removePic.getStyleClass().add("action-button-secondary");
        removePic.setOnAction(e -> showToast("Profile picture removed"));

        picActions.getChildren().addAll(uploadPic, removePic);
        profilePicRow.getChildren().addAll(picContainer, picActions);
        profileSection.getChildren().add(profilePicRow);

        // Username Section
        VBox usernameSection = createSectionBox("👤 Username / Display Name");

        Label currentUsername = new Label("Current Username: " + (loggedInUser != null ? loggedInUser : "Guest"));
        currentUsername.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");

        TextField newUsername = new TextField();
        newUsername.setPromptText("Enter new username");
        newUsername.getStyleClass().add("search-input");
        newUsername.setPrefWidth(300);

        TextField displayName = new TextField();
        displayName.setPromptText("Enter display name");
        displayName.getStyleClass().add("search-input");
        displayName.setPrefWidth(300);

        Button updateUsername = new Button("✅ Update Username");
        updateUsername.getStyleClass().add("action-button-primary");
        updateUsername.setOnAction(e -> {
            if (loggedInUser == null) {
                showAlert("Not Logged In", "Please login to update username");
                return;
            }
            String newName = newUsername.getText().trim();
            if (newName.isEmpty()) {
                showAlert("Invalid", "Username cannot be empty");
                return;
            }
            loggedInUser = newName;
            refreshHeader();
            showToast("Username updated to: " + newName);
            loadSettingsPage();
        });

        usernameSection.getChildren().addAll(currentUsername, new Label("New Username:"), newUsername,
                                              new Label("Display Name:"), displayName, updateUsername);

        // Email Section
        VBox emailSection = createSectionBox("📧 Email Address");

        Label currentEmail = new Label("Current Email: " + (loggedInUser != null ? loggedInUser + "@example.com" : "Not set"));
        currentEmail.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");

        TextField newEmail = new TextField();
        newEmail.setPromptText("Enter new email address");
        newEmail.getStyleClass().add("search-input");
        newEmail.setPrefWidth(300);

        Button updateEmail = new Button("✅ Update Email");
        updateEmail.getStyleClass().add("action-button-primary");
        updateEmail.setOnAction(e -> {
            if (loggedInUser == null) {
                showAlert("Not Logged In", "Please login to update email");
                return;
            }
            String email = newEmail.getText().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                showAlert("Invalid", "Please enter a valid email address");
                return;
            }
            showToast("Email updated successfully!");
            loadSettingsPage();
        });

        emailSection.getChildren().addAll(currentEmail, new Label("New Email:"), newEmail, updateEmail);

        content.getChildren().addAll(profileSection, usernameSection, emailSection);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    // Security Tab
    private ScrollPane createSecurityTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Change Password Section with Strength Indicator
        VBox passwordSection = createSectionBox("🔑 Change Password");

        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Current password");
        oldPassword.getStyleClass().add("search-input");
        oldPassword.setPrefWidth(300);

        // New password with show/hide toggle
        HBox newPasswordRow = new HBox(8);
        newPasswordRow.setAlignment(Pos.CENTER_LEFT);

        TextField newPasswordVisible = new TextField();
        newPasswordVisible.setPromptText("New password");
        newPasswordVisible.getStyleClass().add("search-input");
        newPasswordVisible.setPrefWidth(300);
        newPasswordVisible.setVisible(false);
        newPasswordVisible.setManaged(false);

        PasswordField newPasswordHidden = new PasswordField();
        newPasswordHidden.setPromptText("New password");
        newPasswordHidden.getStyleClass().add("search-input");
        newPasswordHidden.setPrefWidth(300);

        // Bind text properties
        newPasswordVisible.textProperty().bindBidirectional(newPasswordHidden.textProperty());

        Button togglePassword = new Button("👁️ Show");
        togglePassword.getStyleClass().add("action-button-secondary");
        togglePassword.setOnAction(e -> {
            boolean isVisible = newPasswordVisible.isVisible();
            newPasswordVisible.setVisible(!isVisible);
            newPasswordVisible.setManaged(!isVisible);
            newPasswordHidden.setVisible(isVisible);
            newPasswordHidden.setManaged(isVisible);
            togglePassword.setText(isVisible ? "👁️ Show" : "🙈 Hide");
        });

        newPasswordRow.getChildren().addAll(newPasswordHidden, newPasswordVisible, togglePassword);

        // Password strength indicator
        ProgressBar strengthBar = new ProgressBar(0);
        strengthBar.setPrefWidth(300);
        Label strengthLabel = new Label("Password Strength: Weak");
        strengthLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #ff5a8a;");

        newPasswordHidden.textProperty().addListener((obs, old, newVal) -> {
            double strength = calculatePasswordStrength(newVal);
            strengthBar.setProgress(strength);
            if (strength < 0.3) {
                strengthLabel.setText("Password Strength: Weak");
                strengthLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #ff5a8a;");
                strengthBar.setStyle("-fx-accent: #ff5a8a;");
            } else if (strength < 0.7) {
                strengthLabel.setText("Password Strength: Medium");
                strengthLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #ff9800;");
                strengthBar.setStyle("-fx-accent: #ff9800;");
            } else {
                strengthLabel.setText("Password Strength: Strong");
                strengthLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4caf50;");
                strengthBar.setStyle("-fx-accent: #4caf50;");
            }
        });

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm new password");
        confirmPassword.getStyleClass().add("search-input");
        confirmPassword.setPrefWidth(300);

        Button changePassword = new Button("✅ Update Password");
        changePassword.getStyleClass().add("action-button-primary");
        changePassword.setOnAction(e -> {
            if (loggedInUser == null) {
                showAlert("Not Logged In", "Please login to change password");
                return;
            }
            if (newPasswordHidden.getText().isEmpty()) {
                showAlert("Invalid", "Password cannot be empty");
                return;
            }
            if (!newPasswordHidden.getText().equals(confirmPassword.getText())) {
                showAlert("Mismatch", "Passwords do not match");
                return;
            }
            if (calculatePasswordStrength(newPasswordHidden.getText()) < 0.3) {
                showAlert("Weak Password", "Please use a stronger password");
                return;
            }
            showToast("Password updated successfully!");
            oldPassword.clear();
            newPasswordHidden.clear();
            confirmPassword.clear();
        });

        passwordSection.getChildren().addAll(
            new Label("Current Password:"), oldPassword,
            new Label("New Password:"), newPasswordRow,
            strengthBar, strengthLabel,
            new Label("Confirm Password:"), confirmPassword,
            changePassword
        );

        // Two-Factor Authentication Section
        VBox twoFactorSection = createSectionBox("🔐 Two-Factor Authentication (2FA)");

        Label twoFactorStatus = new Label("Status: Disabled");
        twoFactorStatus.setStyle("-fx-font-size: 13px; -fx-text-fill: #ff9800; -fx-font-weight: bold;");

        Label twoFactorDesc = new Label("Add an extra layer of security to your account");
        twoFactorDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #718096;");

        Button enable2FA = new Button("✅ Enable 2FA");
        enable2FA.getStyleClass().add("action-button-primary");
        enable2FA.setOnAction(e -> {
            if (loggedInUser == null) {
                showAlert("Not Logged In", "Please login to enable 2FA");
                return;
            }
            show2FASetupDialog();
        });

        twoFactorSection.getChildren().addAll(twoFactorStatus, twoFactorDesc, enable2FA);

        // Security Alerts Section
        VBox alertsSection = createSectionBox("⚠️ Security Alerts");

        VBox alertsList = new VBox(8);

        // Sample security alert
        HBox alert1 = new HBox(12);
        alert1.setStyle("-fx-background-color: rgba(255, 152, 0, 0.1); -fx-padding: 12; -fx-border-radius: 8; -fx-background-radius: 8;");
        alert1.setAlignment(Pos.CENTER_LEFT);
        Label alertIcon = new Label("⚠️");
        alertIcon.setStyle("-fx-font-size: 20px;");
        VBox alertText = new VBox(4);
        Label alertTitle = new Label("Suspicious login attempt detected");
        alertTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #ff9800;");
        Label alertTime = new Label("2 hours ago from unknown device in New York");
        alertTime.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096;");
        alertText.getChildren().addAll(alertTitle, alertTime);
        alert1.getChildren().addAll(alertIcon, alertText);

        alertsList.getChildren().add(alert1);
        alertsSection.getChildren().add(alertsList);

        content.getChildren().addAll(passwordSection, twoFactorSection, alertsSection);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    // Role & Permissions Tab
    private ScrollPane createRolePermissionsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Current Role Section
        VBox currentRoleSection = createSectionBox("👤 Current Role");

        String currentRole = loggedInUser == null ? "Guest" : (userService.isAdmin(loggedInUser) ? "Admin" : "Customer");
        Label roleLabel = new Label("Your Role: " + currentRole);
        roleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        Label roleDesc = new Label(getRoleDescription(currentRole));
        roleDesc.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");
        roleDesc.setWrapText(true);

        currentRoleSection.getChildren().addAll(roleLabel, roleDesc);

        // Role Descriptions
        VBox rolesSection = createSectionBox("📋 Role Descriptions");

        // Admin role card
        VBox adminCard = createRoleCard("👑 Admin",
            "Full access to all features including warehouse management, user management, and system settings",
            "#7c4dff");

        // Customer role card
        VBox customerCard = createRoleCard("🛍️ Customer",
            "Can browse products, manage cart, place orders, and view order history",
            "#4caf50");

        // Guest role card
        VBox guestCard = createRoleCard("👤 Guest",
            "Can browse products and add to cart. Limited features until login",
            "#718096");

        rolesSection.getChildren().addAll(adminCard, customerCard, guestCard);

        // Permission Matrix
        VBox permissionsSection = createSectionBox("🔒 Permission Matrix");

        GridPane permissionGrid = new GridPane();
        permissionGrid.setHgap(15);
        permissionGrid.setVgap(10);

        // Headers
        Label permHeader = new Label("Permission");
        permHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        Label adminHeader = new Label("Admin");
        adminHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #7c4dff;");
        Label customerHeader = new Label("Customer");
        customerHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #4caf50;");
        Label guestHeader = new Label("Guest");
        guestHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #718096;");

        permissionGrid.add(permHeader, 0, 0);
        permissionGrid.add(adminHeader, 1, 0);
        permissionGrid.add(customerHeader, 2, 0);
        permissionGrid.add(guestHeader, 3, 0);

        // Permissions
        String[][] permissions = {
            {"View Products", "✅", "✅", "✅"},
            {"Add to Cart", "✅", "✅", "✅"},
            {"Place Orders", "✅", "✅", "❌"},
            {"View Order History", "✅", "✅", "❌"},
            {"Manage Products", "✅", "❌", "❌"},
            {"Warehouse Access", "✅", "❌", "❌"},
            {"User Management", "✅", "❌", "❌"},
            {"System Settings", "✅", "❌", "❌"}
        };

        for (int i = 0; i < permissions.length; i++) {
            for (int j = 0; j < permissions[i].length; j++) {
                Label cell = new Label(permissions[i][j]);
                if (j == 0) {
                    cell.setStyle("-fx-text-fill: #2d3748;");
                } else {
                    cell.setStyle("-fx-font-size: 16px;");
                }
                permissionGrid.add(cell, j, i + 1);
            }
        }

        permissionsSection.getChildren().add(permissionGrid);

        // Switch Role Section (Admin only or demo)
        VBox switchRoleSection = createSectionBox("🔄 Switch Role (Demo Only)");

        Label switchWarning = new Label("⚠️ In production, only admins can switch roles");
        switchWarning.setStyle("-fx-font-size: 12px; -fx-text-fill: #ff9800;");

        ComboBox<String> roleSelector = new ComboBox<>();
        roleSelector.getItems().addAll("Guest", "Customer", "Admin");
        roleSelector.setValue(currentRole);
        roleSelector.setPrefWidth(200);

        Button switchRole = new Button("🔄 Switch Role");
        switchRole.getStyleClass().add("action-button-primary");
        switchRole.setOnAction(e -> showRoleSwitchConfirmation(roleSelector.getValue()));

        switchRoleSection.getChildren().addAll(switchWarning, new Label("Select Role:"), roleSelector, switchRole);

        content.getChildren().addAll(currentRoleSection, rolesSection, permissionsSection, switchRoleSection);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    // Active Sessions Tab
    private ScrollPane createSessionsTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        VBox sessionsSection = createSectionBox("💻 Active Sessions");

        Label desc = new Label("Manage devices where you're currently logged in");
        desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");

        VBox sessionsList = new VBox(12);

        // Current session
        HBox currentSession = createSessionCard(
            "💻 Windows PC - Chrome",
            "Current Session",
            "New York, USA",
            "Last active: Now",
            true
        );

        // Other sessions
        HBox session2 = createSessionCard(
            "📱 iPhone 13",
            "192.168.1.100",
            "Los Angeles, USA",
            "Last active: 2 hours ago",
            false
        );

        HBox session3 = createSessionCard(
            "🖥️ MacBook Pro",
            "192.168.1.105",
            "San Francisco, USA",
            "Last active: 1 day ago",
            false
        );

        sessionsList.getChildren().addAll(currentSession, session2, session3);

        Button logoutAll = new Button("🚪 Log Out All Other Devices");
        logoutAll.getStyleClass().add("action-button-danger");
        logoutAll.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "This will log you out from all other devices. Continue?",
                ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Logout");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    showToast("All other sessions terminated");
                    loadSettingsPage();
                }
            });
        });

        sessionsSection.getChildren().addAll(desc, sessionsList, logoutAll);
        content.getChildren().add(sessionsSection);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    // Login History Tab
    private ScrollPane createLoginHistoryTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        VBox historySection = createSectionBox("📊 Login History");

        Label desc = new Label("Recent login activity on your account");
        desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096;");

        VBox historyList = new VBox(10);

        // Sample login history entries
        String[][] history = {
            {"✅", "Successful login", "Windows PC - Chrome", "192.168.1.1", "New York, USA", "2 hours ago"},
            {"✅", "Successful login", "iPhone 13", "192.168.1.100", "Los Angeles, USA", "1 day ago"},
            {"❌", "Failed login attempt", "Unknown device", "203.0.113.0", "Beijing, China", "2 days ago"},
            {"✅", "Successful login", "MacBook Pro", "192.168.1.105", "San Francisco, USA", "3 days ago"},
            {"✅", "Successful login", "Windows PC - Firefox", "192.168.1.1", "New York, USA", "5 days ago"}
        };

        for (String[] entry : history) {
            HBox historyCard = createHistoryCard(entry[0], entry[1], entry[2], entry[3], entry[4], entry[5]);
            historyList.getChildren().add(historyCard);
        }

        historySection.getChildren().addAll(desc, historyList);
        content.getChildren().add(historySection);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    // Helper Methods
    private VBox createSectionBox(String title) {
        VBox section = new VBox(12);
        section.setPadding(new Insets(16));
        section.setStyle("-fx-background-color: rgba(124, 77, 255, 0.05); -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: rgba(124, 77, 255, 0.2); -fx-border-width: 1;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");
        section.getChildren().add(titleLabel);

        return section;
    }

    private VBox createRoleCard(String title, String description, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-color: " + color + "; -fx-border-width: 2;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #718096;");
        descLabel.setWrapText(true);

        card.getChildren().addAll(titleLabel, descLabel);
        return card;
    }

    private HBox createSessionCard(String device, String ip, String location, String lastActive, boolean isCurrent) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-color: rgba(124, 77, 255, 0.2); -fx-border-width: 1;");

        VBox info = new VBox(4);
        Label deviceLabel = new Label(device);
        deviceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        Label ipLabel = new Label(ip);
        ipLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096;");
        Label locationLabel = new Label(location);
        locationLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096;");
        Label timeLabel = new Label(lastActive);
        timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096;");
        info.getChildren().addAll(deviceLabel, ipLabel, locationLabel, timeLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox actions = new VBox(4);
        if (isCurrent) {
            Label currentBadge = new Label("CURRENT");
            currentBadge.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-padding: 4 8; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");
            actions.getChildren().add(currentBadge);
        } else {
            Button logout = new Button("🚪 Log Out");
            logout.getStyleClass().add("action-button-danger");
            logout.setStyle("-fx-font-size: 11px; -fx-padding: 6 12;");
            logout.setOnAction(e -> {
                showToast("Session terminated");
                loadSettingsPage();
            });
            actions.getChildren().add(logout);
        }

        card.getChildren().addAll(info, spacer, actions);
        return card;
    }

    private HBox createHistoryCard(String icon, String action, String device, String ip, String location, String time) {
        HBox card = new HBox(12);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: " + (icon.equals("✅") ? "rgba(76, 175, 80, 0.05)" : "rgba(244, 67, 54, 0.05)") + "; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label statusIcon = new Label(icon);
        statusIcon.setStyle("-fx-font-size: 20px;");

        VBox info = new VBox(3);
        Label actionLabel = new Label(action);
        actionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        Label deviceLabel = new Label(device + " • " + ip);
        deviceLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096;");
        Label locationLabel = new Label(location + " • " + time);
        locationLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #718096;");
        info.getChildren().addAll(actionLabel, deviceLabel, locationLabel);

        card.getChildren().addAll(statusIcon, info);
        return card;
    }

    private double calculatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) return 0;

        double strength = 0;
        if (password.length() >= 8) strength += 0.25;
        if (password.length() >= 12) strength += 0.15;
        if (password.matches(".*[a-z].*")) strength += 0.15;
        if (password.matches(".*[A-Z].*")) strength += 0.15;
        if (password.matches(".*[0-9].*")) strength += 0.15;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength += 0.15;

        return Math.min(strength, 1.0);
    }

    private String getRoleDescription(String role) {
        return switch (role) {
            case "Admin" -> "You have full access to all system features including warehouse management, user administration, and system configuration.";
            case "Customer" -> "You can browse products, manage your cart, place orders, view order history, and manage your account.";
            case "Guest" -> "You can browse products and add items to cart. Please log in to access full features.";
            default -> "Unknown role";
        };
    }

    private void show2FASetupDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Enable Two-Factor Authentication");
        dialog.setHeaderText("Scan QR Code with Authenticator App");
        dialog.setContentText("1. Download Google Authenticator or similar app\n2. Scan this QR code\n3. Enter the 6-digit code to verify\n\n[QR Code would appear here]\n\nBackup codes: XXXX-XXXX-XXXX");
        dialog.showAndWait();
        showToast("2FA setup in progress...");
    }

    private void showRoleSwitchConfirmation(String newRole) {
        if (loggedInUser == null && !newRole.equals("Guest")) {
            showAlert("Login Required", "Please login first to switch to " + newRole + " role");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Role Switch");
        confirm.setHeaderText("Switch to " + newRole + " role?");
        confirm.setContentText(getRoleDescription(newRole) + "\n\nThis action will be logged in the audit trail.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String oldRole = loggedInUser == null ? "Guest" : (userService.isAdmin(loggedInUser) ? "Admin" : "Customer");

                // Switch role
                switch (newRole) {
                    case "Guest" -> loggedInUser = null;
                    case "Admin" -> loggedInUser = "admin";
                    case "Customer" -> loggedInUser = "customer";
                }

                // Log audit trail
                ActionLogger.log("ROLE_CHANGE: " + oldRole + " → " + newRole + " by " + (loggedInUser != null ? loggedInUser : "guest"));

                refreshHeader();
                showToast("Role switched to: " + newRole);
                loadSettingsPage();
            }
        });
    }


    private void loadWarehousePage() {
        canvasArea.getChildren().clear();

        // Check admin access
        if (loggedInUser == null || !userService.isAdmin(loggedInUser)) {
            VBox denied = new VBox(20);
            denied.setAlignment(Pos.CENTER);
            denied.setPadding(new Insets(50));
            Label accessDenied = new Label("⛔ Access Denied - Admin Only");
            accessDenied.setStyle("-fx-font-size: 24px; -fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
            Label desc = new Label("Warehouse management is only available to administrators.");
            desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #718096;");
            denied.getChildren().addAll(accessDenied, desc);
            canvasArea.getChildren().add(denied);
            return;
        }

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Header
        VBox headerBox = new VBox(8);
        headerBox.getStyleClass().add("warehouse-header");
        Label warehouseTitle = new Label("📦 Warehouse Management");
        warehouseTitle.getStyleClass().add("warehouse-title");
        Label subtitle = new Label("Manage inventory, products, and stock levels");
        subtitle.getStyleClass().add("warehouse-subtitle");
        headerBox.getChildren().addAll(warehouseTitle, subtitle);

        // Stats Dashboard
        HBox statsBox = new HBox(16);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        VBox totalProductsStat = createStatCard("📊 Total Products", String.valueOf(productService.count()), "#7c4dff");
        VBox lowStockStat = createStatCard("⚠️ Low Stock", String.valueOf(productService.listAll().stream().filter(p -> p.getStock() < 20).count()), "#ff9800");
        VBox outOfStockStat = createStatCard("❌ Out of Stock", String.valueOf(productService.listAll().stream().filter(p -> p.getStock() == 0).count()), "#ff5a8a");

        statsBox.getChildren().addAll(totalProductsStat, lowStockStat, outOfStockStat);

        // Product Management Section
        Label prodMgmtTitle = new Label("📝 Product Management");
        prodMgmtTitle.getStyleClass().add("section-header");

        // Product List
        VBox productListBox = new VBox(10);
        productListBox.getStyleClass().add("inventory-list");

        for (Product p : productService.listAll()) {
            HBox productRow = createProductRow(p);
            productListBox.getChildren().add(productRow);
        }

        ScrollPane productScroll = new ScrollPane(productListBox);
        productScroll.setFitToWidth(true);
        productScroll.setPrefHeight(300);
        productScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Add New Product Section
        Label addProductTitle = new Label("➕ Add New Product");
        addProductTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        VBox addProductBox = createAddProductForm();

        root.getChildren().addAll(headerBox, statsBox, new Separator(),
                                   prodMgmtTitle, productScroll, new Separator(),
                                   addProductTitle, addProductBox);

        ScrollPane mainScroll = new ScrollPane(root);
        mainScroll.setFitToWidth(true);
        mainScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        canvasArea.getChildren().add(mainScroll);
    }

    private HBox createProductRow(Product p) {
        HBox productRow = new HBox(12);
        productRow.getStyleClass().add("inventory-row");
        productRow.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label(p.getId());
        idLabel.setPrefWidth(80);
        idLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #7c4dff;");

        Label nameLabel = new Label(p.getName());
        nameLabel.setPrefWidth(200);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");

        Label priceLabel = new Label(String.format("$%.2f", p.getPrice()));
        priceLabel.setPrefWidth(80);
        priceLabel.setStyle("-fx-text-fill: #5ce6b8; -fx-font-weight: bold;");

        Label stockLabel = new Label("Stock: " + p.getStock());
        stockLabel.setPrefWidth(100);
        stockLabel.getStyleClass().add("stock-badge");
        if (p.getStock() == 0) {
            stockLabel.getStyleClass().add("stock-out");
        } else if (p.getStock() < 20) {
            stockLabel.getStyleClass().add("stock-low");
        } else {
            stockLabel.getStyleClass().add("stock-high");
        }

        Label categoryLabel = new Label(p.getCategory());
        categoryLabel.setPrefWidth(120);
        categoryLabel.setStyle("-fx-text-fill: #718096;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("✏️ Edit");
        editBtn.getStyleClass().add("action-button-secondary");
        editBtn.setOnAction(e -> showEditProductDialog(p));

        Button removeBtn = new Button("🗑️ Remove");
        removeBtn.getStyleClass().add("action-button-danger");
        removeBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to remove '" + p.getName() + "'?",
                ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Removal");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    productService.remove(p.getId());
                    productService.persist();
                    ActionLogger.log("Product removed: " + p.getId());
                    showToast("Product removed: " + p.getName());
                    loadWarehousePage();
                }
            });
        });

        productRow.getChildren().addAll(idLabel, nameLabel, priceLabel, stockLabel, categoryLabel, spacer, editBtn, removeBtn);
        return productRow;
    }

    private VBox createAddProductForm() {
        VBox addProductBox = new VBox(12);
        addProductBox.setPadding(new Insets(16));
        addProductBox.setStyle("-fx-background-color: rgba(92, 230, 184, 0.08); -fx-border-radius: 10; -fx-background-radius: 10;");

        GridPane addForm = new GridPane();
        addForm.setHgap(12);
        addForm.setVgap(10);

        Label idLbl = new Label("Product ID:");
        idLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField newId = new TextField();
        newId.setPromptText("p031");

        Label nameLbl = new Label("Product Name:");
        nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField newName = new TextField();
        newName.setPromptText("Product Name");

        Label priceLbl = new Label("Price:");
        priceLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField newPrice = new TextField();
        newPrice.setPromptText("29.99");

        Label categoryLbl = new Label("Category:");
        categoryLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        ComboBox<String> newCategory = new ComboBox<>();
        newCategory.getItems().addAll("Electronics", "Clothing", "Home & Garden", "Books", "Sports", "Toys");
        newCategory.getSelectionModel().selectFirst();

        Label stockLbl = new Label("Stock:");
        stockLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField newStock = new TextField();
        newStock.setPromptText("50");

        Label imageLbl = new Label("Image:");
        imageLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d3748;");
        TextField newImage = new TextField();
        newImage.setPromptText("0001.jpg");

        Button chooseImage = new Button("📁 Choose");
        chooseImage.setOnAction(evt -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.setTitle("Select product image");
            chooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            java.io.File selected = chooser.showOpenDialog(null);
            if (selected != null) {
                try {
                    java.nio.file.Path imagesDir = StorageLocation.imageFolderPath;
                    java.nio.file.Files.createDirectories(imagesDir);
                    java.nio.file.Path dest = imagesDir.resolve(selected.getName());
                    java.nio.file.Files.copy(selected.toPath(), dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    newImage.setText(selected.getName());
                } catch (Exception ex) {
                    showToast("Error copying image: " + ex.getMessage());
                }
            }
        });

        addForm.add(idLbl, 0, 0);
        addForm.add(newId, 1, 0);
        addForm.add(nameLbl, 0, 1);
        addForm.add(newName, 1, 1);
        addForm.add(priceLbl, 0, 2);
        addForm.add(newPrice, 1, 2);
        addForm.add(categoryLbl, 2, 0);
        addForm.add(newCategory, 3, 0);
        addForm.add(stockLbl, 2, 1);
        addForm.add(newStock, 3, 1);
        addForm.add(imageLbl, 2, 2);
        addForm.add(newImage, 3, 2);
        addForm.add(chooseImage, 4, 2);

        Label addMsg = new Label();
        addMsg.setStyle("-fx-font-weight: bold;");

        Button addProductBtn = new Button("✅ Add Product");
        addProductBtn.getStyleClass().add("action-button-primary");
        addProductBtn.setOnAction(e -> {
            String id = newId.getText().trim();
            String name = newName.getText().trim();
            String priceStr = newPrice.getText().trim();
            String category = newCategory.getValue();
            String stockStr = newStock.getText().trim();
            String image = newImage.getText().trim();

            if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                addMsg.setText("❌ All fields are required");
                addMsg.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                return;
            }

            if (productService.findById(id).isPresent()) {
                addMsg.setText("❌ Product ID already exists");
                addMsg.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);

                if (price < 0 || stock < 0) {
                    addMsg.setText("❌ Price and stock must be positive");
                    addMsg.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
                    return;
                }

                Product newProduct = new Product(id, name, price, image.isEmpty() ? null : image);
                newProduct.setCategory(category);
                newProduct.setStock(stock);
                productService.add(newProduct);
                productService.persist();
                ActionLogger.log("Product added: " + id);

                addMsg.setText("✅ Product added successfully!");
                addMsg.setStyle("-fx-text-fill: #5ce6b8; -fx-font-weight: bold;");
                showToast("Product added: " + name);

                newId.clear();
                newName.clear();
                newPrice.clear();
                newStock.clear();
                newImage.clear();

                loadWarehousePage();
            } catch (NumberFormatException ex) {
                addMsg.setText("❌ Invalid price or stock format");
                addMsg.setStyle("-fx-text-fill: #ff5a8a; -fx-font-weight: bold;");
            }
        });

        addProductBox.getChildren().addAll(addForm, addMsg, addProductBtn);
        return addProductBox;
    }

    private void showNotificationCenter() {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Notification Center");

        VBox root = new VBox(12);
        root.setPadding(new Insets(16));

        Label title = new Label("🔔 Notifications");
        title.getStyleClass().add("form-title");

        TextArea notifArea = new TextArea();
        notifArea.setEditable(false);
        notifArea.setPrefRowCount(10);
        notifArea.setText(ci553.shoppingcenter.service.NotificationService.readAll());

        Button close = new Button("Close");
        close.getStyleClass().add("secondary-button");
        close.setMaxWidth(Double.MAX_VALUE);
        close.setOnAction(e -> dlg.close());

        root.getChildren().addAll(title, notifArea, close);

        Scene s = new Scene(root, 500, 400);
        dlg.setScene(s);
        dlg.showAndWait();
    }

    // Simple transient toast-like notification
    private void showToast(String message) {
        Stage t = new Stage();
        t.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        t.initOwner(canvasArea.getScene() == null ? null : canvasArea.getScene().getWindow());
        t.setAlwaysOnTop(true);

        Label lab = new Label(message);
        lab.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        lab.setPadding(new Insets(16, 20, 16, 20));
        lab.setWrapText(true);
        lab.setMaxWidth(350);

        StackPane p = new StackPane(lab);
        p.setStyle("-fx-background-color: rgba(124, 77, 255, 0.95); -fx-background-radius: 10; " +
                   "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 20, 0, 0, 5);");

        Scene sc = new Scene(p);
        sc.setFill(null);
        t.setScene(sc);
        t.setWidth(380);
        t.setHeight(80);

        // Position: bottom-right of main window with better offset
        if (canvasArea.getScene() != null && canvasArea.getScene().getWindow() != null) {
            javafx.stage.Window owner = canvasArea.getScene().getWindow();
            t.setX(owner.getX() + owner.getWidth() - 400);
            t.setY(owner.getY() + owner.getHeight() - 120);
        }

        t.show();

        // Fade in/out animation
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), p);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Auto close with fade out
        new Thread(() -> {
            try { Thread.sleep(2500); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> {
                javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), p);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> t.close());
                fadeOut.play();
            });
        }).start();
    }

    // Helper method for showing alert dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

