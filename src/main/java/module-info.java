module com.example.fooddonation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.example.fooddonation to javafx.fxml;
    exports com.example.fooddonation;
}