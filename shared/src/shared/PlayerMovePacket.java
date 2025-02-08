package shared;

public class PlayerMovePacket extends Packet {
    public final byte userId;
    public final Vector2 position;

    public PlayerMovePacket(byte userId, Vector2 position) {
        this.userId = userId;
        this.position = position;
    }

    public PlayerMovePacket(byte[] data) {
        this.userId = data[1];
        this.position = new Vector2(data, 2);
    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }
}
