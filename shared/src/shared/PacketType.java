package shared;

public interface PacketType {
    byte LOGIN = 0;
    byte LOGIN_ACK = 1;
    byte PLAYER_MOVE = 2;
    byte PLAYER_JOIN = 3;
    byte KEEP_ALIVE = 4;
}
