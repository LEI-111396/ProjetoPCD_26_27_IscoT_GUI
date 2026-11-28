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

import iscot.client.gui.IrrigationIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.Atuadores.AreaIrrigationOrderEvent;
import iscot.events.RegistrationEvent;

public class Irrigation {

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
				out.flush();
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

				// Registar com o servidor
				out.writeObject(new RegistrationEvent(Type.ACTUATOR, area, deviceId));
				out.flush();

				JFrame janela = new JFrame("Irrigação em "+area);
				janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			// Criar os componentes gráficos
				JPanel painel=new JPanel(new BorderLayout());

				IrrigationIcon irrigation = new IrrigationIcon();
				irrigation.setAtiva(false);
				painel.add(irrigation,BorderLayout.CENTER);

				janela.setContentPane(painel);
				janela.setLocation(600, 400);
				janela.pack();
				janela.setVisible(true);

				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
				while (true) {
					Object obj = in.readObject();
					if (obj instanceof AreaIrrigationOrderEvent order && order.getArea() == area) {
						boolean ativar = order.isActive();
						int zona = irrigation.getZona();
						int duracao = order.getDuration();

						SwingUtilities.invokeLater(() -> {
							irrigation.setAtiva(ativar);
							if (ativar) {
								irrigation.setZona(zona);
								irrigation.setDuracao(duracao);
							}
						});

						String estadoTexto = ativar
								? "Rega ligada (Zona " + zona + " - " + duracao + " min)"
								: "Rega desligada";
						System.out.println("[" + deviceId + "] " + estadoTexto + " " + LocalTime.now().format(fmt));
					}
				}
			} catch (Exception e) {
				System.err.println("[IRRIGACAO] Erro: " + e.getMessage());
			}

		}
	}
}
