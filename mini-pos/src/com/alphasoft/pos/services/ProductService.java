package com.alphasoft.pos.services;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.contexts.ConnectionManager;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.alphasoft.pos.contexts.SqlHelper.getQuery;

public class ProductService {
    private static ProductService service;

    private ProductService(){

    }



    public void checkAndAddProduct(Product product){
        checkIfCanInsertProduct(product);
        addProduct(product);
    }

    private void addProduct(Product product){
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

    private void checkIfCanInsertProduct(Product product){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getQuery("product.checkIfCanInsert"))
            ){
            preparedStatement.setString(1,product.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) throw new PosException("Product with this name already exists");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void checkAndUpdateProduct(Product product){
        checkIfCanUpdateProduct(product);
        updateProduct(product);
    }

    private void updateProduct(Product product){
        StringBuilder sb = new StringBuilder(getQuery("product.update"));
        List<Object> params = new ArrayList<>();
        params.add(product.getName());
        params.add(product.getPrice());
        params.add(product.getCategoryId());
        params.add(product.isAvailable());
        if(null!=product.getImageFile()){
            sb.append(",image=?");
            params.add(ImageHelper.fileToInputStream(product.getImageFile()));
        }
        sb.append(" where id=?");
        params.add(product.getId());
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString())
        ) {
            for(int i = 0;i<params.size();i++){
                preparedStatement.setObject(i+1,params.get(i));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void checkIfCanUpdateProduct(Product product){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("product.checkIfCanUpdate"))
        ) {
            preparedStatement.setString(1,product.getName());
            preparedStatement.setInt(2,product.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                throw new PosException("Product with this name already exists");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ProductService getService(){
        if(null==service) service = new ProductService();
        return service;
    }
}
