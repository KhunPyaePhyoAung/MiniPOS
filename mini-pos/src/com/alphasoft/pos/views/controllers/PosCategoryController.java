package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.PosSorter;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryService;
import com.alphasoft.pos.views.customs.CategoryCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PosCategoryController implements Initializable {

    @FXML
    private TextField categoryNameInput;

    @FXML
    private ComboBox<PosSorter.Mode> sortModeSelector;

    @FXML
    private FlowPane flowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortModeSelector.getItems().addAll(PosSorter.Mode.values());
        sortModeSelector.getSelectionModel().selectFirst();
        sortModeSelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->loadData());
        loadData();
    }

    @FXML
    public void upload(){

    }

    @FXML
    public void clearInput(){
        categoryNameInput.clear();
    }

    private void loadData(){
        flowPane.getChildren().clear();
        List<ProductCategory> list = ProductCategoryService.getService().getAllCategories();
        PosSorter.sort(list,sortModeSelector.getValue());
        list.stream().map(i->new CategoryCard(i,null)).forEach(c->flowPane.getChildren().add(c));
    }


}
