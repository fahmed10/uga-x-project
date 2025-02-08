package com.example.ugaxproject;

import entities.Player;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class GameView {
    @FXML
    private Canvas gameCanvas;
<<<<<<< HEAD
    @FXML
=======
>>>>>>> 046230607ad908fef236e76e4d987bc0dce82e96
    private AnchorPane rootPane;

    private GraphicsContext gc;
    private Player player;

    @FXML
    public void initialize() {
<<<<<<< HEAD
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
=======
        gc = gameCanvas.getGraphicsContext2D();
        player = new Player(100, 100);
>>>>>>> 046230607ad908fef236e76e4d987bc0dce82e96
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
}
