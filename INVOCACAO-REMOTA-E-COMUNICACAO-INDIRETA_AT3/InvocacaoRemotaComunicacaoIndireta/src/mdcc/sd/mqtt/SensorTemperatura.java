package mdcc.sd.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author Phyllipe
 * 
 *Esse código implementa um sensor de temperatura que utiliza o protocolo MQTT 
 *para publicar a temperatura em um tópico específico. O sensor gera uma temperatura 
 *aleatória a cada intervalo de tempo definido e a publica no tópico. Além disso, é 
 *exibido um log com a temperatura, contendo a data e a hora atual.
 */

public class SensorTemperatura {
	private static final String BROKER = "tcp://localhost:32768"; 	// Define a URL do broker MQTT a ser utilizado.
	private static final String TOPICO_TEMPERATURA = "temperatura"; // Define o tópico no qual a temperatura será publicada.
	private static final int TEMPO_AFERICAO_TEMPERATURA = 60000; 	// Define o intervalo de tempo para a aferição da temperatura em milissegundos.

	private MqttClient clienteMqtt;									// Instância do cliente MQTT.
	private Timer timerAgendadorTarefaAfericaoTemperatura;			// Timer para agendar a tarefa de aferição da temperatura.
	
	/*
	 *  Construtor da classe que cria uma instância do cliente MQTT.
	 */
	
	public SensorTemperatura() throws MqttException {
		clienteMqtt = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
	}
	
	/*
	 * Inicia o sensor de temperatura, conectando-se ao broker MQTT e agendando a tarefa de aferição da temperatura.
	 */
	
	public void iniciar() throws MqttException {
		clienteMqtt.connect(); // Realiza a conexão do cliente MQTT ao broker MQTT.

		timerAgendadorTarefaAfericaoTemperatura = new Timer(); // : Cria um objeto Timer para agendar tarefas.
		timerAgendadorTarefaAfericaoTemperatura.schedule(new TimerTask() {
			@Override
			public void run() {
				double temperatura = gerarTemperatura();// Gera uma temperatura aleatória
				sensorTemperaturaLOG(temperatura);  	// Registra a temperatura no log
				try {
					publicarTemperatura(temperatura);	// Publica a temperatura no tópico MQTT
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}, 0, TEMPO_AFERICAO_TEMPERATURA); // Agendamento da tarefa para execução periódica
	}
	
	/*
	 *  Gera uma temperatura aleatória entre 0 e 210.
	 */
	
	private double gerarTemperatura() {
		Random random = new Random();
		return random.nextDouble() * 411;
	}
	
	/*
	 * Publica a temperatura no tópico MQTT.
	 */
	
	private void publicarTemperatura(double temperatura) throws MqttException {
		MqttMessage mensagem = new MqttMessage(String.valueOf(temperatura).getBytes());
		clienteMqtt.publish(TOPICO_TEMPERATURA, mensagem);
	}
	
	/*
	 * Método estático responsável por exibir o log da temperatura com data e hora.
	 */
	
	public static void sensorTemperaturaLOG(double numeroAleatorio) {

		LocalDateTime dataAtual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");					// Formatantar data e hora.
		System.out.printf("| %s TEMPERATURA: %6.2f C° %n", dataAtual.format(formatter), numeroAleatorio);	// Formatantar temperatura [###.00] com data e hora.

	}

}