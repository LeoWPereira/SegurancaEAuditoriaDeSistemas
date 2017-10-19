package Trabalho04;

import java.util.Arrays;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Trabalho04.trabalho04.servicosRequisitados;
import Trabalho04.trabalho04.tiposServicos;
import Trabalho04.trabalho04.tiposSocket;

public class Cliente extends ServicoBase
{
	static private UDP_Manager 			enviaUDP;
	static private UDP_Manager 			recebeUDP;
	
	static private DES_Manager 			clienteKey;
	static private DES_Manager 			sessaoTicketGrantingServer;
	static private DES_Manager 			sessaoServiceServer;
	
	static private Random				numeroRandomico;

    static private byte 				tempoDeAcesso;
    
    static private byte   				idCliente;
    
    static private byte[] 				numAleatorioMensagem;
    
    static private servicosRequisitados	servicoRequisitado;
    
    private static Scanner 				scanKeyboard 					= new Scanner(System.in);
    
    static private int					inteiro1;
    static private int					inteiro2;
    
    static private int					portaServicoRequisitado;
    
	public Cliente() throws Exception
	{
		enviaUDP = new UDP_Manager(tiposServicos.CLIENTE,
								   trabalho04.portaEnviaUDPCliente,
								   null,
								   null,
								   null,
								   this,
								   tiposSocket.ENVIAR);
				
		recebeUDP = new UDP_Manager(tiposServicos.CLIENTE,
									trabalho04.portaRecebeUDPCliente,
									null,
									null,
									null,
									this,
									tiposSocket.RECEBER);
		
		clienteKey = new DES_Manager(lerChaveArquivo(trabalho04.ClienteArquivoChave, 
													 0));
		
		numeroRandomico 		= new Random();
		
		numAleatorioMensagem 	= new byte[3];
        
		tempoDeAcesso 			= converterIntEmByte(trabalho04.tempoDeAcesso);

		return;
	}
	
	public static void imprimirMenu() throws Exception
	{	
		System.out.println("\n\t\t\t\t\tCLIENTE SERVER");
		
		boolean opcaoValida = false;
		
		while(!opcaoValida)
		{
			System.out.println("Deseja gerar uma nova chave?");
			
			System.out.println("a. Sim\n"
							+  "b. Nao");
			
			switch (scanKeyboard.nextLine())
			{
				case "A":
				case "a":			
					geraChave();

					break;
					
				case "B":
				case "b":
					rotinaPrincipal();
					
					opcaoValida = true;
					
					break;
					
				default:
					System.out.println("Opcao invalida. Escolha novamente\n");
					
					break;
			}
		}
		
		return;
	}
	
	public static void rotinaPrincipal() throws Exception
	{
		Cliente cliente = new Cliente();
		
		cliente.inicializarServico();
		
		cliente.executaLogin();
		
		cliente.enviaPedidoParaAgentServer();
		
		while(true);
	}
	
	private void inicializarServico()
	{
		recebeUDP.start();
		
		return;
	}
	
	private void executaLogin()
	{
		idCliente	= converterIntEmByte(1);
		
		System.out.println("Qual servico voce gostaria de executar?");
		
		System.out.println("1. Servico Soma: ");
		
		switch(scanKeyboard.nextInt()) 
		{
			case 1:
				servicoRequisitado 		= servicosRequisitados.SOMAR_NUMEROS;
				
				portaServicoRequisitado = trabalho04.portaRecebeUDPServiceServer_SOMAINTEIROS;
				
				System.out.println("Digite o primeiro valor: ");
				
				inteiro1				= scanKeyboard.nextInt();
				
				System.out.println("Digite o segundo valor: ");
				
				inteiro2				= scanKeyboard.nextInt();
				
				break;
	
			default:
				break;
		}
		
		return;
	}
	
	private void enviaPedidoParaAgentServer() throws Exception
	{
		byte[] mensagemAEnviar 			= null;
		byte[] mensagemCriptografada	= null;
		
		if(trabalho04.debug)
		{
			System.out.println("\n*******************************************************************");
			System.out.println("\n\t\t\tMensagem 1 para Agent Server\n\n");
		}
		
		// Criptografa a mensagem
        mensagemCriptografada = clienteKey.criptografarByte(montarMensagemACriptografar());
		
		mensagemAEnviar = new byte[]{converterIntEmByte(1), 
									 idCliente};
		
		mensagemAEnviar = append(mensagemAEnviar, 
								 mensagemCriptografada);
		
		if(trabalho04.debug)
        {
        	System.out.println("\n\t\t\t\tId do cliente: " 	+ (int)(mensagemAEnviar[1]) 
        					 + "\n\t\t\t\tId do servico: "  + (int)(clienteKey.decriptografarByte(mensagemCriptografada)[0])
        					 + "\n\t\t\t\tTempo: "			+ (int)(clienteKey.decriptografarByte(mensagemCriptografada)[1]) 
        					 + "\n\t\t\t\tNum aleatorio: " 	+ (int)(clienteKey.decriptografarByte(mensagemCriptografada)[2])  
        					 + "\n\n\n");
        	
        	System.out.println("*******************************************************************\n");
        }
		
		enviaUDP.enviaMensagem(mensagemAEnviar, 
							   "localhost", 
							   trabalho04.portaRecebeUDPAgentServer);
		
		return;
	}
	
	public byte[] montarMensagemACriptografar()
	{
		byte[] mensagemACriptografar 	= new byte[3];
		
		// Id do servico
        mensagemACriptografar[0] = converterIntEmByte(servicoRequisitado.ordinal() + 1);	
        
        // Tempo de acesso - 10 minutos
        mensagemACriptografar[1] = tempoDeAcesso;
        
        // Maior numero aleatorio = 0xFF 
        mensagemACriptografar[2] = numAleatorioMensagem[0] = converterIntEmByte(numeroRandomico.nextInt(255));
        
        return mensagemACriptografar;
	}
	
	public void receberMensagemAgentServer(byte[] ticketTGS, byte[] mensagemCriptografada) throws Exception 
	{
		SecretKey 	keyCToTgs;
        
		byte[] 		mensagemTicketGrantingServer;
		byte[] 		mensagemDecriptografada;
        byte[] 		mensagemToTGSEncrypted;

        System.out.println("Ticket TGS recebido: " + Base64.getEncoder().withoutPadding().encodeToString(ticketTGS));

        // Decriptografa a mensagem
        mensagemDecriptografada = clienteKey.decriptografarByte(mensagemCriptografada);

        if(trabalho04.debug)
        {
	        System.out.println("\n\nTamanho da mensagem decodificada: " + mensagemDecriptografada.length 
	        				 + "\nNúmero aleatorio recebido: " 			+ (int) mensagemDecriptografada[8]);
        }
        
        // Verifica se o numero aleatorio recebido e o mesmo 
        if (mensagemDecriptografada[8] != numAleatorioMensagem[0]) 
        {
        	if(trabalho04.debug)
        		System.out.println("Erro na comunicacao com o Agent Server!\n\n\n\n\n");
        }
        
        else 
        {
            // Pega a chave de sessao com o TGS (possui tamanho 8)
            keyCToTgs = new SecretKeySpec(Arrays.copyOfRange(mensagemDecriptografada, 
            												 0, 
            												 8), 
            						      "DES");
            
            sessaoTicketGrantingServer = new DES_Manager(keyCToTgs);
            
            if(trabalho04.debug)
            	System.out.println("Chave de sessao com o servico: " + Base64.getEncoder().withoutPadding().encodeToString(keyCToTgs.getEncoded()));

            // Criptografa a mensagem
            mensagemToTGSEncrypted = sessaoTicketGrantingServer.criptografarByte(montaMensagemTicketGrantingServer());

            // Junta o id da mensagem com a mensagem criptografada e o ticket
            mensagemTicketGrantingServer = new byte[]{converterIntEmByte(3)};
            
            // M3 = [{ID_C + ID_S + T_R + N2 }K_c_tgs + T_c_tgs]
            mensagemTicketGrantingServer = append(mensagemTicketGrantingServer, 
            									  mensagemToTGSEncrypted, 
            									  ticketTGS);
            
            if(trabalho04.debug)
            	System.out.println("Tamanho total da mensagem enviada para o TGS: " + mensagemTicketGrantingServer.length);

            enviaUDP.enviaMensagem(mensagemTicketGrantingServer, 
            					   "localhost", 
            					   trabalho04.portaRecebeUDPTicketGrantingServer);
        }
        
        return;
	}
	
	private byte[] montaMensagemTicketGrantingServer()
	{
		byte[]				mensagemACriptografar;
		
        GregorianCalendar 	calendar 	= new GregorianCalendar();
        
        byte ano 	= converterIntEmByte(calendar.get(GregorianCalendar.YEAR) % 2000);
        byte mes 	= converterIntEmByte(calendar.get(GregorianCalendar.MONTH));
        byte dia 	= converterIntEmByte(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        byte hora 	= converterIntEmByte(calendar.get(GregorianCalendar.HOUR_OF_DAY));
        byte min 	= converterIntEmByte(calendar.get(GregorianCalendar.MINUTE));

        ////////////////////////////////////////////////////////////////////////////
        // Monta mensagem para o TGS com o a chave da sessao para falar com o TGS //
        //		{ID_C + ID_S + T_R + N2 }K_c_tgs, onde TR e a data atual		  //
        ////////////////////////////////////////////////////////////////////////////
        
        mensagemACriptografar = new byte[]{idCliente, 
        								   converterIntEmByte(servicoRequisitado.ordinal() + 1), 
        								   dia, 
        								   mes, 
        								   ano, 
        								   hora, 
        								   min, 
        								   numAleatorioMensagem[1] = converterIntEmByte(numeroRandomico.nextInt(255))};

        if(trabalho04.debug)
        	System.out.println("\nTamanho da mensagem a ser criptografada com a chave de sessao do TGS: " + mensagemACriptografar.length);
		
		return mensagemACriptografar;
	}
	
	public void receberMensagemTicketGrantingServer(byte[] ticketServico, byte[] mensagemCriptografada) throws Exception 
	{		
        byte[] mensagemDecriptografada;
        
        byte[] mensagemCriptografadaServico;
        
        byte[] mensagemServico;
        
        // Decriptografa a mensagem
        mensagemDecriptografada = sessaoTicketGrantingServer.decriptografarByte(mensagemCriptografada);

        // Verifica se o numero aleatorio recebido e o mesmo
        if (mensagemDecriptografada[8] != numAleatorioMensagem[1]) 
            System.out.println("Erro na comunicacao com o Ticket Granting Server!\n\n\n\n\n"); 
        
        else 
        {
            // Pega a chave de sessao com o servico (possui tamanho 8)
        	SecretKey chaveServico = new SecretKeySpec(Arrays.copyOfRange(mensagemDecriptografada, 
        																  0, 
        																  8), 
        											   "DES");
            
            sessaoServiceServer = new DES_Manager(chaveServico);
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Junta o id da mensagem com a mensagem criptografada e o ticket -> M5 = [{ID_C + T_A + S_R + N3}K_c_s + T_c_s] //
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            mensagemCriptografadaServico = this.montaMensagemCriptografadaServico();
            
            mensagemServico = new byte[]{converterIntEmByte(5)};
            
            mensagemServico = append(mensagemServico, 
            						 sessaoServiceServer.criptografarByte(mensagemCriptografadaServico), 
            						 ticketServico);

            if(trabalho04.debug)
            {
            	System.out.println("Tamanho total da mensagem enviada para o Servico: " + mensagemServico.length
            					 + "\nMensagem enviada para o Servico: " + mensagemCriptografadaServico
            					 + "\nTicket enviada para o Servico: "   + ticketServico);
            }
            
            // Envia para o Service Server
            enviaUDP.enviaMensagem(mensagemServico, 
            					   "localhost", 
            					   portaServicoRequisitado);
        }
        
		return;
	}
	
	private byte[] montaMensagemCriptografadaServico()
	{
		byte[] mensagemACriptografar;
			
		// Armazena a data/hora atual
        GregorianCalendar calendar 	= new GregorianCalendar();
        
        byte ano 	= converterIntEmByte(calendar.get(GregorianCalendar.YEAR) % 2000);
        byte mes 	= converterIntEmByte(calendar.get(GregorianCalendar.MONTH));
        byte dia 	= converterIntEmByte(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        byte hora 	= converterIntEmByte(calendar.get(GregorianCalendar.HOUR_OF_DAY));
        byte min 	= converterIntEmByte(calendar.get(GregorianCalendar.MINUTE));
        
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Monta mensagem para o Servico com a chave da sessao para falar com o Servico -> {ID_C + T_A + S_R + N3}K_c_s //
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		mensagemACriptografar = new byte[]{idCliente, 
										   dia, 
										   mes, 
										   ano, 
										   hora, 
										   min, 
										   converterIntEmByte(servicoRequisitado.ordinal() + 1), 
										   converterIntEmByte(inteiro1),
										   converterIntEmByte(inteiro2), 
										   numAleatorioMensagem[2] = converterIntEmByte(numeroRandomico.nextInt(255))};
		
		if(trabalho04.debug)
			System.out.println("Tamanho da requisicao antes de criptografar: " + mensagemACriptografar.length);
		
		return mensagemACriptografar;
	}
	
	public void receberMensagemServiceServer(byte[] mensagemCriptografada) throws Exception 
	{
        // Decriptografa a mensagem
        byte[] mensagemDecriptografada = sessaoServiceServer.decriptografarByte(mensagemCriptografada);

        // Verifica se o numero aleatorio recebido e o mesmo
        if (mensagemDecriptografada[1] != numAleatorioMensagem[2]) 
            System.out.println("Erro na comunicação com o Service Server!\n\n\n\n\n"); 
        
        else 
        {
            // Exibe o resultado da mensagem
            System.out.println("O resultado do servico Requisitado e: " + mensagemDecriptografada[0]);
        }

		return;
	}
}
