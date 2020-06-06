package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.AutoCompleteTextField;
import com.alphasoft.pos.commons.DecimalFormattedCellFactory;
import com.alphasoft.pos.models.Product;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.models.ProductSaleItem;
import com.alphasoft.pos.services.ProductCategoryRepository;
import com.alphasoft.pos.services.ProductRepository;
import com.alphasoft.pos.views.customs.ProductCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PosSaleController implements Initializable {
    @FXML
    private TableView<ProductSaleItem> saleTable;

    @FXML
    private TableColumn<ProductSaleItem, String> nameColumn;

    @FXML
    private TableColumn<ProductSaleItem, Integer> priceColumn;

    @FXML
    private TableColumn<ProductSaleItem, Integer> qtyColumn;

    @FXML
    private TableColumn<ProductSaleItem, Integer> totalColumn;

    @FXML
    private Label subTotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField categoryNameInput;

    @FXML
    private TextField productNameInput;

    @FXML
    private FlowPane flowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AutoCompleteTextField.attach(categoryNameInput, ProductCategoryRepository.getRepository()::getAllProductCategoriesLike,this::loadProductOf);
        categoryNameInput.textProperty().addListener((l,o,n)->{
            if(n.isEmpty()) loadProducts();
        });
        productNameInput.textProperty().addListener((l,o,n)->loadProducts());
        setupTable();
        loadProducts();
    }

    @FXML
    void clear(ActionEvent event) {

    }

    @FXML
    void clearInput() {
        categoryNameInput.clear();
        productNameInput.clear();
    }

    @FXML
    void hold(ActionEvent event) {

    }

    @FXML
    void pay(ActionEvent event) {

    }

    @FXML
    void recall(ActionEvent event) {

    }

    private void loadProductOf(ProductCategory productCategory){
        categoryNameInput.setText(productCategory.getName());
        categoryNameInput.positionCaret(categoryNameInput.getText().length());
        loadProducts();
    }

    private void loadProducts(){
        flowPane.getChildren().clear();
        List<Product> productList = ProductRepository.getRepository().getAllProducts();
        filterCategories(productList);
        filterProducts(productList);

        productList.stream().map(i->new ProductCard(i,this::addToCart)).forEach(i->flowPane.getChildren().add(i));


    }

    private void filterCategories(List<Product> productList){
        if(!categoryNameInput.getText().trim().isEmpty()){
            productList.retainAll(
                    productList.stream().filter(i->i.getCategoryName().equalsIgnoreCase(categoryNameInput.getText().trim()))
                                        .collect(Collectors.toList())
            );
        }
    }

    private void filterProducts(List<Product> productList){
        productList.retainAll(
                productList.stream().filter(i->i.getName().toLowerCase().contains(productNameInput.getText().trim().toLowerCase()))
                                    .collect(Collectors.toList())
        );
    }

    private void addToCart(Product product){
        
    }

    private void setupTable(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        priceColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        qtyColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        totalColumn.setCellFactory(new DecimalFormattedCellFactory<>());
    }

}
