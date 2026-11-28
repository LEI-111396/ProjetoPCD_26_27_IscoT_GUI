package iscot.client.Sensores;

import java.awt.BorderLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.*;
import iscot.events.Sensores.GroundMoistureSensorEvent;

public class GroundMoisterSensor {

    /**
     *
     * @param server address and port
     * @param roomName: string
     * @throws IOException
     * @throws UnknownHostException
     * @throws NumberFormatException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        if(args.length!=4) {
            System.err.println("Invalid arguments!");
        }
        else {

            String host = args[0];
            int port = Integer.parseInt(args[1]);
            Areas area=Areas.valueOf(args[2]);
            String deviceId = args[3];
            try {
                // Ligar ao servidor
                Socket socket = new Socket(host, port);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                // Registar com o servidor
                out.writeObject(new RegistrationEvent(Type.SENSOR, area, deviceId));
                out.flush();


                JFrame janela = new JFrame("Humidade do Solo em " + area);
                janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


                // Criar os componentes gráficos
                JPanel painel = new JPanel();
                painel.setLayout(new BorderLayout());

                SensorControl sensorControl = new SensorControl(
                        "🌱", "Humidade do Solo - " + area, "%",
                        0, 100, 1, 50,
                        valor -> {
                            try {
                                GroundMoistureSensorEvent evento = new GroundMoistureSensorEvent(area, deviceId, valor);
                                out.writeObject(evento);
                                out.flush();
                                System.out.println("[SENSOR] " + evento);
                                System.out.println("Mudou para:" + valor);
                            } catch (Exception e) {
                                System.err.println("[SENSOR] Erro ao enviar evento: " + e.getMessage());
                            }
                        });
                painel.add(sensorControl, BorderLayout.CENTER);
                janela.setContentPane(painel);
                janela.setLocation(600, 400);
                janela.setMinimumSize(janela.getSize());
                janela.pack();
                janela.setVisible(true);

            }
            catch (Exception e) {
                    System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            }
        }
    }

}
