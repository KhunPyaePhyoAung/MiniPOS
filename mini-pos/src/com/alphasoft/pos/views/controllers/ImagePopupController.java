package com.alphasoft.pos.views.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ImagePopupController {

    @FXML
    private VBox popupWindow;
    @FXML
    private ImageView dataImageView;

    public void setImage(Image image){
        dataImageView.setImage(image);
    }

    public void setSize(double width,double height){
        popupWindow.setPrefSize(width,height);
        dataImageView.setFitWidth(popupWindow.getPrefWidth()-2);
        dataImageView.setFitHeight(popupWindow.getPrefHeight()-2);
    }
}
