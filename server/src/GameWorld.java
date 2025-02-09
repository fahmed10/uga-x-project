import shared.Vector2;

import java.net.InetAddress;
import java.util.*;

public class GameWorld {
    private final Map<Byte, Player> players = new HashMap<>();

    public byte addPlayer(String username, InetAddress address, int port) {
        byte userId = getUnusedPlayerId();

        if (userId == -1) {
            return userId;
        }

        players.put(userId, new Player(userId, username, address, port, new Vector2()));
        return userId;
    }

    public void movePlayerTo(byte userId, Vector2 position) {
        Player player = players.get(userId);
        player.position = position;
        player.keepAlive();
    }

    private byte getUnusedPlayerId() {
        Set<Byte> set = players.keySet();

        for (byte i = 0; i < 127; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }

        return -1;
    }

    public List<Player> removeLostPlayers() {
        List<Player> lostPlayers = new ArrayList<>();
        players.values().removeIf(p -> {
            if (p.isLost()) {
                lostPlayers.add(p);
                return true;
            }

            return false;
        });
        return lostPlayers;
    }

    public Player getPlayer(byte userId) {
        return players.get(userId);
    }

    public Collection<Player> getPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    public boolean hasPlayerId(byte userId) {
        return players.containsKey(userId);
    }
}
