package mdcc.sd.socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * Esse código é um exemplo simplificado de um cliente de socket que se conecta a um servidor 
 * para realizar uma operação calculadora remota. Ele define constantes para o endereço IP e a 
 * porta do servidor, além de variáveis para as entradas numéricas e o parâmetro de operação. 
 * Em seguida, cria um socket, envia os dados para o servidor, lê a resposta e exibe o resultado. 
 * Por fim, fecha o socket cliente.
 *
 */
public class CalculadoraClientSocket {
	
	private static final String QUEBRA_DE_LINHA = "\n";     // Constante para representar a quebra de linha
	private static final String IP_SERVIDOR = "127.0.0.1";  // Endereço IP do servidor
	private static final int PORTA_SERVIDOR = 9090;         // Porta do servidor
	
	
	/**
	 *enum chamado Operacao que representa as diferentes operações 
	 *matemáticas: SOMAR, SUBTRAIR, MULTIPLICAR e DIVIDIR.
	 *
	 */
	
	public enum Operacao {
	    SOMAR(1),       // Constante para representar operação de soma
	    SUBTRAIR(2),    // Constante para representar operação de subtração
	    DIVIDIR(3),     // Constante para representar operação de divisão
		MULTIPLICAR(4); // Constante para representar operação de multiplicação

	    private final int valor;
	    //Um construtor que recebe o valor numérico associado a cada operação
	    Operacao(int valor) {
	        this.valor = valor;
	    }
	    //Retorna o valor associado a cada operação
	    public int getValor() {
	        return valor;
	    }
	}
	/**
	 * 
	 * @param entradaNumericaA Valor da primeira entrada numérica
	 * @param entradaNumericaB Valor da segunda entrada numérica
	 * @param parametroOperacao Parâmetro para indicar a operação a ser realizada 1-somar 2-subtrair 3-dividir 4-multiplicar
	 */
	public  void calcular(double entradaNumericaA, double entradaNumericaB, int parametroOperacao) {
				
		String resultadoRequisicao = ""; // Variável para armazenar o resultado da requisição
        
		try {

        	//Conexão com o Servidor
            Socket clienteSocket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR); 							// Criação do socket para conexão com o servidor
            DataOutputStream socketSaidaServer = new DataOutputStream(clienteSocket.getOutputStream()); // Stream de saída para o servidor
            
            //Enviando os dados
            socketSaidaServer.writeBytes( parametroOperacao + QUEBRA_DE_LINHA); // Envio do parâmetro da operação
            socketSaidaServer.writeBytes( entradaNumericaA  + QUEBRA_DE_LINHA); // Envio da primeira entrada numérica
            socketSaidaServer.writeBytes( entradaNumericaB  + QUEBRA_DE_LINHA); // Envio da segunda entrada numérica
            socketSaidaServer.flush(); 											// Limpeza do buffer e envio dos dados para o servidor

            //Recebendo a resposta
            BufferedReader MensagemServidor = new BufferedReader (new InputStreamReader(clienteSocket.getInputStream())); 	// Stream de entrada para ler a resposta do servidor
            resultadoRequisicao = MensagemServidor.readLine(); 																// Leitura da resposta do servidor
            System.out.println("+----------------------------+");					// Formatacao
            System.out.printf ("|RESULTADO =  %-14.14s |\n", resultadoRequisicao); 	// Exibição do resultado da requisição
            System.out.println("+----------------------------+");					// Fechamento do socket cliente
            clienteSocket.close(); 													// Formatacao

        } catch (IOException e) {
        	// Tratamento da exceção entrada/saída (I/O)
            e.printStackTrace();
        }


	}

    public static void main(String[] args) {
    	// Criação de uma instância do cliente
        CalculadoraClientSocket servidorCalculadora = new CalculadoraClientSocket();
        servidorCalculadora.calcular(10,20,Operacao.SOMAR.getValor());
        servidorCalculadora.calcular(10,40,Operacao.SUBTRAIR.getValor());
        servidorCalculadora.calcular(34,55,Operacao.MULTIPLICAR.getValor());
        servidorCalculadora.calcular(10,20,Operacao.DIVIDIR.getValor());
        
    }
}
