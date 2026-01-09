package ci553.happyshop.client.shoppingcenter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Shipping View - Manage shipping and delivery options
 */
public class ShippingView {
    
    private TextField addressField;
    private TextField cityField;
    private TextField postalCodeField;
    private ComboBox<String> deliveryMethodCombo;
    private TextArea notesArea;
    
    public void start(Stage window) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        VBox shippingForm = createShippingForm();
        
        ScrollPane scrollPane = new ScrollPane(shippingForm);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        StackPane contentArea = new StackPane(scrollPane);
        contentArea.setPadding(new Insets(30));
        
        root.setCenter(contentArea);
        
        Scene scene = new Scene(root, 700, 700);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        window.setScene(scene);
        window.setTitle("📦 Shipping Details");
        window.show();
    }
    
    private VBox createShippingForm() {
        VBox form = new VBox(20);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(600);
        form.setAlignment(Pos.TOP_CENTER);
        
        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label icon = new Label("📦");
        icon.setStyle("-fx-font-size: 32px;");
        
        Label title = new Label("Shipping Information");
        title.getStyleClass().add("form-title");
        
        header.getChildren().addAll(icon, title);
        
        // Address
        VBox addressGroup = new VBox(8);
        Label addressLabel = new Label("Delivery Address");
        addressLabel.getStyleClass().add("form-label");
        addressField = new TextField();
        addressField.setPromptText("Enter street address");
        addressField.getStyleClass().add("form-field");
        addressGroup.getChildren().addAll(addressLabel, addressField);
        
        // City and Postal Code
        HBox locationRow = new HBox(15);
        
        VBox cityGroup = new VBox(8);
        Label cityLabel = new Label("City");
        cityLabel.getStyleClass().add("form-label");
        cityField = new TextField();
        cityField.setPromptText("City");
        cityField.getStyleClass().add("form-field");
        HBox.setHgrow(cityField, Priority.ALWAYS);
        cityGroup.getChildren().addAll(cityLabel, cityField);
        
        VBox postalGroup = new VBox(8);
        Label postalLabel = new Label("Postal Code");
        postalLabel.getStyleClass().add("form-label");
        postalCodeField = new TextField();
        postalCodeField.setPromptText("Postal code");
        postalCodeField.getStyleClass().add("form-field");
        postalGroup.getChildren().addAll(postalLabel, postalCodeField);
        
        locationRow.getChildren().addAll(cityGroup, postalGroup);
        HBox.setHgrow(cityGroup, Priority.ALWAYS);
        
        // Delivery Method
        VBox deliveryGroup = new VBox(8);
        Label deliveryLabel = new Label("Delivery Method");
        deliveryLabel.getStyleClass().add("form-label");
        deliveryMethodCombo = new ComboBox<>();
        deliveryMethodCombo.getItems().addAll(
            "Standard Delivery (3-5 days) - Free",
            "Express Delivery (1-2 days) - £5.99",
            "Next Day Delivery - £9.99",
            "Click & Collect - Free"
        );
        deliveryMethodCombo.setValue("Standard Delivery (3-5 days) - Free");
        deliveryMethodCombo.getStyleClass().add("form-field");
        deliveryMethodCombo.setMaxWidth(Double.MAX_VALUE);
        deliveryGroup.getChildren().addAll(deliveryLabel, deliveryMethodCombo);
        
        // Special Instructions
        VBox notesGroup = new VBox(8);
        Label notesLabel = new Label("Special Instructions (Optional)");
        notesLabel.getStyleClass().add("form-label");
        notesArea = new TextArea();
        notesArea.setPromptText("Any special delivery instructions...");
        notesArea.setPrefRowCount(3);
        notesArea.getStyleClass().add("text-area");
        notesGroup.getChildren().addAll(notesLabel, notesArea);
        
        // Status indicator
        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        statusBox.setPadding(new Insets(10));
        statusBox.setStyle("-fx-background-color: rgba(92, 230, 184, 0.1); -fx-background-radius: 10;");
        
        Label statusIcon = new Label("✓");
        statusIcon.setStyle("-fx-text-fill: #5ce6b8; -fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label statusText = new Label("Ready for delivery");
        statusText.getStyleClass().add("form-label");
        statusText.setStyle("-fx-text-fill: #5ce6b8;");
        
        statusBox.getChildren().addAll(statusIcon, statusText);
        
        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("secondary-button");
        
        Button confirmButton = new Button("Confirm Shipping");
        confirmButton.getStyleClass().add("primary-button");
        confirmButton.setOnAction(e -> confirmShipping());
        
        buttons.getChildren().addAll(cancelButton, confirmButton);
        
        form.getChildren().addAll(header, addressGroup, locationRow, deliveryGroup, 
                                  notesGroup, statusBox, buttons);
        
        return form;
    }
    
    private void confirmShipping() {
        if (addressField.getText().isEmpty() || cityField.getText().isEmpty() || 
            postalCodeField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Information");
            alert.setHeaderText("Please fill in all required fields");
            alert.setContentText("Address, city, and postal code are required.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Shipping Confirmed");
            alert.setHeaderText("Your shipping details have been saved");
            alert.setContentText("Delivery method: " + deliveryMethodCombo.getValue());
            alert.showAndWait();
        }
    }
}