package com.example.ugaxproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.IOException;

public class MainController {
    @FXML
    private TextField usernameField;

    private final Client client = new Client("localhost", 5000);

    public MainController() throws IOException {}

    @FXML
    public void handleStartGame() throws IOException {
        System.out.println("hello");
        String response = client.send("Test Message");
        System.out.println("Received: " + response);
        GameApplication.switchToGameScreen(client);
    }
}
