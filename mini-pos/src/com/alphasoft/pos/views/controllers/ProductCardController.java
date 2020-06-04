package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.FileHelper;
import com.alphasoft.pos.commons.StringUtils;
import com.alphasoft.pos.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

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
        imageView.setImage(new Image(Objects.requireNonNull(FileHelper.blobToInputStream(product.getImageBlob()))));
        productName.setText(product.getName());
        categoryName.setText(product.getCategoryName());
        price.setText(StringUtils.formatToMmk(product.getPrice()));
        product_card.getStyleClass().add(product.isAvailable()? "product_card_available":"product_card_unavailable");
    }

}
