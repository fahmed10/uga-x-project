package shared;

public class DamagePacket extends Packet {
    public final byte userId;
    public final byte targetUserId;
    public final byte newHealth;

    public DamagePacket(byte userId, byte newHealth, byte targetUserId) {
        this.userId = userId;
        this.newHealth = newHealth;
        this.targetUserId = targetUserId;
    }

    public DamagePacket(byte[] data) {
        this.userId = data[1];
        this.targetUserId = data[2];
        this.newHealth = data[3];
    }

    @Override
    public byte[] getData() {
        return Utils.asArray(PacketType.DAMAGE, userId, targetUserId, newHealth);
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
