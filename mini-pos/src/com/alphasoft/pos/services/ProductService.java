package com.alphasoft.pos.services;

import com.alphasoft.pos.contexts.ConnectionManager;
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

    public List<Product> getAllProduct(){
        List<Product> list = new ArrayList<>();

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("product.select.all"))
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                list.add(getProduct(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setCategoryId(resultSet.getInt("category_id"));
        product.setCategoryName(resultSet.getString("category_name"));
        product.setPrice(resultSet.getInt("price"));
        product.setImageBlob(resultSet.getBlob("image"));
        product.setAvailable(resultSet.getBoolean("available"));
        return product;
    }

    public static ProductService getService(){
        if(null==service) service = new ProductService();
        return service;
    }
}
