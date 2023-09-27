module com.example.fooddonation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.example.fooddonation to javafx.fxml;
    opens com.example.fooddonation.controller;
    opens com.example.fooddonation.model;
    exports com.example.fooddonation;
    exports com.example.fooddonation.controller;
    exports com.example.fooddonation.model;
}