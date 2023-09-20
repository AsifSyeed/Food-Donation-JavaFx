package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class SignupController implements Initializable {
    @FXML
    private TextField fieldUsername;

    @FXML
    private TextField fieldContactNumber;

    @FXML
    private ComboBox<String> dropdownLocation;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private PasswordField fieldConfirmPassword;

    @FXML
    private Button btnSignup;

    @FXML
    private Button btnLogin;

    @FXML
    private Label labelSignupMessage;

    private Map<String, String> locationCodeMap = new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getLocationList();
    }
    public void signupButtonAction(ActionEvent event) {
        if (fieldUsername.getText().isBlank()) {
            labelSignupMessage.setText("Username cannot be empty");
        } else if (fieldContactNumber.getText().isBlank()) {
            labelSignupMessage.setText("Contact number cannot be empty");
        } else if (fieldPassword.getText().isBlank()) {
            labelSignupMessage.setText("Password cannot be empty");
        } else if (fieldConfirmPassword.getText().isBlank()) {
            labelSignupMessage.setText("Confirm Password cannot be empty");
        } else if (dropdownLocation.getSelectionModel().getSelectedItem().isBlank()) {
            labelSignupMessage.setText("Location cannot be empty");
        } else {
            validateSignup();
        }
    }

    private void validateSignup() {
        if (!Objects.equals(fieldPassword.getText(), fieldConfirmPassword.getText())) {
            labelSignupMessage.setText("Password and confirm password must be same");
        } else {
            if (isUniqueUsername() && isUniqueContactNumber()) {
                createNewUser();
            }
        }
    }

    private void createNewUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        String signUpQuery = "INSERT INTO user_account (username, contact_number, password, user_type, location) VALUES (?, ?, ?, ?, ?)";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(signUpQuery)
        ) {
            preparedStatement.setString(1, fieldUsername.getText());
            preparedStatement.setString(2, fieldContactNumber.getText());
            preparedStatement.setString(3, fieldPassword.getText());
            preparedStatement.setInt(4, 1);

            String selectedLocationName = dropdownLocation.getSelectionModel().getSelectedItem();
            preparedStatement.setString(5, locationCodeMap.get(selectedLocationName));

            int queryResult = preparedStatement.executeUpdate();

            if (queryResult > 0) {
                labelSignupMessage.setText("Sign up successful!. Please login with your user account");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            labelSignupMessage.setText("Sign up failed. Please try again later!");
        }
    }

    private boolean isUniqueContactNumber() {
        DatabaseConnection connectNow = new DatabaseConnection();
        String checkContactNumberQuery = "SELECT COUNT(*) FROM user_account WHERE contact_number = ?";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(checkContactNumberQuery)) {

            preparedStatement.setString(1, fieldContactNumber.getText());
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next() && queryResult.getInt(1) > 0) {
                labelSignupMessage.setText("Contact number already exists");
                return false;
            } else {
                System.out.println("Contact number is unique");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            labelSignupMessage.setText("Invalid information");
            return false;
        }
    }

    private boolean isUniqueUsername() {
        DatabaseConnection connectNow = new DatabaseConnection();
        String checkUsernameQuery = "SELECT COUNT(*) FROM user_account WHERE username = ?";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(checkUsernameQuery)) {

            preparedStatement.setString(1, fieldUsername.getText());
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next() && queryResult.getInt(1) > 0) {
                labelSignupMessage.setText("Username already exists");
                return false;
            } else {
                System.out.println("Username is unique");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            labelSignupMessage.setText("Invalid information");
            return false;
        }
    }

    private void getLocationList() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String locationQuery = "SELECT location_name, location_code FROM location";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(locationQuery);

            while (queryResult.next()) {
                String locationName = queryResult.getString("location_name");
                String locationCode = queryResult.getString("location_code");

                // Populate the ComboBox with location names
                dropdownLocation.getItems().add(locationName);

                // Store the location code in the map
                locationCodeMap.put(locationName, locationCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void loginButtonAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/fooddonation/login.fxml"));

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 520, 400));
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
