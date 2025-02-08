package com.example.ugaxproject;

import shared.LoginAckPacket;
import shared.LoginPacket;
import shared.PlayerMovePacket;
import shared.Vector2;

import java.io.IOException;

public class GameClient {
    private final Client client = new Client("localhost", 5000);
    private byte userId = -1;

    public GameClient() throws IOException {}

    public void login(String username) throws IOException {
        LoginAckPacket response = (LoginAckPacket) client.send(new LoginPacket(username));
        userId = response.userId;
    }

    public void moveTo(Vector2 position) throws IOException {
        client.send(new PlayerMovePacket(userId, position));
    }
}
