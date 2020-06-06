package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.views.controllers.ProductCategoryCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.function.Consumer;

public class CategoryCard extends VBox {


    public CategoryCard(ProductCategory productCategory, Consumer<ProductCategory> clickListener){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/product_category_card.fxml"));
            Parent view = fxmlLoader.load();
            ProductCategoryCardController controller = fxmlLoader.getController();
            controller.setProductCategory(productCategory);
            getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setOnMouseClicked(e->{
            if(e.getClickCount()>1) clickListener.accept(productCategory);
        });
    }
}
