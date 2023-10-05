package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class NewRequestController implements Initializable {
    @FXML
    private TextField fieldFoodName;

    @FXML
    private TextField fieldFoodQuantity;

    @FXML
    private  TextField fieldDonorLocation;

    @FXML
    private Button btnNewRequest;

    @FXML
    private Label labelNewRequestMessage;

    @FXML
    private ImageView imageViewBack;

    private String username;

    private String userLocationCode;

    public void displayName(String username) {
        setUsername(username);
        getUserLocation();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUserLocation();
        Image imageBack = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/back.png"));
        imageViewBack.setImage(imageBack);
    }

    private void getUserLocation() {
        DatabaseConnection connectNow = new DatabaseConnection();
        String locationQuery = "SELECT location FROM user_account WHERE username = ?";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(locationQuery)) {

            preparedStatement.setString(1, getUsername());
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                setUserLocationCode(queryResult.getString("location"));
                getLocationString(getUserLocationCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void getLocationString(String location) {
        DatabaseConnection connectNow = new DatabaseConnection();
        String locationQuery = "SELECT location_name FROM location WHERE location_code = ?";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(locationQuery)) {

            preparedStatement.setString(1, location);
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                fieldDonorLocation.setText(queryResult.getString("location_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public String getUserLocationCode() {
        return userLocationCode;
    }

    public void setUserLocationCode(String userLocationCode) {
        this.userLocationCode = userLocationCode;
    }

    public void newRequestAction() {
        DatabaseConnection connectNow = new DatabaseConnection();
        String newRequestQuery = "INSERT INTO donation_request (donation_tag, food_name, food_quantity, requested_by, donation_status, donor_location) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(newRequestQuery)) {

            String donationTag = generateDonationTag();
            //Check if the donation tag is unique in database
            while (!isUniqueDonationTag(donationTag)) {
                donationTag = generateDonationTag();
            }

            preparedStatement.setString(1, donationTag);
            preparedStatement.setString(2, fieldFoodName.getText());
            preparedStatement.setInt(3, Integer.parseInt(fieldFoodQuantity.getText()));
            preparedStatement.setString(4, getUsername());
            preparedStatement.setInt(5, 1);
            preparedStatement.setString(6, getUserLocationCode());

            int queryResult = preparedStatement.executeUpdate();

            if (queryResult > 0) {
                labelNewRequestMessage.setText("New Request added");
                navigateToDashboard();
            } else {
                labelNewRequestMessage.setText("New request cannot be added. Please try again later");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private boolean isUniqueDonationTag(String donationTag) {
        DatabaseConnection connectNow = new DatabaseConnection();
        String checkDonationTagQuery = "SELECT COUNT(*) FROM donation_request WHERE donation_tag = ?";

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(checkDonationTagQuery)) {

            preparedStatement.setString(1, donationTag);
            ResultSet queryResult = preparedStatement.executeQuery();

            return !queryResult.next() || queryResult.getInt(1) <= 0;
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            return false;
        }
    }

    private String generateDonationTag() {
        //Generate donation tag which will be 2 Alphabets followed by 4 digits
        StringBuilder donationTag = new StringBuilder();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";

        //Generate 2 random alphabets
        for (int i = 0; i < 2; i++) {
            donationTag.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }

        //Generate 4 random numbers
        for (int i = 0; i < 4; i++) {
            donationTag.append(numbers.charAt((int) (Math.random() * numbers.length())));
        }

        return donationTag.toString();
    }

    public void navigateToDashboard() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/dashboard.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            DashboardController dashboardController = loader.getController();
            dashboardController.displayName(getUsername());

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
