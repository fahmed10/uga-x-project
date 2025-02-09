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

import shared.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;


public class GameView {
    GameClient client;
    private GraphicsContext gc;
    private Player player;
    Set<Input> inputs = new HashSet<>();
    Direction direction = Direction.LEFT;
    Vector2 inputVector = new Vector2();
    AnimationTimer timer;
    long lastTime = 0;
    Map<Byte, Player> others = new HashMap<>();
    final Queue<Packet> packetQueue = new LinkedList<>();
    private static Direction[] directions = Direction.values();
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

        new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(Constants.SERVER_HOSTNAME);
                int port = Constants.SERVER_PORT;
                DatagramPacket dPacket;

                while (true) {
                    byte[] buffer = new PacketRequestPacket(client.userId).getData();
                    dPacket = new DatagramPacket(buffer, buffer.length, address, port);
                    socket.send(dPacket);
                    buffer = new byte[Constants.MAX_PACKET_LENGTH];
                    dPacket = new DatagramPacket(buffer, buffer.length, address, port);
                    socket.receive(dPacket);
                    if (dPacket.getData()[0] == PacketType.COMPOSITE) {
                        CompositePacket packet = new CompositePacket(dPacket.getData());
                        synchronized (packetQueue) {
                            packetQueue.addAll(List.of(packet.packets));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setupGame() {
        // Queries server for data to setup the game session. Server should return back any data needed for that:
        // Could be locations of objects that spawn on the map at the start of a game session, or anything else the
        // server should be authoritatively informing the client of when the game is set up.
    }

    private void handlePacket(Packet packet) {
        switch (packet) {
            case PlayerMovePacket playerMovePacket -> {
                if (!others.containsKey(playerMovePacket.userId)) {
                    others.put(playerMovePacket.userId, new Player(playerMovePacket.position.x, playerMovePacket.position.y));
                } else {
                    Player other = others.get(playerMovePacket.userId);
                    other.getPosition().set(playerMovePacket.position.x, playerMovePacket.position.y);
                    other.setDirection(directions[playerMovePacket.direction]);
                }
            }
            case PlayerJoinPacket playerJoinPacket -> {
                if (others.containsKey(playerJoinPacket.userId)) {
                    return;
                }

                others.put(playerJoinPacket.userId, new Player(playerJoinPacket.position.x, playerJoinPacket.position.y));
            }
            default -> {}
        }
    }

    void run(long currentTime) {
        // Establish delta time to normalize framerates
        double delta = (currentTime - lastTime) / 1E9;
        lastTime = System.nanoTime();

        synchronized (packetQueue) {
            while (!packetQueue.isEmpty()) {
                handlePacket(packetQueue.poll());
            }
        }

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
                client.moveTo(newPosition, player.getDirection());
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

        for (Player p : others.values()) {
            p.draw(gc);
        }

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
