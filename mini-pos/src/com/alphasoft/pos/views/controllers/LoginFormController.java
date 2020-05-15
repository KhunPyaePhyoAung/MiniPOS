package com.alphasoft.pos.views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginFormController{
    @FXML
    private Label message;

    @FXML
    private TextField loginIdInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    void close(MouseEvent event) {
        message.getScene().getWindow().hide();
    }

    @FXML
    void login(MouseEvent event) {

    }
}