package mdcc.sd.mqtt;


import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ServicoAlarmes {
    private static final String BROKER = "tcp://localhost:32768";							// URL do broker MQTT.
    private static final String TOPICO_AUMENTO_TEMPERATURA = "aumento_temperatura_rept"; 	// Tópico de aumento_temperatura_rept
    private static final String TOPICO_TEMPERATURA_ALTA = "temperatura_alta"; 				// Tópico de temperatura_alta

  
    private MqttClient clienteMqtt;
    
    /**
     * Inicializando o cliente MQTT com o endereço do broker, um ID de cliente gerado e uma persistência em memória
     * @throws MqttException
     */
    
    public ServicoAlarmes() throws MqttException {
        clienteMqtt = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
    }

    public void iniciar() throws MqttException {
    	// Conectando o cliente MQTT ao broker
        clienteMqtt.connect();
        // Subscrevendo aos tópicos de aumento de temperatura e temperatura alta, passando o método exibirAlarme como callback
        clienteMqtt.subscribe(TOPICO_AUMENTO_TEMPERATURA, this::exibirAlarme);
        clienteMqtt.subscribe(TOPICO_TEMPERATURA_ALTA, this::exibirAlarme);
    }

    private void exibirAlarme(String topico, MqttMessage mensagem) {
    	// Convertendo o payload da mensagem MQTT para uma string
    	String alarme = new String(mensagem.getPayload());
    	// Variável para armazenar o tipo de alerta
        String tipoAlerta = "";
    	// Verificando se o tópico é de aumento de temperatura
        if(topico.equals(TOPICO_AUMENTO_TEMPERATURA)) {
        	// Chamando o método mensagem para exibir um alerta com o tipo "ATENCAO" e a mensagem do alarme
        	tipoAlerta = "| ALERTA : ";
        	mensagem("ATENCAO"," ALERTA : " + alarme);
        	
        }
        // Verificando se o tópico é de temperatura alta
        if(topico.equals(TOPICO_TEMPERATURA_ALTA)) {
        	// Chamando o método mensagem para exibir um alerta com o tipo "PERIGO" e a mensagem do alarme
        	tipoAlerta = "| RISCO  : ";
        	mensagem("PERIGO"," RISCO : " + alarme);
        }
        // Imprimindo o tipo de alerta e a mensagem do alarme
        System.out.println( tipoAlerta + alarme );

    }
    
    /*
     * Cria Popup de alerta usando java swing
     */
    
    private static boolean botaoOKPressionado = false;

    public void mensagem(String tituloMensagem,String conteudoMensagem) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JOptionPane.showMessageDialog(frame, conteudoMensagem, tituloMensagem, JOptionPane.WARNING_MESSAGE);
        botaoOKPressionado = true;

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if (botaoOKPressionado) {
            fecharTodasJanelas();
        }
    }

    
    public static void fecharTodasJanelas() {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JFrame) {
                window.dispose();
            }
        }
    }

}