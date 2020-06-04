package com.alphasoft.pos.services;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.contexts.ConnectionManager;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.alphasoft.pos.contexts.SqlHelper.getQuery;

public class ProductService {
    private static ProductService service;

    private ProductService(){

    }




    public void addProduct(Product product){
        if(null==product)return;
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("product.insert"))
        ) {
            preparedStatement.setString(1,product.getName());
            preparedStatement.setInt(2,product.getCategoryId());
            preparedStatement.setInt(3,product.getPrice());
            preparedStatement.setBlob(4, ImageHelper.fileToInputStream(product.getImageFile()));
            preparedStatement.setBoolean(5,product.isAvailable());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void checkIfCanInsertProduct(String productName){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getQuery("product.checkIfCanInsert"))
            ){
            preparedStatement.setString(1,productName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) throw new PosException("Product with this name already exists");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ProductService getService(){
        if(null==service) service = new ProductService();
        return service;
    }
}
