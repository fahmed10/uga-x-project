package shared;

public interface PacketType {
    byte LOGIN = 0;
    byte LOGIN_ACK = 1;
    byte PLAYER_MOVE = 2;
    byte PLAYER_JOIN = 3;
    byte KEEP_ALIVE = 4;
    byte PLAYER_LEAVE = 5;
    byte STRING = 6;
    byte COMPOSITE = 7;
    byte REQUEST = 8;
    byte DAMAGE = 9;
}
