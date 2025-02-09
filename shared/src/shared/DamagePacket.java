package shared;

public class DamagePacket extends Packet {
    public final byte userId;
    public final byte targetUserId;
    public final byte damage;

    public DamagePacket(byte userId, byte damage, byte targetUserId) {
        this.userId = userId;
        this.damage = damage;
        this.targetUserId = targetUserId;
    }

    public DamagePacket(byte[] data) {
        this.userId = data[1];
        this.targetUserId = data[2];
        this.damage = data[3];
    }

    @Override
    public byte[] getData() {
        return Utils.asArray(PacketType.DAMAGE, userId, targetUserId, damage);
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
