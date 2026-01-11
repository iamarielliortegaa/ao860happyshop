package ci553.shoppingcenter.client.picker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PickerClient extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Picker Client (Modern)");
        primaryStage.setScene(new Scene(new Label("Picker Client - Modernized"), 400, 200));
        primaryStage.show();
    }
}
