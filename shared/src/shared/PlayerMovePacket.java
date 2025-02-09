package shared;

public class PlayerMovePacket extends Packet {
    public final byte userId;
    public final byte direction;
    public final Vector2 position;

    public PlayerMovePacket(byte userId, byte direction, Vector2 position) {
        this.userId = userId;
        this.direction = direction;
        this.position = position;
    }

    public PlayerMovePacket(byte[] data) {
        this.userId = data[1];
        this.direction = data[2];
        this.position = new Vector2(data, 3);
    }

    @Override
    public byte[] getData() {
        return Utils.combineArrays(Utils.asArray(PacketType.PLAYER_MOVE, userId, direction), position.getData());
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
