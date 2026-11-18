package iscot.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.IrrigationIcon;
import iscot.client.gui.LightIcon;
import iscot.client.gui.SensorControl;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.*;

public class HumiditySensor {

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
			

			JFrame janela = new JFrame("Humidade em "+area);
			janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());
			IrrigationIcon irrigationIcon = new IrrigationIcon();

			SensorControl sensorControl= new SensorControl( "☀", "Humidade - "+area, "%",
				    0, 100, 1, 50,
				    valor -> {
				    	
				        HumiditySensorEvent evento = new HumiditySensorEvent(area, deviceId, valor);
						AreaIrrigationOrderEvent irrigationOrderEvent = new AreaIrrigationOrderEvent(area, "Irrigacao_1", false, 10);
				        if (valor < 50) {
				        	AreaIrrigationOrderEvent irrigation = new  AreaIrrigationOrderEvent(area, "Irrigacao_1", true, 10);
				        	irrigationIcon.setAtiva(irrigation.isActive());
							System.out.println("[ATUADOR] " + irrigation + " - Humidade baixa, ligar rega");
				        } else {
							irrigationIcon.setAtiva(false);
							System.out.println("[ATUADOR] " + irrigationOrderEvent + " - Humidade suficiente, desligar rega");
				        }
				    	// TODO handle change in sensor
				    	System.out.println("Mudou para:"+valor);
				    });
			painel.add(sensorControl,BorderLayout.CENTER);
			painel.add(irrigationIcon,BorderLayout.EAST);
			janela.setContentPane(painel);
			janela.setLocation(600, 400);
			janela.setMinimumSize(janela.getSize());
			janela.pack();
			janela.setVisible(true);
			
			
		}
	}

}
