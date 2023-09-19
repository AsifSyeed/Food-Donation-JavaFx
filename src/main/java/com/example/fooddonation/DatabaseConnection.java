package com.example.fooddonation;

import org.postgresql.Driver;

import java.sql.*;
public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "FoodDonation";
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;
        String user = "postgres";
        String password = "4421";

        try {
            Class.forName("org.postgresql.Driver");
            databaseLink = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }
}
