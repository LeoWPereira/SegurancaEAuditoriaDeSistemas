package Trabalho01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Vigenere 
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	private static BufferedReader br;
	
	private static BufferedWriter bw;
    
	private static FileWriter fw;
	
	public static void imprimirMenuVegenere()
	{
//		System.out.println("Este extra utilizará o método de Kasiski-Babbage e demonstrará passo a passo como fazer para decifrar uma cifra de Vigenère");
//		
//		System.out.println("Para codificar um texto, digite o comando:   -c 'chave'		texto-entrada.txt	texto-saida.txt\n"
//						+  "Para decodificar um texto, digite o comando: -d 'chave'		texto-entrada.txt	texto-saida.txt");
//		
//		while(!verificarComando(scanKeyboard.nextLine().split(" ")));
		
		 String key = "PLUTO";
	        String ori = "ISYUZPNEVOIQIKHWPGHGIHCERNPNFCHEBHATWSGOGCUMWKPQAWRSCTAPMINH IZJXBXYYBHWPLXLEPWMBDCMHZXNCMPTWCXTBLXBBSPYWKDFFWWQPNHSMAYVHXECGQPDYPVTCYFMKPLRGTYMXGGPDXQIEBXWGZQGSKTXXBRPSXHBLXTAXYIMOCOPXFNDOKSAJXHWCZNWFTLGUIIEIFCGCIPWSTYTBSEIWONTQHIAOOGPJCXXBBJMHIAXSBABPXBOIPJNFEZMXWHEIIZPNYUSUZLXHWPQHFAOJEOXYFRGJNWBBREFROCOQBHWZOMQDXGXBILMXFXPMHTBPLXVDFMXVDWXXJTYNLWCEBXWGNIGGTBOXBRPMMVTDYXJTYNLVPGYMSGCCYWTOBTJTEIKHJCYWVPGYWSHELHMTOGXMTECPWAWHHHPENXAEENHSMAINBSEBXAIZGXHWPSAOKPJKSHPHMSSWCMHAPVNHWZLKCGEIFOCJNASNHCEZHPYFZTDMMSGCCUZTEBTBQLLHEJPMASGPUYHTCJXFWLJLGDXYBBIPFESREGTMQPZHICOQAWRSQBZACYWIRPGTDWLHMOHXNHHWPWHABZHIZPNYLCBPCGHTWFXQIXIKSRLFFADCYECVTWTZPYXYOGWYLGTIWBHPMFXHWLHFMDHHPVXNBPWAWJXFRPCOSXYNASRTLVIBDNTBRPMBRTEUBZLTNAOLPHHH";
	        String enc = encrypt(ori, key);
	        System.out.println(enc);
	        System.out.println(decrypt(enc, key));
		
		return;
	}
	
	static String encrypt(String text, final String key) {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') continue;
            res += (char)((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % key.length();
        }
        return res;
    }
 
    static String decrypt(String text, final String key) {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') continue;
            res += (char)((c - key.charAt(j) + 26) % 26 + 'A');
            j = ++j % key.length();
        }
        return res;
    }
	
	public static boolean verificarComando(String[] args)
	{
		if((args.length != 4) ||
			((!args[0].equalsIgnoreCase("-c")) && (!args[0].equalsIgnoreCase("-d"))))
		{
			System.out.println("Comando digitado de forma incorreta. Entre com novo comando ...");
			
			return false;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// comando está correto, agora precisa avaliar e armazenar a chave e executar o processo principal de (de)codificação //
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 
		 if(args[0].equalsIgnoreCase("-c"))
			 codificar(args[1], args[2], args[3]);
		 
		 else
			 decodificar(args[1], args[2], args[3]);
		 
		return true;
	}
	
	public static String codificar(String key, String arqAberto, String arqCifrado) 
	{
        String res = "";
        
        arqCifrado = arqCifrado.toUpperCase();
        
        for (int i = 0, j = 0; i < arqCifrado.length(); i++) 
        {
            char c = arqCifrado.charAt(i);
        
            if (c < 'A' || c > 'Z') 
            	continue;
            
            res += (char)((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
            
            j = ++j % key.length();
        }
        
        return res;
    }
	
	public static String decodificar(String key, String arqCifrado, String arqAberto)
	{
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(arqCifrado), "ISO-8859-1"));

            fw = new FileWriter(arqAberto);
            bw = new BufferedWriter(fw);
            
            String x = "";
            String res = "";
            
            char[] linha;

            while ((x = br.readLine()) != null) 
            {
                //Converte a linha lida para minusculas e retira acentos antes de tratar
                linha = trabalho01.convertToASCII2(x).toCharArray();

                for (int i = 0, j = 0; i < linha.length; i++) 
                {
                	char c = x.charAt(i);
                    
                    if (c < 'A' || c > 'Z') 
                    	continue;
                    
                    res += (char)((c - key.charAt(j) + 26) % 26 + 'A');
                    
                    j = ++j % key.length();
                    
                    bw.write(res);
                }
                
                bw.write("\r\n");
            }
            
			bw.close();
            
            br.close();
		}

		catch (FileNotFoundException e) 
		{
            System.out.println("Arquivo não existe.");
        } 
		
		catch (IOException e) 
		{
            System.out.println("Erro na leitura do arquivo.");
        }
		
		
        String res = "";
        
        arqCifrado = arqCifrado.toUpperCase();
        
        for (int i = 0, j = 0; i < arqCifrado.length(); i++) 
        {
            char c = arqCifrado.charAt(i);
        
            if (c < 'A' || c > 'Z') 
            	continue;
            
            res += (char)((c - key.charAt(j) + 26) % 26 + 'A');
            
            j = ++j % key.length();
        }
        
        return res;
    }
}
