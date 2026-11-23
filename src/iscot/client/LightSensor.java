package iscot.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import iscot.client.gui.LightIcon;
import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.*;

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
            // TODO connect to the server

            // TODO register with the server
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            Areas area=Areas.valueOf(args[2]);
            String deviceId = args[3];


            JFrame janela = new JFrame("Luz em "+area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            // Criar os componentes gráficos
            JPanel painel=new JPanel();
            painel.setLayout(new BorderLayout());
            //teste


            SensorControl sensorControl= new SensorControl( "☀", "Luz - "+area, "%",
                    0, 100, 1, 50,
                    valor -> {

                        LightSensorEvent evento = new LightSensorEvent(area, deviceId, valor);

                        // TODO handle change in sensor
                        System.out.println("[SENSOR] " + evento);
                        System.out.println("Mudou para:" + valor);
                    });
            painel.add(sensorControl,BorderLayout.CENTER);
            janela.setContentPane(painel);
            janela.setLocation(600, 400);
            janela.setMinimumSize(janela.getSize());
            janela.pack();
            janela.setVisible(true);


        }
    }

}
