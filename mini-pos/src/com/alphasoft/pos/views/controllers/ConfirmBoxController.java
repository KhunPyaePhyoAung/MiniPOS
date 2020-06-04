package com.alphasoft.pos.views.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmBoxController implements Initializable {
    @FXML
    private Label title;

    @FXML
    private Text text;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;


    public void setTitle(String titleText){
        title.setText(titleText);
    }

    public void setContentText(String contentText){
        text.setText(contentText);
    }

    public void setOnConfirmed(EventHandler<ActionEvent> eventHandler){
        confirmButton.setOnAction(eventHandler);
    }
    public void setOnCanceled(EventHandler<ActionEvent> eventHandler){
        cancelButton.setOnAction(eventHandler);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setOnAction(event -> text.getScene().getWindow().hide());
    }
}
