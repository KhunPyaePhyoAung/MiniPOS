package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.utils.FileHelper;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.repos.ProductRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ProductCategoryCardController {
    @FXML
    private ImageView imageView;

    @FXML
    private Label name;

    @FXML
    private Label itemCount;



    public void setProductCategory(ProductCategory productCategory){
        int totalItem = (int) ProductRepository.getRepository().getAllProducts().stream().filter(i->i.getCategoryId()==productCategory.getId()).count();
        itemCount.setText("Item : "+totalItem);
        imageView.setImage(new Image(Objects.requireNonNull(FileHelper.blobToInputStream(productCategory.getImageBlob()))));
        name.setText(productCategory.getName());
    }
}
