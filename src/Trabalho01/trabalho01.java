package Trabalho01;

import java.util.Scanner;

public class trabalho01
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void imprimirMenuTrabalho01()
	{
		System.out.println("\nQual implementa��o?");
		
		boolean opcaoValida = false;
		
		while(!opcaoValida)
		{
			System.out.println("a. Cesar\n"
							+  "b. Vernan\n"
							+  "c. Analisador de Frequ�ncia");
			
			switch (scanKeyboard.nextLine())
			{
				case "A":
				case "a":
					Cesar.imprimirMenuCesar();
					
					opcaoValida = true;
					break;
					
				case "B":
				case "b":
					Vernan.imprimirMenuVernan();
					
					opcaoValida = true;
					break;
					
				case "C":
				case "c":
					AnalisadorDeFrequencia.imprimirMenuAnalisadorDeFrequencia();
					
					opcaoValida = true;
					break;
					
				default:
					System.out.println("Op��o n�o � v�lida. Escolha novamente\n");
					
					break;
			}
		}
		
		return;
	}
	
	public static String convertToASCII2(String text) 
	{
        return text.replaceAll("[���]", "a")
                   .replaceAll("[��]", "e")
	               .replaceAll("[�]", "i")
	               .replaceAll("[��]", "o")
	               .replaceAll("[�]", "u")
	               .replaceAll("[���]", "A")
	               .replaceAll("[��]", "E")
	               .replaceAll("[�]", "I")
	               .replaceAll("[��]", "O")
	               .replaceAll("[�]", "U")
	               .replace('�', 'c')
	               .replace('�', 'C');
    }
}
