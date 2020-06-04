package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.AutoCompleteTextField;
import com.alphasoft.pos.contexts.ProductCategorySorter;
import com.alphasoft.pos.factories.ProductCategorySorterFactory;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryRepository;
import com.alphasoft.pos.views.customs.CategoryCard;
import com.alphasoft.pos.views.customs.PosWindowStage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PosCategoryController implements Initializable {

    @FXML
    private TextField categoryNameInput;

    @FXML
    private ComboBox<ProductCategorySorter.Mode> sortModeSelector;

    @FXML
    private FlowPane flowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AutoCompleteTextField.attach(categoryNameInput,ProductCategoryRepository.getRepository()::getAllProductCategoriesLike,this::loadData);
        sortModeSelector.getItems().addAll(ProductCategorySorter.Mode.values());
        sortModeSelector.getSelectionModel().selectFirst();
        sortModeSelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->loadData());
        categoryNameInput.textProperty().addListener((l,o,n)->{
            if(n.isEmpty()) loadData();
        });
        loadData();
    }

    @FXML
    public void upload() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/com/alphasoft/pos/views/product_category_form.fxml"));
        PosWindowStage stage = new PosWindowStage(MainWindowController.mainStage);
        stage.setScene(new Scene(view));
        stage.showAndWait();
        loadData();
    }

    @FXML
    public void clearInput(){
        categoryNameInput.clear();
    }

    private void loadData(ProductCategory productCategory){
        categoryNameInput.setText(productCategory.getName());
        categoryNameInput.positionCaret(categoryNameInput.getText().length());
        loadData();
    }

    private void loadData(){
        flowPane.getChildren().clear();
        List<ProductCategory> list = ProductCategoryRepository.getRepository().getAllProductCategories();
        if(!categoryNameInput.getText().trim().isEmpty())
            list.retainAll(
                list.stream().filter(i->i.getName().toLowerCase().equals(categoryNameInput.getText().trim().toLowerCase())).collect(Collectors.toList())
            );
        sortCategory(list);
        list.stream().map(i->new CategoryCard(i,this::editProductCategory)).forEach(c->flowPane.getChildren().add(c));
    }

    private void sortCategory(List<ProductCategory> list){
        ProductCategorySorter sorter = ProductCategorySorterFactory.getFactory().getSorter(sortModeSelector.getSelectionModel().getSelectedItem());
        if(null!=sorter){
            sorter.sort(list);
        }
    }

    private void editProductCategory(ProductCategory category){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/product_category_form.fxml"));
            Parent view = fxmlLoader.load();
            ProductCategoryFormController controller = fxmlLoader.getController();
            controller.setData(category);
            PosWindowStage stage = new PosWindowStage(MainWindowController.mainStage);
            stage.setScene(new Scene(view));
            stage.showAndWait();
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
