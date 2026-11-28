package iscot.events.Decisores;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.Atuadores.AreaLightingOrderEvent;
import iscot.events.RegistrationEvent;
import iscot.events.Sensores.LightSensorEvent;

public class LightDecider {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 5) {
            System.err.println("Invalid arguments!");
        } else {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            Areas area = Areas.valueOf(args[2]);
            String deviceId = args[3];
            double limiteIntensidade = Double.parseDouble(args[4]);

            try {
                // Ligar ao servidor
                Socket socket = new Socket(host, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                // Registar com o servidor
                out.writeObject(new RegistrationEvent(Type.DECIDER, area, deviceId));
                out.flush();

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
                System.out.println("[" + deviceId + "] LightDecider ativo em " + area + " (Limite: " + limiteIntensidade + "%)");


                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof LightSensorEvent lightEvent && lightEvent.getArea()== area) {
                        double luzAtual = lightEvent.getLight();
                        boolean ligar = luzAtual < limiteIntensidade;
                        int intensidade = ligar ? 100 : 0;
                        AreaLightingOrderEvent order =
                                new AreaLightingOrderEvent(lightEvent.getArea(), deviceId, ligar, intensidade);
                        out.writeObject(order);
                        out.flush();
                        out.reset();

                        String estadoTexto = ligar ? "Ligar lâmpada (" + intensidade + "%)" : "Desligar lâmpada";
                        System.out.println("[" + deviceId + "] Luz lida: " + luzAtual + "% (Limite: " + limiteIntensidade + "%) -> " + estadoTexto + " [" + LocalTime.now().format(fmt) + "]");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}