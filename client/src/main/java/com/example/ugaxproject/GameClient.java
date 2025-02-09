package com.example.ugaxproject;

import shared.*;

import java.io.IOException;

public class GameClient {
    private final Client client = new Client("localhost", 5000);
    public byte userId = -1;
    private long lastKeepAlive = System.currentTimeMillis();

    public GameClient() throws IOException {}

    public void login(String username) throws IOException {
        LoginAckPacket response = (LoginAckPacket) client.send(new LoginPacket(username));
        userId = response.userId;
        lastKeepAlive = System.currentTimeMillis();
    }

    public void moveTo(Vector2 position, Direction direction) throws IOException {
        client.send(new PlayerMovePacket(userId, (byte)direction.ordinal(), position));
        lastKeepAlive = System.currentTimeMillis();
    }

    public void keepAlive() throws IOException {
        if (System.currentTimeMillis() - lastKeepAlive > 1000) {
            client.send(new KeepAlivePacket(userId));
            lastKeepAlive = System.currentTimeMillis();
        }
    }

    public void damage(byte targetUserId, byte newHealth) throws IOException {
        client.send(new DamagePacket(userId, newHealth, targetUserId));
    }

    public void close() {
        client.close();
    }
}
