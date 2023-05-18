package mdcc.sd.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ProjetoMQTT {
	
    public static void main(String[] args) throws InterruptedException {
    	
        try {
        	// Criando uma inst�ncia do ServicoCAT
            ServicoCAT cat = new ServicoCAT();
            // Iniciando o servi�o do CAT
            cat.iniciar();
            
            // Criando inst�ncias de sensores de temperatura A, B e C
            SensorTemperatura sensorA = new SensorTemperatura();
            SensorTemperatura sensorB = new SensorTemperatura();
            SensorTemperatura sensorC = new SensorTemperatura();
            
            
            sensorA.iniciar();
            Thread.sleep(500);
            sensorB.iniciar();
            Thread.sleep(500);
            sensorC.iniciar();
            
            // Criando uma inst�ncia do ServicoAlarmes
            ServicoAlarmes alarms = new ServicoAlarmes();
            alarms.iniciar();
            
        } catch (MqttException e) {
        	// Imprimindo a pilha de exce��es caso ocorra uma exce��o do tipo MqttException
            e.printStackTrace();
        }
   
    }
}
