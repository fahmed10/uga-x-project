module com.example.ugaxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media; requires shared;


    opens com.example.ugaxproject to javafx.fxml;
    exports com.example.ugaxproject;
    exports entities;
}