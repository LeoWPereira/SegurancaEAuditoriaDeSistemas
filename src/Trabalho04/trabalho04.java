package Trabalho04;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class trabalho04
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void imprimirMenuTrabalho04() throws InterruptedException
	{
		System.out.println("\nTrabalho não desenvolvido ainda. Retornando ao menu anterior\n");
		
		int tempoRestante = 3;
		
		while(tempoRestante > 0)
		{
			TimeUnit.SECONDS.sleep(1);
			
			System.out.println(".\n");
			
			tempoRestante--;
		}
		
		return;
	}
}
