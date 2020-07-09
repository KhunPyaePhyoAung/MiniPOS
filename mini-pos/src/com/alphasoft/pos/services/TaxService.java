package com.alphasoft.pos.services;

import com.alphasoft.pos.database.ConnectionManager;
import com.alphasoft.pos.models.TaxInfo;
import com.alphasoft.pos.repos.TaxRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.alphasoft.pos.utils.MySqlHelper.getQuery;

public class TaxService {
    private static TaxService service;

    private TaxService(){}

    public void saveTaxInfo(LocalDate startDate, int taxRate){
        boolean hasDate = TaxRepository.getRepository().getAllTaxInfos().stream().map(TaxInfo::getStartDate).anyMatch(i->i.equals(startDate));
        if(hasDate){
            updateTaxInfo(startDate,taxRate);
        }else {
            addTaxInfo(startDate,taxRate);
        }
    }

    public void removeTaxInfo(int id){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement deleteTaxStatement = connection.prepareStatement(getQuery("tax.delete"))
        ) {
            deleteTaxStatement.setInt(1,id);
            deleteTaxStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void addTaxInfo(LocalDate startDate,int taxRate){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement insertTaxStatement = connection.prepareStatement(getQuery("tax.insert"))
        ) {
            insertTaxStatement.setDate(1,Date.valueOf(startDate));
            insertTaxStatement.setInt(2,taxRate);
            insertTaxStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateTaxInfo(LocalDate startDate,int taxRate){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement updateTaxInfo = connection.prepareStatement(getQuery("tax.update"))
        ) {
            updateTaxInfo.setInt(1,taxRate);
            updateTaxInfo.setDate(2,Date.valueOf(startDate));
            updateTaxInfo.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public static TaxService getService(){
        if(null==service) service = new TaxService();
        return service;
    }
}
