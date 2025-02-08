package shared;

public class LoginPacket extends Packet {
    public final byte listenPort;
    public final String username;

    public LoginPacket(byte[] data) {
        this.listenPort = data[1];
        this.username = Utils.nulTerminateString(new String(data, 2, data.length - 2));
    }

    public LoginPacket(byte listenPort, String username) {
        this.listenPort = listenPort;
        this.username = username;
    }

    public byte[] getData() {
        return Utils.combineArrays(Utils.asArray(PacketType.LOGIN, listenPort), username.getBytes());
    }

    @Override
    public boolean expectsReply() {
        return true;
    }
}