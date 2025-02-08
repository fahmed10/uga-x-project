package com.example.ugaxproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MainController {
    @FXML
    private TextField usernameField;

    private final GameClient client = new GameClient();

    public MainController() throws IOException {}

    @FXML
    public void handleStartGame() {
        try {
            client.login(usernameField.getText());
            GameApplication.switchToGameScreen(client);
        } catch (IOException e) {
            GameApplication.showAlert("Network Error", "Connection failed. Please try again.", Alert.AlertType.ERROR);
        }
    }
}
