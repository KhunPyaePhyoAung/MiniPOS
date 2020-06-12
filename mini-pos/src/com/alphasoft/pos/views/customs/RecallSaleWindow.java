package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.views.controllers.MainWindowController;
import com.alphasoft.pos.views.controllers.RecallSaleWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.function.Consumer;

public class RecallSaleWindow {

    private PosWindowStage stage;

    public RecallSaleWindow(Consumer<Sale> onRecall){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/recall_sale_window.fxml"));
            Parent view = fxmlLoader.load();
            RecallSaleWindowController controller = fxmlLoader.getController();
            controller.setOnRecall(onRecall);
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
