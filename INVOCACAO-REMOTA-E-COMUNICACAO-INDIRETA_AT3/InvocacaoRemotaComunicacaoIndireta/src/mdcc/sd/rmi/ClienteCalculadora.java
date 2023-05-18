package mdcc.sd.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteCalculadora {
	 public static void main(String[] args) {
	        try {
	            // Localiza��o do registro RMI na porta 1099
	            Registry registroRMI = LocateRegistry.getRegistry(1099);

	            // Obten��o da refer�ncia da calculadora remota registrada no servidor
	            Calculadora calculadora = (Calculadora) registroRMI.lookup("CalculadoraRemota");

	            // Invoca��o dos m�todos remotos
	            int soma = calculadora.somar(13, 13);
	            int subtracao = calculadora.subtrair(13, 13);
	            int multiplicacao = calculadora.multiplicar(13, 13);
	            int divisao = calculadora.dividir(13, 13);

	            System.out.println("| SOMA:          " + soma);
	            System.out.println("| SUBTRACAO:     " + subtracao);
	            System.out.println("| MULTIPLICACAO: " + multiplicacao);
	            System.out.println("| DIVISAO:       " + divisao);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
