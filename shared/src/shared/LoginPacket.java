package shared;

public class LoginPacket {
    private final String username;

    public LoginPacket(byte[] data) {
        this.username = new String(data);
    }

    public LoginPacket(String username) {
        this.username = username;
    }

    public byte[] getData() {
        return username.getBytes();
    }
}