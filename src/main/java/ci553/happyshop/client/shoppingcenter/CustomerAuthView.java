package ci553.happyshop.client.shoppingcenter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Customer Authentication View - handles registration, login, and password recovery
 */
public class CustomerAuthView {
    
    private VBox loginForm;
    private VBox registerForm;
    private VBox forgotPasswordForm;
    private StackPane contentArea;
    
    public void start(Stage window) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(50));
        
        createForms();
        showLogin();
        
        root.setCenter(contentArea);
        
        Scene scene = new Scene(root, 500, 600);
        scene.getStylesheets().add(getClass().getResource("/shopping-center-styles.css").toExternalForm());
        
        window.setScene(scene);
        window.setTitle("Customer Authentication");
        window.show();
    }
    
    private void createForms() {
        loginForm = createLoginForm();
        registerForm = createRegisterForm();
        forgotPasswordForm = createForgotPasswordForm();
    }
    
    private VBox createLoginForm() {
        VBox form = new VBox(20);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(400);
        form.setAlignment(Pos.CENTER);
        
        Label title = new Label("Customer Login");
        title.getStyleClass().add("form-title");
        
        VBox emailGroup = new VBox(8);
        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("form-label");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("form-field");
        emailGroup.getChildren().addAll(emailLabel, emailField);
        
        VBox passwordGroup = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("form-field");
        passwordGroup.getChildren().addAll(passwordLabel, passwordField);
        
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        
        HBox links = new HBox(20);
        links.setAlignment(Pos.CENTER);
        Hyperlink registerLink = new Hyperlink("Create account");
        registerLink.setOnAction(e -> showRegister());
        Hyperlink forgotLink = new Hyperlink("Forgot password?");
        forgotLink.setOnAction(e -> showForgotPassword());
        links.getChildren().addAll(registerLink, forgotLink);
        
        form.getChildren().addAll(title, emailGroup, passwordGroup, loginButton, links);
        
        return form;
    }
    
    private VBox createRegisterForm() {
        VBox form = new VBox(20);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(400);
        form.setAlignment(Pos.CENTER);
        
        Label title = new Label("Create Account");
        title.getStyleClass().add("form-title");
        
        VBox nameGroup = new VBox(8);
        Label nameLabel = new Label("Full Name");
        nameLabel.getStyleClass().add("form-label");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.getStyleClass().add("form-field");
        nameGroup.getChildren().addAll(nameLabel, nameField);
        
        VBox emailGroup = new VBox(8);
        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("form-label");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("form-field");
        emailGroup.getChildren().addAll(emailLabel, emailField);
        
        VBox passwordGroup = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");
        passwordField.getStyleClass().add("form-field");
        passwordGroup.getChildren().addAll(passwordLabel, passwordField);
        
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("primary-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        
        Hyperlink backLink = new Hyperlink("Back to login");
        backLink.setOnAction(e -> showLogin());
        
        form.getChildren().addAll(title, nameGroup, emailGroup, passwordGroup, registerButton, backLink);
        
        return form;
    }
    
    private VBox createForgotPasswordForm() {
        VBox form = new VBox(20);
        form.getStyleClass().add("form-container");
        form.setMaxWidth(400);
        form.setAlignment(Pos.CENTER);
        
        Label title = new Label("Reset Password");
        title.getStyleClass().add("form-title");
        
        Label instruction = new Label("Enter your email to receive a password reset link");
        instruction.getStyleClass().add("form-label");
        instruction.setWrapText(true);
        
        VBox emailGroup = new VBox(8);
        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("form-label");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.getStyleClass().add("form-field");
        emailGroup.getChildren().addAll(emailLabel, emailField);
        
        Button resetButton = new Button("Send Reset Link");
        resetButton.getStyleClass().add("primary-button");
        resetButton.setMaxWidth(Double.MAX_VALUE);
        
        Hyperlink backLink = new Hyperlink("Back to login");
        backLink.setOnAction(e -> showLogin());
        
        form.getChildren().addAll(title, instruction, emailGroup, resetButton, backLink);
        
        return form;
    }
    
    private void showLogin() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(loginForm);
    }
    
    private void showRegister() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(registerForm);
    }
    
    private void showForgotPassword() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(forgotPasswordForm);
    }
}