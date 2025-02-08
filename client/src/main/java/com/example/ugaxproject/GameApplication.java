package com.example.ugaxproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GameApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        switchToStartScreen();
        stage.setTitle("UGAHacks Game");
        stage.show();
    }

    public static void switchToStartScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        primaryStage.setScene(scene);
    }

    public static void switchToGameScreen(Client client) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        GameView gameView = fxmlLoader.getController();
        gameView.initialize(client);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch();
    }
}
