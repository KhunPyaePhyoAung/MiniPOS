package com.alphasoft.pos.repos;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.alphasoft.pos.utils.MySqlHelper.getQuery;

public class ProductRepository {

    private static ProductRepository repository;

    private ProductRepository(){}

    public List<Product> getAllProducts(){
        List<Product> list = new ArrayList<>();

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("product.select.all"))
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                list.add(parseProductFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    public Product get(int id){
        return getAllProducts().stream().filter(i->i.getId()==id).findAny().orElse(null);
    }

    public Product get(String name){
        return getAllProducts().stream().filter(i->i.getName().equals(name)).findAny().orElse(null);
    }


    private Product parseProductFromResultSet(ResultSet resultSet) throws SQLException {
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


    public static ProductRepository getRepository(){
        if(null==repository) repository = new ProductRepository();
        return repository;
    }

}
