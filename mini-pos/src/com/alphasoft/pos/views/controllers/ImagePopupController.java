package com.alphasoft.pos.views.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImagePopupController {
    @FXML
    private ImageView dataImageView;

    public void setImage(Image image){
        dataImageView.setImage(image);
    }
}
