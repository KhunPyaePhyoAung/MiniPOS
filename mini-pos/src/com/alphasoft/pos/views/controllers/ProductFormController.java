package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.commons.NumberInput;
import com.alphasoft.pos.commons.Validations;
import com.alphasoft.pos.contexts.PosException;
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
        imageView.setImage(new Image(Objects.requireNonNull(ImageHelper.blobToInputStream(product.getImageBlob()))));
        nameInput.setText(product.getName());
        priceInput.setText(String.valueOf(product.getPrice()));
        availability.setSelected(product.isAvailable());
        categorySelector.getSelectionModel().select(ProductCategoryRepository.getRepository().getCategoryById(product.getId()));

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
            Image image = new Image(Objects.requireNonNull(ImageHelper.fileToInputStream(selectedFile)));
            if(image.getWidth()==image.getHeight()){
                imageFile = selectedFile;
                imageView.setImage(new Image(Objects.requireNonNull(ImageHelper.fileToInputStream(imageFile))));
            }else {
                imageFile=null;
                imageView.setImage(null);
                showAlert("Invalid Image","Image must be square");
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberInput.attach(priceInput);
        categorySelector.getItems().addAll(ProductCategoryRepository.getRepository().getAllProductCategories());
        setupButton();
        runLater();
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
        addButton.setOnAction(e->{
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
        });

    }

    private void runLater(){
        Platform.runLater(()->{
            if(null==product){
                title.setText("Add New Product");
                actionButtonGroup.getChildren().add(addButton);
            }else{
                title.setText("Edit Product");
                actionButtonGroup.getChildren().addAll(deleteButton,updateButton);
            }
        });
    }

    private void addProduct(){
        Product product = new Product();
        product.setName(nameInput.getText().trim());
        product.setPrice(Integer.parseInt(priceInput.getText().trim()));
        product.setAvailable(availability.isSelected());
        product.setCategoryId(categorySelector.getSelectionModel().getSelectedItem().getId());
        product.setCategoryName(categorySelector.getSelectionModel().getSelectedItem().getName());
        product.setImageFile(imageFile);
        ProductService.getService().checkIfCanInsertProduct(product.getName());
        ProductService.getService().addProduct(product);
    }

    private void showAlert(String title,String message){
        AlertBox alertBox = new AlertBox((Stage)imageView.getScene().getWindow());
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.show();
    }
}
