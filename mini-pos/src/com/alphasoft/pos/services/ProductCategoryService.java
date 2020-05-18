package com.alphasoft.pos.services;

import com.alphasoft.pos.contexts.ConnectionManager;
import com.alphasoft.pos.models.ProductCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static ProductCategoryService getService(){
        if(null==service)service = new ProductCategoryService();
        return service;
    }
}
