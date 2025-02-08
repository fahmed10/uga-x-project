import shared.LoginAckPacket;
import shared.LoginPacket;
import shared.Packet;
import shared.PacketType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class Server extends Thread {
    private final DatagramSocket socket;
    private static final int MAX_PACKET_LENGTH = 512;
    private final byte[] buffer = new byte[MAX_PACKET_LENGTH];
    private byte userId = 0;

    public Server(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void run() {
        while (true) {
            Arrays.fill(buffer, (byte) 0);
            DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(dPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = dPacket.getAddress();
            int port = dPacket.getPort();
            dPacket = new DatagramPacket(buffer, buffer.length, address, port);
            Packet received = switch (dPacket.getData()[0]) {
                case PacketType.LOGIN -> new LoginPacket(dPacket.getData());
                default -> throw new IllegalStateException("Unexpected packet type: " + dPacket.getData()[0]);
            };

            Packet response = handlePacket(received);
            if (response == null) {
                continue;
            }

            try {
                dPacket.setData(response.getData());
                socket.send(dPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Packet handlePacket(Packet packet) {
        return switch (packet) {
            case LoginPacket loginPacket -> {
                System.out.println("Player " + loginPacket.username + " logged in");
                yield new LoginAckPacket(userId++);
            }
            default -> {
                System.out.println("Unhandled packet");
                yield null;
            }
        };
    }
}
