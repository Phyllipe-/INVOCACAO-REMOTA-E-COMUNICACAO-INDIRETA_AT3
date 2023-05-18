package mdcc.sd.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorCalculadora {
	 public static void main(String[] args) {
	        try {
	            // Criação da instância da calculadora remota
	            Calculadora calculadora = new CalculadoraRMI();

	            // Inicialização do servidor RMI na porta 1099
	            Registry registry = LocateRegistry.createRegistry(1099);

	            // Registro da calculadora no servidor RMI
	            registry.rebind("CalculadoraRemota", calculadora);

	            System.out.println("SERVIDOR DA CALCULADORA PRONTO PARA RECEBER REQUISIÇÕES.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
