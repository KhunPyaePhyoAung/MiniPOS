package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ProductCardController {

    @FXML
    private HBox product_card;

    @FXML
    private ImageView imageView;

    @FXML
    private Label productName;

    @FXML
    private Label categoryName;

    @FXML
    private Label price;

    public void setProduct(Product product){
        String unavailableStyle = "-fx-text-fill:#dd1111";
        imageView.setImage(new Image(ImageHelper.blobToInputStream(product.getImageBlob())));
        productName.setText(product.getName());
        categoryName.setText(product.getCategoryName());
        price.setText(String.valueOf(product.getPrice()));
        if(!product.isAvailable()){
            product_card.getStyleClass().remove("product_card");
            product_card.getStyleClass().add("product_card_deactivate");
        }
    }

}
