package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.ProductCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alphasoft.pos.database.SqlHelper.getQuery;

public class ProductCategoryRepository {

    private static ProductCategoryRepository repository;

    private ProductCategoryRepository(){

    }

    public List<ProductCategory> getAllProductCategories(){
        List<ProductCategory> list = new ArrayList<>();
        try(
                Connection connection = ConnectionManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getQuery("category.select.all"))
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                list.add(parseCategoryFrmResultSet(resultSet));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return list;
    }

    public List<ProductCategory> getAllProductCategoriesLike(String productCategoryName){
        return getAllProductCategories().stream().filter(i->i.getName().toLowerCase().contains(productCategoryName.toLowerCase())).limit(10).collect(Collectors.toList());
    }

    private ProductCategory parseCategoryFrmResultSet(ResultSet resultSet) throws SQLException {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(resultSet.getInt("id"));
        productCategory.setName(resultSet.getString("name"));
        productCategory.setImageBlob(resultSet.getBlob("image"));
        return productCategory;
    }

    public ProductCategory getCategory(int id){
        return getAllProductCategories().stream().filter(i->i.getId()==id).findAny().orElse(null);
    }

    public static ProductCategoryRepository getRepository(){
        if(null == repository) repository = new ProductCategoryRepository();
        return repository;
    }
}
