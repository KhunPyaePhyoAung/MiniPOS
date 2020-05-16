package com.alphasoft.pos.services;

import com.alphasoft.pos.models.Account;

public class AccountService {

    private static AccountService accountService;

    private AccountService(){

    }

    public Account login(String loginId,String password){
        Account account = new Account();


        return account;
    }

    private Account findAccountByLoginId(){

        return null;
    }

    public AccountService getService(){
        if(null == accountService) accountService = new AccountService();
        return accountService;
    }
}
