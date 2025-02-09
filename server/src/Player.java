import shared.Vector2;

import java.net.InetAddress;

public class Player {
    private static final int TIME_UNTIL_LOST = 5000;
    final byte userId;
    final String username;
    InetAddress address;
    int port;
    Vector2 position;
    byte direction;
    long lastPacketTime;

    public Player(byte userId, String username, InetAddress address, int port, Vector2 position) {
        this.userId = userId;
        this.username = username;
        this.position = position;
        this.address = address;
        this.port = port;
        keepAlive();
    }

    public void keepAlive() {
        lastPacketTime = System.currentTimeMillis();
    }

    public boolean isLost() {
        return System.currentTimeMillis() - lastPacketTime >= TIME_UNTIL_LOST;
    }
}
