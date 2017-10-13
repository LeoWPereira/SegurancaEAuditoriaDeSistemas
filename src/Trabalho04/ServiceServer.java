package Trabalho04;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import Trabalho04.trabalho04.tiposServicos;
import Trabalho04.trabalho04.tiposSocket;

public class ServiceServer extends ServicoBase
{
	static private UDP_Manager enviaUDP;
	static private UDP_Manager recebeUDP;

    static private DES_Manager serviceServerKey;
	
	public ServiceServer() throws Exception
	{
		enviaUDP = new UDP_Manager(tiposServicos.SERVICE_SERVER,
								   trabalho04.portaEnviaUDPServiceServer,
								   null,
								   null,
								   this,
								   null,
								   tiposSocket.ENVIAR);
		
		recebeUDP = new UDP_Manager(tiposServicos.SERVICE_SERVER,
									trabalho04.portaRecebeUDPServiceServer,
									null,
									null,
									this,
									null,
									tiposSocket.RECEBER);
		
		serviceServerKey = new DES_Manager(lerChaveArquivo(trabalho04.ServiceServerArquivoChave, 
														   0));
		
		return;
	}
	
	public static void imprimirMenu() throws Exception
	{	
		ServiceServer serviceServer = new ServiceServer();
		
		serviceServer.inicializarServico();
		
		while(true);
	}
	
	private void inicializarServico()
	{
		recebeUDP.start();
		
		return;
	}
	
	public void responderCliente(byte[] mensagemCliente, byte[] ticketService) throws NoSuchAlgorithmException, IOException, Exception 
	{ 
		
	}
	
	private boolean verificaSeTicketValido(int anoTicket, int mesTicket, int diaTicket, int horaTicket, int minTicket) 
	{
		return false;
		
	}
}
