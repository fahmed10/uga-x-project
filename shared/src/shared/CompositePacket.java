package shared;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositePacket extends Packet {
    private byte packetCount;
    private byte[] packetSizes;
    public final Packet[] packets;

    public CompositePacket(Packet... packets) {
        this.packets = packets;
    }

    public CompositePacket(byte[] data) {
        packetCount = data[1];

        if (packetCount == 0) {
            packets = new Packet[0];
            return;
        }

        if (data.length < 2 + packetCount) {
            packets = new Packet[0];
            return;
        }

        packetSizes = new byte[packetCount];
        packets = new Packet[packetCount];
        int offset = 2 + packetCount;
        for (int i = 0; i < packetCount; i++) {
            packetSizes[i] = data[i + 2];
            byte[] packetData = Arrays.copyOfRange(data, offset, offset + packetSizes[i]);
            packets[i] = switch (packetData[0]) {
                case PacketType.PLAYER_JOIN -> new PlayerJoinPacket(packetData);
                case PacketType.PLAYER_LEAVE -> new PlayerLeavePacket(packetData);
                case PacketType.PLAYER_MOVE -> new PlayerMovePacket(packetData);
                case PacketType.STRING -> new StringPacket(packetData);
                default -> throw new IllegalStateException("Unexpected value: " + packetData[0]);
            };
            offset += packetSizes[i];
        }
    }

    @Override
    public byte[] getData() {
        if (packets.length == 0) {
            return Utils.asArray(PacketType.COMPOSITE, (byte)0);
        }

        byte[] sizes = new byte[packets.length];
        List<Byte> data = new ArrayList<>();
        for (int i = 0; i < packets.length; i++) {
            for (Byte b : packets[i].getData()) {
                data.add(b);
            }
            sizes[i] = (byte)packets[i].getData().length;
        }
        byte[] bytes = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            bytes[i] = data.get(i);
        }
        return Utils.combineArrays(Utils.asArray(PacketType.COMPOSITE, (byte)packets.length), sizes, bytes);
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
