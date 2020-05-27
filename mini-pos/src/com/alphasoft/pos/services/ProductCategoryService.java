package com.alphasoft.pos.services;

import com.alphasoft.pos.commons.ImageHelper;
import com.alphasoft.pos.contexts.ConnectionManager;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.ProductCategory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.alphasoft.pos.contexts.SqlHelper.getQuery;

public class ProductCategoryService {
    private static ProductCategoryService service;

    private ProductCategoryService(){

    }

    public List<ProductCategory> getAllCategories(){
        List<ProductCategory> list = new ArrayList<>();
        try(
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getQuery("category.select.all"))
                ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                ProductCategory item = new ProductCategory();
                item.setId(resultSet.getInt("id"));
                item.setName(resultSet.getString("name"));
                item.setImageBlob(resultSet.getBlob("image"));
                list.add(item);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public void addCategory(String name, File imageFile){
        canInsert(name);
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("category.insert"))
        ) {
            preparedStatement.setString(1,name);
            preparedStatement.setBlob(2, ImageHelper.fileToInputStream(imageFile));
            preparedStatement.executeLargeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private void canInsert(String name){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("category.find.name"))
            ) {
               preparedStatement.setString(1,name);
               ResultSet resultSet = preparedStatement.executeQuery();
               if(resultSet.next()){
                   throw new PosException("Item with this name already exists");
               }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static ProductCategoryService getService(){
        if(null==service)service = new ProductCategoryService();
        return service;
    }
}
