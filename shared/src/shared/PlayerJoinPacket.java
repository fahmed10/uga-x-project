package shared;

public class PlayerJoinPacket extends Packet {
    public final byte userId;
    public final Vector2 position;

    public PlayerJoinPacket(byte userId, Vector2 position) {
        this.userId = userId;
        this.position = position;
    }

    public PlayerJoinPacket(byte[] data) {
        this.userId = data[1];
        this.position = new Vector2(data, 2);
    }

    @Override
    public byte[] getData() {
        return Utils.combineArrays(Utils.asArray(PacketType.PLAYER_JOIN, userId), position.getData());
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
