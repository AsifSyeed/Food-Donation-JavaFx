package com.example.fooddonation.controller;

import com.example.fooddonation.model.enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class LandingController implements Initializable {
    @FXML
    private ImageView imageViewLogo;

    @FXML
    private Label labelTitle;

    @FXML
    private ImageView imageViewDelivery;

    @FXML
    private ImageView imageViewDonate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImages();
    }

    private void setImages() {
        Image imageLogo = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/logo.png"));
        imageViewLogo.setImage(imageLogo);

        Image imageDonate = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/donate.png"));
        imageViewDonate.setImage(imageDonate);

        Image imageDelivery = new Image(getClass().getResourceAsStream("/com/example/fooddonation/assets/delivery.png"));
        imageViewDelivery.setImage(imageDelivery);
    }

    public void donateButtonAction() {
        navigateToLoginPage(UserType.DONOR.getValue());
    }

    private void navigateToLoginPage(int value) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fooddonation/login.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 800, 600));
            registerStage.show();

            LoginController loginController = loader.getController();
            loginController.userTypeValue(value);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void deliveryButtonAction() {
        navigateToLoginPage(UserType.DELIVERY_BOY.getValue());
    }

    public void agentButtonAction() {
        navigateToLoginPage(UserType.AGENT.getValue());
    }

    public void adminButtonAction() {
        navigateToLoginPage(UserType.ADMIN.getValue());
    }
}
