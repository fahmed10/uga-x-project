package com.example.ugaxproject;

import javafx.fxml.FXML;

import java.io.IOException;

import java.io.IOException;

public class MainController {
    private final Client client = new Client("localhost", 5000);

    public MainController() throws IOException {}

    @FXML
    public void handleStartGame() throws IOException {
        System.out.println("hello");
        String response = client.send("Test Message");
        System.out.println("Receieved: " + response);
        GameApplication.switchToGameScreen();
    }
}