package Trabalho02;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class trabalho02
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	static MessageDigest algorithm;
	
	static String seedPassword  = "6AtQFwmJUPxYqtg8jBSXjg==";  // Vem de abcdef
	
	public static void imprimirMenuTrabalho02() throws InterruptedException, NoSuchAlgorithmException
	{
		algorithm = MessageDigest.getInstance("MD5");
		
		System.out.println("\nQual Terminal?");
		
		boolean opcaoValida = false;
		
		while(!opcaoValida)
		{
			System.out.println("a. Gerador de senha (Token)\n"
							+  "b. Aplicativo (Cliente)");
			
			switch (scanKeyboard.nextLine())
			{
				case "A":
				case "a":
					Token.imprimirMenu();
					
					opcaoValida = true;
					break;
					
				case "B":
				case "b":
					Cliente.imprimirMenu();
					
					opcaoValida = true;
					break;
					
				default:
					System.out.println("Opção inválida. Escolha novamente\n");
					
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
