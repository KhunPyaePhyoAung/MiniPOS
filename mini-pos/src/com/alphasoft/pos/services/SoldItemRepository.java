package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.SoldItem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.alphasoft.pos.database.SqlHelper.getQuery;

public class SoldItemRepository {

    private static SoldItemRepository repository;

    private SoldItemRepository(){}

    public List<SoldItem> getItems(LocalDate startDate,LocalDate endDate){
        List<SoldItem> soldItemList = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder(getQuery("sold.item.select.all"));
        List<Object> params = new ArrayList<>();

        if(null!=startDate){
            stringBuilder.append(" where sd.sale_date>=?");
            params.add(Date.valueOf(startDate));
            if(null!=endDate){
                stringBuilder.append(" and sd.sale_date<=?");
                params.add(endDate);
            }
        }else if(null!=endDate){
            stringBuilder.append(" where sd.sale_date<=?");
            params.add(endDate);
        }

        stringBuilder.append(" group by si.product_id");

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement soldItemStatement = connection.prepareStatement(stringBuilder.toString())
        ) {
            for(int i = 0;i<params.size();i++){
                soldItemStatement.setObject(i+1,params.get(i));
            }

            ResultSet resultSet = soldItemStatement.executeQuery();
            while (resultSet.next()){
                SoldItem soldItem = new SoldItem();
                soldItem.setProductId(resultSet.getInt("product_id"));
                soldItem.setProductName(resultSet.getString("product_name"));
                soldItem.setSoldAmount(resultSet.getInt("sold_amount"));
                soldItem.setSoldQuantity(resultSet.getInt("sold_quantity"));
                soldItemList.add(soldItem);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return soldItemList;
    }

    public static SoldItemRepository getRepository(){
        if(null==repository) repository = new SoldItemRepository();
        return repository;
    }
}
