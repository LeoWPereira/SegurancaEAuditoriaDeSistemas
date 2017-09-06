package Trabalho02;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Token 
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	private static Calendar calendar;	
	
    static Map<String, String> users = new HashMap<String, String>();
    
    static String [] senhasDescartaveis = new String[5];
	
	public static void imprimirMenu()
	{
		users.put("e54h6t61sgGdp65UX/4Iuw==", "4QrcOUm6Wau+VuBX8g+IPg=="); // <Leonardo, 123456>
		
		try 
		{
			System.out.println("Primeira vez?");
			
			boolean opcaoValida = false;
			
			while(!opcaoValida)
			{
				System.out.println("a. Sim\n"
								+  "b. Não");
				
				switch (scanKeyboard.nextLine())
				{
					case "A":
					case "a":			
						geraHashDaSenha();
						
						opcaoValida = true;
						break;
						
					case "B":
					case "b":
						menuUsuario();
						
						opcaoValida = true;
						break;
						
					default:
						System.out.println("Opção inválida. Escolha novamente\n");
						
						break;
				}
			}
		}
		
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	public static void menuUsuario() throws UnsupportedEncodingException
	{
		System.out.println("Digite o seu nome de usuário: ");
		
		byte []algorithmHelper = trabalho02.algorithm.digest(scanKeyboard.next().getBytes("UTF-8"));
        
		if(!users.containsKey(trabalho02.base64Encode(algorithmHelper)))
		{
			System.out.println("Usuário inválido");
			
			return;
		}
		
		System.out.println("Digite a sua senha: ");
        
		byte []senhaHashLocal = trabalho02.algorithm.digest(scanKeyboard.next().getBytes("UTF-8"));
        
        if(users.get(trabalho02.base64Encode(algorithmHelper)).equals(trabalho02.base64Encode(senhaHashLocal)))
        {
            System.out.println("Senha autenticada com sucesso.");
            
            int minNow, minPrev;
            int senhasDisponiveis = 5;
            String senhaAtt;           
            
            while(true)
            {
                System.out.println("1. Gerar uma nova senha\n2. Terminar o programa");
                
                switch(scanKeyboard.nextInt())
                {
                case 1:
                	calendar = Calendar.getInstance();
                	
                    minNow = minPrev = calendar.get(Calendar.MINUTE);
                    
                    //Gera 5 senhas
                    senhaAtt = trabalho02.seedPassword + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE);

                    senhasDescartaveis[0] = trabalho02.base64Encode(trabalho02.algorithm.digest(senhaAtt.getBytes("UTF-8")));
                    
                    for(int i = 1; i < 5; i++)
                        senhasDescartaveis[i] = trabalho02.base64Encode(trabalho02.algorithm.digest(senhasDescartaveis[i-1].getBytes("UTF-8")));

                    senhasDisponiveis = 5;
                    
                    //Enquanto o tempo for menor que 1 minuto e o numero de senhas disponiveis for maior que 0
                    do
                    {
                        if (senhasDisponiveis > 0)
                        {
                            System.out.println("Você deseja uma nova senha? \nDigite 1 para pegar uma nova senha e 2 para voltar ao menu anterior");
                            
                            if(scanKeyboard.nextInt() == 1)
                            {
                                System.out.println("A nova senha é " + senhasDescartaveis[senhasDisponiveis-1].substring(0,5)+"\n\n\n\n\n\n\n\n\n");
                                
                                senhasDisponiveis--;
                                
                                if(senhasDisponiveis == 0) 
                                	System.out.println("Suas tentativas acabaram. Em no máximo 1 min novas senhas serão geradas");
                            }
                            else
                            	break;   
                        }

                        calendar = Calendar.getInstance();
                        
                        minNow = calendar.get(Calendar.MINUTE);
                        
                    } while(minNow == minPrev);
                    
                    if(minNow != minPrev)
                        System.out.println("Senhas expiradas!");
                    
                	break;
                	
                	default:
                		
                		System.out.println("O programa será encerrado. Obrigada por utilizar o token");
                		
                		return;
                }
            }
        }  
        
        return;
	}
	
	public static void geraHashDaSenha() throws UnsupportedEncodingException
    {
		System.out.println("Vamos configurar o Token!");
		
		
        System.out.println("Primeiramente, digite a sua senha que será utilizada como seed (semente): ");
        
        byte[] senhaBytes= trabalho02.algorithm.digest(scanKeyboard.next().getBytes("UTF-8"));
        
        
        System.out.println("\n\nAgora entre com um nome de usuário!");
	    
        byte[] userBytes = trabalho02.algorithm.digest(scanKeyboard.next().getBytes("UTF-8"));
        
        
        System.out.println("\n\nAgora entre com uma senha para o seu usuário!");
		    
        byte[] localBytes= trabalho02.algorithm.digest(scanKeyboard.next().getBytes("UTF-8"));
        
        
        
        System.out.println("A senha semente é: " + trabalho02.base64Encode(senhaBytes));
        
        System.out.println("O usuário é: " + trabalho02.base64Encode(userBytes));
        System.out.println("A senha local é: " + trabalho02.base64Encode(localBytes));
        
        return;
    }
}
