package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    @FXML
    private Button btnSignup;

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
        String loginQuery = "SELECT username FROM user_account WHERE username = ? AND password = ?";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(loginQuery)
        ) {
            preparedStatement.setString(1, fieldUsername.getText());
            preparedStatement.setString(2, fieldPassword.getText());
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                labelLoginMessage.setText("Login successful");
                String retrievedUsername = queryResult.getString("username");
                navigateToDashboard(retrievedUsername);
            } else {
                labelLoginMessage.setText("Please enter valid username and password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void navigateToDashboard(String retrievedUsername) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/dashboard.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            DashboardController dashboardController = loader.getController();
            dashboardController.displayName(retrievedUsername);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void signupButtonAction(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/fooddonation/signup.fxml"));

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
