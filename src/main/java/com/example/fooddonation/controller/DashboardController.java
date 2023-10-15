package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import com.example.fooddonation.model.DonationRequest;
import com.example.fooddonation.model.enums.DonationStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label labelUsername;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnNewRequest;

    @FXML
    private TableView<DonationRequest> tableRequest;

    @FXML
    private TableColumn<DonationRequest, String> colTag;

    @FXML
    private TableColumn<DonationRequest, String> colFoodName;

    @FXML
    private TableColumn<DonationRequest, Integer> colQuantity;

    @FXML
    private TableColumn<DonationRequest, Integer> colStatus;

    private String username;
    ObservableList<DonationRequest> donationList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTag.setCellValueFactory(new PropertyValueFactory<DonationRequest, String>("donationTag"));
        colFoodName.setCellValueFactory(new PropertyValueFactory<DonationRequest, String>("foodName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<DonationRequest, Integer>("foodQuantity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<DonationRequest, Integer>("donationStatus"));

        donationList = getDonationByCurrentUser();
        tableRequest.setItems(donationList);
    }

    private ObservableList<DonationRequest> getDonationByCurrentUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        String getDonationListQuery = "SELECT * FROM donation_request WHERE requested_by = ?";
        ObservableList<DonationRequest> dataList = FXCollections.observableArrayList();

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(getDonationListQuery)) {

            preparedStatement.setString(1, username);
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                dataList.add(new DonationRequest(
                        queryResult.getString("donation_tag"),
                        queryResult.getInt("food_quantity"),
                        DonationStatus.getStringValue(queryResult.getInt("donation_status")),
                        queryResult.getString("food_name"),
                        queryResult.getString("requested_by"),
                        "",
                        ""
                ));
            }

            return dataList;

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return dataList;
    }

    public void displayName(String username) {
        setUsername(username);
        labelUsername.setText("Username: " + username);

        donationList = getDonationByCurrentUser();
        tableRequest.setItems(donationList);
    }

    public void logoutButtonAction(ActionEvent event) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void navigateToNewRequestPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/new-request.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            NewRequestController newRequestController = loader.getController();
            newRequestController.displayName(getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
