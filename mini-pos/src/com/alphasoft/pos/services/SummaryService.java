package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.Summary;
import com.alphasoft.pos.repos.TaxRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.alphasoft.pos.utils.MySqlHelper.getQuery;

public class SummaryService {
    private static SummaryService service;

    private SummaryService(){}

    public Summary getSummary(){
        Summary summary = new Summary();

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement statementForSalesForToday = connection.prepareStatement(getQuery("sales.for.today"));
            PreparedStatement statementForUnpaid = connection.prepareStatement(getQuery("sales.unpaid"));
            PreparedStatement statementForAvailableProducts = connection.prepareStatement(getQuery("products.select.available"))
                ) {
            ResultSet salesForTodayResultSet = statementForSalesForToday.executeQuery();
            ResultSet unpaidResultSet = statementForUnpaid.executeQuery();
            ResultSet availableProductsResultSet = statementForAvailableProducts.executeQuery();

            if(salesForTodayResultSet.next()){
                summary.setSalesForToday(salesForTodayResultSet.getInt("sales_for_today"));
            }

            if(unpaidResultSet.next()){
                summary.setUnpaid(unpaidResultSet.getInt("unpaid"));
            }

            if(availableProductsResultSet.next()){
                summary.setAvailableProducts(availableProductsResultSet.getInt("available_product_count"));
            }

            summary.setTaxRate(TaxRepository.getRepository().getTaxRate(LocalDate.now()));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return summary;
    }

    public static SummaryService getService(){
        if(null==service) service = new SummaryService();
        return service;
    }
}
