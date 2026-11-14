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
        if(args.length!=3) {
            System.err.println("Invalid arguments!");
        }
        else {
            // TODO connect to the server

            // TODO register with the server
            Areas area=Areas.valueOf(args[2]);
            String deviceId = args[1];


            JFrame janela = new JFrame("Luz em "+area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            // Criar os componentes gráficos
            JPanel painel=new JPanel();
            painel.setLayout(new BorderLayout());
            //teste
            LightIcon lightIcon = new LightIcon();
            lightIcon.setLigada(false);
            SensorControl sensorControl= new SensorControl( "☀", "Luz - "+area, "%",
                    0, 100, 1, 50,
                    valor -> {

                        LightSensorEvent evento = new LightSensorEvent(area, deviceId, valor);
                        AreaLightingOrderEvent eventoLight = new  AreaLightingOrderEvent(area, "Luz_1", false, 100);
                        if (valor < 50) {
                            lightIcon.setLigada(true); // tem de receber true
                            lightIcon.setIntensidade(100);
                            System.out.println("[ATUADOR] " + eventoLight + " - Luz insuficiente, ligar luz");
                        } else {

                            lightIcon.setLigada(false);
                            lightIcon.setIntensidade(0);
                            System.out.println("[ATUADOR] " + eventoLight + " - Luz suficiente, desligar luz");
                        }
                        // TODO handle change in sensor
                        System.out.println("[SENSOR] " + evento);
                        System.out.println("Mudou para:"+valor);
                    });
            painel.add(sensorControl,BorderLayout.CENTER);
            painel.add(lightIcon, BorderLayout.EAST);
            janela.setContentPane(painel);
            janela.setLocation(600, 400);
            janela.setMinimumSize(janela.getSize());
            janela.pack();
            janela.setVisible(true);


        }
    }

}
