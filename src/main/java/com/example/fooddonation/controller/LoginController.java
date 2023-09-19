package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
        labelLoginMessage.setText("You try to login");
        if (!fieldUsername.getText().isBlank() && !fieldPassword.getText().isBlank()) {
            validateLogin();
        } else {
            labelLoginMessage.setText("Please enter valid username and password");
        }
    }

    public void validateLogin() {

        DatabaseConnection connectNow = new DatabaseConnection();

        Connection connectDB = connectNow.getConnection();

        String loginQuery = "SELECT count(1) FROM user_account WHERE username = '" + fieldUsername.getText() + "' AND password = '" + fieldPassword.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(loginQuery);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    labelLoginMessage.setText("Login successful");
                } else {
                    labelLoginMessage.setText("Please enter valid username and password");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
