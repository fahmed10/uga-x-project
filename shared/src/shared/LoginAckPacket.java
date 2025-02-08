package shared;

public class LoginAckPacket extends Packet {
    public final byte userId;

    public LoginAckPacket(byte[] data) {
        this.userId = data[1];
    }

    public LoginAckPacket(byte userId) {
        this.userId = userId;
    }

    @Override
    public byte[] getData() {
        return Utils.asArray(PacketType.LOGIN_ACK, userId);
    }
}
