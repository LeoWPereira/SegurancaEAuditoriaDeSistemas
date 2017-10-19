package Trabalho04;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import Trabalho04.trabalho04.tiposServicos;
import Trabalho04.trabalho04.tiposSocket;

public class UDP_Manager extends Thread
{
	private DatagramSocket 			socket_Datagram = null;
    private InetAddress 			host_Inet 		= null;
    private DatagramPacket			request			= null;
    private tiposServicos			tipoServico		= null;
    
    private AgentServer 			agentServer;
    private TicketGrantingServer 	ticketGrantingServer;
    private ServiceServer			serviceServer; 
    private Cliente					cliente;
    
	public UDP_Manager(tiposServicos tipoServico, int porta, AgentServer ag, TicketGrantingServer tgs, ServiceServer service, Cliente cliente, tiposSocket tipoSocket)
	{
		this.tipoServico	= tipoServico;
		
		try
		{
			if(tipoSocket == tiposSocket.ENVIAR)
				socket_Datagram	= new DatagramSocket();
			
			else
				socket_Datagram	= new DatagramSocket(porta);
			
			switch(this.tipoServico)
			{
				case CLIENTE:
					this.cliente = cliente;
					
					break;
					
				case AGENT_SERVER:
					this.agentServer = ag;
					
					break;
					
				case SERVICE_SERVER:
					this.serviceServer = service;
					
					break;
					
				case TICKET_GRANTING_SERVER:
					this.ticketGrantingServer = tgs;
					
					break;
				
				default:
					break;
			}
		}
		
		catch (SocketException e)
		{
			System.out.println("Falha no Socket: " + e.getMessage());
		}
		
		return;
	}
	
	public void enviaMensagem(byte[] mensagem, String enderecoDestino, int portaDestino) throws java.rmi.UnknownHostException, IOException 
	{
		switch(this.tipoServico)
		{
			case CLIENTE:
				System.out.println("Cliente enviou uma mensagem!\n\n");
				
				break;
			
			case AGENT_SERVER:
				System.out.println("Agent Server enviou uma mensagem!\n\n");
				
				break;
			
			case SERVICE_SERVER:
				System.out.println("Service Server enviou uma mensagem!\n\n");

				break;
			
			case TICKET_GRANTING_SERVER:
				System.out.println("Ticket Granting Server enviou uma mensagem!\n\n");
				
				break;
			
			default:
				break;
		}
		
		switch(this.tipoServico)
		{
		case AGENT_SERVER:
		case CLIENTE:
		case SERVICE_SERVER:
		case TICKET_GRANTING_SERVER:
			host_Inet	= InetAddress.getByName(enderecoDestino);
			
			if(trabalho04.debug)
			{
				System.out.println("Opcode: " + (char)mensagem[0] +
								   "\nDestino: " + host_Inet.getHostName() + " - " + host_Inet.getHostAddress() +
								   "\nPorta: " + portaDestino);
			}
			
			request	= new DatagramPacket(mensagem, 
										 mensagem.length, 
										 host_Inet, 
										 portaDestino);
			
			socket_Datagram.send(request);
			
			break;
		
			default:
				break;
		}
		
		return;
	}
	
	public void run() 
	{
		byte[] buffer = new byte[100];
		byte[] mensagemRecebida;
		
		while(true)
		{
			try
			{
				// Recebe a mensagem
				request	= new DatagramPacket(buffer, 
											 buffer.length);
				
				socket_Datagram.receive(request);
				
				mensagemRecebida	= request.getData();
				
				switch(this.tipoServico)
				{
					case CLIENTE:
						byte[] mensagemCriptografada;
						
						switch((int) mensagemRecebida[0])
						{
							case 2:
								mensagemCriptografada = Arrays.copyOfRange(mensagemRecebida, 
																		   1, 
																		   17);
			                    
								byte[] ticketTGS = Arrays.copyOfRange(mensagemRecebida, 
																	  17, 
																	  33);
			                    
								System.out.println("\n\nCliente recebeu mensagem do Agent Server");
			                    
								cliente.receberMensagemAgentServer(ticketTGS,
																   mensagemCriptografada);
			                    
								break;
								
							case 4:
								mensagemCriptografada = Arrays.copyOfRange(mensagemRecebida, 
																		   1, 
																		   17);
			                    
								byte[] ticketServico = Arrays.copyOfRange(mensagemRecebida, 
																		  17, 
																		  33);
			                    
								System.out.println("\n\nCliente recebeu mensagem do Ticket Granting Server");
			                    
								cliente.receberMensagemTicketGrantingServer(ticketServico,
																			mensagemCriptografada);
			                    
								break;
								
							case 6:
								mensagemCriptografada = Arrays.copyOfRange(mensagemRecebida, 
																		   1, 
																		   9);
			                    
								System.out.println("\n\nCliente recebeu mensagem do Service Server");
			                    
								cliente.receberMensagemServiceServer(mensagemCriptografada);
			                    
								break;
							
							default:
								break;
						}
						
						break;
					
					case AGENT_SERVER:
						System.out.println("\n\nAgent Server recebeu a mensagem: " + mensagemRecebida[0]);
						
						// Recebeu mensagem do cliente 
						if((int)mensagemRecebida[0] == 1)
							agentServer.responderCliente(mensagemRecebida);
						
						break;
					
					case SERVICE_SERVER:
						System.out.println("\n\nService Server recebeu a mensagem: " + mensagemRecebida[0]);
						
						// Recebeu mensagem do cliente 
						if((int)mensagemRecebida[0] == 5)
						{
							byte[] mensagemCliente = Arrays.copyOfRange(mensagemRecebida, 
																		1, 
																		17);
		                    
							byte[] ticketServico = Arrays.copyOfRange(mensagemRecebida, 
																	  17, 
																	  33);
							
							serviceServer.responderCliente(mensagemCliente, 
														   ticketServico);
						}
						
						break;
					
					case TICKET_GRANTING_SERVER:	                
		                System.out.println("\n\nTicket Granting Server recebeu a mensagem " + mensagemRecebida[0]);
		                
		                // Recebeu mensagem do cliente
		                if ((int) mensagemRecebida[0] == 3)
		                {
		                    byte[] mensagemCliente = Arrays.copyOfRange(mensagemRecebida, 
		                    											1, 
		                    											17);
		                    
		                    byte[] ticketTGS = Arrays.copyOfRange(mensagemRecebida, 
		                    									  17, 
		                    									  33);
		                    
		                    ticketGrantingServer.responderCliente(mensagemCliente, 
		                    					 				  ticketTGS);
		                }
						
						break;
					
					default:
						break;
				}
			}
			
			catch(IOException e)
			{
				Logger.getLogger(UDP_Manager.class.getName()).log(Level.SEVERE, 
								 null, 
								 e);
			}
			
			catch(NoSuchAlgorithmException e)
			{
				Logger.getLogger(UDP_Manager.class.getName()).log(Level.SEVERE, 
								 null, 
								 e);
			}
			
			catch(Exception e)
			{
				Logger.getLogger(UDP_Manager.class.getName()).log(Level.SEVERE, 
								 null, 
								 e);
			}
		}
	}
}
