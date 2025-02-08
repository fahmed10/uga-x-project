package com.example.ugaxproject;

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
    private static final int TIMEOUT = 3000;

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

        buffer = new byte[512];
        dPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(dPacket);

        buffer = dPacket.getData();
        return switch (buffer[0]) {
            case PacketType.LOGIN_ACK -> new LoginAckPacket(buffer);
            default -> throw new IllegalStateException("Unexpected packet type: " + buffer[0]);
        };
    }

    public void close() {
        socket.close();
    }
}
