package iscot.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.Event;
import iscot.events.RegistrationEvent;

public class DealWithClient extends Thread {

    private final Socket socket;
    private final IscoTServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private int clientId;
    private Type clientType;
    private Areas area;
    private String deviceId;

    public DealWithClient(Socket socket, IscoTServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Ordem no servidor: primeiro OUT, depois IN
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // 1. Ler a mensagem inicial de registo
            Object firstMessage = in.readObject();
            if (!(firstMessage instanceof RegistrationEvent reg)) {
                System.err.println("[SERVIDOR] Ligação recusada: primeira mensagem não é RegistrationEvent.");
                return;
            }

            // 2. Validar o tipo de cliente (ignorar DWC conforme enum)
            if (reg.getType() == null || reg.getType() == Type.DWC) {
                System.err.println("[SERVIDOR] Ligação recusada: tipo de cliente inválido (" + reg.getType() + ").");
                return;
            }

            this.clientType = reg.getType();
            this.area = reg.getArea();
            this.deviceId = reg.getDeviceId();
            this.clientId = server.registerClient(this);

            // 3. Ciclo de escuta para eventos futuros (Fase 5)
            while (true) {
                Event event = (Event) in.readObject();
                System.out.println("[SERVIDOR] Evento recebido de ID " + clientId + ": " + event);
            }

        } catch (EOFException | SocketException e) {
            // Ocorre normalmente quando o utilizador fecha a janela do cliente
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[SERVIDOR] Erro na comunicação com o cliente " + clientId + ": " + e.getMessage());
        } finally {
            server.removeClient(this);
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {
        }
    }

    public int getClientId() { return clientId; }
    public Type getClientType() { return clientType; }
    public Areas getArea() { return area; }
    public String getDeviceId() { return deviceId; }
}