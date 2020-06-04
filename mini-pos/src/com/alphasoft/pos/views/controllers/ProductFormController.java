package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.FileHelper;
import com.alphasoft.pos.commons.NumberInput;
import com.alphasoft.pos.commons.Validations;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.contexts.ProductCategorySorter;
import com.alphasoft.pos.factories.ProductCategorySorterFactory;
import com.alphasoft.pos.models.Product;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryRepository;
import com.alphasoft.pos.services.ProductService;
import com.alphasoft.pos.views.customs.AlertBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {
    @FXML
    private Label title;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceInput;

    @FXML
    private CheckBox availability;

    @FXML
    private ComboBox<ProductCategory> categorySelector;

    private Product product;

    @FXML
    private HBox actionButtonGroup;

    private Button addButton,updateButton,deleteButton;

    private File imageFile;

    public void setProduct(Product product){
        this.product = product;
        imageView.setImage(new Image(Objects.requireNonNull(FileHelper.blobToInputStream(product.getImageBlob()))));
        nameInput.setText(product.getName());
        priceInput.setText(String.valueOf(product.getPrice()));
        availability.setSelected(product.isAvailable());
        categorySelector.getSelectionModel().select(ProductCategoryRepository.getRepository().getCategory(product.getCategoryId()));

    }

    @FXML
    void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image File","*.jpg","*.jpeg","*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if(null!=selectedFile){
            Image image = new Image(Objects.requireNonNull(FileHelper.fileToInputStream(selectedFile)));
            if(image.getWidth()==image.getHeight()){
                imageFile = selectedFile;
                imageView.setImage(new Image(Objects.requireNonNull(FileHelper.fileToInputStream(imageFile))));
            }else {
                showAlert("Invalid Image","Image must be square");
            }
        }
        if(null!=product){
            toggleUpdateButton();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberInput.attach(priceInput);
        List<ProductCategory> productCategoryList = ProductCategoryRepository.getRepository().getAllProductCategories();
        ProductCategorySorterFactory.getFactory().getSorter(ProductCategorySorter.Mode.NAME_ASCENDING).sort(productCategoryList);
        categorySelector.getItems().addAll(productCategoryList);
        setupButton();
        Platform.runLater(this::runLater);
    }

    @FXML
    public void cancel(){
        close();
    }

    private void close(){
        imageView.getScene().getWindow().hide();
    }

    private void setupButton(){
        addButton = new Button("Add");
        updateButton = new Button("Update");
        deleteButton = new Button("Delete");
        addButton.setOnAction(e->onAdd());
        updateButton.setOnAction(e->onUpdate());
    }

    private void onAdd(){
        try{
            Validations.notEmptyString(nameInput.getText().trim(),"Please enter product name");
            Validations.notEmptyString(priceInput.getText().trim(),"Please enter price");
            Validations.notNull(categorySelector.getSelectionModel().getSelectedItem(),"Please select a category");
            Validations.notNull(imageFile,"Please select an image");
            addProduct();
            close();
        }catch (PosException exception){
            showAlert("Action cannot be completed",exception.getMessage());
        }
    }

    private void onUpdate(){
        try{
            Validations.notEmptyString(nameInput.getText().trim(),"Please enter product name");
            Validations.notEmptyString(priceInput.getText().trim(),"Please enter price");
            Validations.notNull(categorySelector.getSelectionModel().getSelectedItem(),"Please select a category");
            if(null==imageView.getImage()) Validations.notNull(imageFile,"Please select an image");
            updateProduct();
            close();
        }catch (PosException exception){
            showAlert("Action cannot be completed",exception.getMessage());
        }
    }

    private void runLater(){
        Platform.runLater(()->{
            if(null==product){
                title.setText("Add New Product");
                actionButtonGroup.getChildren().add(addButton);
            }else{
                title.setText("Edit Product");
                nameInput.textProperty().addListener((l,o,n)->toggleUpdateButton());
                priceInput.textProperty().addListener((l,o,n)->toggleUpdateButton());
                availability.selectedProperty().addListener((l,o,n)->toggleUpdateButton());
                categorySelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->toggleUpdateButton());
                actionButtonGroup.getChildren().addAll(deleteButton,updateButton);
                toggleUpdateButton();
            }
        });
    }

    private void addProduct(){
        Product newProduct = new Product();
        newProduct.setName(nameInput.getText().trim());
        newProduct.setPrice(Integer.parseInt(priceInput.getText().trim()));
        newProduct.setAvailable(availability.isSelected());
        newProduct.setCategoryId(categorySelector.getSelectionModel().getSelectedItem().getId());
        newProduct.setCategoryName(categorySelector.getSelectionModel().getSelectedItem().getName());
        newProduct.setImageFile(imageFile);
        ProductService.getService().checkAndAddProduct(newProduct);
    }

    private void updateProduct(){
        Product editedProduct = new Product();
        editedProduct.setId(product.getId());
        editedProduct.setName(nameInput.getText().trim());
        editedProduct.setCategoryId(getSelectedCategory().getId());
        editedProduct.setPrice(Integer.parseInt(priceInput.getText().trim()));
        editedProduct.setAvailable(availability.isSelected());
        if(null!=imageFile) editedProduct.setImageFile(imageFile);
        ProductService.getService().checkAndUpdateProduct(editedProduct);
    }

    private ProductCategory getSelectedCategory(){
        return categorySelector.getSelectionModel().getSelectedItem();
    }

    private void toggleUpdateButton(){
        updateButton.setDisable(imageFile==null && nameInput.getText().trim().contentEquals(product.getName()) && priceInput.getText().trim().contentEquals(String.valueOf(product.getPrice())) && availability.isSelected()==product.isAvailable() && getSelectedCategory().getId()==product.getCategoryId());
    }

    private void showAlert(String title,String message){
        AlertBox alertBox = new AlertBox((Stage)imageView.getScene().getWindow());
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.show();
    }
}
