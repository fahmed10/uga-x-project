package com.example.ugaxproject;

import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import shared.Vector2;

import java.util.HashSet;
import java.util.Set;

public class GameView {
    Client client;
    private GraphicsContext gc;
    private Player player;
    Set<Input> inputs = new HashSet<>();
    Vector2 inputVector = new Vector2();
    AnimationTimer timer;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private AnchorPane rootPane;

    public void init(Client client) {
        this.client = client;
        setupGame();

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

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                run();
            }
        };

        timer.start();
    }

    public void setupGame() {
        // Queries server for data to setup the game session. Server should return back any data needed for that:
        // Could be locations of objects that spawn on the map at the start of a game session, or anything else the
        // server should be authoritatively informing the client of when the game is set up.
    }

    void run() {
        // Update player movement based on movement booleans
        inputVector.set(0, 0);
        if (inputs.contains(Input.MOVE_UP)) {inputVector.add(0, -1);}
        if (inputs.contains(Input.MOVE_DOWN)) {inputVector.add(0, 1);}
        if (inputs.contains(Input.MOVE_LEFT)) {inputVector.add(-1, 0);}
        if (inputs.contains(Input.MOVE_RIGHT)) {inputVector.add(1, 0);}

        if (inputVector.x != 0 || inputVector.y != 0) {
            player.move(inputVector);
        }

        // draw/paint scene for current frame
        drawGame();
    }

    private void drawGame() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        player.draw(gc);
    }

    @FXML
    void handleKeyPress(KeyEvent event) {
        inputs.add(
        switch (event.getCode()) {
            case W, UP -> Input.MOVE_UP;
            case S, DOWN -> Input.MOVE_DOWN;
            case A, LEFT -> Input.MOVE_LEFT;
            case D, RIGHT -> Input.MOVE_RIGHT;
            default -> null;
        });
    }

    @FXML
    void handleKeyRelease(KeyEvent event) {
        inputs.remove(
                switch (event.getCode()) {
                    case W, UP -> Input.MOVE_UP;
                    case S, DOWN -> Input.MOVE_DOWN;
                    case A, LEFT -> Input.MOVE_LEFT;
                    case D, RIGHT -> Input.MOVE_RIGHT;
                    default -> null;
                });
    }
}
