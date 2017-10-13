package Trabalho04;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import Trabalho04.trabalho04.tiposServicos;
import Trabalho04.trabalho04.tiposSocket;

public class AgentServer extends ServicoBase
{
	static private UDP_Manager enviaUDP;
	static private UDP_Manager recebeUDP;

    static private DES_Manager agentServerKey;
    static private DES_Manager ticketGrantingServerKey;
    
	public AgentServer() throws Exception
	{
		enviaUDP 				= new UDP_Manager(tiposServicos.AGENT_SERVER,
												  trabalho04.portaEnviaUDPAgentServer,
												  this,
												  null,
												  null,
												  null,
												  tiposSocket.ENVIAR);
		
		recebeUDP 				= new UDP_Manager(tiposServicos.AGENT_SERVER,
										  		  trabalho04.portaRecebeUDPAgentServer,
										  		  this,
												  null,
												  null,
												  null,
												  tiposSocket.RECEBER);
		
		agentServerKey			= new DES_Manager(lerChaveArquivo(trabalho04.agentServerArquivoChave, 
										  				  		  0));
		
		ticketGrantingServerKey	= new DES_Manager(lerChaveArquivo(trabalho04.agentServerArquivoChave, 
				  												  1));
		
		return;
	}
	
	public static void imprimirMenu() throws Exception
	{	
		AgentServer agentServer = new AgentServer();
		
		agentServer.inicializarServico();
		
		while(true);
	}
	
	private void inicializarServico()
	{
		recebeUDP.start();
		
		return;
	}
	
	public void responderCliente(byte[] mensagemCliente) throws NoSuchAlgorithmException, IOException, Exception 
	{
		
	}
}
