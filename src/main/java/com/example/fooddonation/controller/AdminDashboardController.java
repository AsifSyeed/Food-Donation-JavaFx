package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import com.example.fooddonation.model.DonationRequest;
import com.example.fooddonation.model.enums.DonationStatus;
import com.example.fooddonation.model.enums.UserType;
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

public class AdminDashboardController implements Initializable {
    @FXML
    private Label labelUsername;

    @FXML
    private Button btnLogout;

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

    @FXML
    private TableColumn<DonationRequest, String> colRequestedBy;

    @FXML
    private TableColumn<DonationRequest, String> colAgent;

    @FXML
    private TableColumn<DonationRequest, String> colDeliveryBoy;

    @FXML
    private Button btnAddAgent;

    private String username;
    private int userType;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    ObservableList<DonationRequest> donationList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTag.setCellValueFactory(new PropertyValueFactory<DonationRequest, String>("donationTag"));
        colFoodName.setCellValueFactory(new PropertyValueFactory<DonationRequest, String>("foodName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<DonationRequest, Integer>("foodQuantity"));
        colStatus.setCellValueFactory(new PropertyValueFactory<DonationRequest, Integer>("donationStatus"));
        colRequestedBy.setCellValueFactory(new PropertyValueFactory<DonationRequest, String>("requestedBy"));
        colAgent.setCellValueFactory(new PropertyValueFactory<DonationRequest, String>("agent"));
        colDeliveryBoy.setCellValueFactory(new PropertyValueFactory<DonationRequest, String>("deliveryBoy"));
    }

    private ObservableList<DonationRequest> getDonationByCurrentUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        String getDonationListQuery = "";

        if (getUserType() == UserType.ADMIN.getValue()) {
            getDonationListQuery = "SELECT * FROM donation_request";
        } else if (getUserType() == UserType.AGENT.getValue()) {
            getDonationListQuery = "SELECT * FROM donation_request WHERE agent = ?";
        } else if (getUserType() == UserType.DELIVERY_BOY.getValue()) {
            getDonationListQuery = "SELECT * FROM donation_request WHERE delivery_boy = ?";
        }

        ObservableList<DonationRequest> dataList = FXCollections.observableArrayList();

        try (Connection connectDB = connectNow.getConnection();
             PreparedStatement preparedStatement = connectDB.prepareStatement(getDonationListQuery)) {

            if (getUserType() == UserType.AGENT.getValue() || getUserType() == UserType.DELIVERY_BOY.getValue()) {
                preparedStatement.setString(1, getUsername());
            }

            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                dataList.add(new DonationRequest(
                        queryResult.getString("donation_tag"),
                        queryResult.getInt("food_quantity"),
                        DonationStatus.getStringValue(queryResult.getInt("donation_status")),
                        queryResult.getString("food_name"),
                        queryResult.getString("requested_by"),
                        queryResult.getString("agent"),
                        queryResult.getString("delivery_boy")
                ));
            }

            return dataList;

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return dataList;
    }

    public void displayName(String username, int userType) {
        setUsername(username);
        setUserType(userType);
        labelUsername.setText("Username: " + username);

        donationList = getDonationByCurrentUser();
        tableRequest.setItems(donationList);

        if (getUserType() != UserType.ADMIN.getValue()) {
            btnAddAgent.setVisible(false);
        }
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

    //Mouse event handlers for tableView items
    public void getDonationItem() {
        String requestTag = tableRequest.getSelectionModel().getSelectedItem().getDonationTag();
        System.out.println("Clicked on " + requestTag);

        if (requestTag != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/admin-update-request.fxml"));
                Parent root = loader.load();

                Stage registerStage = new Stage();
                registerStage.initStyle(StageStyle.DECORATED);
                registerStage.setScene(new Scene(root, 800, 600));
                registerStage.show();

                AdminUpdateRequestController adminUpdateRequestController = loader.getController();
                adminUpdateRequestController.displayRequestTag(requestTag, getUsername(), getUserType());

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    public void addAgentAction(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/signup.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            SignupController signupController = loader.getController();
            signupController.passUserType(UserType.AGENT.getValue());

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}

