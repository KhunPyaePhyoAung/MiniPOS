package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.views.controllers.AlertBoxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertBox {

    private AlertBoxController controller;
    private PosWindowStage stage;

    public AlertBox(){
        setup();
    }

    public AlertBox(String title,String contentText){
        setup();
        controller.setTitle(title);
        controller.setContentText(contentText);
    }

    public AlertBox(Stage owner){
        setup();
        stage.initOwner(owner);
    }

    public void setTitle(String title){
        controller.setTitle(title);
    }

    public void setContentText(String contentText){
        controller.setContentText(contentText);
    }

    private void setup(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/alert_box.fxml"));
            Parent view = fxmlLoader.load();
            controller = fxmlLoader.getController();
            stage = new PosWindowStage();
            stage.setScene(new Scene(view));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(){
        stage.show();
    }

    public void showAndWait(){
        stage.showAndWait();
    }


}
