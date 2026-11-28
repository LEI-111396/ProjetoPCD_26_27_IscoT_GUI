package iscot.server;

import iscot.events.Atuadores.AreaLightingOrderEvent;
import iscot.events.Event;
import iscot.events.Sensores.LightSensorEvent;

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
    public synchronized void routeEvent(Event event) {
      if(event instanceof iscot.events.SensorEvent) {
          boolean hasDecider = false;
          for (DealWithClient client : clients) {
              if (client.getClientType() == iscot.enums.Type.DWC && client.getArea() == event.getArea()) {
                  client.sendEvent(event);
              }
          }
          if (!hasDecider && event instanceof LightSensorEvent lightEvent) {
              boolean ligar = lightEvent.getLight() < 50;
              int intensidade = ligar ? 100 : 0;
              AreaLightingOrderEvent order =
                      new AreaLightingOrderEvent(lightEvent.getArea(), "ServerAuto", ligar, intensidade);
              routeEvent(order); // Reenvia como ordem para os atuadores da área
          }
      }
      else if (event instanceof iscot.events.HighLevelOrderEvent) {
          for (DealWithClient client : clients) {
              if (client.getClientType() == iscot.enums.Type.ACTUATOR && client.getArea() == event.getArea()) {
                  client.sendEvent(event);
              }
          }
      }
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        new IscoTServer(port).startServing();
    }
}