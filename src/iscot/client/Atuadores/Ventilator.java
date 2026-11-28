package iscot.client.Atuadores;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import iscot.client.gui.VentilatorIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.Atuadores.AreaVentilationOrderEvent;

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
		if (args.length != 4) {

			System.err.println("Invalid arguments!");
		} else {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			Areas area = Areas.valueOf(args[2]);
			String deviceId = args[3];

			try {

				//Ligar ao servidor
				Socket socket = new Socket(host, port);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

				//Registar no servidor
				out.writeObject(new iscot.events.RegistrationEvent(Type.ACTUATOR, area, deviceId));
				out.flush();
				//Parte Grafica
				JFrame janela1 = new JFrame("Ventilador em " + area);
				janela1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// Criar os componentes gráficos
				JPanel painel = new JPanel(new BorderLayout());
				VentilatorIcon ventilator = new VentilatorIcon();
				ventilator.setLigado(false);
				painel.add(ventilator, BorderLayout.CENTER);

				janela1.setContentPane(painel);
				janela1.setLocation(600, 400);
				janela1.setMinimumSize(janela1.getSize());
				janela1.pack();
				janela1.setVisible(true);

				// Loop para receber eventos do servidor
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
				while (true) {
					Object obj = in.readObject();
					if (obj instanceof AreaVentilationOrderEvent order && order.getArea() == area) {
						boolean ligar = order.getState();

						SwingUtilities.invokeLater(() -> ventilator.setLigado(ligar));

						String estadoTexto = ligar ? "Ventilador ligado" : "Ventilador desligado";
						System.out.println("[" + deviceId + "] " + estadoTexto + " " + LocalTime.now().format(fmt));
					}
				}
			} catch (Exception e) {
				System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
			}
		}
	}
}
