package mdcc.sd.http;

/**
 *   Este código representa um projeto em Java que utiliza a classe CalculadoraClientHTTP 
 *   para fazer requisições a um servidor PHP e realizar diferentes operações matemáticas.
 */

public class ProjetoHTTP {
	public static void main(String[] args) {
		// Criação de uma instância da classe CalculadoraClientHTTP
		CalculadoraClientHTTP calculadoraClientHTTP = new CalculadoraClientHTTP();
		
		// Array de strings com os nomes das operações
		String[] operacao = {"SOMAR", "SUBTRAIR", "MULTIPLICAR", "DIVIDIR"};
		
		// Loop para iterar sobre as operações
		for (int tipoOperacao = 0; tipoOperacao < 4; tipoOperacao++) {
			// Chamada do método calcular da classe CalculadoraClientHTTP para realizar a operação no servidor
			// e obter o resultado
			String resultado = calculadoraClientHTTP.calcular(tipoOperacao + 1, 14, 14);
			
			// Impressão do resultado no console junto com o nome da operação
			System.out.printf("| %-11s | RESPOSTA DO SERVIDOR PHP = %s\n", operacao[tipoOperacao], resultado);
		}
	}
}

