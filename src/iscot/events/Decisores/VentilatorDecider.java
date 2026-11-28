package iscot.events.Decisores;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.Atuadores.AreaVentilationOrderEvent;
import iscot.events.RegistrationEvent;
import iscot.events.Sensores.HumiditySensorEvent;

public class VentilatorDecider {

    /**
     * Argumentos: <host> <port> <AREA> <deviceId> <limiteHumidade>
     * Exemplo: localhost 8080 WC decider_vent1 60
     */
    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Invalid arguments! Uso: <host> <port> <AREA> <deviceId> <limiteHumidade>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Areas area = Areas.valueOf(args[2]);
        String deviceId = args[3];
        double limiteHumidade = Double.parseDouble(args[4]);

        try (Socket socket = new Socket(host, port)) {
            // Criar ObjectOutputStream antes de ObjectInputStream para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 1. Registar no servidor como DECIDER
            out.writeObject(new RegistrationEvent(Type.DECIDER, area, deviceId));
            out.flush();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println("[" + deviceId + "] VentilationDecider ativo em " + area + " (Limite Humidade: " + limiteHumidade + "%)");

            // 2. Escutar eventos do sensor de humidade e emitir ordens para o ventilador
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof HumiditySensorEvent humEvent && humEvent.getArea() == area) {
                    double humidadeAtual = humEvent.getHumidity();

                    // Liga o ventilador APENAS se a humidade medida estiver acima do limite passado como argumento
                    boolean ligar = humidadeAtual > limiteHumidade;

                    AreaVentilationOrderEvent order = new AreaVentilationOrderEvent(area, deviceId, ligar);
                    out.writeObject(order);
                    out.flush();
                    out.reset();

                    String estadoTexto = ligar ? "Ligar ventilador" : "Desligar ventilador";
                    System.out.println("[" + deviceId + "] Humidade lida: " + humidadeAtual + "% (Limite: " + limiteHumidade + "%) -> "
                            + estadoTexto + " [" + LocalTime.now().format(fmt) + "]");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[VENTILATION_DECIDER] Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}
