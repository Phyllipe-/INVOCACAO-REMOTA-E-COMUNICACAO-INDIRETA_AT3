package mdcc.sd.socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * Esse c�digo � um exemplo simplificado de um cliente de socket que se conecta a um servidor 
 * para realizar uma opera��o calculadora remota. Ele define constantes para o endere�o IP e a 
 * porta do servidor, al�m de vari�veis para as entradas num�ricas e o par�metro de opera��o. 
 * Em seguida, cria um socket, envia os dados para o servidor, l� a resposta e exibe o resultado. 
 * Por fim, fecha o socket cliente.
 *
 */
public class CalculadoraClientSocket {
	
	private static final String QUEBRA_DE_LINHA = "\n";     // Constante para representar a quebra de linha
	private static final String IP_SERVIDOR = "127.0.0.1";  // Endere�o IP do servidor
	private static final int PORTA_SERVIDOR = 9090;         // Porta do servidor
	
	
	/**
	 *enum chamado Operacao que representa as diferentes opera��es 
	 *matem�ticas: SOMAR, SUBTRAIR, MULTIPLICAR e DIVIDIR.
	 *
	 */
	
	public enum Operacao {
	    SOMAR(1),       // Constante para representar opera��o de soma
	    SUBTRAIR(2),    // Constante para representar opera��o de subtra��o
	    DIVIDIR(3),     // Constante para representar opera��o de divis�o
		MULTIPLICAR(4); // Constante para representar opera��o de multiplica��o

	    private final int valor;
	    //Um construtor que recebe o valor num�rico associado a cada opera��o
	    Operacao(int valor) {
	        this.valor = valor;
	    }
	    //Retorna o valor associado a cada opera��o
	    public int getValor() {
	        return valor;
	    }
	}
	/**
	 * 
	 * @param entradaNumericaA Valor da primeira entrada num�rica
	 * @param entradaNumericaB Valor da segunda entrada num�rica
	 * @param parametroOperacao Par�metro para indicar a opera��o a ser realizada 1-somar 2-subtrair 3-dividir 4-multiplicar
	 */
	public  void calcular(double entradaNumericaA, double entradaNumericaB, int parametroOperacao) {
				
		String resultadoRequisicao = ""; // Vari�vel para armazenar o resultado da requisi��o
        
		try {

        	//Conex�o com o Servidor
            Socket clienteSocket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR); 							// Cria��o do socket para conex�o com o servidor
            DataOutputStream socketSaidaServer = new DataOutputStream(clienteSocket.getOutputStream()); // Stream de sa�da para o servidor
            
            //Enviando os dados
            socketSaidaServer.writeBytes( parametroOperacao + QUEBRA_DE_LINHA); // Envio do par�metro da opera��o
            socketSaidaServer.writeBytes( entradaNumericaA  + QUEBRA_DE_LINHA); // Envio da primeira entrada num�rica
            socketSaidaServer.writeBytes( entradaNumericaB  + QUEBRA_DE_LINHA); // Envio da segunda entrada num�rica
            socketSaidaServer.flush(); 											// Limpeza do buffer e envio dos dados para o servidor

            //Recebendo a resposta
            BufferedReader MensagemServidor = new BufferedReader (new InputStreamReader(clienteSocket.getInputStream())); 	// Stream de entrada para ler a resposta do servidor
            resultadoRequisicao = MensagemServidor.readLine(); 																// Leitura da resposta do servidor
            System.out.println("+----------------------------+");					// Formatacao
            System.out.printf ("|RESULTADO =  %-14.14s |\n", resultadoRequisicao); 	// Exibi��o do resultado da requisi��o
            System.out.println("+----------------------------+");					// Fechamento do socket cliente
            clienteSocket.close(); 													// Formatacao

        } catch (IOException e) {
        	// Tratamento da exce��o entrada/sa�da (I/O)
            e.printStackTrace();
        }


	}

    public static void main(String[] args) {
    	// Cria��o de uma inst�ncia do cliente
        CalculadoraClientSocket servidorCalculadora = new CalculadoraClientSocket();
        servidorCalculadora.calcular(10,20,Operacao.SOMAR.getValor());
        servidorCalculadora.calcular(10,40,Operacao.SUBTRAIR.getValor());
        servidorCalculadora.calcular(34,55,Operacao.MULTIPLICAR.getValor());
        servidorCalculadora.calcular(10,20,Operacao.DIVIDIR.getValor());
        
    }
}
