package com.example.ugaxproject;

import java.io.IOException;
import java.net.*;

public class Client {
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    public Client(String host, int port) throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(host);
        this.port = port;
    }

    public String send(String msg) {
        try {
            byte[] buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, msg.length(), address, port);
            socket.send(packet);
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            return new String(packet.getData(), 0, packet.getLength());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        socket.close();
    }
}
