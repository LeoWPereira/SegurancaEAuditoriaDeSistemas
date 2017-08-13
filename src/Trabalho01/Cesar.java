/*
 * Atividade: Implementar Cifra de César. Ele deve funcionar da seguinte forma:
 *
 *  Para cifrar:  
 *  cesar -c -k 5 < texto-aberto.txt > texto-cifrado.txt
 *  
 *  Para decifrar:
 *  
 *  cesar -d -k 5 < texto-cifrado.txt > texto-aberto.txt
 *
 * -d -k 17 Decifrar.txt texto-decifrado.txt
 */

package Trabalho01;

import java.util.Scanner;

public class Cesar
{
	private static int chave;
    
	private static String alfabetoCesar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void imprimirMenuCesar()
	{
		System.out.println("\n"
						+  "Para codificar um texto, digite o comando:   cesar -c -k 5 texto-aberto.txt  texto-cifrado.txt\n"
						+  "Para decodificar um texto, digite o comando: cesar -d -k 5 texto-cifrado.txt texto-aberto.txt\n");
		
		while(!verificarComando(scanKeyboard.nextLine().split(" ")));
		
		return;
	}
	
	public static boolean verificarComando(String[] args)
	{
		if((args.length != 5) ||
		   ((!args[0].equalsIgnoreCase("-c")) && (!args[0].equalsIgnoreCase("-d"))) ||
		   (!args[1].equalsIgnoreCase("-k")))
		{
			System.out.println("Comando digitado de forma incorreta. Entre com novo comando ...");
			
			return false;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// comando está correto, agora precisa avaliar e armazenar a chave e executar o processo principal de (de)codificação //
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		 chave = Integer.valueOf(args[2]) % (alfabetoCesar.length());
		 
		 if(args[0].equalsIgnoreCase("-c"))
			 codificar(args[3], args[4]);
		
		 else
			 decodificar(args[3], args[4]);
		 
		return true;
	}
	
	public static void codificar(String arquivoEntrada, String arquivoSaida)
	{
		
	}
	
	public static void decodificar(String arquivoEntrada, String arquivoSaida)
	{
		
	}
}
