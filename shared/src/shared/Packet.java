package shared;

public abstract class Packet {
    public abstract byte[] getData();
    public abstract boolean expectsReply();
}
