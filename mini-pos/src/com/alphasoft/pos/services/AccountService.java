package com.alphasoft.pos.services;

import com.alphasoft.pos.models.Account;

public class AccountService {

    private static AccountService accountService;
    private AccountService(){

    }

    public void login(Account account){

    }

    public AccountService getService(){
        if(null == accountService) accountService = new AccountService();
        return accountService;
    }
}
