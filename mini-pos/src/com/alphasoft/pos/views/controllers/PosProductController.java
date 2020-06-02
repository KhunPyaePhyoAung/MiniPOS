package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.AutoCompleteTextField;
import com.alphasoft.pos.contexts.PosSorter;
import com.alphasoft.pos.contexts.ProductFilter;
import com.alphasoft.pos.models.Product;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryService;
import com.alphasoft.pos.services.ProductService;
import com.alphasoft.pos.views.customs.ProductCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PosProductController implements Initializable {

    @FXML
    private TextField categoryNameInput;

    @FXML
    private TextField productNameInput;

    @FXML
    private ComboBox<ProductFilter.Mode> showModeSelector;

    @FXML
    private ComboBox<PosSorter.Mode> sortModeSelector;

    @FXML
    private FlowPane flowPane;

    @FXML
    void clearInput() {
        categoryNameInput.clear();
        productNameInput.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AutoCompleteTextField.attach(categoryNameInput, ProductCategoryService.getService()::searchCategories,this::loadData);
        categoryNameInput.textProperty().addListener((l,o,n)->{
            if(n.isEmpty())
                loadData();
        });
        productNameInput.textProperty().addListener((l,o,n)->loadData());
        setupSelector();
        loadData();
    }


    private void loadData(ProductCategory category){
        categoryNameInput.setText(category.getName());
        categoryNameInput.positionCaret(categoryNameInput.getText().length());
        loadData();
    }
    private void loadData(){
        flowPane.getChildren().clear();
        List<Product> productList = ProductService.getService().getAllProduct();
        if(!categoryNameInput.getText().trim().isEmpty())
            productList.retainAll(
                    productList.stream()
                    .filter(i->i.getCategoryName().toLowerCase().equals(categoryNameInput.getText().trim().toLowerCase()))
                    .collect(Collectors.toList())
            );
        productList.retainAll(productList.stream()
                .filter(i->i.getName().toLowerCase().contains(productNameInput.getText().trim().toLowerCase()))
                .collect(Collectors.toList()));
        ProductFilter.getFilter().filter(productList,showModeSelector.getSelectionModel().getSelectedItem());
        productList.stream().map(i->new ProductCard(i,this::editProduct)).forEach(i->flowPane.getChildren().add(i));
    }

    @FXML
    public void uploadProduct(){

    }

    private void editProduct(Product product){

    }

    private void setupSelector(){
        showModeSelector.getItems().addAll(ProductFilter.Mode.values());
        showModeSelector.getSelectionModel().selectFirst();
        showModeSelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->loadData());
        sortModeSelector.getItems().addAll(PosSorter.Mode.values());
        sortModeSelector.getSelectionModel().selectFirst();
    }
}
