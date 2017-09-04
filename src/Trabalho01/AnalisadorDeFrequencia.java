package Trabalho01;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AnalisadorDeFrequencia
{
	private static String alfabetoPTBR = "abcdefghijklmnopqrstuvwxyz0123456789";

	private static Map<Character, Integer> freqMap = new HashMap<Character, Integer>();
	private static Map<Character, Float> freqFloatMap = new HashMap<Character, Float>();
	
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void imprimirMenuAnalisadorDeFrequencia()
	{
		analiseDeFrequencia(alfabetoPTBR);
		
		return;
	}
	
	public static void analiseDeFrequencia(String alfabeto)
	{
		int numCaractAlfabeto = 0;
        
		for (int i = 0; i < alfabeto.length(); i++)
		{
			freqMap.put(alfabeto.charAt(i), 0);
			
			freqFloatMap.put(alfabeto.charAt(i), (float) 0.0);
		}
		
		BufferedReader br;
        
		System.out.println("Digite o nome do arquivo a ser analisado:");
		
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(scanKeyboard.nextLine()), "ISO-8859-1"));
			
			String x;
			
            char[] linha;
            
            int indice;
            
            while((x = br.readLine()) != null) 
            {
                // Converte a linha lida para minusculas e retira acentos antes de tratar
            	linha = trabalho01.convertToASCII2(x).toLowerCase().toCharArray();

                for (int i = 0; i < linha.length; i++) 
                {
                    indice = alfabeto.indexOf(linha[i]);
                    
                    // Se for uma letra ou um número
                    if (indice != -1) 
                    { 
                    	freqMap.put(alfabeto.charAt(indice), freqMap.get(alfabeto.charAt(indice)) + 1);
                    	
                        numCaractAlfabeto++;
                    }
                }
            }
            
            for (int i = 0; i < alfabeto.length(); i++) 
            {
            	freqFloatMap.put(alfabeto.charAt(i), ((float)freqMap.get(alfabeto.charAt(i)) / (float)numCaractAlfabeto) * 100);      
	        
            	DecimalFormat df = new DecimalFormat();
            	df.setMaximumFractionDigits(3);
            	df.setMinimumFractionDigits(3);
            	
	        	System.out.println("Letra " + alfabeto.charAt(i) + ": \t" + freqMap.get(alfabeto.charAt(i)) + " \t- " + df.format(freqFloatMap.get(alfabeto.charAt(i))) + "% \t- Chave de César: " + alfabeto.indexOf(alfabeto.charAt(i)));
            }
           
	        br.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Arquivo não existe.");
		}
		catch(IOException e)
		{
			System.out.println("Erro na leitura do arquivo.");
		}
		
		return;
	}
}
