package Trabalho01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Cesar
{
	private static int chave;
    
	private static String alfabetoCesar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	private static BufferedReader br;
	
	private static BufferedWriter bw;
    
	private static FileWriter fw;
	
	public static void imprimirMenuCesar()
	{
		System.out.println("\n"
						+  "Para codificar um texto, digite o comando:   -c -k 5 texto-aberto.txt  texto-cifrado.txt\n"
						+  "Para decodificar um texto, digite o comando: -d -k 5 texto-cifrado.txt texto-aberto.txt\n");
		
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
		try 
		{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoEntrada), "ISO-8859-1"));

            fw = new FileWriter(arquivoSaida);
            bw = new BufferedWriter(fw);

            String x;
            
            char[] linha;
            
            int indice;

            while ((x = br.readLine()) != null) 
            {
                //Converte a linha lida para minusculas e retira acentos antes de tratar
                linha = trabalho01.convertToASCII2(x).toCharArray();

                for (int i = 0; i < linha.length; i++) 
                {
                    indice = alfabetoCesar.indexOf(linha[i]);

                    if (indice == -1) // Não é uma letra ou um número
                        bw.write(linha[i]);

                    else if ((indice + chave) < alfabetoCesar.length()) // A letra com a chave está dentro do alfabeto
                    {
                        indice = indice + chave;
                        
                        bw.write(alfabetoCesar.charAt(indice));
                    } 
                    
                    else	//	chegou no fim do alfabeto 
                    { 
                        indice = indice + chave - alfabetoCesar.length();
                        
                        bw.write(alfabetoCesar.charAt(indice));
                    }
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
		
		return;
	}
	
	public static void decodificar(String arquivoEntrada, String arquivoSaida)
	{
        try 
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoEntrada), "ISO-8859-1"));
        
            fw = new FileWriter(arquivoSaida);
            
            bw = new BufferedWriter(fw);

            String x;
            String output = "";
            
            char[] linha;
            
            int indice;
            
            while ((x = br.readLine()) != null) 
            {
                //Converte a linha lida para minusculas e retira acentos antes de tratar
                linha = trabalho01.convertToASCII2(x).toCharArray();
                
                for (int i = 0; i < linha.length; i++) 
                {
                    indice = alfabetoCesar.indexOf(linha[i]);
                    
                    if (indice == -1) // Não é uma letra ou um número
                    {
                        bw.write(linha[i]);
                        
                        output += linha[i];
                    }
                    
                    else if ((indice - chave) >= 0) // está dentro do alfabeto
                    {
                        indice = indice - chave;
               
                        bw.write(alfabetoCesar.charAt(indice));
                        
                        output += alfabetoCesar.charAt(indice);
                    } 
                    
                    else	// chegou no fim do alfabeto 
                    { 
                        indice = alfabetoCesar.length() + (indice - chave);
                    
                        bw.write(alfabetoCesar.charAt(indice));
                        
                        output += alfabetoCesar.charAt(indice);
                    }
                }
                
                bw.write("\r\n");
                
                output += "\r\n";
            }
            
            System.out.println("o seguinte texto está correto?\n (S)im ou (N)ao");
            
            System.out.println(output);
            
            switch (scanKeyboard.nextLine()) 
            {
				case "S":
				case "s":
					bw.close();
					
					break;
	
				default:
					break;
			}
            
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
        
        return;
	}
}
