package ci553.happyshop.client.shoppingcenter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * CustomerAuthView - Handles customer authentication
 * Provides Registration, Login, and Forgot Password functionality
 */
public class CustomerAuthView extends VBox {
    
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField emailField;
    private String currentMode = "login"; // login, register, forgot
    
    public CustomerAuthView() {
        setSpacing(20);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        
        showLoginForm();
    }
    
    private void showLoginForm() {
        currentMode = "login";
        getChildren().clear();
        
        VBox form = new VBox(16);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(400);
        form.setAlignment(Pos.CENTER);
        
        Label title = new Label("Customer Login");
        title.getStyleClass().add("form-title");
        
        // Username field
        VBox usernameBox = new VBox(8);
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("form-label");
        usernameField = new TextField();
        usernameField.getStyleClass().add("form-field");
        usernameField.setPromptText("Enter your username");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        
        // Password field
        VBox passwordBox = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("form-field");
        passwordField.setPromptText("Enter your password");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        
        // Buttons
        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("primary-button");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> handleLogin());
        
        HBox linkBox = new HBox(10);
        linkBox.setAlignment(Pos.CENTER);
        
        Hyperlink registerLink = new Hyperlink("Register");
        registerLink.setStyle("-fx-text-fill: #7c4dff;");
        registerLink.setOnAction(e -> showRegisterForm());
        
        Hyperlink forgotLink = new Hyperlink("Forgot Password?");
        forgotLink.setStyle("-fx-text-fill: #7c4dff;");
        forgotLink.setOnAction(e -> showForgotPasswordForm());
        
        linkBox.getChildren().addAll(registerLink, new Label("|"), forgotLink);
        
        form.getChildren().addAll(title, usernameBox, passwordBox, loginBtn, linkBox);
        
        getChildren().add(form);
    }
    
    private void showRegisterForm() {
        currentMode = "register";
        getChildren().clear();
        
        VBox form = new VBox(16);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(400);
        form.setAlignment(Pos.CENTER);
        
        Label title = new Label("Customer Registration");
        title.getStyleClass().add("form-title");
        
        // Username field
        VBox usernameBox = new VBox(8);
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("form-label");
        usernameField = new TextField();
        usernameField.getStyleClass().add("form-field");
        usernameField.setPromptText("Choose a username");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        
        // Email field
        VBox emailBox = new VBox(8);
        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("form-label");
        emailField = new TextField();
        emailField.getStyleClass().add("form-field");
        emailField.setPromptText("Enter your email");
        emailBox.getChildren().addAll(emailLabel, emailField);
        
        // Password field
        VBox passwordBox = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("form-field");
        passwordField.setPromptText("Choose a password");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        
        // Buttons
        Button registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("primary-button");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setOnAction(e -> handleRegister());
        
        Button backBtn = new Button("Back to Login");
        backBtn.getStyleClass().add("secondary-button");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> showLoginForm());
        
        form.getChildren().addAll(title, usernameBox, emailBox, passwordBox, registerBtn, backBtn);
        
        getChildren().add(form);
    }
    
    private void showForgotPasswordForm() {
        currentMode = "forgot";
        getChildren().clear();
        
        VBox form = new VBox(16);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(400);
        form.setAlignment(Pos.CENTER);
        
        Label title = new Label("Forgot Password");
        title.getStyleClass().add("form-title");
        
        Label instructions = new Label("Enter your email to reset your password");
        instructions.setStyle("-fx-text-fill: #bfb6dd;");
        
        // Email field
        VBox emailBox = new VBox(8);
        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("form-label");
        emailField = new TextField();
        emailField.getStyleClass().add("form-field");
        emailField.setPromptText("Enter your email");
        emailBox.getChildren().addAll(emailLabel, emailField);
        
        // Buttons
        Button resetBtn = new Button("Reset Password");
        resetBtn.getStyleClass().add("primary-button");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> handleForgotPassword());
        
        Button backBtn = new Button("Back to Login");
        backBtn.getStyleClass().add("secondary-button");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> showLoginForm());
        
        form.getChildren().addAll(title, instructions, emailBox, resetBtn, backBtn);
        
        getChildren().add(form);
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }
        
        // TODO: Integrate with existing authentication system
        showAlert("Success", "Login successful for user: " + username);
    }
    
    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }
        
        // TODO: Integrate with database to create new user
        showAlert("Success", "Registration successful! You can now login.");
        showLoginForm();
    }
    
    private void handleForgotPassword() {
        String email = emailField.getText();
        
        if (email.isEmpty()) {
            showAlert("Error", "Please enter your email");
            return;
        }
        
        // TODO: Implement password reset logic
        showAlert("Success", "Password reset link sent to: " + email);
        showLoginForm();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
