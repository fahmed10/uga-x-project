import shared.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

public class Server extends Thread {
    private final DatagramSocket socket;
    private final byte[] buffer = new byte[Constants.MAX_PACKET_LENGTH];
    private final GameWorld gameWorld = new GameWorld();
    Map<Byte, Queue<Packet>> packetQueue = new HashMap<>();

    void direct(Packet packet, byte userId) {
        packetQueue.computeIfAbsent(userId, k -> new LinkedList<>()).add(packet);
    }

    void broadcast(Packet packet, int count) {
        broadcast(packet, (byte) -1);
    }

    void broadcast(Packet packet, byte excludeId, int count) {
        for (int i = 0; i < count; i++) {
            for (Player player : gameWorld.getPlayers()) {
                if (player.userId == excludeId) {
                    continue;
                }

                direct(packet, player.userId);
            }
        }
    }

    void clearBroadcast(byte userId) {
        packetQueue.remove(userId);
    }

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
                case PacketType.STRING -> new StringPacket(dPacket.getData());
                case PacketType.REQUEST -> new PacketRequestPacket(dPacket.getData());
                case PacketType.DAMAGE -> new DamagePacket(dPacket.getData());
                default -> throw new IllegalStateException("Unexpected packet type: " + dPacket.getData()[0]);
            };

            for (Player lostPlayer : lostPlayers) {
                clearBroadcast(lostPlayer.userId);
                broadcast(new PlayerLeavePacket(lostPlayer.userId), 3);
            }

            try {
                Packet response = handlePacket(received, dPacket);
                if (response != null) {
                    dPacket.setAddress(address);
                    dPacket.setPort(port);
                    dPacket.setData(response.getData());
                    socket.send(dPacket);
                }
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

                broadcast(new PlayerJoinPacket(userId, new Vector2()), userId, 2);
                for (Player player : gameWorld.getPlayers()) {
                    if (player.userId != userId) {
                        direct(new PlayerJoinPacket(player.userId, player.position), userId);
                    }
                }

                yield new LoginAckPacket(userId);
            }
            case PlayerMovePacket playerMovePacket -> {
                // System.out.println("[" + playerMovePacket.userId + "] Move to: " + playerMovePacket.position);
                gameWorld.movePlayerTo(playerMovePacket.userId, playerMovePacket.position, playerMovePacket.direction);
                broadcast(playerMovePacket, playerMovePacket.userId, 2);
                yield null;
            }
            case KeepAlivePacket keepAlivePacket -> {
                // System.out.println("[" + keepAlivePacket.userId + "] Keep alive");

                if (!gameWorld.hasPlayerId(keepAlivePacket.userId)) {
                    yield null;
                }

                gameWorld.getPlayer(keepAlivePacket.userId).keepAlive();
                Player player = gameWorld.getPlayer(keepAlivePacket.userId);
                broadcast(new PlayerMovePacket(keepAlivePacket.userId, player.direction, player.position), keepAlivePacket.userId, 2);
                yield null;
            }
            case DamagePacket damagePacket -> {
                System.out.println("[" + damagePacket.userId + "] Damaged " + damagePacket.targetUserId + " to " + damagePacket.newHealth);
                broadcast(damagePacket, damagePacket.userId, 3);
                yield null;
            }
            case PacketRequestPacket packetRequestPacket -> {
                Queue<Packet> queue = packetQueue.getOrDefault(packetRequestPacket.userId, new LinkedList<>());

                if (!queue.isEmpty()) {
                    yield queue.remove();
                }

                yield new CompositePacket();
            }
            default -> {
                System.out.println("Unhandled packet");
                yield null;
            }
        };
    }
}
