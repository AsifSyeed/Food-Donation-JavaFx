package com.example.fooddonation.model;

public class DonationRequest {
    int donationId, foodQuantity;
    String foodName, donationStatus;

    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public String getDonationStatus() {
        return donationStatus;
    }

    public void setDonationStatus(String donationStatus) {
        this.donationStatus = donationStatus;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public DonationRequest(int donationId, int foodQuantity, String donationStatus, String foodName) {
        this.donationId = donationId;
        this.donationStatus = donationStatus;
        this.foodQuantity = foodQuantity;
        this.foodName = foodName;
    }
}
