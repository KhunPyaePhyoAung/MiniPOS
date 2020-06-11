package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.Payment;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.models.SaleDetail;
import com.alphasoft.pos.models.SaleItem;

import java.sql.*;
import java.util.List;

import static com.alphasoft.pos.database.SqlHelper.getQuery;

public class SaleService {
    private static SaleService service;

    private SaleService(){}

    public void save(Sale sale,boolean paid){
        if(sale.getSaleDetail().getId()==0){
            insertSale(sale,paid);
        }else{
            updateSale(sale,paid);
        }
    }

    private void insertSale(Sale sale,boolean paid){
        SaleDetail saleDetail = sale.getSaleDetail();
        List<SaleItem> saleItemList = sale.getSaleItemList();
        Payment payment = sale.getPayment();

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement insertSaleDetail = connection.prepareStatement(getQuery("sale_detail.insert"), Statement.RETURN_GENERATED_KEYS);
            PreparedStatement insertSaleItem = connection.prepareStatement(getQuery("sale_item.insert"));
            PreparedStatement insertPayment = connection.prepareStatement(getQuery("payment.insert"))
        ) {
            insertSaleDetail.setInt(1,saleDetail.getSalePersonId());
            insertSaleDetail.setDate(2,Date.valueOf(saleDetail.getSaleDate()));
            insertSaleDetail.setTime(3,Time.valueOf(saleDetail.getSaleTime()));
            insertSaleDetail.setInt(4,saleDetail.getTaxRate());
            insertSaleDetail.setBoolean(5,paid);
            insertSaleDetail.executeUpdate();
            ResultSet resultSet = insertSaleDetail.getGeneratedKeys();

            if(resultSet.next()){
                int saleId = resultSet.getInt(1);
                for (SaleItem saleItem : saleItemList) {
                    insertSaleItem.setInt(1, saleId);
                    insertSaleItem.setInt(2, saleItem.getProductId());
                    insertSaleItem.setInt(3, saleItem.getPrice());
                    insertSaleItem.setInt(4, saleItem.getQuantity());
                    insertSaleItem.addBatch();
                }
                insertSaleItem.executeBatch();

                insertPayment.setInt(1,saleId);
                insertPayment.setInt(2,payment.discountPercentProperty().get());
                insertPayment.setInt(3,payment.discountCashProperty().get());
                insertPayment.setInt(4,payment.tenderedProperty().get());
                insertPayment.executeUpdate();

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    private void updateSale(Sale sale,boolean paid){

    }

    public static SaleService getService(){
        if(null == service) service = new SaleService();
        return service;
    }

}
