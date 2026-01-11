package ci553.shoppingcenter.client.ordertracker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class OrderTrackerClient extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Order Tracker (Modern)");
        primaryStage.setScene(new Scene(new Label("Order Tracker - Modernized"), 400, 200));
        primaryStage.show();
    }
}
