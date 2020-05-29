package com.alphasoft.pos.views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class AlertBoxController {

    @FXML
    Label title;

    @FXML
    Text text;

    public void setTitle(String titleText){
        title.setText(titleText);
    }

    public void setContentText(String contentText){
        text.setText(contentText);
    }

    public void ok(){
        text.getScene().getWindow().hide();
    }

}
