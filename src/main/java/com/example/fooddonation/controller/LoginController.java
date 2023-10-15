package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import com.example.fooddonation.model.enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;

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

    @FXML
    private ImageView imageViewLogo;

    @FXML
    private Label labelTitle;

    @FXML
    private ImageView imageViewBack;

    @FXML
    private Label labelSignUp;

    private int userType;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void userTypeValue(int value) {
        setUserType(value);
        setLogo();
    }

    private void setLogo() {
        if (getUserType() == UserType.DONOR.getValue()) {
            Image imageDonate = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/donate.png"));
            imageViewLogo.setImage(imageDonate);
            labelTitle.setText("Donor Login");
        } else if (getUserType() == UserType.DELIVERY_BOY.getValue()) {
            Image imageDelivery = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/delivery.png"));
            imageViewLogo.setImage(imageDelivery);
            labelTitle.setText("Delivery Boy Login");
        } else {
            btnSignup.setVisible(false);
            labelSignUp.setVisible(false);

            if (getUserType() == UserType.ADMIN.getValue()) {
                Image imageAdmin = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/admin.png"));
                imageViewLogo.setImage(imageAdmin);
                labelTitle.setText("Admin Login");
            } else if (getUserType() == UserType.AGENT.getValue()) {
                Image imageAgent = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/agent.png"));
                imageViewLogo.setImage(imageAgent);
                labelTitle.setText("Agent Login");
            }
        }

        Image imageBack = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/back.png"));
        imageViewBack.setImage(imageBack);
    }

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
        String loginQuery = "SELECT username FROM user_account WHERE username = ? AND password = ? AND user_type = ?";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(loginQuery)
        ) {
            preparedStatement.setString(1, fieldUsername.getText());
            preparedStatement.setString(2, fieldPassword.getText());
            preparedStatement.setInt(3, getUserType());
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                labelLoginMessage.setText("Login successful");
                String retrievedUsername = queryResult.getString("username");

                if (getUserType() == UserType.DONOR.getValue()) {
                    navigateToDashboard(retrievedUsername);
                } else {
                    navigateToAdminDashboard(retrievedUsername);
                }
            } else {
                labelLoginMessage.setText("Please enter valid username and password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void navigateToAdminDashboard(String retrievedUsername) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/admin-dashboard.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            AdminDashboardController adminDashboardController = loader.getController();
            adminDashboardController.displayName(retrievedUsername, getUserType());

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
            registerStage.initStyle(StageStyle.DECORATED);
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/signup.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            SignupController signupController = loader.getController();
            signupController.passUserType(getUserType());
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void backButtonAction() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/fooddonation/landing-page.fxml"));

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void forgotPasswordAction() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/forgot-password.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            ForgotPasswordController forgotPasswordController = loader.getController();
            forgotPasswordController.userTypeValue(getUserType());

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
