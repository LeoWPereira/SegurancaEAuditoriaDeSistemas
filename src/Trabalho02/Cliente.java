package Trabalho02;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Cliente 
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	private static Calendar calendar;	
	
	static String[] senhasDescartaveis = new String[5];
	
	public static void imprimirMenu() throws InterruptedException
	{
		int minNow, minPrev;
        
		int senhasDisponiveis;
        
		String senhaAtt, senhaCliente;
        
		boolean senhaValida;
        
		try 
		{			
			 while (true) 
			 {
		            calendar = Calendar.getInstance();
		            
		            minNow = minPrev = calendar.get(Calendar.MINUTE);

		            //Gera 5 senhas
		            senhaAtt = trabalho02.seedPassword + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE);
		            
		            senhasDescartaveis[0] = trabalho02.base64Encode(trabalho02.algorithm.digest(senhaAtt.getBytes("UTF-8")));
		            
		            for (int i = 1; i < 5; i++)
		                senhasDescartaveis[i] = trabalho02.base64Encode(trabalho02.algorithm.digest(senhasDescartaveis[i - 1].getBytes("UTF-8")));
		             
		            senhasDisponiveis = 5;

		            //Enquanto o tempo for menor que 1 minuto e o numero de senhas disponiveis for maior que 0
		            do 
		            {
		            	senhaValida= false;
		            	
		                System.out.println("Digite sua senha: ");
		                
		                senhaCliente = scanKeyboard.next();
		                
		                //Gera a Hash do Cliente
		                // Procura a senha na lista de senhas geradas
		                for (int i = senhasDisponiveis; i > 0; i--) 
		                {
		                    if ((senhasDescartaveis[i-1].substring(0,5)).equals(senhaCliente)) 
		                    {
		                        System.out.println("***Senha valida***\n");
		                    
		                        //Se achou a senha, da um update do no senhas Dispon�veis para que 
		                        // as senhas depois dessa n�o possam mais ser usadas.
		                        senhasDisponiveis = i-1;
		                        
		                        senhaValida = true;
		                    }
		                }
		                
		                if(!senhaValida)
		                {
		                    System.out.println("Senha invalida");
		                }
		                
		                calendar = Calendar.getInstance();
		                
		                minNow = calendar.get(Calendar.MINUTE);

		            } while (minNow == minPrev);
		        }
		}
		
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		
		return;
	}
}
