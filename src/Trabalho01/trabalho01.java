package Trabalho01;

import java.util.Scanner;

public class trabalho01
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void imprimirMenuTrabalho01()
	{
		System.out.println("\nQual implementação?");
		
		boolean opcaoValida = false;
		
		while(!opcaoValida)
		{
			System.out.println("a. Cesar\n"
							+  "b. Vernan\n"
							+  "c. Analisador de Frequência");
			
			switch (scanKeyboard.nextLine())
			{
				case "a":
					Cesar.imprimirMenuCesar();
					
					opcaoValida = true;
					break;
					
				case "b":
					Vernan.imprimirMenuVernan();
					
					opcaoValida = true;
					break;
					
				case "c":
					AnalisadorDeFrequencia.imprimirMenuAnalisadorDeFrequencia();
					
					opcaoValida = true;
					break;
		
				default:
					System.out.println("Opção náo é válida. Escolha novamente\n");
					
					break;
			}
		}
		
		return;
	}
}
