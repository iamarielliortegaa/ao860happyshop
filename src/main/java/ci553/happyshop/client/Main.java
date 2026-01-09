package ci553.happyshop.client;

import ci553.happyshop.client.customer.*;
import ci553.happyshop.client.emergency.EmergencyExit;
import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.client.picker.PickerController;
import ci553.happyshop.client.picker.PickerModel;
import ci553.happyshop.client.picker.PickerView;
import ci553.happyshop.client.warehouse.*;
import ci553.happyshop.client.shoppingcenter.*;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The Main JavaFX application class - UPDATED
 * Now launches ONLY the new unified Shopping Center interface
 * All functionality integrated into ONE window
 *
 * @version 3.0
 * @author  Arielli Ortega, University of Brighton
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws IOException {
        // Initialize OrderHub first
        initializeOrderMap();
        
        // Launch ONLY the new unified Shopping Center interface
        startShoppingCenter();
        
        /* OLD CLIENTS - COMMENTED OUT
        startCustomerAuth();
        startAdminAuth();
        startCartView();
        startShippingView();
        startSendingsView();
        startCustomerClient();
        startPickerClient();
        startOrderTracker();
        startWarehouseClient();
        startEmergencyExit();
        */
    }

    private void startShoppingCenter() {
        ShoppingCenterView shoppingCenter = new ShoppingCenterView();
        shoppingCenter.start(new Stage());
    }
    
    private void initializeOrderMap(){
        OrderHub orderHub = OrderHub.getOrderHub();
        orderHub.initializeOrderMap();
    }
    
    private void startCustomerAuth() {
        CustomerAuthView customerAuth = new CustomerAuthView();
        customerAuth.start(new Stage());
    }
    
    private void startAdminAuth() {
        AdminAuthView adminAuth = new AdminAuthView();
        adminAuth.start(new Stage());
    }
    
    private void startCartView() {
        CartView cart = new CartView();
        cart.start(new Stage());
    }
    
    private void startShippingView() {
        ShippingView shipping = new ShippingView();
        shipping.start(new Stage());
    }
    
    private void startSendingsView() {
        SendingsView sendings = new SendingsView();
        sendings.start(new Stage());
    }

    private void startCustomerClient(){
        CustomerView cusView = new CustomerView();
        CustomerController cusController = new CustomerController();
        CustomerModel cusModel = new CustomerModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        cusView.cusController = cusController;
        cusController.cusModel = cusModel;
        cusModel.cusView = cusView;
        cusModel.databaseRW = databaseRW;
        cusView.start(new Stage());
    }

    private void startPickerClient(){
        PickerModel pickerModel = new PickerModel();
        PickerView pickerView = new PickerView();
        PickerController pickerController = new PickerController();
        pickerView.pickerController = pickerController;
        pickerController.pickerModel = pickerModel;
        pickerModel.pickerView = pickerView;
        pickerModel.registerWithOrderHub();
        pickerView.start(new Stage());
    }

    private void startOrderTracker(){
        OrderTracker orderTracker = new OrderTracker();
        orderTracker.registerWithOrderHub();
    }

    private void startWarehouseClient(){
        WarehouseView view = new WarehouseView();
        WarehouseController controller = new WarehouseController();
        WarehouseModel model = new WarehouseModel();
        DatabaseRW databaseRW = DatabaseRWFactory.createDatabaseRW();

        view.controller = controller;
        controller.model = model;
        model.view = view;
        model.databaseRW = databaseRW;
        view.start(new Stage());

        HistoryWindow historyWindow = new HistoryWindow();
        AlertSimulator alertSimulator = new AlertSimulator();

        model.historyWindow = historyWindow;
        model.alertSimulator = alertSimulator;
        historyWindow.warehouseView = view;
        alertSimulator.warehouseView = view;
    }

    private void startEmergencyExit(){
        EmergencyExit.getEmergencyExit();
    }
}
