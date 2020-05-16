package com.alphasoft.pos.views.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    @FXML
    public void requestView(MouseEvent event){
        var source = event.getSource();
        if(source instanceof VBox){
            VBox vBox = (VBox)source;
            vBox.requestFocus();
        }
    }

    public static void show(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent view = fxmlLoader.load(PosHomeController.class.getResource("/com/alphasoft/pos/views/main_window.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(view));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
