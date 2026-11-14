package iscot.client;

import java.awt.BorderLayout;

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
            ACIcon ACIcon = new ACIcon();
            ACIcon.setModo(AirConditioningMode.Desligado);
            VentilatorIcon ventilatorIcon = new VentilatorIcon();
            ventilatorIcon.setLigado(false);
            SensorControl sensorControl= new SensorControl( "☀", "Temperatura - "+area, "%",
                    0, 100, 1, 50,
                    valor -> {

                        TemperatureSensorEvent temperatura = new TemperatureSensorEvent(area, deviceId, valor);
                        AreaTemperatureOrderEvent eventoTemperaturaAC = new  AreaTemperatureOrderEvent(area, "AC_1", AirConditioningMode.Desligado, 100);
                        AreaVentilationOrderEvent eventoVentilador = new AreaVentilationOrderEvent(area, "Ventilator_1", false);
                        if (valor < 40) {


                            ACIcon.setAtivo(true); // tem de receber true
                            ventilatorIcon.setLigado(false);
                            System.out.println("[ATUADOR] " + temperatura + " - Temperatura baixa, ligar AC");
                        } else if (valor > 60){

                            ACIcon.setAtivo(false);
                            ventilatorIcon.setLigado(true);
                            System.out.println("[ATUADOR] " + temperatura + " - Temperatura alta, ligar ventilador");
                        } else {
                            ACIcon.setAtivo(false);
                            ventilatorIcon.setLigado(false);
                            System.out.println("[ATUADOR] " + temperatura + " - Temperatura normal, desligar AC e Ventilador");
                        }


                            System.out.println("[ATUADOR] " + temperatura + " - Luz suficiente, desligar luz");
                        // TODO handle change in sensor
                        System.out.println("[SENSOR] " + temperatura);
                        System.out.println("Mudou para:"+valor);
                    });
            painel.add(sensorControl,BorderLayout.CENTER);
            painel.add(ACIcon, BorderLayout.EAST);
            painel.add(ventilatorIcon, BorderLayout.WEST);
            janela.setContentPane(painel);
            janela.setLocation(600, 400);
            janela.setMinimumSize(janela.getSize());
            janela.pack();
            janela.setVisible(true);


        }
    }

}
