package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.contexts.Logger;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.Account;
import com.alphasoft.pos.services.AccountService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginFormController{
    @FXML
    private Label message;

    @FXML
    private TextField loginIdInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    void close() {
        message.getScene().getWindow().hide();
    }

    @FXML
    void login() {
        try{
            Account account = AccountService.getService().login(loginIdInput.getText(),passwordInput.getText());
            Logger.getLogger().setLoggedAccount(account);

        }catch (PosException exception){
            message.setText(exception.getMessage());
        }

    }
}