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
 * Classe em Java que implementa uma calculadora utilizando requisições HTTP. 
 */
public class CalculadoraClientHTTP {
	
	private static String URL_SERVIDOR = "https://double-nirvana-273602.appspot.com/?hl=pt-BR";
	private static String METODO_REQUISICAO = "POST";
	private static String CODIFICACAO_CARACTERES = "UTF-8";
	private static int TEMPO_ESPARA_CONEXAO = 15000;
	private static int TEMPO_LEITURA_DADOS_RESPOSTA = 10000;
	
	/**
	 * O método calcular recebe três parâmetros:
	 * @param operacao representando o tipo de operação a ser realizada
	 * @param numeroA, numeroB os números que serão utilizados na operação
	 * @return O resultado da operação é retornado como uma string.
	 */
	public String calcular(int operacao, int numeroA,int numeroB) {
		
		String resultadoRequisicao = "";
		
		try {
			//O método estabelece uma conexão com o servidor remoto utilizando a classe HttpsURLConnection.
			URL urlServidor = new URL(URL_SERVIDOR);
			HttpsURLConnection conecaoServidor = (HttpsURLConnection) urlServidor.openConnection();
			conecaoServidor.setReadTimeout(TEMPO_LEITURA_DADOS_RESPOSTA);
			conecaoServidor.setConnectTimeout(TEMPO_ESPARA_CONEXAO);
			conecaoServidor.setRequestMethod(METODO_REQUISICAO);
			conecaoServidor.setDoInput(true);
			conecaoServidor.setDoOutput(true) ;

			//ENVIO DOS PARAMETROS
			//Os parâmetros são enviados para o servidor no corpo da requisição através de um OutputStream.
			OutputStream outputStream = conecaoServidor.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, CODIFICACAO_CARACTERES));
			writer.write("oper1="+numeroA+"&oper2="+numeroB+"&operacao="+operacao);
			writer.flush();
			writer.close();
			outputStream.close();
			
			/*
			 * A variável codigoRespostaRequisicao está recebendo o código de resposta da requisição 
			 * feita ao servidor. Em seguida, é verificado se o código de resposta é igual a HttpsURLConnection.HTTP_OK.
			 */
			
			int codigoRespostaRequisicao = conecaoServidor.getResponseCode();
			if (codigoRespostaRequisicao == HttpsURLConnection.HTTP_OK) {

				//RECBIMENTO DOS PARAMETROS
				// Após o envio dos parâmetros, é feita a leitura da resposta do servidor utilizando um BufferedReader.
				BufferedReader br = new BufferedReader(
						new InputStreamReader(conecaoServidor.getInputStream(), CODIFICACAO_CARACTERES));
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				// O resultado da operação é retornado como uma string.
				resultadoRequisicao = response.toString();

			}
		/*
		* Se ocorrer algum erro durante o processo de requisição ou leitura da resposta, 
		* uma exceção é lançada e o rastreamento do erro é impresso no console.
		*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultadoRequisicao;
	}
}
