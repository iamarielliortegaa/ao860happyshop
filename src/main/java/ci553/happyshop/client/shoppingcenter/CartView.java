package ci553.happyshop.client.shoppingcenter;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.ProductListFormatter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Cart View - Shopping cart management
 */
public class CartView {
    
    private TextArea cartArea;
    private Label totalLabel;
    private ArrayList<Product> cartItems = new ArrayList<>();
    
    public void start(Stage window) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        VBox cartContainer = createCartView();
        
        StackPane contentArea = new StackPane(cartContainer);
        contentArea.setPadding(new Insets(30));
        
        root.setCenter(contentArea);
        
        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        window.setScene(scene);
        window.setTitle("🛒 Shopping Cart");
        window.show();
    }
    
    private VBox createCartView() {
        VBox container = new VBox(20);
        container.setMaxWidth(600);
        container.setAlignment(Pos.TOP_CENTER);
        
        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label icon = new Label("🛒");
        icon.setStyle("-fx-font-size: 32px;");
        
        Label title = new Label("Shopping Cart");
        title.getStyleClass().add("form-title");
        
        header.getChildren().addAll(icon, title);
        
        // Cart items area
        VBox cartSection = new VBox(10);
        
        Label itemsLabel = new Label("Cart Items");
        itemsLabel.getStyleClass().add("form-label");
        
        cartArea = new TextArea();
        cartArea.setEditable(false);
        cartArea.setPrefHeight(300);
        cartArea.getStyleClass().add("text-area");
        cartArea.setText("Your cart is empty.\n\nAdd products to get started!");
        
        cartSection.getChildren().addAll(itemsLabel, cartArea);
        
        // Total section
        HBox totalSection = new HBox(10);
        totalSection.setAlignment(Pos.CENTER_RIGHT);
        totalSection.setPadding(new Insets(10));
        totalSection.setStyle("-fx-background-color: rgba(124, 77, 255, 0.1); -fx-background-radius: 10;");
        
        Label totalText = new Label("Total:");
        totalText.getStyleClass().add("form-label");
        totalText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        totalLabel = new Label("£0.00");
        totalLabel.getStyleClass().add("form-title");
        totalLabel.setStyle("-fx-text-fill: #5ce6b8;");
        
        totalSection.getChildren().addAll(totalText, totalLabel);
        
        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button clearButton = new Button("Clear Cart");
        clearButton.getStyleClass().add("secondary-button");
        clearButton.setOnAction(e -> clearCart());
        
        Button checkoutButton = new Button("Proceed to Checkout");
        checkoutButton.getStyleClass().add("primary-button");
        checkoutButton.setOnAction(e -> checkout());
        
        buttons.getChildren().addAll(clearButton, checkoutButton);
        
        container.getChildren().addAll(header, cartSection, totalSection, buttons);
        
        return container;
    }
    
    public void addProduct(Product product) {
        cartItems.add(product);
        updateCartDisplay();
    }
    
    private void updateCartDisplay() {
        if (cartItems.isEmpty()) {
            cartArea.setText("Your cart is empty.\n\nAdd products to get started!");
            totalLabel.setText("£0.00");
        } else {
            String cartContent = ProductListFormatter.buildString(cartItems);
            cartArea.setText(cartContent);
            
            double total = cartItems.stream()
                .mapToDouble(p -> p.getUnitPrice() * p.getOrderedQuantity())
                .sum();
            totalLabel.setText(String.format("£%.2f", total));
        }
    }
    
    private void clearCart() {
        cartItems.clear();
        updateCartDisplay();
    }
    
    private void checkout() {
        if (cartItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cart Empty");
            alert.setHeaderText("Your cart is empty");
            alert.setContentText("Please add items before checking out.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checkout");
            alert.setHeaderText("Proceeding to checkout");
            alert.setContentText("Total: " + totalLabel.getText());
            alert.showAndWait();
        }
    }
}