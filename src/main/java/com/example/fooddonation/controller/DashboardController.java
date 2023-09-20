package com.example.fooddonation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label labelUsername;

    private String username;

    public void displayName(String username) {
        setUsername(username);
        labelUsername.setText("Hello: " + username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
