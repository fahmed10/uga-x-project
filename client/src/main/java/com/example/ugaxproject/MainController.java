package com.example.ugaxproject;

import javafx.event.ActionEvent;

import java.io.IOException;

public class MainController {

    public void handleStartGame(ActionEvent actionEvent) throws IOException {
        System.out.println("hello");
        GameApplication.switchToGameScreen();
    }
}