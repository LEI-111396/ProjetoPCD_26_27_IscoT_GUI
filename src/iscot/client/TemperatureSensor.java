package iscot.client;

import java.awt.BorderLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.ACIcon;
import iscot.client.gui.LightIcon;
import iscot.client.gui.SensorControl;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.*;

public class TemperatureSensor {

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

            JFrame janela = new JFrame("Temperatura em "+ area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            try {
                // Ligar ao servidor
                Socket socket = new Socket(host, port);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                // Registar com o servidor
                out.writeObject(new RegistrationEvent(Type.SENSOR, area, deviceId));
                out.flush();

            // Criar os componentes gráficos
            JPanel painel=new JPanel();
            painel.setLayout(new BorderLayout());

            SensorControl sensorControl= new SensorControl( "☀", "Temperatura - "+area, "%",
                    0, 100, 1, 50,
                    valor -> {
                        TemperatureSensorEvent evento = new TemperatureSensorEvent(area, deviceId, valor);
                        System.out.println("[SENSOR] " + evento);
                        System.out.println("Mudou para:"+valor);
                    });
            painel.add(sensorControl,BorderLayout.CENTER);
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
