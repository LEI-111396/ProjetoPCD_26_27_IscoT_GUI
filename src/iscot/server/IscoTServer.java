package iscot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class IscoTServer {

    private final int port;
    private final List<DealWithClient> clients = new ArrayList<>();
    private int nextClientId = 1;

    public IscoTServer(int port) {
        this.port = port;
    }

    public void startServing() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVIDOR] IscoT iniciado no porto " + port + ". À espera de clientes...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                DealWithClient handler = new DealWithClient(clientSocket, this);
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("[SERVIDOR] Erro no ServerSocket: " + e.getMessage());
        }
    }

    public synchronized int registerClient(DealWithClient client) {
        int assignedId = nextClientId++;
        clients.add(client);
        System.out.println("[SERVIDOR] Cliente registado -> ID: " + assignedId
                + " | Tipo: " + client.getClientType()
                + " | Área: " + client.getArea()
                + " | Dispositivo: " + client.getDeviceId()
                + " (Total ligados: " + clients.size() + ")");
        return assignedId;
    }

    public synchronized void removeClient(DealWithClient client) {
        if (clients.remove(client)) {
            System.out.println("[SERVIDOR] Cliente desligado -> ID: " + client.getClientId()
                    + " (" + client.getDeviceId() + "). Total ligados: " + clients.size());
        }
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        new IscoTServer(port).startServing();
    }
}