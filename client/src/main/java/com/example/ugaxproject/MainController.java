package com.example.ugaxproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import shared.LoginAckPacket;
import shared.LoginPacket;

import java.io.IOException;

public class MainController {
    @FXML
    private TextField usernameField;

    private final Client client = new Client("localhost", 5000);

    public MainController() throws IOException {}

    @FXML
    public void handleStartGame() throws IOException {
        try {
            LoginAckPacket response = (LoginAckPacket) client.send(new LoginPacket(usernameField.getText()));
            GameApplication.showAlert("Info", "Assigned user ID " + response.userId + " by server", Alert.AlertType.INFORMATION);
            GameApplication.switchToGameScreen(client);
        } catch (IOException e) {
            GameApplication.showAlert("Network Error", "Connection failed. Please try again.", Alert.AlertType.ERROR);
        }
    }
}
