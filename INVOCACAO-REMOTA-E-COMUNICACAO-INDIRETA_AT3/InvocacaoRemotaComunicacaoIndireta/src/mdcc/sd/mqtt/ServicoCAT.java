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
 * Esse c�digo implementa um servi�o de monitoramento de temperatura usando MQTT. 
 * Ele se conecta a um broker MQTT, recebe as leituras de temperatura do t�pico 
 * "temperatura" e realiza algumas verifica��es.
 */
public class ServicoCAT {
    private static final String BROKER = "tcp://localhost:32768"; 							// URL do broker MQTT.
    private static final String TOPICO_TEMPERATURA = "temperatura"; 						// T�pico de temperatura.
    private static final String TOPICO_AUMENTO_TEMPERATURA = "aumento_temperatura_rept";	// T�pico de aumento de temperatura repentino.
    private static final String TOPICO_TEMPERATURA_ALTA = "temperatura_alta";				// T�pico de temperatura alta.
    private static final int TEMPO_AFERICAO_TEMPERATURA_MEDIA = 120;						// Tempo de aferi��o da temperatura m�dia.
    private static final int LIMITE_TEMPERATURA_ALTA = 200;									// Limite de temperatura alta.
    private static final int MARGEM_DIFERENCA_TEMPERATURA = 5;								// Margem de diferen�a de temperatura para detec��o de aumento repentino.
    
    private MqttClient clienteMqtt;				// Cliente do tipo MqttClient para estabelecer a conex�o MQTT.
    private List<Double> medidasTemperaturas;	// Lista do tipo <Double> para armazenar as medidas de temperatura.
    /*
     * Declara��o do construtor da classe ServicoCAT que lan�a uma exce��o MqttException.
     * Inicializa��o da vari�vel clienteMqtt com um novo objeto MqttClient passando o BROKER, 
     * um ID de cliente gerado e uma inst�ncia de MemoryPersistence.
     */
    public ServicoCAT() throws MqttException {
        clienteMqtt = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
        //Inicializa��o da vari�vel medidasTemperaturas com um novo objeto ArrayList.
        medidasTemperaturas = new ArrayList<>(); 
    }

    public void iniciar() throws MqttException {
    	//  Estabelecimento da conex�o MQTT.
        clienteMqtt.connect();
        // Inscri��o no t�pico TOPICO_TEMPERATURA com uma fun��o lambda que recebe o t�pico e a mensagem.
        clienteMqtt.subscribe(TOPICO_TEMPERATURA, (topic, message) -> {
            double temperatura = Double.parseDouble(new String(message.getPayload()));
            // Chamada do m�todo adicionarTemperatura passando a temperatura recebida.
            adicionarTemperatura(temperatura);
            // C�lculo da m�dia da temperatura.
            double mediaTemperatura = calcularMediaTemperatura();
            // Verifica��o de aumento repentino de temperatura.
            verificarAumentoTemperaturaRepentina(mediaTemperatura);
            // Verifica��o de temperatura alta.
            verificarTemperaturaAlta(mediaTemperatura);
        });
    }
    
	/*
	 * Declara��o do m�todo adicionarTemperatura que recebe a temperatura.
	 * Adiciona a temperatura � lista de medidas de temperatura.
	 * Verifica se a lista de medidas de temperatura ultrapassou o tempo de aferi��o m�dia.
	 * Remove da primeira medida de temperatura da lista.
	 */
    
    private void adicionarTemperatura(double temperatura) {
        medidasTemperaturas.add(temperatura);
        if (medidasTemperaturas.size() > TEMPO_AFERICAO_TEMPERATURA_MEDIA) {
            medidasTemperaturas.remove(0);
        }
    }
    
    /*
     * M�todo calcularMediaTemperatura que calcula a m�dia da temperatura.
     * Declara��o da vari�vel soma do tipo double para acumular a soma das temperaturas.
     * Loop sobre as medidas de temperatura.
     * Acumula temperaturas na vari�vel soma.
     * C�lcula da m�dia da temperatura dividindo a soma pelo n�mero de medidas.
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
     * Declara do m�todo verificarAumentoTemperaturaRepentina que verifica se houve um aumento repentino na temperatura, 
     * lan�ando uma exce��o MqttException.
     * Verifica se existem pelo menos duas medidas de temperatura.
     * C�lcula da m�dia da temperatura anterior.
     * Verifica se a diferen�a absoluta entre a m�dia atual e a m�dia anterior � maior ou igual � margem de diferen�a de temperatura.
     * Publica de uma mensagem no t�pico TOPICO_AUMENTO_TEMPERATURA com o conte�do "AUMENTO DE TEMPERATURA REPENTINA!".
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
     * Declara do m�todo verificarTemperaturaAlta que verifica se a temperatura atual � alta, lan�ando uma exce��o @throws MqttException.
     * Verifica se a temperatura atual � maior que o limite de temperatura alta.
     * Publica de uma mensagem no t�pico TOPICO_TEMPERATURA_ALTA com o conte�do "TEMPERATURA ALTA!".
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
     * Declara do m�todo calcularMediaTemperaturaAnterior que calcula a m�dia da temperatura anterior.
     * Declara da vari�vel tamanho do tipo int que representa o tamanho da lista de medidas de temperatura.
     * Declara da vari�vel soma do tipo double para acumular a soma das temperaturas anteriores.
     * Loop sobre as duas �ltimas medidas de temperatura.
     * Acumula da temperatura na vari�vel soma.
     * @return C�lculo da m�dia da temperatura anterior dividindo a soma por 2.
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
     * Declara��o do m�todo publicarMensagem que publica uma mensagem em um t�pico MQTT, lan�ando uma exce��o MqttException.
     * Cria de um objeto MqttMessage com os bytes da mensagem.
     * Publica da mensagem no t�pico especificado.
     * @param topico
     * @param mensagem
     * @throws MqttException
     */
    private void publicarMensagem(String topico, String mensagem) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(mensagem.getBytes());
        clienteMqtt.publish(topico, mqttMessage);
    }
	/*
	 * M�todo est�tico respons�vel por exibir o log da temperatura com data e hora.
	 */
	public static void sensorTemperaturaLOG(double numeroAleatorio) {

		LocalDateTime dataAtual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");					// Formatantar data e hora.
		System.out.printf("| %s MEDIA TEMP : %6.2f C� %n", dataAtual.format(formatter), numeroAleatorio);	// Formatantar temperatura [###.00] com data e hora.

	}
}
