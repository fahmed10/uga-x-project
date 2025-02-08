module com.example.ugaxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires shared;
    requires java.xml;


    opens com.example.ugaxproject to javafx.fxml;
    exports com.example.ugaxproject;
    exports entities;
}