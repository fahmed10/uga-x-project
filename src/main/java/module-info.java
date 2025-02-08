module com.example.ugaxproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ugaxproject to javafx.fxml;
    exports com.example.ugaxproject;
}