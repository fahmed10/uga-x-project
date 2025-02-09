package com.example.ugaxproject;

import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

import shared.Vector2;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class GameView {
    GameClient client;
    private GraphicsContext gc;
    private Player player;
    Set<Input> inputs = new HashSet<>();
    Direction direction = Direction.LEFT;
    Vector2 inputVector = new Vector2();
    AnimationTimer timer;
    long lastTime = 0;
    float startX = 100.0f;
    float startY = 100.0f;
    public static float worldX = 0.0f;    // x-coords the player has moved this session
    public static float worldY = 0.0f;    // y-coords the player has moved this session

    @FXML
    private Canvas gameCanvas;

    @FXML
    private AnchorPane rootPane;

    public void init(GameClient client) {
        this.client = client;
        setupGame();

        if (gameCanvas == null) {
            System.out.println("Error: gameCanvas is null! Check your FXML.");
            return;
        }

        gc = gameCanvas.getGraphicsContext2D();
        player = new Player(startX,startY);

        gameCanvas.widthProperty().bind(rootPane.widthProperty());
        gameCanvas.heightProperty().bind(rootPane.heightProperty());

        gameCanvas.widthProperty().addListener(evt -> drawBackground());
        gameCanvas.heightProperty().addListener(evt -> drawBackground());

        timer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                run(currentTime);
            }
        };

        timer.start();
    }

    public void setupGame() {
        // Queries server for data to setup the game session. Server should return back any data needed for that:
        // Could be locations of objects that spawn on the map at the start of a game session, or anything else the
        // server should be authoritatively informing the client of when the game is set up.
    }

    void run(long currentTime) {
        // Establish delta time to normalize framerates
        double delta = (currentTime - lastTime) / 1E9;
        lastTime = System.nanoTime();
//        System.out.println(delta);
//        System.out.println(1/delta);

        // Update player movement based on movement booleans
        inputVector.set(0, 0);
        if (inputs.contains(Input.MOVE_UP)) {inputVector.add(0, -1);}
        if (inputs.contains(Input.MOVE_DOWN)) {inputVector.add(0, 1);}
        if (inputs.contains(Input.MOVE_LEFT)) {inputVector.add(-1, 0);}
        if (inputs.contains(Input.MOVE_RIGHT)) {inputVector.add(1, 0);}

        if (inputVector.x != 0 || inputVector.y != 0) {
            boolean stillRolling = player.move(inputVector, inputs.contains(Input.ROLL), delta);
            if (!stillRolling) {
                inputs.remove(Input.ROLL);
            }
            Vector2 newPosition = player.getPosition();
            try {
                client.moveTo(newPosition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                client.keepAlive();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (inputVector.x != 0) {
            direction = inputVector.x < 0 ? Direction.LEFT : Direction.RIGHT;
        }

        // draw/paint scene for current frame
        drawGame(delta);
    }

    private void drawBackground() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }


    private void drawGame(double delta) {
        drawBackground();

        double translateX = (gameCanvas.getWidth() / 2) - startX - worldX - 64;    // the 0 will draw the player in the center of the screen
        double translateY = (gameCanvas.getHeight() / 2) - startY - worldY - 64;

        gc.translate(translateX, translateY);

        player.walkAnimation(direction, inputs, delta);
//        player.draw(gc);
        player.drawCentered(gc);

        gc.translate(-translateX, -translateY);
    }

    @FXML
    void handleKeyPress(KeyEvent event) {
        inputs.add(
        switch (event.getCode()) {
            case W, UP -> Input.MOVE_UP;
            case S, DOWN -> Input.MOVE_DOWN;
            case A, LEFT -> Input.MOVE_LEFT;
            case D, RIGHT -> Input.MOVE_RIGHT;
            case SPACE -> Input.ROLL;
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

    @FXML
    void handleMousePress(MouseEvent event) {
        if (event.getEventType().equals(MOUSE_PRESSED)) {
            player.attack();
        }
    }


}
