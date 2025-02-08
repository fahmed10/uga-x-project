import shared.Vector2;

import java.net.InetAddress;

public class Player {
    final byte userId;
    final String username;
    InetAddress address;
    int port;
    Vector2 position;

    public Player(byte userId, String username, InetAddress address, int port, Vector2 position) {
        this.userId = userId;
        this.username = username;
        this.position = position;
        this.address = address;
        this.port = port;
    }
}
