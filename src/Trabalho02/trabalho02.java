package Trabalho02;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class trabalho02
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void imprimirMenuTrabalho02() throws InterruptedException
	{
		System.out.println("\nTrabalho n�o desenvolvido ainda. Retornando ao menu anterior\n");
		
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