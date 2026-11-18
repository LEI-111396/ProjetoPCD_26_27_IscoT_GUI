package iscot.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.IrrigationIcon;
import iscot.client.gui.IrrigationIcon;
import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.*;

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
        if(args.length!=3) {
            System.err.println("Invalid arguments!");
        }
        else {
            // TODO connect to the server

            // TODO register with the server
            Areas area=Areas.valueOf(args[2]);
            String deviceId = args[1];


            JFrame janela = new JFrame("Humidade do Solo em "+area);
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            // Criar os componentes gráficos
            JPanel painel=new JPanel();
            painel.setLayout(new BorderLayout());
            //teste
            IrrigationIcon irrigationIcon = new IrrigationIcon();
            irrigationIcon.setAtiva(false);
            SensorControl sensorControl = new SensorControl(
                    "🌱", "Humidade do Solo - " + area, "%",
                    0, 100, 1, 50,
                    valor -> {
                        GroundMoistureSensorEvent evento = new GroundMoistureSensorEvent(area, deviceId, valor);
                        System.out.println("[SENSOR] " + evento);
                        AreaIrrigationOrderEvent ordemRega = new AreaIrrigationOrderEvent(area, "Rega_1", false, 0);
                        if (valor < 30) {

                            irrigationIcon.setAtiva(true);
                            irrigationIcon.setDuracao(10);
                            System.out.println("[ATUADOR] " + ordemRega + " - Solo seco, ligar rega");
                        } else {

                            irrigationIcon.setAtiva(false);
                            System.out.println("[ATUADOR] " + ordemRega + " - Solo húmido, desligar rega");
                        }
                        // TODO handle change in sensor
                        System.out.println("[SENSOR] " + evento);
                        System.out.println("Mudou para:"+valor);
                    });
            painel.add(sensorControl,BorderLayout.CENTER);
            painel.add(irrigationIcon, BorderLayout.EAST);
            janela.setContentPane(painel);
            janela.setLocation(600, 400);
            janela.setMinimumSize(janela.getSize());
            janela.pack();
            janela.setVisible(true);


        }
    }

}
