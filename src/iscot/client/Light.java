package iscot.client;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.events.AreaLightingOrderEvent;
import iscot.events.RegistrationEvent;

public class Light {

    /**
     *
     * @param server    address and port
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
                // Ligar ao servidor
                Socket socket = new Socket(host, port);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                // Registar com o servidor
                out.writeObject(new RegistrationEvent(Type.ACTUATOR, area, deviceId));
                out.flush();

                JFrame janela = new JFrame("Luz em " + area);
                janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // Criar os componentes gráficos
                JPanel painel = new JPanel(new BorderLayout());
                LightIcon light = new LightIcon();
                light.setLigada(false);
                painel.add(light, BorderLayout.CENTER);

                janela.setContentPane(painel);
                janela.setLocation(600, 400);
                janela.setMinimumSize(janela.getSize());
                janela.pack();
                janela.setVisible(true);

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof AreaLightingOrderEvent order && order.getArea() == area) {
                        boolean ligar = order.getState();
                        int intensidade = order.getIntensity();

                        SwingUtilities.invokeLater(() -> {
                            light.setLigada(ligar);
                            light.setIntensidade(intensidade);
                        });
                        String estadoTexto = ligar ? "Lâmpada ligada (" + intensidade + "%)" : "Lâmpada desligada";
                        System.out.println("[" + deviceId + "] " + estadoTexto + " " + LocalTime.now().format(fmt));

                    }
                }
            } catch (Exception e) {
                System.err.println("[LUZ] Erro: " + e.getMessage());
            }
        }
    }
}
