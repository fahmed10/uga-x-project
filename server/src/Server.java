import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;

public class Server extends Thread {
    private final DatagramSocket socket;
    private final byte[] buf = new byte[256];

    public Server(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), Charset.defaultCharset());
            received = received.substring(0, received.indexOf('\0'));
            System.out.println("Received: " + received);

            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
