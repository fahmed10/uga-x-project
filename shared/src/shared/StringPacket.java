package shared;

public class StringPacket extends Packet {
    public final String str;

    public StringPacket(String str) {
        this.str = str;
    }

    public StringPacket(byte[] data) {
        String str = new String(data, 1, data.length - 1);
        this.str = data[data.length - 1] == '\0' ? Utils.nulTerminateString(str) : str;
    }

    @Override
    public byte[] getData() {
        return Utils.combineArrays(Utils.asArray(PacketType.STRING), str.getBytes());
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
