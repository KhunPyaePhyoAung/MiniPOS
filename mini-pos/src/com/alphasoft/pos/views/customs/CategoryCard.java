package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.models.ProductCategory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.function.Consumer;

public class CategoryCard extends VBox {


    public CategoryCard(ProductCategory productCategory, Consumer<ProductCategory> clickListener){
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(150,200);
        getStylesheets().add("/com/alphasoft/pos/views/styles/default_theme.css");
        getStyleClass().add("category-card");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setImage(new Image(Objects.requireNonNull(ImageHelper.blobToInputStream(productCategory.getImageBlob()))));
        VBox dataBox = new VBox();
        dataBox.getStyleClass().add("category-data-box");
        dataBox.setPrefSize(150,50);
        dataBox.setAlignment(Pos.CENTER_LEFT);
        dataBox.setSpacing(5);
        dataBox.setPadding(new Insets(10,5,5,10));
        dataBox.getChildren().add(new Label(productCategory.getName()));
        getChildren().addAll(imageView,dataBox);
        setOnMouseClicked(e->{
            if(e.getClickCount()>1) clickListener.accept(productCategory);
        });
    }
}
