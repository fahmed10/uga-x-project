package com.example.ugaxproject;

import shared.*;

import java.io.IOException;

public class GameClient {
    private final Client client = new Client("localhost", 5000);
    private byte userId = -1;
    private long lastKeepAlive = System.currentTimeMillis();

    public GameClient() throws IOException {}

    public void login(String username) throws IOException {
        LoginAckPacket response = (LoginAckPacket) client.send(new LoginPacket(username));
        userId = response.userId;
        lastKeepAlive = System.currentTimeMillis();
    }

    public void moveTo(Vector2 position) throws IOException {
        client.send(new PlayerMovePacket(userId, position));
        lastKeepAlive = System.currentTimeMillis();
    }

    public void keepAlive() throws IOException {
        if (System.currentTimeMillis() - lastKeepAlive > 2000) {
            client.send(new KeepAlivePacket(userId));
            lastKeepAlive = System.currentTimeMillis();
        }
    }
}
