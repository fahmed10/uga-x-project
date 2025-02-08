package com.example.ugaxproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainController {

    @FXML
    private VBox rootVBox;

    @FXML
    private Text titleText;

    @FXML
    public Text titleText2;

    @FXML
    private TextField usernameField;

    @FXML
    private Button startButton;

    public void initialize() {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/font/Rebellion.ttf"), 30);
        titleText.setFont(customFont);
        titleText2.setFont(customFont);
        usernameField.setFont(customFont);
        startButton.setFont(customFont);

    }


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
