package mdcc.sd.http;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/**
 * Classe em Java que implementa uma calculadora utilizando requisi��es HTTP. 
 */
public class CalculadoraClientHTTP {
	
	private static String URL_SERVIDOR = "https://double-nirvana-273602.appspot.com/?hl=pt-BR";
	private static String METODO_REQUISICAO = "POST";
	private static String CODIFICACAO_CARACTERES = "UTF-8";
	private static int TEMPO_ESPARA_CONEXAO = 15000;
	private static int TEMPO_LEITURA_DADOS_RESPOSTA = 10000;
	
	/**
	 * O m�todo calcular recebe tr�s par�metros:
	 * @param operacao representando o tipo de opera��o a ser realizada
	 * @param numeroA, numeroB os n�meros que ser�o utilizados na opera��o
	 * @return O resultado da opera��o � retornado como uma string.
	 */
	public String calcular(int operacao, int numeroA,int numeroB) {
		
		String resultadoRequisicao = "";
		
		try {
			//O m�todo estabelece uma conex�o com o servidor remoto utilizando a classe HttpsURLConnection.
			URL urlServidor = new URL(URL_SERVIDOR);
			HttpsURLConnection conecaoServidor = (HttpsURLConnection) urlServidor.openConnection();
			conecaoServidor.setReadTimeout(TEMPO_LEITURA_DADOS_RESPOSTA);
			conecaoServidor.setConnectTimeout(TEMPO_ESPARA_CONEXAO);
			conecaoServidor.setRequestMethod(METODO_REQUISICAO);
			conecaoServidor.setDoInput(true);
			conecaoServidor.setDoOutput(true) ;

			//ENVIO DOS PARAMETROS
			//Os par�metros s�o enviados para o servidor no corpo da requisi��o atrav�s de um OutputStream.
			OutputStream outputStream = conecaoServidor.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, CODIFICACAO_CARACTERES));
			writer.write("oper1="+numeroA+"&oper2="+numeroB+"&operacao="+operacao);
			writer.flush();
			writer.close();
			outputStream.close();
			
			/*
			 * A vari�vel codigoRespostaRequisicao est� recebendo o c�digo de resposta da requisi��o 
			 * feita ao servidor. Em seguida, � verificado se o c�digo de resposta � igual a HttpsURLConnection.HTTP_OK.
			 */
			
			int codigoRespostaRequisicao = conecaoServidor.getResponseCode();
			if (codigoRespostaRequisicao == HttpsURLConnection.HTTP_OK) {

				//RECBIMENTO DOS PARAMETROS
				// Ap�s o envio dos par�metros, � feita a leitura da resposta do servidor utilizando um BufferedReader.
				BufferedReader br = new BufferedReader(
						new InputStreamReader(conecaoServidor.getInputStream(), CODIFICACAO_CARACTERES));
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				// O resultado da opera��o � retornado como uma string.
				resultadoRequisicao = response.toString();

			}
		/*
		* Se ocorrer algum erro durante o processo de requisi��o ou leitura da resposta, 
		* uma exce��o � lan�ada e o rastreamento do erro � impresso no console.
		*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultadoRequisicao;
	}
}
