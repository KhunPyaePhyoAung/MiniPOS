package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.Account;

public class Logger {
    private static Logger logger;
    private static Account loggedAccount;

    private Logger(){}

    public void setLoggedAccount(Account account){
        loggedAccount = account;
    }

    public Account getLoggedAccount(){
        return loggedAccount;
    }

    public static Logger getLogger(){
        if(null == logger) logger = new Logger();
        return logger;
    }
}
