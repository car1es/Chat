package server;

import logger.Logger;
import network.TCPConnection;
import network.TCPConnectionListener;
import settings.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public class Server implements TCPConnectionListener {
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    Logger logger = Logger.getInstance();
    private static final int PORT = new Settings().getPort();

    private Server() {
        logger.log("Server running");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // порт из настроек
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    logger.log("TCPConnection exception " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        logger.log("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
    sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        logger.log("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        logger.log("TCPConnection exception " + e);
    }

    private void sendToAllConnections(String value) {
        logger.log(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) {
            connections.get(i).sendString(value);
        }
    }

}
