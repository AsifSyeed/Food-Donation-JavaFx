package com.example.fooddonation.model;

public class DonationRequest {
    int foodQuantity;
    String foodName, donationStatus, donationTag, requestedBy;

    public String getDonationTag() {
        return donationTag;
    }

    public void setDonationTag(String DonationTag) {
        this.donationTag = donationTag;
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

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public DonationRequest(String donationTag, int foodQuantity, String donationStatus, String foodName, String requestedBy) {
        this.donationTag = donationTag;
        this.donationStatus = donationStatus;
        this.foodQuantity = foodQuantity;
        this.foodName = foodName;
        this.requestedBy = requestedBy;
    }
}
