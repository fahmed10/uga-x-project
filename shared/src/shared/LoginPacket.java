package shared;

public class LoginPacket extends Packet {
    public final String username;

    public LoginPacket(byte[] data) {
        this.username = Utils.nulTerminateString(new String(data, 1, data.length - 1));
    }

    public LoginPacket(String username) {
        this.username = username;
    }

    public byte[] getData() {
        return Utils.combineArrays(Utils.asArray(PacketType.LOGIN), username.getBytes());
    }

    @Override
    public boolean expectsReply() {
        return true;
    }
}