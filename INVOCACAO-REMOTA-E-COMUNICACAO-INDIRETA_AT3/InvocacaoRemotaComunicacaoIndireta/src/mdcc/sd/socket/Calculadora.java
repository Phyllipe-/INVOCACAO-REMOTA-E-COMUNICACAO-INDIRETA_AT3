package mdcc.sd.socket;
/** 
 * Esse código define uma classe chamada Calculadora que contém métodos para realizar operações matemáticas básicas
 *
 */

public class Calculadora {

	// Método que retorna um array de String contendo nome e sobrenome
	public String[] sayHello(String nome, String sobreNome) {
		return new String[] { nome, sobreNome };
	}

	// Método que realiza a operação de soma entre duas parcelas
	public double somar(double parcelaA, double parcelaB) {
		return parcelaA + parcelaB;
	}

	// Método que realiza a operação de subtração entre um minuendo e um subtraendo
	public double subtrair(double minuendo, double subtraendo) {
		return minuendo - subtraendo;
	}

	// Método que realiza a operação de multiplicação entre dois fatores
	public double multiplicar(double fatorA, double fatorB) {
		return fatorA * fatorB;
	}

	// Método que realiza a operação de divisão entre um dividendo e um divisor
	public double dividir(double dividendo, double divisor) {

		// Verifica se o divisor é igual a zero, se sim, lança uma exceção de ArithmeticException
		if (divisor == 0)
			throw new ArithmeticException();

		return dividendo / divisor;
	}
}