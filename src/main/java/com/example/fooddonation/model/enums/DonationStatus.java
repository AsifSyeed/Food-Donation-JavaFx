package com.example.fooddonation.model.enums;

public enum DonationStatus {
    PENDING(1, "Requested"),
    APPROVED(2, "Rejected"),
    REJECTED(3, "Approved"),
    AGENT_ASSIGNED(4, "Agent Assigned"),
    DELIVERY_BOY_ASSIGNED(5, "Delivery Boy Assigned"),
    DELIVERED(6, "Delivered");

    private final int intValue;
    private final String stringValue;

    DonationStatus(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    // Helper method to get the string representation based on an integer value
    public static String getStringValue(int intValue) {
        for (DonationStatus status : DonationStatus.values()) {
            if (status.getIntValue() == intValue) {
                return status.getStringValue();
            }
        }
        return "Unknown";
    }
}
