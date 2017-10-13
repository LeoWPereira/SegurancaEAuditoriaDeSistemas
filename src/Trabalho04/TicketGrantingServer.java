package Trabalho04;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import Trabalho04.trabalho04.tiposServicos;
import Trabalho04.trabalho04.tiposSocket;

public class TicketGrantingServer extends ServicoBase
{
	static private UDP_Manager enviaUDP;
	static private UDP_Manager recebeUDP;

    static private DES_Manager ticketGrantingServerKey;
    
    static private int tempoAcessoPermitido;
    
	public TicketGrantingServer() throws Exception
	{
		enviaUDP = new UDP_Manager(tiposServicos.TICKET_GRANTING_SERVER,
								   trabalho04.portaEnviaUDPTicketGrantingServer,
								   null,
								   this,
								   null,
								   null,
								   tiposSocket.ENVIAR);
		
		recebeUDP = new UDP_Manager(tiposServicos.TICKET_GRANTING_SERVER,
				   					trabalho04.portaRecebeUDPTicketGrantingServer,
				   					null,
				   					this,
				   					null,
				   					null,
				   					tiposSocket.RECEBER);
		
		ticketGrantingServerKey = new DES_Manager(lerChaveArquivo(trabalho04.ticketGrantingServerArquivoChave, 
																  0));
		
		return;
	}
	
	public static void imprimirMenu() throws Exception
	{	
		TicketGrantingServer ticketGrantingServer = new TicketGrantingServer();
		
		ticketGrantingServer.inicializarServico();
		
		while(true);
	}
	
	private void inicializarServico()
	{
		recebeUDP.start();
		
		return;
	}
	
	public void responderCliente(byte[] mensagemCliente, byte[] ticketTGS) throws NoSuchAlgorithmException, IOException, Exception 
	{
		
	}
	
	private boolean verificaSeTicketValido(int anoTicket, int mesTicket, int diaTicket, int horaTicket, int minTicket) 
	{
		return false;
	}
}
