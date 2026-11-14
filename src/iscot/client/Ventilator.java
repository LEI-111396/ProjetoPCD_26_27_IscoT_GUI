package iscot.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.LightIcon;
import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.AreaVentilationOrderEvent;
import iscot.events.HumiditySensorEvent;

public class Ventilator {

	/**
	 * 
	 * @param server address and port
	 * @param roomName: string
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=3) {

			System.err.println("Invalid arguments!");
		}
		else {
			// TODO connect to server
			
			// TODO register with the server
			Areas area=Areas.valueOf(args[2]);
			
			JFrame janela1 = new JFrame("Ventilador em "+area);
			janela1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
			
			// Criar os componentes gráficos
			JPanel painel=new JPanel();
			painel.setLayout(new BorderLayout());

			AreaVentilationOrderEvent ventilationOrderEvent = new AreaVentilationOrderEvent(area, "Ventilator_1", false);
			VentilatorIcon ventilator = new VentilatorIcon();
			painel.add(ventilator,BorderLayout.CENTER);
			janela1.setContentPane(painel);
			janela1.setLocation(600, 400);
			janela1.setMinimumSize(janela1.getSize());
			janela1.pack();
			janela1.setVisible(true);
			
			Thread.sleep(3000);
			System.out.println("Ventilador ligado" + ventilationOrderEvent);
			ventilator.setAtivo(true);

			// TODO: receive and handle messages from server
			
			// TODO: for first phase: create an appropriate event hardcoded locally and change the GUI accordingly
		}
	}
}
