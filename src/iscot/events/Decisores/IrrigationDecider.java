package iscot.events.Decisores;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.Atuadores.AreaIrrigationOrderEvent;
import iscot.events.RegistrationEvent;
import iscot.events.Sensores.GroundMoistureSensorEvent;

public class IrrigationDecider {

    /**
     * Argumentos: <host> <port> <AREA> <deviceId> <limiteHumidadeSolo>
     * Exemplo: localhost 8080 GARDEN decider_rega1 40
     */
    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Invalid arguments! Uso: <host> <port> <AREA> <deviceId> <limiteHumidadeSolo>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Areas area = Areas.valueOf(args[2]);
        String deviceId = args[3];
        double limiteHumidadeSolo = Double.parseDouble(args[4]);

        try (Socket socket = new Socket(host, port)) {
            // Criar ObjectOutputStream antes de ObjectInputStream para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 1. Registar no servidor como DECIDER
            out.writeObject(new RegistrationEvent(Type.DECIDER, area, deviceId));
            out.flush();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println("[" + deviceId + "] IrrigationDecider ativo em " + area + " (Limite Solo: " + limiteHumidadeSolo + "%)");

            // 2. Escutar eventos do sensor de humidade do solo e emitir ordens para a irrigação
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof GroundMoistureSensorEvent soilEvent && soilEvent.getArea() == area) {
                    double humidadeSoloAtual = soilEvent.getMoisture();

                    // Liga a rega APENAS se a humidade do solo estiver abaixo do limite passado como argumento
                    boolean ativar = humidadeSoloAtual < limiteHumidadeSolo;
                    int duracao = ativar ? 10 : 0;

                    AreaIrrigationOrderEvent order = new AreaIrrigationOrderEvent(area, deviceId, ativar, duracao);
                    out.writeObject(order);
                    out.flush();
                    out.reset();

                    String estadoTexto = ativar ? "Ativar rega (" + duracao + " min)" : "Desativar rega";
                    System.out.println("[" + deviceId + "] Solo lido: " + humidadeSoloAtual + "% (Limite: " + limiteHumidadeSolo + "%) -> " + estadoTexto + " [" + LocalTime.now().format(fmt) + "]");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[IRRIGATION_DECIDER] Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}