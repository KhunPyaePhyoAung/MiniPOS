package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.views.controllers.MainWindowController;
import com.alphasoft.pos.views.controllers.PaymentWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.function.Consumer;

public class PaymentWindow {

    private PosWindowStage stage;

    public PaymentWindow(Sale sale,Consumer<Sale> onSavePayment){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/payment_window.fxml"));
            Parent view = fxmlLoader.load();
            PaymentWindowController controller = fxmlLoader.getController();
            controller.setSale(sale);
            controller.setOnSave(onSavePayment);
            stage = new PosWindowStage(MainWindowController.mainStage);
            stage.setScene(new Scene(view));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAndWait(){
        stage.showAndWait();
    }

}
