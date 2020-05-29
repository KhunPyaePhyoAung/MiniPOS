package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.commons.Validations;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryService;
import com.alphasoft.pos.views.customs.AlertBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductCategoryFormController implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private TextField categoryNameInput;


    @FXML
    private HBox mainButtonBox;

    private ProductCategory category;

    private Button deleteButton,addButton,updateButton;

    private File imageFile;


    public void setData(ProductCategory category){
        this.category = category;
        imageView.setImage(new Image(Objects.requireNonNull(ImageHelper.blobToInputStream(category.getImageBlob()))));
        categoryNameInput.setText(category.getName());
    }

    @FXML
    void cancel() {
        close();
    }

    @FXML
    void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image","*.jpg","*.png","*.jpeg"));
        File file = fileChooser.showOpenDialog(MainWindowController.mainStage);
        if(null!=file){
            Image image = new Image(Objects.requireNonNull(ImageHelper.fileToInputStream(file)));
            if(image.getWidth()==image.getHeight()){
                imageFile = file;
                imageView.setImage(new Image(Objects.requireNonNull(ImageHelper.fileToInputStream(imageFile))));
            }else {
                imageFile=null;
                imageView.setImage(null);
                AlertBox alertBox = new AlertBox((Stage)imageView.getScene().getWindow());
                alertBox.setTitle("Invalid Image");
                alertBox.setContentText("Image must be square");
                alertBox.show();
            }

        }
        if(null!=category){
            toggleUpdateButton();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupButton();
        Platform.runLater(()->{
            if(null==category){
                mainButtonBox.getChildren().addAll(addButton);
            }else{
                mainButtonBox.getChildren().addAll(deleteButton,updateButton);
                categoryNameInput.textProperty().addListener((l,o,n)->toggleUpdateButton());
                toggleUpdateButton();
            }
        });
    }

    private void setupButton(){
        deleteButton = new Button("Delete");
        updateButton = new Button("Update");
        addButton = new Button("Add");

        addButton.setOnAction(e->{
            try{
                Validations.notEmptyInput(categoryNameInput.getText().trim(),"category name");
                Validations.notNull(imageFile,"No image selected");
                ProductCategoryService.getService().addCategory(categoryNameInput.getText().trim(),imageFile);
                close();
            }catch (PosException exception){
                AlertBox alertBox = new AlertBox((Stage)imageView.getScene().getWindow());
                alertBox.setTitle("Action cannot be completed");
                alertBox.setContentText(exception.getMessage());
                alertBox.show();
            }
        });
        updateButton.setOnAction(e->{
            try{
                Validations.notEmptyInput(categoryNameInput.getText().trim(),"category name");
                if(null==imageView.getImage()){
                    Validations.notNull(imageFile,"No image selected");
                }

                ProductCategoryService.getService().updateCategory(category.getId(),categoryNameInput.getText().trim(),imageFile);
                close();
            }catch (PosException exception){
                AlertBox alertBox = new AlertBox((Stage)imageView.getScene().getWindow());
                alertBox.setTitle("Action cannot be completed");
                alertBox.setContentText(exception.getMessage());
                alertBox.show();
            }
        });
    }

    private void toggleUpdateButton(){
        updateButton.setDisable((imageFile==null && category.getName().contentEquals(categoryNameInput.getText().trim())));
    }

    private void close(){
        categoryNameInput.getScene().getWindow().hide();
    }



}
