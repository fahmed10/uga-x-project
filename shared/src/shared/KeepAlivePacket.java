package shared;

public class KeepAlivePacket extends Packet {
    public final byte userId;

    public KeepAlivePacket(byte userId) {
        this.userId = userId;
    }

    public KeepAlivePacket(byte[] data) {
        this.userId = data[1];
    }

    public byte[] getData() {
        return Utils.asArray(PacketType.KEEP_ALIVE, userId);
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
