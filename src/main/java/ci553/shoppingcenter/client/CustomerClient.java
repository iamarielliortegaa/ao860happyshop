package ci553.shoppingcenter.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Minimal CustomerClient for the modernized shopping center (self-contained)
 */
public class CustomerClient extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Client (Modern)");
        primaryStage.setScene(new Scene(new Label("Customer Client - Modernized"), 400, 200));
        primaryStage.show();
    }
}
