package mdcc.sd.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorCalculadora {
	 public static void main(String[] args) {
	        try {
	            // Cria��o da inst�ncia da calculadora remota
	            Calculadora calculadora = new CalculadoraRMI();

	            // Inicializa��o do servidor RMI na porta 1099
	            Registry registry = LocateRegistry.createRegistry(1099);

	            // Registro da calculadora no servidor RMI
	            registry.rebind("CalculadoraRemota", calculadora);

	            System.out.println("SERVIDOR DA CALCULADORA PRONTO PARA RECEBER REQUISI��ES.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
