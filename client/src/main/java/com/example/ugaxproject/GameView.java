package com.example.ugaxproject;

import entities.Player;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.Stage;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class GameView {

    Client client;

    public void initialize(Client client) {
        this.client = client;
        setupGame();
    }

    public void setupGame() {
        // Queries server for data to setup the game session. Server should return back any data needed for that:
        // Could be locations of objects that spawn on the map at the start of a game session, or anything else the
        // server should be authoritatively informing the client of when the game is set up.
    }
    
    @FXML
    private Canvas gameCanvas;

    @FXML
    private AnchorPane rootPane;

    private GraphicsContext gc;
    private Player player;

    @FXML
    public void initialize() {
        if (gameCanvas == null) {
            System.out.println("Error: gameCanvas is null! Check your FXML.");
            return;
        }

        gc = gameCanvas.getGraphicsContext2D();
        player = new Player(100,100);


        gameCanvas.widthProperty().bind(rootPane.widthProperty());
        gameCanvas.heightProperty().bind(rootPane.heightProperty());


        gameCanvas.widthProperty().addListener(evt -> drawGame());
        gameCanvas.heightProperty().addListener(evt -> drawGame());

        gc = gameCanvas.getGraphicsContext2D();
        player = new Player(100, 100);
        drawGame();
    }

    private void drawGame() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());


        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());


        player.draw(gc);
    }

    @FXML
    void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case W: player.move("up"); break;
            case S: player.move("down"); break;
            case A: player.move("left"); break;
            case D: player.move("right"); break;
        }
        drawGame();
    }

    @FXML
    void handleMousePress(MouseEvent event) {
        if (event.getEventType().equals(MOUSE_PRESSED)) {
            player.attack();
        }
    }


}
