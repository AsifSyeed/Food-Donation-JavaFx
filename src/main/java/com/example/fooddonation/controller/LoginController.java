package com.example.fooddonation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
public class LoginController {
    @FXML
    private Button btnLogin;

    @FXML
    private TextField fieldUsername;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Label labelLoginMessage;

    public void loginButtonAction(ActionEvent event) {
        if (!fieldUsername.getText().isBlank() && !fieldPassword.getText().isBlank()) {
            validateLogin();
        } else {
            labelLoginMessage.setText("Please enter valid username and password");
        }
    }

    public void validateLogin() {
        labelLoginMessage.setText("You try to login");
    }
}
