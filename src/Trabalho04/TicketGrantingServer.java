package Trabalho04;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
		System.out.println("\n\t\t\t\t\tTICKET GRANTING SERVER");
		
		TicketGrantingServer ticketGrantingServer = new TicketGrantingServer();
		
		ticketGrantingServer.inicializarServico();
		
		while(true);
	}
	
	private void inicializarServico()
	{
		recebeUDP.start();
		
		return;
	}
	
	public void responderCliente(byte[] mensagemRecebida, byte[] ticketTGS) throws NoSuchAlgorithmException, IOException, Exception 
	{
		byte[] mensagemCliente						= null;
		
		// Guarda a chave de sessao TGS
		DES_Manager chaveSessaoTicketGrantingServer	= new DES_Manager(new SecretKeySpec(Arrays.copyOfRange(ticketGrantingServerKey.decriptografarByte(ticketTGS), 
																					 					   6, 
																					 					   14), 
																						"DES"));
				
		byte idClienteTicket 						= ticketGrantingServerKey.decriptografarByte(ticketTGS)[0];
		
		if(trabalho04.debug)
        {
        	System.out.println("\nTicket recebido: " 			+ Base64.getEncoder().withoutPadding().encodeToString(ticketTGS)
        					 + "\nChave de sessao com o TGS: " 	+ chaveSessaoTicketGrantingServer.mostraChave());
        }     
        
        // Decodifica o ticket de sessao TGS T_c_tgs = {ID_C + T_R + K_c_tgs}K_tgs
        // Se o ticket ainda for valido, decodifica a mensagem do cliente
        if(verificaSeTicketValido(ticketGrantingServerKey.decriptografarByte(ticketTGS)))
        {
        	byte[] mensagemClienteDecriptografada	= chaveSessaoTicketGrantingServer.decriptografarByte(mensagemRecebida);
        	
        	this.mostraDadosMensagemCliente(mensagemClienteDecriptografada);
        	
        	byte idClienteMensagemCliente				= mensagemClienteDecriptografada[0];
    		byte numeroAleatorioMensagemCliente			= mensagemClienteDecriptografada[7];
    		
    		int  idServicoRequisitado					= mensagemClienteDecriptografada[1];
        	
    		SecretKey chave = lerChaveArquivo(trabalho04.ticketGrantingServerArquivoChave, 
            								  idServicoRequisitado);

    		///////////////////////////////////////////////////////////////////////////////////
            // Verifica se achou o servico na lista de chaves e se o cliente e o dono ticket //
    		///////////////////////////////////////////////////////////////////////////////////
    		
            if((chave != null) && (idClienteTicket == idClienteMensagemCliente))
            {
                mensagemCliente				= this.montaMensagemCliente(chave,
                														numeroAleatorioMensagemCliente,
                														chaveSessaoTicketGrantingServer,
                														idClienteTicket);

                // Envia a mensagem para o cliente
                enviaUDP.enviaMensagem(mensagemCliente, 
                					   "localhost", 
                					   trabalho04.portaRecebeUDPCliente);
            }
        }
        
        return;
	}
	
	private void mostraDadosMensagemCliente(byte[] mensagemRecebida)
	{
		///////////////////////////////////////
		// [{ID_C + ID_S + T_R + N2 }K_c_tgs //
		///////////////////////////////////////
		
		byte idCliente 		= mensagemRecebida[0];
		
		int idServico 		= (int)mensagemRecebida[1];
        
		byte numAleatorio	= mensagemRecebida[7];

		if(trabalho04.debug)
		{
			System.out.println("\n\nRequisicao do cliente contem as seguintes informacoes:"
							 + "\nId do cliente: " 			+ (int) idCliente 
							 + "\nServico requisitado: " 	+ idServico 
							 + "\nData da requisicao: " 	+ (int)mensagemRecebida[2] + "/" + ((int)mensagemRecebida[3] + 1) + "/" + (int)mensagemRecebida[4] + " as " + (int)mensagemRecebida[5] + ":" + (int)mensagemRecebida[6] 
							 + "\nNumero aleatorio: " 		+ numAleatorio + "\n\n");
		}
		
		return;
	}
	
	private byte[] montaMensagemCliente(SecretKey chave, byte numeroAleatorioMensagemCliente, DES_Manager chaveSessaoTicketGrantingServer, byte idClienteTicket) throws Exception
	{
		byte[] mensagemCliente			= null;
		byte[] mensagemACriptografar	= null;
		
		// Inicia o Key Manager do servico
    	DES_Manager chaveServico 	= new DES_Manager(chave);
    	
        // Cria uma chave de sessao entre o cliente e o servico
    	chave 						= geraChave();
		
		// id da mensagem
		mensagemCliente 			= new byte[]{converterIntEmByte(4)};
		
		// Mensagem a criptografar para o cliente -> chave de sessao + n2
		mensagemACriptografar 		= append(chave.getEncoded(), 
								       		 new byte[]{numeroAleatorioMensagemCliente});
	     
		// Adiciona a mensagem para o cliente criptografada com a chave de sessao do TGS
	    mensagemCliente 			= append(mensagemCliente, 
	     						 			 chaveSessaoTicketGrantingServer.criptografarByte(mensagemACriptografar));
	
	     if(trabalho04.debug)
	     	System.out.println("\nO tamanho da mensagem criptografada com a chave do cliente e: " + (mensagemCliente.length - 1));
	
	     mensagemACriptografar		= this.montaMensagemACriptografar(idClienteTicket);
	     
	     // Adiciona a chave de sessao
	     mensagemACriptografar = append(mensagemACriptografar, 
	    		 						chave.getEncoded());
	    
	     // Criptografa tudo com a chave de servico
	     byte[] ticketServico = chaveServico.criptografarByte(mensagemACriptografar);
	
	     if(trabalho04.debug)
	     {
		     System.out.println("\nO tamanho do ticket para o Service Server: " 	+ ticketServico.length
		     				  + "\nValor do Ticket para o Service Server: " 			+ Base64.getEncoder().withoutPadding().encodeToString(ticketServico));
	     }
	     
	     ////////////////////////////////////////////////////////////////////////////////////
	     // Junta a mensagem do cliente com o ticket -> M4 = [{K_c_s + N2}K_c_tgs + T_c_s] //
	     ////////////////////////////////////////////////////////////////////////////////////
	     
	     mensagemCliente	= append(mensagemCliente, 
	    		 					 ticketServico);
		
		return mensagemCliente;
	}
	
	private byte[] montaMensagemACriptografar(byte idClienteTicket)
	{
		byte[] mensagemACriptografar	= null;
		
		// Tempo de validade do ticket
	    Calendar calendar 				= new GregorianCalendar();
	     
	    // Adiciona o tempo de acesso permitido
	    calendar.add(Calendar.MINUTE, 
	    		     tempoAcessoPermitido);
	
	     byte ano 	= converterIntEmByte(calendar.get(GregorianCalendar.YEAR) % 2000);
	     byte mes 	= converterIntEmByte(calendar.get(GregorianCalendar.MONTH));
	     byte dia 	= converterIntEmByte(calendar.get(GregorianCalendar.DAY_OF_MONTH));
	     byte hora 	= converterIntEmByte(calendar.get(GregorianCalendar.HOUR_OF_DAY));
	     byte min 	= converterIntEmByte(calendar.get(GregorianCalendar.MINUTE));
	
	     ///////////////////////////////////////////////////////////////
	     // Cria o Ticket de sessao ->T_c_s = {ID_C + T_A + K_c_s}K_s //
	     // 	Ticket com o ID do cliente e o tempo requisitado	  //
	     ///////////////////////////////////////////////////////////////
	     
	     mensagemACriptografar = new byte[]{idClienteTicket, 
	    		 							dia, 
	    		 							mes, 
	    		 							ano, 
	    		 							hora, 
	    		 							min};
	     
		return mensagemACriptografar;
	}
}
