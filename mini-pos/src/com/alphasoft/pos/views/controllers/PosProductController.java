package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.AutoCompleteTextField;
import com.alphasoft.pos.contexts.ProductFilter;
import com.alphasoft.pos.contexts.ProductSorter;
import com.alphasoft.pos.factories.ProductFilterFactory;
import com.alphasoft.pos.factories.ProductSorterFactory;
import com.alphasoft.pos.models.Product;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryRepository;
import com.alphasoft.pos.services.ProductRepository;
import com.alphasoft.pos.views.customs.PosWindowStage;
import com.alphasoft.pos.views.customs.ProductCard;
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

public class PosProductController implements Initializable {

    @FXML
    private TextField categoryNameInput;

    @FXML
    private TextField productNameInput;

    @FXML
    private ComboBox<ProductFilter.Mode> showModeSelector;

    @FXML
    private ComboBox<ProductSorter.Mode> sortModeSelector;

    @FXML
    private FlowPane flowPane;

    @FXML
    void clearInput() {
        categoryNameInput.clear();
        productNameInput.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AutoCompleteTextField.attach(categoryNameInput, ProductCategoryRepository.getRepository()::getAllProductCategoriesLike,this::loadData);
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
        List<Product> productList = ProductRepository.getRepository().getAllProducts();
        if(!categoryNameInput.getText().trim().isEmpty())
            productList.retainAll(
                    productList.stream()
                    .filter(i->i.getCategoryName().toLowerCase().equals(categoryNameInput.getText().trim().toLowerCase()))
                    .collect(Collectors.toList())
            );
        productList.retainAll(productList.stream()
                .filter(i->i.getName().toLowerCase().contains(productNameInput.getText().trim().toLowerCase()))
                .collect(Collectors.toList()));
        filterProduct(productList);
        sortProduct(productList);
        productList.stream().map(i->new ProductCard(i,this::editProduct)).forEach(i->flowPane.getChildren().add(i));
    }

    @FXML
    public void uploadProduct(){
        showProductForm(null);
        loadData();
    }

    private void editProduct(Product product) {
        showProductForm(product);
        loadData();
    }

    private void showProductForm(Product product){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ProductFormController.class.getResource("/com/alphasoft/pos/views/product_form.fxml"));
            Parent view = fxmlLoader.load();
            if(null!=product)((ProductFormController)fxmlLoader.getController()).setProduct(product);
            PosWindowStage stage = new PosWindowStage(MainWindowController.mainStage);
            stage.setScene(new Scene(view));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSelector(){
        showModeSelector.getItems().addAll(ProductFilter.Mode.values());
        showModeSelector.getSelectionModel().selectFirst();
        showModeSelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->loadData());
        sortModeSelector.getItems().addAll(ProductSorter.Mode.values());
        sortModeSelector.getSelectionModel().selectFirst();
        sortModeSelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->loadData());
    }

    private void filterProduct(List<Product> productList){
        ProductFilter productFilter = ProductFilterFactory.getFactory().getFilter(showModeSelector.getSelectionModel().getSelectedItem());
        if(null!=productFilter){
            productFilter.filter(productList);
        }
    }
    private void sortProduct(List<Product> productList){
        ProductSorter productSorter = ProductSorterFactory.getFactory().getSorter(sortModeSelector.getSelectionModel().getSelectedItem());
        if(null!=productSorter){
            productSorter.sort(productList);
        }

    }
}
