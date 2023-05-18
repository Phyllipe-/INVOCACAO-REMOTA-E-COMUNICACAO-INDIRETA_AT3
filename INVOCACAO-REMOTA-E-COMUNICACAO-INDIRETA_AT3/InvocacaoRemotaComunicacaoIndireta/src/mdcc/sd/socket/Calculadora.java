package mdcc.sd.socket;
/** 
 * Esse c�digo define uma classe chamada Calculadora que cont�m m�todos para realizar opera��es matem�ticas b�sicas
 *
 */

public class Calculadora {

	// M�todo que retorna um array de String contendo nome e sobrenome
	public String[] sayHello(String nome, String sobreNome) {
		return new String[] { nome, sobreNome };
	}

	// M�todo que realiza a opera��o de soma entre duas parcelas
	public double somar(double parcelaA, double parcelaB) {
		return parcelaA + parcelaB;
	}

	// M�todo que realiza a opera��o de subtra��o entre um minuendo e um subtraendo
	public double subtrair(double minuendo, double subtraendo) {
		return minuendo - subtraendo;
	}

	// M�todo que realiza a opera��o de multiplica��o entre dois fatores
	public double multiplicar(double fatorA, double fatorB) {
		return fatorA * fatorB;
	}

	// M�todo que realiza a opera��o de divis�o entre um dividendo e um divisor
	public double dividir(double dividendo, double divisor) {

		// Verifica se o divisor � igual a zero, se sim, lan�a uma exce��o de ArithmeticException
		if (divisor == 0)
			throw new ArithmeticException();

		return dividendo / divisor;
	}
}