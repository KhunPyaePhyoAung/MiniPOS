package com.alphasoft.pos.services;

import com.alphasoft.pos.commons.FileHelper;
import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.alphasoft.pos.database.SqlHelper.getQuery;

public class ProductCategoryService {
    private static ProductCategoryService service;

    private ProductCategoryService(){}

    
    public void checkAndAddCategory(ProductCategory productCategory){
        checkIfCanInsertCategory(productCategory);
        addCategory(productCategory);
    }

    public void checkAndUpdateCategory(ProductCategory productCategory){
        checkIfCanUpdateCategory(productCategory);
        updateCategory(productCategory);
    }




    private void addCategory(ProductCategory productCategory){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("category.insert"))
        ) {
            preparedStatement.setString(1,productCategory.getName());
            preparedStatement.setBlob(2, FileHelper.fileToInputStream(productCategory.getImageFile()));
            preparedStatement.executeLargeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private void updateCategory(ProductCategory productCategory){
        List<Object> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder(getQuery("category.update"));
        sb.append(" name=?");
        params.add(productCategory.getName());
        if(null!=productCategory.getImageFile()){
            sb.append(",image=?");
            params.add(FileHelper.fileToInputStream(productCategory.getImageFile()));
        }
        sb.append(" where id=?");
        params.add(productCategory.getId());
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

    private void checkIfCanInsertCategory(ProductCategory productCategory){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("category.checkIfCanInsert"))
            ) {
               preparedStatement.setString(1,productCategory.getName());
               ResultSet resultSet = preparedStatement.executeQuery();
               if(resultSet.next()){
                   throw new PosException("Item with this name already exists");
               }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private void checkIfCanUpdateCategory(ProductCategory category){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("category.checkIfCanUpdate"))
        ) {
            preparedStatement.setString(1,category.getName());
            preparedStatement.setInt(2,category.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                throw new PosException("Item with this name already exists");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public static ProductCategoryService getService(){
        if(null==service)service = new ProductCategoryService();
        return service;
    }
}
