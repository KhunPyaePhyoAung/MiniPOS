package com.alphasoft.pos.services;

import com.alphasoft.pos.contexts.ConnectionManager;
import com.alphasoft.pos.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.alphasoft.pos.contexts.SqlHelper.getQuery;

public class AccountService {

    private static AccountService accountService;

    private AccountService(){

    }

    public Account login(String loginId,String password){

        Account account = findAccountByLoginId(loginId);



        return account;
    }

    private Account findAccountByLoginId(String loginId){
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getQuery(""))
        ) {
            preparedStatement.setString(1,loginId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return getAccount(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private Account getAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setLoginId(resultSet.getString("login_id"));
        account.setPassword(resultSet.getString("password"));
        account.setName(resultSet.getString("name"));
        account.setPhone(resultSet.getString("phone"));
        account.setEmail(resultSet.getString("email"));
        return account;
    }

    public AccountService getService(){
        if(null == accountService) accountService = new AccountService();
        return accountService;
    }
}
