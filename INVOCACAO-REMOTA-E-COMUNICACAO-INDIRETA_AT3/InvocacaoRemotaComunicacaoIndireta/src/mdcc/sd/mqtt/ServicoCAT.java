package mdcc.sd.mqtt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 
 * @author Phyllipe
 * 
 * Esse código implementa um serviço de monitoramento de temperatura usando MQTT. 
 * Ele se conecta a um broker MQTT, recebe as leituras de temperatura do tópico 
 * "temperatura" e realiza algumas verificações.
 */
public class ServicoCAT {
    private static final String BROKER = "tcp://localhost:32768"; 							// URL do broker MQTT.
    private static final String TOPICO_TEMPERATURA = "temperatura"; 						// Tópico de temperatura.
    private static final String TOPICO_AUMENTO_TEMPERATURA = "aumento_temperatura_rept";	// Tópico de aumento de temperatura repentino.
    private static final String TOPICO_TEMPERATURA_ALTA = "temperatura_alta";				// Tópico de temperatura alta.
    private static final int TEMPO_AFERICAO_TEMPERATURA_MEDIA = 120;						// Tempo de aferição da temperatura média.
    private static final int LIMITE_TEMPERATURA_ALTA = 200;									// Limite de temperatura alta.
    private static final int MARGEM_DIFERENCA_TEMPERATURA = 5;								// Margem de diferença de temperatura para detecção de aumento repentino.
    
    private MqttClient clienteMqtt;				// Cliente do tipo MqttClient para estabelecer a conexão MQTT.
    private List<Double> medidasTemperaturas;	// Lista do tipo <Double> para armazenar as medidas de temperatura.
    /*
     * Declaração do construtor da classe ServicoCAT que lança uma exceção MqttException.
     * Inicialização da variável clienteMqtt com um novo objeto MqttClient passando o BROKER, 
     * um ID de cliente gerado e uma instância de MemoryPersistence.
     */
    public ServicoCAT() throws MqttException {
        clienteMqtt = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
        //Inicialização da variável medidasTemperaturas com um novo objeto ArrayList.
        medidasTemperaturas = new ArrayList<>(); 
    }

    public void iniciar() throws MqttException {
    	//  Estabelecimento da conexão MQTT.
        clienteMqtt.connect();
        // Inscrição no tópico TOPICO_TEMPERATURA com uma função lambda que recebe o tópico e a mensagem.
        clienteMqtt.subscribe(TOPICO_TEMPERATURA, (topic, message) -> {
            double temperatura = Double.parseDouble(new String(message.getPayload()));
            // Chamada do método adicionarTemperatura passando a temperatura recebida.
            adicionarTemperatura(temperatura);
            // Cálculo da média da temperatura.
            double mediaTemperatura = calcularMediaTemperatura();
            // Verificação de aumento repentino de temperatura.
            verificarAumentoTemperaturaRepentina(mediaTemperatura);
            // Verificação de temperatura alta.
            verificarTemperaturaAlta(mediaTemperatura);
        });
    }
    
	/*
	 * Declaração do método adicionarTemperatura que recebe a temperatura.
	 * Adiciona a temperatura à lista de medidas de temperatura.
	 * Verifica se a lista de medidas de temperatura ultrapassou o tempo de aferição média.
	 * Remove da primeira medida de temperatura da lista.
	 */
    
    private void adicionarTemperatura(double temperatura) {
        medidasTemperaturas.add(temperatura);
        if (medidasTemperaturas.size() > TEMPO_AFERICAO_TEMPERATURA_MEDIA) {
            medidasTemperaturas.remove(0);
        }
    }
    
    /*
     * Método calcularMediaTemperatura que calcula a média da temperatura.
     * Declaração da variável soma do tipo double para acumular a soma das temperaturas.
     * Loop sobre as medidas de temperatura.
     * Acumula temperaturas na variável soma.
     * Cálcula da média da temperatura dividindo a soma pelo número de medidas.
     */
    
    private double calcularMediaTemperatura() {
        double soma = 0;
        for (double temperatura : medidasTemperaturas) {
            soma += temperatura;
        }
        sensorTemperaturaLOG(soma / medidasTemperaturas.size());
        return soma / medidasTemperaturas.size();
    }
    
    /*
     * Declara do método verificarAumentoTemperaturaRepentina que verifica se houve um aumento repentino na temperatura, 
     * lançando uma exceção MqttException.
     * Verifica se existem pelo menos duas medidas de temperatura.
     * Cálcula da média da temperatura anterior.
     * Verifica se a diferença absoluta entre a média atual e a média anterior é maior ou igual à margem de diferença de temperatura.
     * Publica de uma mensagem no tópico TOPICO_AUMENTO_TEMPERATURA com o conteúdo "AUMENTO DE TEMPERATURA REPENTINA!".
     */
    
    private void verificarAumentoTemperaturaRepentina(double mediaAtual) throws MqttException {
        if (medidasTemperaturas.size() >= 2) {
            double mediaAnterior = calcularMediaTemperaturaAnterior();
            if (Math.abs(mediaAtual - mediaAnterior) >= MARGEM_DIFERENCA_TEMPERATURA) {
                publicarMensagem(TOPICO_AUMENTO_TEMPERATURA, "AUMENTO DE TEMPERATURA REPENTINA!");
            }
        }
    }
    /**
     * Declara do método verificarTemperaturaAlta que verifica se a temperatura atual é alta, lançando uma exceção @throws MqttException.
     * Verifica se a temperatura atual é maior que o limite de temperatura alta.
     * Publica de uma mensagem no tópico TOPICO_TEMPERATURA_ALTA com o conteúdo "TEMPERATURA ALTA!".
     * 
     * @param mediaAtual
     * 
     */
    private void verificarTemperaturaAlta(double mediaAtual) throws MqttException {
        if (mediaAtual > LIMITE_TEMPERATURA_ALTA) {
            publicarMensagem(TOPICO_TEMPERATURA_ALTA, "TEMPERATURA ALTA!");
        }
    }
    
    /**
     * Declara do método calcularMediaTemperaturaAnterior que calcula a média da temperatura anterior.
     * Declara da variável tamanho do tipo int que representa o tamanho da lista de medidas de temperatura.
     * Declara da variável soma do tipo double para acumular a soma das temperaturas anteriores.
     * Loop sobre as duas últimas medidas de temperatura.
     * Acumula da temperatura na variável soma.
     * @return Cálculo da média da temperatura anterior dividindo a soma por 2.
     */
    
    private double calcularMediaTemperaturaAnterior() {
        int tamanho = medidasTemperaturas.size();
        double soma = 0;
        for (int i = tamanho - 2; i < tamanho; i++) {
            soma += medidasTemperaturas.get(i);
        }
        return soma / 2;
    }
    /**
     * Declaração do método publicarMensagem que publica uma mensagem em um tópico MQTT, lançando uma exceção MqttException.
     * Cria de um objeto MqttMessage com os bytes da mensagem.
     * Publica da mensagem no tópico especificado.
     * @param topico
     * @param mensagem
     * @throws MqttException
     */
    private void publicarMensagem(String topico, String mensagem) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(mensagem.getBytes());
        clienteMqtt.publish(topico, mqttMessage);
    }
	/*
	 * Método estático responsável por exibir o log da temperatura com data e hora.
	 */
	public static void sensorTemperaturaLOG(double numeroAleatorio) {

		LocalDateTime dataAtual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");					// Formatantar data e hora.
		System.out.printf("| %s MEDIA TEMP : %6.2f C° %n", dataAtual.format(formatter), numeroAleatorio);	// Formatantar temperatura [###.00] com data e hora.

	}
}
