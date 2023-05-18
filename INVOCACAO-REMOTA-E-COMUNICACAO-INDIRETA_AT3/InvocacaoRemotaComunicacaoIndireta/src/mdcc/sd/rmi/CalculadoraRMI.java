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
	// M�todo que realiza a opera��o de soma entre duas parcelas
	public int somar(int parcelaA, int parcelaB) {
		return parcelaA + parcelaB;
	}

	// M�todo que realiza a opera��o de subtra��o entre um minuendo e um subtraendo
	public int subtrair(int minuendo, int subtraendo) {
		return minuendo - subtraendo;
	}

	// M�todo que realiza a opera��o de multiplica��o entre dois fatores
	public int multiplicar(int fatorA, int fatorB) {
		return fatorA * fatorB;
	}

	// M�todo que realiza a opera��o de divis�o entre um dividendo e um divisor
	public int dividir(int dividendo, int divisor) {

		// Verifica se o divisor � igual a zero, se sim, lan�a uma exce��o de ArithmeticException
		if (divisor == 0)
			throw new ArithmeticException();

		return dividendo / divisor;
	}
}
