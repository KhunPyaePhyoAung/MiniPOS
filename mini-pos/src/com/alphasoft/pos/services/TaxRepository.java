package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.TaxInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.alphasoft.pos.database.SqlHelper.getQuery;

public class TaxRepository {
    private static TaxRepository repository;

    private TaxRepository(){}


    public List<TaxInfo> getAllTaxInfos(){
        List<TaxInfo> taxInfoList = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery("tax.select.all"))
        ){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                taxInfoList.add(parseTaxInfoFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return taxInfoList;
    }

    public int getTaxRate(LocalDate startDate){
        return getAllTaxInfos().stream().filter(i->i.getStartDate().compareTo(startDate)<=0).max(Comparator.comparing(TaxInfo::getStartDate)).map(TaxInfo::getTaxRate).orElse(0);
    }

    private TaxInfo parseTaxInfoFromResultSet(ResultSet resultSet) throws SQLException {
        TaxInfo taxInfo = new TaxInfo();
        taxInfo.setId(resultSet.getInt("id"));
        taxInfo.setStartDate(resultSet.getDate("start_date").toLocalDate());
        taxInfo.setTaxRate(resultSet.getInt("tax_rate"));
        return taxInfo;
    }

    public static TaxRepository getRepository(){
        if(null==repository) repository = new TaxRepository();
        return repository;
    }
}
