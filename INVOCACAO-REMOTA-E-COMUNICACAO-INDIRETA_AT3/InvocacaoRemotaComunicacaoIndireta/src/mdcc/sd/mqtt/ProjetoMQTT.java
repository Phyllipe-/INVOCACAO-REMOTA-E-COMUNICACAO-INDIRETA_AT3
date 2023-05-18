package mdcc.sd.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ProjetoMQTT {
	
    public static void main(String[] args) throws InterruptedException {
    	
        try {
        	// Criando uma instância do ServicoCAT
            ServicoCAT cat = new ServicoCAT();
            // Iniciando o serviço do CAT
            cat.iniciar();
            
            // Criando instâncias de sensores de temperatura A, B e C
            SensorTemperatura sensorA = new SensorTemperatura();
            SensorTemperatura sensorB = new SensorTemperatura();
            SensorTemperatura sensorC = new SensorTemperatura();
            
            
            sensorA.iniciar();
            Thread.sleep(500);
            sensorB.iniciar();
            Thread.sleep(500);
            sensorC.iniciar();
            
            // Criando uma instância do ServicoAlarmes
            ServicoAlarmes alarms = new ServicoAlarmes();
            alarms.iniciar();
            
        } catch (MqttException e) {
        	// Imprimindo a pilha de exceções caso ocorra uma exceção do tipo MqttException
            e.printStackTrace();
        }
   
    }
}
