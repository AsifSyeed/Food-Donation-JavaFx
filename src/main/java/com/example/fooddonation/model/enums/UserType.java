package com.example.fooddonation.model.enums;

//Create an enum called UserType with the following values: Donor(1), Agent(2), DeliveryBoy(3), Admin(4)
public enum UserType {
    DONOR(1),
    AGENT(2),
    DELIVERY_BOY(3),
    ADMIN(4);

    private final int value;

    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}