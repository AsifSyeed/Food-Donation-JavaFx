package com.example.fooddonation.model.enums;

public enum DonationStatus {
    PENDING(1, "Requested"),
    REJECTED(2, "Rejected"),
    AGENT_ASSIGNED(3, "Agent Assigned"),
    DELIVERY_BOY_ASSIGNED(4, "Delivery Boy Assigned"),
    DELIVERED(5, "Delivered");

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

    public static int getIntValue(String stringValue) {
        for (DonationStatus status : DonationStatus.values()) {
            if (status.getStringValue().equals(stringValue)) {
                return status.getIntValue();
            }
        }
        return 0;
    }
}
