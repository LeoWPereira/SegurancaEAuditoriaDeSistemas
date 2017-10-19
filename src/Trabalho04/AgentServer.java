package Trabalho04;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.SecretKey;

import Trabalho04.trabalho04.tiposServicos;
import Trabalho04.trabalho04.tiposSocket;

public class AgentServer extends ServicoBase
{
	static private UDP_Manager enviaUDP;
	static private UDP_Manager recebeUDP;

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
		
		ticketGrantingServerKey	= new DES_Manager(lerChaveArquivo(trabalho04.agentServerArquivoChave, 
				  												  1));
		
		return;
	}
	
	public static void imprimirMenu() throws Exception
	{	
		System.out.println("\n\t\t\t\t\tAGENT SERVER");
		
		AgentServer agentServer = new AgentServer();
		
		agentServer.inicializarServico();
		
		while(true);
	}
	
	private void inicializarServico()
	{
		recebeUDP.start();
		
		return;
	}
	
	public void responderCliente(byte[] mensagemRecebida) throws NoSuchAlgorithmException, IOException, Exception 
	{
		byte[] 		mensagemClienteDecriptografada	= null;
		byte[]		mensagemCliente					= null;
		byte[]		ticketTGS						= null;
		
		// Le o id do cliente
		int 		idCliente	= Byte.toUnsignedInt(mensagemRecebida[1]);
		
		// Verifica se achou o cliente na lista de chaves
		SecretKey 	chave		= lerChaveArquivo(trabalho04.agentServerArquivoChave, 
										  		  1 + idCliente);
        
        if (chave != null) 
        {
            // Inicia o DES_Manager do cliente
        	DES_Manager clienteKey	= new DES_Manager(chave);
            
        	// Cria uma chave para a sessao
            chave 					= geraChave();
            
            if(trabalho04.debug)
            	System.out.println("Tamanho da chave: " + chave.getEncoded().length);
           
            mensagemClienteDecriptografada	= this.decodificaMensagemRecebida(mensagemRecebida, 
            							    								  clienteKey);

            if(trabalho04.debug)
    		{
    			System.out.println("\n*******************************************************************");
    			System.out.println("\n\t\t\tMensagem 2 para Cliente\n\n");
    		}
            
            mensagemCliente 				= this.montaMensagemCliente(mensagemRecebida,
            															mensagemClienteDecriptografada,
            														    clienteKey,
            														    chave);
            
            ticketTGS						= this.montaMensagemTicketGrantingServer(mensagemRecebida, 
            																		 mensagemClienteDecriptografada, 
            																		 chave);
            
            // Junta a mensagem do cliente com o ticket 
            mensagemCliente 				= append(mensagemCliente,
            						 				 ticketTGS);
            
            if(trabalho04.debug)
            {            	
            	System.out.println("*******************************************************************\n");
            }
            
            enviaUDP.enviaMensagem(mensagemCliente,
            					   "localhost",
            					   trabalho04.portaRecebeUDPCliente);
        }
		
		return;
	}
	
	private byte[] decodificaMensagemRecebida(byte[] mensagemRecebida, DES_Manager clienteKey) throws Exception
	{
		byte[] 		mensagemClienteDecriptografada;
		
		if(trabalho04.debug)
		{
			System.out.println("\n*******************************************************************");
			System.out.println("\n\t\t\tMensagem 1 do Cliente\n\n");
		}
		
		/////////////////////////////////////////////////////////////////////
		// 			Todas as mensagens do cliente possuem 8 bytes		   //
        // A mensagem decriptografada tem as informações {ID_S + T_R + N1} //
		/////////////////////////////////////////////////////////////////////
		
		mensagemClienteDecriptografada = clienteKey.decriptografarByte(Arrays.copyOfRange(mensagemRecebida, 
																						  2, 
																						  10));
        
		if(trabalho04.debug)
		{
	        System.out.println("\n\t\t\tId do Cliente: "						+ (int)mensagemRecebida[1]
	        				 + "\n\t\t\tId do Servico: " 						+ (int)mensagemClienteDecriptografada[0]
	        				 + "\n\t\t\tTempo de Acesso: "					+ (int)mensagemClienteDecriptografada[1] 
	        				 + "\n\t\t\tNumero aleatorio: " 					+ (int)mensagemClienteDecriptografada[2]);
	        
	        System.out.println("*******************************************************************\n");
		}
		
        return mensagemClienteDecriptografada;
	}
	
	private byte[] montaMensagemCliente(byte[] mensagemRecebida, byte[] mensagemClienteDecriptografada, DES_Manager clienteKey, SecretKey chave) throws Exception
	{
		byte[]		mensagemACriptografar			= null;
		byte[]		mensagemCliente					= null;
		
		// id da mensagem
        mensagemCliente = new byte[]{converterIntEmByte(2)};
        
        // Mensagem a criptografar para o cliente -> chave de sessao + n1
        mensagemACriptografar = append(chave.getEncoded(), 
        							   new byte[]{mensagemClienteDecriptografada[2]});
        
        // Adiciona a mensagem para o cliente criptografada
        mensagemCliente = append(mensagemCliente, 
        						 clienteKey.criptografarByte(mensagemACriptografar));
        
        if(trabalho04.debug)
        	System.out.println("\nO tamanho da mensagem criptografada com a chave do cliente e: " + (mensagemCliente.length - 1));
        
		return mensagemCliente;
	}
	
	private byte[] montaMensagemTicketGrantingServer(byte[] mensagemRecebida, byte[] mensagemClienteDecriptografada, SecretKey chave) throws Exception
	{
		byte[]		mensagemACriptografar			= null;
		byte[]		ticketTGS						= null;
		
		// Tempo de validade do ticket
        Calendar 	calendar 						= new GregorianCalendar();   
        
       // Adiciona o tempo de acesso permitido
        calendar.add(Calendar.MINUTE, 
        			 (int)mensagemClienteDecriptografada[1]);
        
        byte ano 	= converterIntEmByte(calendar.get(GregorianCalendar.YEAR) % 2000);
        byte mes 	= converterIntEmByte(calendar.get(GregorianCalendar.MONTH));
        byte dia 	= converterIntEmByte(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        byte hora 	= converterIntEmByte(calendar.get(GregorianCalendar.HOUR_OF_DAY));
        byte min 	= converterIntEmByte(calendar.get(GregorianCalendar.MINUTE));
        
        //////////////////////////////////////////////////////////////////////
        // Cria o Ticket de sessao -> T_c_tgs = {ID_C + T_R + K_c_tgs}K_tgs //
        //////////////////////////////////////////////////////////////////////
        
        // Ticket com o ID do cliente e o tempo requisitado
        mensagemACriptografar = new byte[]{mensagemRecebida[1],
        								   dia, 
        								   mes, 
        								   ano, 
        								   hora, 
        								   min};
        
        if(trabalho04.debug)
        	System.out.println("\nValidade do ticket: "	+ (int)dia + "/" + ((int)mes +1) + "/" + (int)ano + " " + (int)hora + ":" + (int)min);
        
        //Adiciona a chave de sessao
        mensagemACriptografar = append(mensagemACriptografar, 
        							   chave.getEncoded());
        
        // Criptografa tudo com a chave do TGS
        ticketTGS = ticketGrantingServerKey.criptografarByte(mensagemACriptografar);
        
        if(trabalho04.debug)
        	System.out.println("\nValor do Ticket para o TGS: " + Base64.getEncoder().withoutPadding().encodeToString(ticketTGS));
        
        return ticketTGS;
	}
}
