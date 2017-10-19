package Trabalho04;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

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
								   trabalho04.portaEnviaUDPServiceServer_SOMAINTEIROS,
								   null,
								   null,
								   this,
								   null,
								   tiposSocket.ENVIAR);
		
		recebeUDP = new UDP_Manager(tiposServicos.SERVICE_SERVER,
									trabalho04.portaRecebeUDPServiceServer_SOMAINTEIROS,
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
		System.out.println("\n\t\t\t\t\tSERVICE SERVER");
		
		ServiceServer serviceServer = new ServiceServer();
		
		serviceServer.inicializarServico();
		
		while(true);
	}
	
	private void inicializarServico()
	{
		recebeUDP.start();
		
		return;
	}
	
	public void responderCliente(byte[] mensagemRecebida, byte[] ticketService) throws NoSuchAlgorithmException, IOException, Exception 
	{
		byte[] mensagemCliente			= null;
		
        byte[] mensagemACriptografar	= null;
        
        if(trabalho04.debug)
        {
        	System.out.println("\nMensagem recebida: " + Base64.getEncoder().withoutPadding().encodeToString(mensagemRecebida)
        					 + "\nTicket recebido: " + Base64.getEncoder().withoutPadding().encodeToString(ticketService));
        
        }
        ///////////////////////////////////////////////////////////////////
        // Decodifica o ticket de sessao T_c_s = {ID_C + T_A + K_c_s}K_s //
        ///////////////////////////////////////////////////////////////////
        
        // Guarda a chave de sessao
        DES_Manager serviceSessionKey = new DES_Manager(new SecretKeySpec(Arrays.copyOfRange(serviceServerKey.decriptografarByte(ticketService), 
        																		 			 6, 
        																		 			 14), 
        													  			  "DES"));
        
        byte idClienteTicket 			= serviceServerKey.decriptografarByte(ticketService)[0];
        byte idClienteMensagemCliente 	= serviceSessionKey.decriptografarByte(mensagemRecebida)[0];
    
        int inteiro1 					= serviceSessionKey.decriptografarByte(mensagemRecebida)[7];
        int inteiro2 					= serviceSessionKey.decriptografarByte(mensagemRecebida)[8];
        
        byte numAleatorio 				= serviceSessionKey.decriptografarByte(mensagemRecebida)[9];
        
        this.mostraDadosMensagemCliente(serviceSessionKey.decriptografarByte(mensagemRecebida), 
        								idClienteMensagemCliente);
        
        // Verifica se o cliente e o dono do ticket e se o ticket e valido
        if((idClienteTicket == idClienteMensagemCliente) && (verificaSeTicketValido(serviceServerKey.decriptografarByte(ticketService))))
        {
            ////////////////////////////////////////////////////////////////
            // Monta mensagem para o cliente - M6 = [{Resposta, N3}K_c_s] //
            ////////////////////////////////////////////////////////////////
        	
            // id da mensagem 
        	mensagemCliente = new byte[]{converterIntEmByte(6)};
            
            // Mensagem a encriptar para o cliente -> chave de sessao + n2
        	mensagemACriptografar = new byte[]{converterIntEmByte(inteiro1 + inteiro2), numAleatorio};
            
            // Adiciona a mensagem para o cliente criptografada com a chave de sessao do Servico
            mensagemCliente = append(mensagemCliente, 
            					  	 serviceSessionKey.criptografarByte(mensagemACriptografar));

            if(trabalho04.debug)
            	System.out.println("\nO tamanho da mensagem criptografada com a chave de sessao do cliente e: " + (mensagemCliente.length - 1));
            
            //Envia a mensagem para o cliente
            enviaUDP.enviaMensagem(mensagemCliente, 
            					   "localhost", 
            					   trabalho04.portaRecebeUDPCliente);
        }
		
		return;
	}
	
	private void mostraDadosMensagemCliente(byte[] mensagemRecebida, byte idClienteMensagemCliente)
	{
		//////////////////////////////////
		// {ID_C + T_A + S_R + N3}K_c_s //
		//////////////////////////////////
		
		int idServico		= (int)mensagemRecebida[6];
        int inteiro1 		= mensagemRecebida[7];
        int inteiro2 		= mensagemRecebida[8];
        
        byte numAleatorio 	= mensagemRecebida[9];
        
		if(trabalho04.debug)
        {
        	System.out.println("\n\nRequisicao do cliente contem as seguintes informacoes:"
        				 	 + "\nId do cliente: " 			+ (int)idClienteMensagemCliente 
        				 	 + "\nServico requisitado: " 	+ idServico 
        				 	 + "\nData da requisicao: " 	+ (int)mensagemRecebida[1] +  "/" + ((int)mensagemRecebida[2] + 1) + "/" + (int)mensagemRecebida[3] + " as " + (int)mensagemRecebida[4] + ":" + (int)mensagemRecebida[5] 
        				 	 + "Numeros a somar: " 			+ inteiro1 + " e " + inteiro2
        				 	 + "\nNumero aleatorio: " 		+ numAleatorio + "\n\n");
        }
		
		return;
	}
}
