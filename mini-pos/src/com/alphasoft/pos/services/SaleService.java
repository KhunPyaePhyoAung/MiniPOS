package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.Payment;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.models.SaleDetail;
import com.alphasoft.pos.models.SaleItem;

import java.sql.*;
import java.util.List;

import static com.alphasoft.pos.utils.MySqlHelper.getQuery;

public class SaleService {
    private static SaleService service;

    private SaleService(){}

    public void save(Sale sale){
        if(sale.getSaleDetail().getId()==0){
            insertSale(sale);
        }else{
            updateSale(sale);
        }
    }

    private void insertSale(Sale sale){
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
            insertSaleDetail.setBoolean(5,saleDetail.isPaid());
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

    private void updateSale(Sale sale){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement updateSaleDetail = connection.prepareStatement(getQuery("sale_detail.update.byId"));
            PreparedStatement deleteSaleDetail = connection.prepareStatement(getQuery("sale_item.delete.bySaleId"));
            PreparedStatement insertSaleItem = connection.prepareStatement(getQuery("sale_item.insert"));
            PreparedStatement updatePayment = connection.prepareStatement(getQuery("payment.update.bySaleId"))

        ) {
            SaleDetail saleDetail = sale.getSaleDetail();
            Payment payment = sale.getPayment();

            updateSaleDetail.setInt(1,saleDetail.getSalePersonId());
            updateSaleDetail.setDate(2,Date.valueOf(saleDetail.getSaleDate()));
            updateSaleDetail.setTime(3,Time.valueOf(saleDetail.getSaleTime()));
            updateSaleDetail.setInt(4,saleDetail.getTaxRate());
            updateSaleDetail.setBoolean(5,saleDetail.isPaid());
            updateSaleDetail.setInt(6,saleDetail.getId());
            updateSaleDetail.executeUpdate();

            deleteSaleDetail.setInt(1,saleDetail.getId());
            deleteSaleDetail.executeUpdate();

            for(SaleItem saleItem:sale.getSaleItemList()){
                insertSaleItem.setInt(1,saleDetail.getId());
                insertSaleItem.setInt(2,saleItem.getProductId());
                insertSaleItem.setInt(3,saleItem.getPrice());
                insertSaleItem.setInt(4,saleItem.getQuantity());
                insertSaleItem.addBatch();
            }
            insertSaleItem.executeBatch();

            updatePayment.setInt(1,payment.discountPercentProperty().get());
            updatePayment.setInt(2,payment.discountCashProperty().get());
            updatePayment.setInt(3,payment.tenderedProperty().get());
            updatePayment.setInt(4,saleDetail.getId());
            updatePayment.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void remove(Sale sale){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement deleteSaleDetail = connection.prepareStatement(getQuery("sale_detail.delete.byId"));
            PreparedStatement deleteSaleItem = connection.prepareStatement(getQuery("sale_item.delete.bySaleId"));
            PreparedStatement deletePayment = connection.prepareStatement(getQuery("payment.delete.bySaleId"))
        ) {
            int saleId = sale.getSaleDetail().getId();

            deleteSaleDetail.setInt(1,saleId);
            deleteSaleItem.setInt(1,saleId);
            deletePayment.setInt(1,saleId);

            deleteSaleDetail.executeUpdate();
            deleteSaleItem.executeUpdate();
            deletePayment.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static SaleService getService(){
        if(null == service) service = new SaleService();
        return service;
    }

}
