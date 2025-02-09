package com.example.ugaxproject;

import shared.Constants;
import shared.LoginAckPacket;
import shared.Packet;
import shared.PacketType;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Client {
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;
    private static final int TIMEOUT = 1500;

    public Client(String host, int port) throws IOException {
        socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);
        address = InetAddress.getByName(host);
        this.port = port;
    }

    public Packet send(Packet packet) throws IOException {
        byte[] buffer = packet.getData();
        DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(dPacket);

        if (!packet.expectsReply()) {
            return null;
        }

        buffer = new byte[Constants.MAX_PACKET_LENGTH];
        dPacket = new DatagramPacket(buffer, buffer.length, address, port);
        socket.receive(dPacket);

        buffer = dPacket.getData();
        return switch (buffer[0]) {
            case PacketType.LOGIN_ACK -> new LoginAckPacket(buffer);
            default -> throw new IllegalStateException("Unexpected packet type: " + buffer[0]);
        };
    }

    public void close() {
        socket.disconnect();
        socket.close();
    }
}
