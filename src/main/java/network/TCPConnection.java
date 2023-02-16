package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final Thread rxTread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader input;
    private final BufferedWriter output;


    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        rxTread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while (!rxTread.isInterrupted()) {
                        eventListener.onReceiveString(TCPConnection.this, input.readLine());
                    }

                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }

            }
        });
        rxTread.start();
    }

    public synchronized void sendString(String value) {
        try {
            output.write(value + "\r\n");
            output.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        rxTread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return socket.getInetAddress() + ": " + socket.getPort();
    }
}
