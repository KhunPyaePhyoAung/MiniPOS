package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.views.controllers.ConfirmBoxController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmBox {

    private ConfirmBoxController controller;
    private PosWindowStage stage;

    public ConfirmBox(){
        setup();
    }

    public ConfirmBox(Stage owner){
        setup();
        stage.initOwner(owner);
    }

    public ConfirmBox(String title,String contentText){
        setup();
        controller.setTitle(title);
        controller.setContentText(contentText);
    }

    public void setTitle(String title){
        controller.setTitle(title);
    }

    public void setContentText(String contentText){
        controller.setContentText(contentText);
    }

    public void setOnConfirmed(EventHandler<ActionEvent> eventHandler){
        controller.setOnConfirmed(eventHandler);

    }

    public void setOnCanceled(EventHandler<ActionEvent> eventHandler){
        controller.setOnCanceled(eventHandler);
    }

    public void close(){
        stage.close();
    }

    private void setup(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/confirm_box.fxml"));
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
