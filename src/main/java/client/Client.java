package client;

import network.TCPConnection;
import network.TCPConnectionListener;
import settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client implements TCPConnectionListener {
    private TCPConnection connection;
    private String nickName;
    private static final String IP_ADDR = new Settings().getHost();
    private static final int PORT = new Settings().getPort();

    public static void main(String[] args) {
        new Client();
    }

    private Client() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\"Добро пожаловать в наш чат! Для выхода из чата напишите - exit. Введите ваше имя/nickname \"");
            nickName = reader.readLine();
            connection = new TCPConnection(this, IP_ADDR, PORT);

            while (true) {
                String msg = reader.readLine();
                if (msg.contains("exit")) {
                    connection.disconnect();
                    break;
                }
                connection.sendString(nickName + ": " + msg);
            }
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }


    }

    @Override
    public String toString() {
        return nickName;
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);
    }

    private void printMsg(String msg) {
        System.out.println(msg);
    }
}