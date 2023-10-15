package com.example.fooddonation.controller;

import com.example.fooddonation.DatabaseConnection;
import com.example.fooddonation.model.enums.DonationStatus;
import com.example.fooddonation.model.enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class AdminUpdateRequestController {

    @FXML
    private Button btnUpdateRequest;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField fieldRequestTag;

    @FXML
    private TextField fieldRequestedBy;

    @FXML
    private TextField fieldLocation;

    @FXML
    private ComboBox<String> dropdownStatus;

    @FXML
    private ComboBox<String> dropdownAgent;

    @FXML
    private ComboBox<String> dropdownDeliveryBoy;

    @FXML
    private Label labelAlertMessage;

    private String requestTag;

    private final ArrayList<DonationStatus> donationStatusArrayList = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(String requestTag) {
        this.requestTag = requestTag;
    }

    //Function to dismiss the view when the user clicks on the button
    public void dismissView() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/admin-dashboard.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            AdminDashboardController adminDashboardController = loader.getController();
            adminDashboardController.displayName(getUsername());

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void cancelButtonAction(ActionEvent event) {
        dismissView();
    }

    public void displayRequestTag(String requestTag) {
        setUsername(username);
        setRequestTag(requestTag);
        fieldRequestTag.setText(requestTag);

        getRequestInformation();
        getDonationStatusList();
    }

    public void updatedRequestAction(ActionEvent event) {
        validateUpdateRequest();
    }

    private void validateUpdateRequest() {

        if (dropdownStatus.getSelectionModel().getSelectedItem() == null) {
            labelAlertMessage.setText("Status must be updated");
        } else if (dropdownStatus.getSelectionModel().getSelectedItem().equals(DonationStatus.AGENT_ASSIGNED.getStringValue()) && dropdownAgent.getSelectionModel().getSelectedItem() == null) {
            labelAlertMessage.setText("Agent must be selected");
        } else if (dropdownStatus.getSelectionModel().getSelectedItem().equals(DonationStatus.DELIVERY_BOY_ASSIGNED.getStringValue()) && dropdownDeliveryBoy.getSelectionModel().getSelectedItem() == null) {
            labelAlertMessage.setText("Delivery boy must be selected");
        } else {
            updateRequest();
        }
    }

    private void updateRequest() {
        DatabaseConnection connectNow = new DatabaseConnection();
        //Update query to update the request status
        String updateQuery = "";

        if (dropdownStatus.getSelectionModel().getSelectedItem().equals(DonationStatus.AGENT_ASSIGNED.getStringValue())) {
            updateQuery = "UPDATE donation_request SET donation_status = ?, agent = ? WHERE donation_tag = ?";
        } else if (dropdownStatus.getSelectionModel().getSelectedItem().equals(DonationStatus.DELIVERY_BOY_ASSIGNED.getStringValue())) {
            updateQuery = "UPDATE donation_request SET donation_status = ?, delivery_boy = ? WHERE donation_tag = ?";
        } else {
            updateQuery = "UPDATE donation_request SET donation_status = ? WHERE donation_tag = ?";
        }

        try (Connection connectDb = connectNow.getConnection()) {
            PreparedStatement preparedStatement = connectDb.prepareStatement(updateQuery);
            preparedStatement.setInt(1, DonationStatus.getIntValue(dropdownStatus.getSelectionModel().getSelectedItem()));

            if (dropdownStatus.getSelectionModel().getSelectedItem().equals(DonationStatus.AGENT_ASSIGNED.getStringValue())) {
                preparedStatement.setString(2, dropdownAgent.getSelectionModel().getSelectedItem());
                preparedStatement.setString(3, getRequestTag());
            } else if (dropdownStatus.getSelectionModel().getSelectedItem().equals(DonationStatus.DELIVERY_BOY_ASSIGNED.getStringValue())) {
                preparedStatement.setString(2, dropdownDeliveryBoy.getSelectionModel().getSelectedItem());
                preparedStatement.setString(3, getRequestTag());
            } else {
                preparedStatement.setString(2, getRequestTag());
            }

            int queryResult = preparedStatement.executeUpdate();

            if (queryResult > 0) {
                labelAlertMessage.setText("Request updated successfully");
                dismissView();
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            labelAlertMessage.setText("Request update failed. Please try again later.");
        }
    }

    private void getDonationStatusList() {

        donationStatusArrayList.addAll(List.of(DonationStatus.values()));

        for (DonationStatus donationStatus : donationStatusArrayList) {
            dropdownStatus.getItems().add(donationStatus.getStringValue());
        }
    }

    private void getRequestInformation() {
        //Get the request information from the database
        //Set the information to the fields
        DatabaseConnection connectNow = new DatabaseConnection();
        String getRequestQuery = "SELECT * FROM donation_request WHERE donation_tag = ?";

        try(Connection connectDb = connectNow.getConnection()) {
            PreparedStatement preparedStatement = connectDb.prepareStatement(getRequestQuery);
            preparedStatement.setString(1, getRequestTag());

            //Execute the query
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                fieldRequestedBy.setText(queryResult.getString("requested_by"));
                fieldLocation.setText(getDonorLocation(queryResult.getString("donor_location")));
                dropdownStatus.getSelectionModel().select(DonationStatus.getStringValue(queryResult.getInt("donation_status")));
                getAgentList(queryResult.getString("donor_location"));
                getDeliveryBoyList(queryResult.getString("donor_location"));

                String agent = queryResult.getString("agent");
                String deliveryBoy = queryResult.getString("delivery_boy");

                if (agent != null) {
                    dropdownAgent.getSelectionModel().select(agent);
                }

                if (deliveryBoy != null) {
                    dropdownDeliveryBoy.getSelectionModel().select(deliveryBoy);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void getDeliveryBoyList(String donorLocation) {
        DatabaseConnection connectNow = new DatabaseConnection();

        String getDeliveryBoyQuery = "SELECT username FROM user_account WHERE user_type = ? AND location = ? ORDER BY account_id";

        try(Connection connectDb = connectNow.getConnection()) {
            PreparedStatement preparedStatement = connectDb.prepareStatement(getDeliveryBoyQuery);
            preparedStatement.setInt(1, UserType.DELIVERY_BOY.getValue());
            preparedStatement.setString(2, donorLocation);

            //Execute the query
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                dropdownDeliveryBoy.getItems().add(queryResult.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void getAgentList(String donorLocation) {
        DatabaseConnection connectNow = new DatabaseConnection();
        //Sorted by agent id
        String getAgentQuery = "SELECT username FROM user_account WHERE user_type = ? AND location = ? ORDER BY account_id";

        try(Connection connectDb = connectNow.getConnection()) {
            PreparedStatement preparedStatement = connectDb.prepareStatement(getAgentQuery);
            preparedStatement.setInt(1, UserType.AGENT.getValue());
            preparedStatement.setString(2, donorLocation);

            //Execute the query
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                dropdownAgent.getItems().add(queryResult.getString("username"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private String getDonorLocation(String donorLocation) {
        //Get the location name from the database
        //Return the location name
        DatabaseConnection connectNow = new DatabaseConnection();
        String getLocationQuery = "SELECT location_name FROM location WHERE location_code = ?";
        String locationName = "";

        try(Connection connectDb = connectNow.getConnection()) {
            PreparedStatement preparedStatement = connectDb.prepareStatement(getLocationQuery);
            preparedStatement.setString(1, donorLocation);

            //Execute the query
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                locationName = queryResult.getString("location_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return locationName;
    }
}
