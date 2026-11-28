package iscot.events.Decisores;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.Atuadores.AreaTemperatureOrderEvent;
import iscot.events.RegistrationEvent;
import iscot.events.Sensores.TemperatureSensorEvent;

public class ACDecider {

    /**
     * Argumentos: <host> <port> <AREA> <deviceId> <temperaturaAlvo>
     * Exemplo: localhost 8080 LIVINGROOM decider_ac1 22
     */
    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Invalid arguments! Uso: <host> <port> <AREA> <deviceId> <temperaturaAlvo>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Areas area = Areas.valueOf(args[2]);
        String deviceId = args[3];
        double tempAlvo = Double.parseDouble(args[4]);

        try (Socket socket = new Socket(host, port)) {
            // Criar ObjectOutputStream antes de ObjectInputStream para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // 1. Registar no servidor como DECIDER
            out.writeObject(new RegistrationEvent(Type.DECIDER, area, deviceId));
            out.flush();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
            System.out.println("[" + deviceId + "] ACDecider ativo em " + area + " (Alvo: " + tempAlvo + "°C)");

            // 2. Escutar eventos do sensor de temperatura e emitir ordens para o AC
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof TemperatureSensorEvent tempEvent && tempEvent.getArea() == area) {
                    double tempAtual = tempEvent.getTemperature();
                    AirConditioningMode modo;

                    if (tempAtual > tempAlvo + 1.0) {
                        modo = AirConditioningMode.Frio;
                    } else if (tempAtual < tempAlvo - 1.0) {
                        modo = AirConditioningMode.Aquecimento;
                    } else {
                        modo = AirConditioningMode.Desligado;
                    }

                    // Garante que a temperatura enviada está dentro dos limites válidos do ACIcon (16°C a 30°C)
                    double tempValidada = Math.max(16.0, Math.min(30.0, tempAlvo));

                    AreaTemperatureOrderEvent order = new AreaTemperatureOrderEvent(area, deviceId, modo, tempValidada);
                    out.writeObject(order);
                    out.flush();
                    out.reset();

                    System.out.println("[" + deviceId + "] Temp lida: " + tempAtual + "°C (Alvo: " + tempValidada + "°C) -> AC "
                            + modo + " [" + LocalTime.now().format(fmt) + "]");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[AC_DECIDER] Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}