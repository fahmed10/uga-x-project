package shared;

public class PlayerLeavePacket extends Packet {
    public final byte userId;

    public PlayerLeavePacket(byte userId) {
        this.userId = userId;
    }

    public PlayerLeavePacket(byte[] data) {
        this.userId = data[1];
    }

    public byte[] getData() {
        return Utils.asArray(PacketType.PLAYER_LEAVE, userId);
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
