package iscot;

import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
//import iscot.events.AreaLightingOrderEvent;
import iscot.events.LightSensorEvent;
//import iscot.events.RegistrationEvent;

import javax.swing.*;
import java.awt.*;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("=== A INICIAR TESTE LOCAL DA FASE 3 ===");

        // 1. Testar o evento de Registo
        //RegistrationEvent regEvent = new RegistrationEvent(Type.SENSOR, Areas.LivingRoom, "SensorLuz_1");
        //System.out.println("Registo criado: " + regEvent);

        // 2. Testar o evento de Sensor de Luz
        LightSensorEvent lightEvent = new LightSensorEvent(Areas.LivingRoom, "SensorLuz_1", 320.0);
        System.out.println("Evento de sensor gerado: " + lightEvent);

        // 3. Testar uma Ordem de Alto Nível (Decisor -> Atuador)
        //AreaLightingOrderEvent orderEvent = new AreaLightingOrderEvent(Areas.LivingRoom, "Decisor_1", true, 80);
        //System.out.println("Ordem de decisor gerada: " + orderEvent);

        // 4. Testar visualmente a GUI do Atuador (Lâmpada) para veres a interacção da Fase 1/3
        SwingUtilities.invokeLater(() -> {
            JFrame janelaTeste = new JFrame("Teste Visual - Atuador Lâmpada");
            janelaTeste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janelaTeste.setSize(200, 200);
            janelaTeste.setLayout(new FlowLayout());

            // Criar o ícone da lâmpada fornecido pelo código base
            LightIcon lampadaIcon = new LightIcon();

            // Simular o comportamento de receber a ordem: ligar a lâmpada com intensidade 80
            //lampadaIcon.setLigada(orderEvent.getState());
            //lampadaIcon.setIntensidade(orderEvent.getIntensidade());

            janelaTeste.add(lampadaIcon);
            janelaTeste.setLocationRelativeTo(null);
            janelaTeste.setVisible(true);

            System.out.println("Janela gráfica aberta com sucesso! A lâmpada está ligada? " + lampadaIcon.isLigada());
        });
    }
}
