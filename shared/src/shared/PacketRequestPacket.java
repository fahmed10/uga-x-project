package shared;

public class PacketRequestPacket extends Packet {
    public final byte userId;

    public PacketRequestPacket(byte userId) {
        this.userId = userId;
    }

    public PacketRequestPacket(byte[] data) {
        this.userId = data[1];
    }

    @Override
    public byte[] getData() {
        return Utils.combineArrays(Utils.asArray(PacketType.REQUEST, userId));
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
