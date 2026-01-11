package ci553.shoppingcenter.client.warehouse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WarehouseClient extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Warehouse Client (Modern)");
        primaryStage.setScene(new Scene(new Label("Warehouse Client - Modernized"), 400, 200));
        primaryStage.show();
    }
}
