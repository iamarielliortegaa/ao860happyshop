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
 * Sendings View - Track order delivery status
 */
public class SendingsView {
    
    public void start(Stage window) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        VBox trackingContainer = createTrackingView();
        
        ScrollPane scrollPane = new ScrollPane(trackingContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        StackPane contentArea = new StackPane(scrollPane);
        contentArea.setPadding(new Insets(30));
        
        root.setCenter(contentArea);
        
        Scene scene = new Scene(root, 700, 700);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        window.setScene(scene);
        window.setTitle("🚚 Order Tracking");
        window.show();
    }
    
    private VBox createTrackingView() {
        VBox container = new VBox(25);
        container.setMaxWidth(600);
        container.setAlignment(Pos.TOP_CENTER);
        
        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label icon = new Label("🚚");
        icon.setStyle("-fx-font-size: 32px;");
        
        VBox titleBox = new VBox(5);
        Label title = new Label("Order Tracking");
        title.getStyleClass().add("form-title");
        
        Label subtitle = new Label("Track your delivery status");
        subtitle.getStyleClass().add("form-label");
        
        titleBox.getChildren().addAll(title, subtitle);
        
        header.getChildren().addAll(icon, titleBox);
        
        // Order info card
        VBox orderCard = new VBox(15);
        orderCard.getStyleClass().add("node-card");
        
        Label orderTitle = new Label("Order #12345");
        orderTitle.getStyleClass().add("node-title");
        
        HBox orderDetails = new HBox(30);
        
        VBox dateBox = new VBox(5);
        Label dateLabel = new Label("Order Date");
        dateLabel.getStyleClass().add("form-label");
        Label dateValue = new Label("Jan 9, 2026");
        dateValue.setStyle("-fx-text-fill: #eae6f7; -fx-font-size: 14px;");
        dateBox.getChildren().addAll(dateLabel, dateValue);
        
        VBox statusBox = new VBox(5);
        Label statusLabel = new Label("Status");
        statusLabel.getStyleClass().add("form-label");
        
        HBox statusValue = new HBox(8);
        statusValue.setAlignment(Pos.CENTER_LEFT);
        Circle statusDot = new Circle(4);
        statusDot.setFill(Color.web("#5ce6b8"));
        Label statusText = new Label("In Transit");
        statusText.setStyle("-fx-text-fill: #5ce6b8; -fx-font-size: 14px; -fx-font-weight: bold;");
        statusValue.getChildren().addAll(statusDot, statusText);
        
        statusBox.getChildren().addAll(statusLabel, statusValue);
        
        orderDetails.getChildren().addAll(dateBox, statusBox);
        
        orderCard.getChildren().addAll(orderTitle, orderDetails);
        
        // Tracking timeline
        VBox timeline = new VBox(0);
        
        timeline.getChildren().addAll(
            createTrackingStep("Order Placed", "Jan 9, 2026 10:30 AM", true, false),
            createTrackingStep("Processing", "Jan 9, 2026 11:15 AM", true, false),
            createTrackingStep("Shipped", "Jan 9, 2026 14:20 PM", true, false),
            createTrackingStep("In Transit", "Jan 9, 2026 16:45 PM", true, true),
            createTrackingStep("Out for Delivery", "Expected Jan 10, 2026", false, false),
            createTrackingStep("Delivered", "Expected Jan 10, 2026", false, true)
        );
        
        // Action buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        
        Button contactButton = new Button("Contact Support");
        contactButton.getStyleClass().add("secondary-button");
        
        Button detailsButton = new Button("View Order Details");
        detailsButton.getStyleClass().add("primary-button");
        
        buttons.getChildren().addAll(contactButton, detailsButton);
        
        container.getChildren().addAll(header, orderCard, timeline, buttons);
        
        return container;
    }
    
    private VBox createTrackingStep(String title, String time, boolean completed, boolean isLast) {
        VBox step = new VBox(5);
        step.setPadding(new Insets(0, 0, 20, 0));
        
        HBox stepRow = new HBox(15);
        stepRow.setAlignment(Pos.TOP_LEFT);
        
        // Timeline indicator
        VBox indicator = new VBox(5);
        indicator.setAlignment(Pos.TOP_CENTER);
        
        Circle dot = new Circle(8);
        if (completed) {
            dot.setFill(Color.web("#5ce6b8"));
            dot.setStroke(Color.web("#5ce6b8"));
        } else {
            dot.setFill(Color.TRANSPARENT);
            dot.setStroke(Color.web("#bfb6dd"));
        }
        dot.setStrokeWidth(2);
        
        Region line = new Region();
        line.setPrefWidth(2);
        line.setPrefHeight(40);
        if (!isLast) {
            if (completed) {
                line.setStyle("-fx-background-color: #5ce6b8;");
            } else {
                line.setStyle("-fx-background-color: #bfb6dd;");
            }
        }
        
        indicator.getChildren().addAll(dot, line);
        
        // Step content
        VBox content = new VBox(5);
        
        Label stepTitle = new Label(title);
        if (completed) {
            stepTitle.setStyle("-fx-text-fill: #eae6f7; -fx-font-size: 16px; -fx-font-weight: bold;");
        } else {
            stepTitle.setStyle("-fx-text-fill: #bfb6dd; -fx-font-size: 16px;");
        }
        
        Label stepTime = new Label(time);
        stepTime.getStyleClass().add("form-label");
        stepTime.setStyle("-fx-font-size: 13px;");
        
        content.getChildren().addAll(stepTitle, stepTime);
        
        stepRow.getChildren().addAll(indicator, content);
        
        step.getChildren().add(stepRow);
        
        return step;
    }
}