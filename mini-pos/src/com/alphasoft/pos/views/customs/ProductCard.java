package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.models.Product;
import com.alphasoft.pos.views.controllers.ProductCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.function.Consumer;

public class ProductCard extends HBox {
    public ProductCard(Product product, Consumer<Product> clickListener){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/product_card.fxml"));
            Parent view = fxmlLoader.load();
            ProductCardController controller = fxmlLoader.getController();
            controller.setProduct(product);
            getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
