package com.alphasoft.pos.services;

import com.alphasoft.pos.commons.FileHelper;
import javafx.scene.image.Image;


public class ProductImageHelper {
    private static ProductImageHelper helper;

    private ProductImageHelper(){}

    public Image getImage(String productName){
        return new Image(FileHelper.blobToInputStream(ProductRepository.getRepository().get(productName).getImageBlob()));
    }

    public static ProductImageHelper getInstance(){
        if(null==helper) helper = new ProductImageHelper();
        return helper;
    }
}
