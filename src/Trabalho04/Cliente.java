package Trabalho04;

import java.io.IOException;
import java.rmi.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.SecretKey;

import Trabalho04.trabalho04.tiposServicos;
import Trabalho04.trabalho04.tiposSocket;

public class Cliente extends ServicoBase
{
	static private UDP_Manager 		enviaUDP;
	static private UDP_Manager 		recebeUDP;
	
	static private SecretKey 		chave;

	static private DES_Manager 		clienteKey;
	static private DES_Manager 		sessaoTicketGrantingServer;
	static private DES_Manager 		sessaoServiceServer;
	
	static private Random			numeroRandomico;
    
	static private int 				inteiro1;
    static private int 				inteiro2;

    static private byte 			tempoDeAcesso;
    
    static private byte[] 			numAleatorioMensagem;
    
    static private int				servicoRequisitado;
    
    private static Scanner 			scanKeyboard 					= new Scanner(System.in);
    
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
		
		servicoRequisitado 		= 1;

		return;
	}
	
	public static void imprimirMenu() throws Exception
	{	
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
		return;
	}
	
	private void enviaPedidoParaAgentServer() throws Exception
	{
		byte[] mensagemAEnviar 			= null;
		byte[] mensagemCriptografada	= null;
		
		byte idCliente = converterIntEmByte(1);
		
		// Criptografa a mensagem
        mensagemCriptografada = clienteKey.criptografarByte(montarMensagemACriptografar());
		
		mensagemAEnviar = new byte[]{converterIntEmByte(1), idCliente};
		
		mensagemAEnviar = append(mensagemAEnviar, mensagemCriptografada);
		
		enviaUDP.enviaMensagem(mensagemAEnviar, 
							   "localhost", 
							   trabalho04.portaRecebeUDPAgentServer);
		
		return;
	}
	
	public byte[] montarMensagemACriptografar()
	{
		byte[] mensagemACriptografar 	= new byte[3];
		
		// Id do servico
        mensagemACriptografar[0] = converterIntEmByte(servicoRequisitado);	
        
        // Tempo de acesso - 10 minutos
        mensagemACriptografar[1] = tempoDeAcesso;
        
        // Maior numero aleatorio = 0xFF 
        mensagemACriptografar[2] = numAleatorioMensagem[0] = converterIntEmByte(numeroRandomico.nextInt(255));
        
        if(trabalho04.debug)
        {
        	System.out.println("\n\n\nMsg criptografada:\nId do cliente: " + (int) (mensagemACriptografar[0]) + "\nTempo: "
                    		   + (int) (mensagemACriptografar[1]) + "\nNum aleatorio: " + (int) (mensagemACriptografar[2]) + "\n\n\n");
        }
        
        return mensagemACriptografar;
	}
	
	public void receberMensagemAgentServer(byte[] ticketTGS, byte[] mensagemCriptografada) throws Exception 
	{
		
	}
	
	public void receberMensagemTicketGrantingServer(byte[] ticketServico, byte[] mensagemCriptografada) throws Exception 
	{
		
	}
	
	public void receberMensagemServiceServer(byte[] mensagemCriptografada) throws Exception 
	{
		
	}
}
