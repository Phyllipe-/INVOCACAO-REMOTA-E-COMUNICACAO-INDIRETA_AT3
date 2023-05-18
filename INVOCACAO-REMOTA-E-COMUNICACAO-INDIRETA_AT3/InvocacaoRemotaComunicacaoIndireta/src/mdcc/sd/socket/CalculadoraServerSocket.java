package mdcc.sd.socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/* 
O código implementa um servidor socket que recebe conexões de clientes e realiza operações matemáticas com base nos parâmetros enviados.
Ele utiliza a classe Calculadora para realizar as operações e envia o resultado de volta para o cliente.
 */

public class CalculadoraServerSocket {
	private static final int PORTA_SERVIDOR = 9090;

	private ServerSocket servidorCalculadoraSocket;
	private Calculadora calculadora;
	private int numeroClientesNoServidor;

	public void run() {
		try {
			// Criação do servidor socket na porta definida
			servidorCalculadoraSocket = new ServerSocket(PORTA_SERVIDOR);
			calculadora = new Calculadora();
			setNumeroClientesNoServidor(0);
			System.out.println("+----------------------------+");
			System.out.println("|       SERVIDOR NO AR       |");
			System.out.println("+----------------------------+");

			// Loop infinito para aceitar conexões dos clientes
			while (true) {
				Socket conexaoServidorCalculadoraSocket = servidorCalculadoraSocket.accept();
				setNumeroClientesNoServidor(getNumeroClientesNoServidor() + 1);
				System.out.println("+----------------------------+");
				System.out.println("|        NOVA CONEXAO        |");
				System.out.println("+----------------------------+");

				// Preparação para leitura dos dados enviados pelo cliente
				BufferedReader socketEntradaDadosServidor = new BufferedReader(
						new InputStreamReader(conexaoServidorCalculadoraSocket.getInputStream()));

				// Leitura dos parâmetros da operação enviados pelo cliente
				String parametroServidor = socketEntradaDadosServidor.readLine();
				String entradaNumericaA = socketEntradaDadosServidor.readLine();
				String entradaNumericaB = socketEntradaDadosServidor.readLine();

				double resultadoOperacaoCalculadora = 0.0;

				// Realização da operação com base no parâmetro recebido
				switch (parametroServidor) {
				case "1": // Soma
					resultadoOperacaoCalculadora = calculadora.somar(
							Double.parseDouble(entradaNumericaA),
							Double.parseDouble(entradaNumericaB)
							);
					break;
				case "2": // Subtração
					resultadoOperacaoCalculadora = calculadora.subtrair(
							Double.parseDouble(entradaNumericaA),
							Double.parseDouble(entradaNumericaB)
							);
					break;
				case "3": // Divisão
					resultadoOperacaoCalculadora = calculadora.dividir(
							Double.parseDouble(entradaNumericaA),
							Double.parseDouble(entradaNumericaB)
							);
					break;
				case "4": // Multiplicação
					resultadoOperacaoCalculadora = calculadora.multiplicar(
							Double.parseDouble(entradaNumericaA),
							Double.parseDouble(entradaNumericaB)
							);
					break;
				default:
					System.out.println("+----------------------------+");
					System.out.println("|      OPERACAO INVALIDA     |");
					System.out.println("+----------------------------+");
					break;
				}

				// Envio do resultado da operação de volta para o cliente
				DataOutputStream socketSaida = new DataOutputStream(conexaoServidorCalculadoraSocket.getOutputStream());
				socketSaida.writeBytes(String.valueOf(resultadoOperacaoCalculadora) + '\n');
				socketSaida.flush();
				socketSaida.close();
				System.out.println("+----------------------------+");
				System.out.println("|RESULTADO DA OPERACAO: " + resultadoOperacaoCalculadora);
				System.out.println("+----------------------------+");

				// Fechamento de recursos
				socketEntradaDadosServidor.close();
				conexaoServidorCalculadoraSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getNumeroClientesNoServidor(){
		return numeroClientesNoServidor;
	}

	public void setNumeroClientesNoServidor(int numeroClientesNoServidor) {
		this.numeroClientesNoServidor = numeroClientesNoServidor;
	}

	public static void main(String[] args) {
		// Criação de uma instância do servidor e execução do método run
		CalculadoraServerSocket servidorCalculadora = new CalculadoraServerSocket();
		servidorCalculadora.run();
	}
}

