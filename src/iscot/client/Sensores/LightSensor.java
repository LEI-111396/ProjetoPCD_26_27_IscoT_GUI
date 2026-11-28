package iscot.client.Sensores;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.*;
import iscot.events.Sensores.LightSensorEvent;

public class LightSensor {

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

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                // Registar com o servidor
                out.writeObject(new RegistrationEvent(Type.SENSOR, area, deviceId));
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                // Criar os componentes gráficos
                JFrame janela = new JFrame("Luz em "+area);
                janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel painel=new JPanel(new BorderLayout());

                SensorControl sensorControl= new SensorControl( "☀", "Luz - "+area, "%",
                        0, 100, 1, 50,
                        valor -> {
                            try {
                                LightSensorEvent evento = new LightSensorEvent(area, deviceId, valor);
                                out.writeObject(evento);
                                out.flush();
                                out.reset();
                                System.out.println("[SENSOR] Enviado para servidor: " + evento + " (" + valor + "%)");
                            } catch (IOException e) {
                                System.err.println("[SENSOR] Erro ao enviar evento: " + e.getMessage());
                            }
                        });
                painel.add(sensorControl,BorderLayout.CENTER);
                janela.setContentPane(painel);
                janela.setLocation(200, 400);
                janela.pack();
                janela.setVisible(true);

            }
            catch (Exception e) {
                    System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
                }
        }
    }

}
