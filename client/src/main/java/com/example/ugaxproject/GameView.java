package com.example.ugaxproject;

import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

import shared.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.*;
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
    public float startX = 256.0f;
    public float startY = 256.0f;
    public static float worldX = 256.0f;    // x-coords the player has moved this session
    public static float worldY = 256.0f;    // y-coords the player has moved this session

    // Tile Variables
    public final int maxWorldCol = 70;
    public final int maxWorldRow = 35;
    public float screenX;
    public float screenY;
    TileManager tileManager = new TileManager(this);

    @FXML
    private Canvas gameCanvas;

    @FXML
    private AnchorPane rootPane;

    private MediaPlayer mediaPlayer;
    private MediaPlayer guitarHitPlayer;

    private Media loadMedia(String path) throws URISyntaxException {
        return new Media(GameApplication.class.getResource("music.mp3").toURI().toString());
    }

    public void init(GameClient client) {
        this.client = client;
        setupGame();

        try {
            mediaPlayer = new MediaPlayer(loadMedia("music.mp3"));
            mediaPlayer.setVolume(0.15);
            mediaPlayer.play();
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            guitarHitPlayer = new MediaPlayer(loadMedia("guitar.mp3"));
            guitarHitPlayer.setVolume(0.15);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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

        // Tile-related variable declarations
        screenX = (float) (gameCanvas.getWidth() / 2) - 64;
        screenY = (float) (gameCanvas.getHeight() / 2) - 64;

        timer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                run(currentTime);
            }
        };

        timer.start();
        player.getPosition().add(150 + (new Random().nextFloat(200)), 150);
        try {
            client.moveTo(player.getPosition(), player.getDirection());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    } else {
                        Packet packet = switch (dPacket.getData()[0]) {
                            case PacketType.PLAYER_JOIN -> new PlayerJoinPacket(dPacket.getData());
                            case PacketType.PLAYER_LEAVE -> new PlayerLeavePacket(dPacket.getData());
                            case PacketType.PLAYER_MOVE -> new PlayerMovePacket(dPacket.getData());
                            case PacketType.DAMAGE -> new DamagePacket(dPacket.getData());
                            default -> throw new IllegalStateException("Unexpected value: " + dPacket.getData()[0]);
                        };
                        synchronized (packetQueue) {
                            packetQueue.add(packet);
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
                    others.put(playerMovePacket.userId, new Player(playerMovePacket.position.x, playerMovePacket.position.y, true));
                } else {
                    Player other = others.get(playerMovePacket.userId);
                    other.moveTo(playerMovePacket.position.x, playerMovePacket.position.y);
                    other.setDirection(directions[playerMovePacket.direction]);
                }
            }
            case PlayerJoinPacket playerJoinPacket -> {
                if (!others.containsKey(playerJoinPacket.userId)) {
                    others.put(playerJoinPacket.userId, new Player(playerJoinPacket.position.x, playerJoinPacket.position.y, true));
                }
            }
            case PlayerLeavePacket playerLeavePacket -> {
                others.remove(playerLeavePacket.userId);
            }
            case DamagePacket damagePacket -> {
                if (others.containsKey(damagePacket.targetUserId)) {
                    Player other = others.get(damagePacket.targetUserId);
                    other.setHealth(damagePacket.newHealth);
                } else if (client.userId == damagePacket.targetUserId) {
                    player.setHealth(damagePacket.newHealth);

                    if (player.getHealth() <= 0) {
                        // TODO: Game over logic
                        System.out.println("Game over!");
                    }
                }
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
        tileManager.draw(gc);

        double translateX = (gameCanvas.getWidth() / 2) - startX - worldX - 64;    // the 0 will draw the player in the center of the screen
        double translateY = (gameCanvas.getHeight() / 2) - startY - worldY - 64;

        gc.translate(translateX, translateY);

//        tileManager.draw(gc);

        for (Player p : others.values()) {
            Vector2 diff = Vector2.sub(p.getPosition(), p.getLastPosition());
            Set<Input> inputSet = new HashSet<>();
            if (diff.x > 1) inputSet.add(Input.MOVE_RIGHT);
            if (diff.x < -1) inputSet.add(Input.MOVE_LEFT);
            if (diff.y > 1) inputSet.add(Input.MOVE_DOWN);
            if (diff.y < -1) inputSet.add(Input.MOVE_UP);
            p.walkAnimation(p.getDirection(), inputSet, delta);
            p.draw(gc);
        }

        player.walkAnimation(direction, inputs, delta);
//        player.draw(gc);
        player.draw(gc);

        gc.translate(-translateX, -translateY);
    }

    @FXML
    void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.M) {
            mediaPlayer.setMute(!mediaPlayer.isMute());
        }

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
            // guitarHitPlayer.play();

            for (Map.Entry<Byte, Player> pair : others.entrySet()) {
                if (pair.getValue() != player && Vector2.distance(player.getPosition(), pair.getValue().getPosition()) < 100) {
                    try {
                        int newHealth = pair.getValue().getHealth() - 21;
                        client.damage(pair.getKey(), (byte) newHealth);
                        pair.getValue().setHealth(newHealth);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
