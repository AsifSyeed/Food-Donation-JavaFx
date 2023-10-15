package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
    @FXML
    private ImageView imageViewBack;

    @FXML
    private TextField fieldUsername;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private PasswordField fieldConfirmPassword;

    @FXML
    private Label labelAlertMessage;

    private int userType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image imageBack = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/back.png"));
        imageViewBack.setImage(imageBack);
    }

    public void backButtonAction() {
        Stage stage = (Stage) imageViewBack.getScene().getWindow();
        stage.close();
    }

    public void userTypeValue(int value) {
        setUserType(value);
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void resetPasswordAction() {
        String username = fieldUsername.getText();
        String password = fieldPassword.getText();
        String confirmPassword = fieldConfirmPassword.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            labelAlertMessage.setText("Please fill all the fields");
        } else if (!password.equals(confirmPassword)) {
            labelAlertMessage.setText("Password and Confirm Password do not match");
        } else {
            checkUsername(username, getUserType());
        }
    }

    private void checkUsername(String username, int userType) {
        try {
            //Check if the username exists with the given user type
            DatabaseConnection connectNow = new DatabaseConnection();
            String query = "SELECT username FROM user_account WHERE username = ? AND user_type = ?";

            try (Connection connectDb = connectNow.getConnection()) {
                PreparedStatement statement = connectDb.prepareStatement(query);
                statement.setString(1, username);
                statement.setInt(2, userType);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    updatePassword(username);
                } else {
                    labelAlertMessage.setText("Username does not exist");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePassword(String username) {
        //Update the password for the given username
        DatabaseConnection connectNow = new DatabaseConnection();
        String query = "UPDATE user_account SET password = ? WHERE username = ?";

        try (Connection connectDb = connectNow.getConnection()) {
            PreparedStatement statement = connectDb.prepareStatement(query);
            statement.setString(1, fieldPassword.getText());
            statement.setString(2, username);

            int queryResult = statement.executeUpdate();
            if (queryResult > 0) {
                labelAlertMessage.setText("Password updated successfully");
            } else {
                labelAlertMessage.setText("Password update failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
