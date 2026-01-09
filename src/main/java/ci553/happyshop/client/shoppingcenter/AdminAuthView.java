package ci553.happyshop.client.shoppingcenter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Admin Authentication View - handles admin login
 */
public class AdminAuthView {
    
    public void start(Stage window) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        VBox form = createAdminLoginForm();
        
        StackPane contentArea = new StackPane(form);
        contentArea.setPadding(new Insets(50));
        
        root.setCenter(contentArea);
        
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        window.setScene(scene);
        window.setTitle("Admin Authentication");
        window.show();
    }
    
    private VBox createAdminLoginForm() {
        VBox form = new VBox(20);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(400);
        form.setAlignment(Pos.CENTER);
        
        Label title = new Label("Admin Login");
        title.getStyleClass().add("form-title");
        
        Label subtitle = new Label("🔐 Administrator Access Only");
        subtitle.getStyleClass().add("form-label");
        
        VBox usernameGroup = new VBox(8);
        Label usernameLabel = new Label("Admin Username");
        usernameLabel.getStyleClass().add("form-label");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter admin username");
        usernameField.getStyleClass().add("form-field");
        usernameGroup.getChildren().addAll(usernameLabel, usernameField);
        
        VBox passwordGroup = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter admin password");
        passwordField.getStyleClass().add("form-field");
        passwordGroup.getChildren().addAll(passwordLabel, passwordField);
        
        Button loginButton = new Button("Admin Login");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        
        form.getChildren().addAll(title, subtitle, usernameGroup, passwordGroup, loginButton);
        
        return form;
    }
}