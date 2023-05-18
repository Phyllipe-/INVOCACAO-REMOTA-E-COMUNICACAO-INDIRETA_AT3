package mdcc.sd.http;

/**
 *   Este c�digo representa um projeto em Java que utiliza a classe CalculadoraClientHTTP 
 *   para fazer requisi��es a um servidor PHP e realizar diferentes opera��es matem�ticas.
 */

public class ProjetoHTTP {
	public static void main(String[] args) {
		// Cria��o de uma inst�ncia da classe CalculadoraClientHTTP
		CalculadoraClientHTTP calculadoraClientHTTP = new CalculadoraClientHTTP();
		
		// Array de strings com os nomes das opera��es
		String[] operacao = {"SOMAR", "SUBTRAIR", "MULTIPLICAR", "DIVIDIR"};
		
		// Loop para iterar sobre as opera��es
		for (int tipoOperacao = 0; tipoOperacao < 4; tipoOperacao++) {
			// Chamada do m�todo calcular da classe CalculadoraClientHTTP para realizar a opera��o no servidor
			// e obter o resultado
			String resultado = calculadoraClientHTTP.calcular(tipoOperacao + 1, 14, 14);
			
			// Impress�o do resultado no console junto com o nome da opera��o
			System.out.printf("| %-11s | RESPOSTA DO SERVIDOR PHP = %s\n", operacao[tipoOperacao], resultado);
		}
	}
}

