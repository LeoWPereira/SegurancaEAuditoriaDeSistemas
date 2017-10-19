package Trabalho04;

import java.io.IOException;
import java.util.Scanner;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import Trabalho04.Cliente;
import Trabalho04.AgentServer;
import Trabalho04.TicketGrantingServer;
import Trabalho04.ServiceServer;

public class trabalho04
{
	public static 	boolean	debug										= true;
	
	public static 	int 	portaEnviaUDPCliente 						= 6000;
	public static 	int 	portaEnviaUDPAgentServer 					= 6001;
	public static 	int 	portaEnviaUDPTicketGrantingServer 			= 6002;
	public static 	int 	portaEnviaUDPServiceServer_SOMAINTEIROS 	= 6003;
	
	public static 	int 	portaRecebeUDPCliente 						= 6200;
	public static 	int 	portaRecebeUDPAgentServer 					= 6201;
	public static 	int 	portaRecebeUDPTicketGrantingServer 			= 6202;
	public static 	int 	portaRecebeUDPServiceServer_SOMAINTEIROS 	= 6203;
	
	public static 	int 	tempoDeAcesso							= 10;	// 10 minutos de tempo de requisicao
	
	public static 	String 	ClienteArquivoChave 					= "chavesCliente.txt";
	public static 	String 	agentServerArquivoChave 				= "chavesAgentServer.txt";
	public static 	String 	ticketGrantingServerArquivoChave 		= "chavesTicketGrantingServer.txt";
	public static 	String 	ServiceServerArquivoChave 				= "chavesServiceServer.txt";
	
	public enum tiposServicos
	{
		CLIENTE,
		AGENT_SERVER,
		TICKET_GRANTING_SERVER,
		SERVICE_SERVER		
	}
	
	public enum servicosRequisitados
	{
		SOMAR_NUMEROS
	}
	
	public enum tiposSocket
	{
		ENVIAR,
		RECEBER
	}
	
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void imprimirMenuTrabalho04() throws Exception
	{
		System.out.println("\n\t\t\t\t\tSistema de Autenticacao KERBEROS");
		
		System.out.println("\nQual Terminal?");
		
		boolean opcaoValida = false;
		
		while(!opcaoValida)
		{
			System.out.println("a. Cliente\n"
							+  "b. Agent Server (AS)\n"
							+  "c. Ticket Granting Server(TGS)\n"
							+  "d. Service Server");
			
			switch (scanKeyboard.nextLine())
			{
				case "A":
				case "a":
					Cliente.imprimirMenu();
					
					opcaoValida = true;
					break;
					
				case "B":
				case "b":
					AgentServer.imprimirMenu();
					
					opcaoValida = true;
					break;
				
				case "C":
				case "c":
					TicketGrantingServer.imprimirMenu();
					
					opcaoValida = true;
					break;
				
				case "D":
				case "d":
					ServiceServer.imprimirMenu();
					
					opcaoValida = true;
					break;
					
				default:
					System.out.println("Opcao invalida. Escolha novamente\n");
					
					break;
			}
		}
		
		return;
	}
	
	public static String base64Encode(byte[] bytes) 
	{
        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] base64Decode(String s) throws IOException 
    {
        return new BASE64Decoder().decodeBuffer(s);
    }
}
