package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.Payment;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.models.SaleDetail;
import com.alphasoft.pos.models.SaleItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.alphasoft.pos.database.SqlHelper.getQuery;

public class SaleRepository {
    private static SaleRepository repository;

    private SaleRepository(){}

    public List<Sale> getAllSales(){
            List<Sale> saleList = new ArrayList<>();
            try (Connection connection = ConnectionManager.getConnection();
                 PreparedStatement selectAllSaleDetails = connection.prepareStatement(getQuery("sale_detail.select.all"));
                 PreparedStatement selectSaleItems = connection.prepareStatement(getQuery("sale_item.select.bySaleId"));
                 PreparedStatement selectPayments = connection.prepareStatement(getQuery("payment.select.bySaleId"))
            ){
                ResultSet saleDetails = selectAllSaleDetails.executeQuery();

                while (saleDetails.next()){
                    Sale sale = new Sale();
                    SaleDetail saleDetail = sale.getSaleDetail();
                    saleDetail.setId(saleDetails.getInt("id"));
                    saleDetail.setSaleDate(saleDetails.getDate("sale_date").toLocalDate());
                    saleDetail.setSaleTime(saleDetails.getTime("sale_time").toLocalTime());
                    saleDetail.setSalePersonId(saleDetails.getInt("sale_person_id"));
                    saleDetail.setSalePersonName(saleDetails.getString("cashier"));
                    saleDetail.setQuantity(saleDetails.getInt("quantity"));
                    saleDetail.setSubTotal(saleDetails.getInt("sub_total"));
                    saleDetail.setTaxRate(saleDetails.getInt("tax_rate"));
                    saleDetail.setPaid(saleDetails.getBoolean("paid"));


                    selectSaleItems.setInt(1,saleDetails.getInt("id"));
                    ResultSet saleItems = selectSaleItems.executeQuery();

                    while (saleItems.next()){
                        if(saleItems.getInt("sale_id")==saleDetail.getId()){
                            SaleItem saleItem = new SaleItem();
                            saleItem.setSaleId(saleItems.getInt("sale_id"));
                            saleItem.setProductId(saleItems.getInt("product_id"));
                            saleItem.setProductName(saleItems.getString("product_name"));
                            saleItem.setCategoryId(saleItems.getInt("category_id"));
                            saleItem.setCategoryName(saleItems.getString("category_name"));
                            saleItem.setSaleDate(saleItems.getDate("sale_date").toLocalDate());
                            saleItem.setPrice(saleItems.getInt("price"));
                            saleItem.setQuantity(saleItems.getInt("quantity"));
                            saleItem.setTaxRate(saleItems.getInt("tax_rate"));
                            sale.getSaleItemList().add(saleItem);
                        }
                    }

                    selectPayments.setInt(1,saleDetails.getInt("id"));
                    ResultSet payments = selectPayments.executeQuery();

                    while (payments.next()){
                        if(payments.getInt("sale_id")==saleDetail.getId()){
                            Payment payment = sale.getPayment();
                            payment.setSaleId(payments.getInt("sale_id"));
                            payment.subTotalProperty().set(saleDetail.getSubTotal());
                            payment.taxRateProperty().set(saleDetail.getTaxRate());
                            payment.discountPercentProperty().set(payments.getInt("discount_percent"));
                            payment.discountCashProperty().set(payments.getInt("discount_cash"));
                            payment.tenderedProperty().set(payments.getInt("tendered"));
                        }
                    }
                    saleList.add(sale);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return saleList;
    }

    public Sale get(int saleId){
        return getAllSales().stream().filter(i->i.getSaleDetail().getId()==saleId).findAny().orElse(null);
    }

    public static SaleRepository getRepository(){
        if(null==repository) repository = new SaleRepository();
        return repository;
    }

}
