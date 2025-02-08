import shared.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

public class Server extends Thread {
    private final DatagramSocket socket;
    private final byte[] buffer = new byte[Constants.MAX_PACKET_LENGTH];
    private final GameWorld gameWorld = new GameWorld();

    public Server(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void run() {
        while (true) {
            Arrays.fill(buffer, (byte) 0);
            DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length);
            gameWorld.removeLostPlayers();

            List<Player> lostPlayers = gameWorld.removeLostPlayers();

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
                case PacketType.PLAYER_MOVE -> new PlayerMovePacket(dPacket.getData());
                case PacketType.KEEP_ALIVE -> new KeepAlivePacket(dPacket.getData());
                default -> throw new IllegalStateException("Unexpected packet type: " + dPacket.getData()[0]);
            };

            for (Player lostPlayer : lostPlayers) {
                for (Player player : gameWorld.getPlayers()) {
                    dPacket.setAddress(player.address);
                    dPacket.setPort(player.port);
                    dPacket.setData(new PlayerLeavePacket(lostPlayer.userId).getData());
                    try {
                        socket.send(dPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                Packet response = handlePacket(received, dPacket);
                if (response == null) {
                    continue;
                }

                dPacket.setAddress(address);
                dPacket.setPort(port);
                dPacket.setData(response.getData());
                socket.send(dPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Packet handlePacket(Packet packet, DatagramPacket dPacket) throws IOException {
        return switch (packet) {
            case LoginPacket loginPacket -> {
                System.out.println("Player " + loginPacket.username + " logged in");
                byte userId = gameWorld.addPlayer(loginPacket.username, dPacket.getAddress(), dPacket.getPort());

                if (userId == -1) {
                    yield null;
                }

                for (Player player : gameWorld.getPlayers()) {
                    if (player.userId == userId) {
                        continue;
                    }

                    dPacket.setAddress(player.address);
                    dPacket.setPort(player.port);
                    dPacket.setData(new PlayerJoinPacket(userId, new Vector2()).getData());
                    socket.send(dPacket);
                }

                yield new LoginAckPacket(userId);
            }
            case PlayerMovePacket playerMovePacket -> {
                System.out.println("[" + playerMovePacket.userId + "] Move to: " + playerMovePacket.position);
                gameWorld.movePlayerTo(playerMovePacket.userId, playerMovePacket.position);
                for (Player player : gameWorld.getPlayers()) {
                    if (player.userId == playerMovePacket.userId) {
                        continue;
                    }

                    dPacket.setAddress(player.address);
                    dPacket.setPort(player.port);
                    socket.send(dPacket);
                    System.out.println(player.address + " / " + player.port);
                }
                yield null;
            }
            case KeepAlivePacket keepAlivePacket -> {
                System.out.println("[" + keepAlivePacket.userId + "] Keep alive");
                gameWorld.getPlayer(keepAlivePacket.userId).keepAlive();
                yield null;
            }
            default -> throw new IllegalStateException("Unhandled packet");
        };
    }
}
