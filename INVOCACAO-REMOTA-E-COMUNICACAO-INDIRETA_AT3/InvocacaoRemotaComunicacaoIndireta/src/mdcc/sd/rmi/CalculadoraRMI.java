package mdcc.sd.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculadoraRMI extends UnicastRemoteObject implements Calculadora {
    /**
	 * Implemente a classe CalculadoraRMI que implementa a interface Calculadora
	 */
	private static final long serialVersionUID = 1L;

	protected CalculadoraRMI() throws RemoteException {
        super();
    }
	// Método que realiza a operação de soma entre duas parcelas
	public int somar(int parcelaA, int parcelaB) {
		return parcelaA + parcelaB;
	}

	// Método que realiza a operação de subtração entre um minuendo e um subtraendo
	public int subtrair(int minuendo, int subtraendo) {
		return minuendo - subtraendo;
	}

	// Método que realiza a operação de multiplicação entre dois fatores
	public int multiplicar(int fatorA, int fatorB) {
		return fatorA * fatorB;
	}

	// Método que realiza a operação de divisão entre um dividendo e um divisor
	public int dividir(int dividendo, int divisor) {

		// Verifica se o divisor é igual a zero, se sim, lança uma exceção de ArithmeticException
		if (divisor == 0)
			throw new ArithmeticException();

		return dividendo / divisor;
	}
}
