package Trabalho01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Vernan
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	private static String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	private static File fDecif;
	private static File fChave;
    
	private static BufferedReader brDecif;
	private static BufferedReader brChave;
    
	private static BufferedWriter bw;
    
	private static FileWriter fw;
	
	public static void imprimirMenuVernan()
	{
        System.out.println("Para codificar um texto, digite o comando: -c chave.dat		texto-aberto.txt	texto-cifrado.txt\n");

        while(!verificarComando(scanKeyboard.nextLine().split(" ")));

		return;
	}
	
	public static boolean verificarComando(String[] args)
	{
		if((args.length != 4) ||
		   (!args[0].equalsIgnoreCase("-c")))
		{
			System.out.println("Comando digitado de forma incorreta. Entre com novo comando ...");
			
			return false;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// comando está correto, agora precisa avaliar e armazenar a chave e executar o processo principal de (de)codificação //
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 
		 if(args[0].equalsIgnoreCase("-c"))
			 codificar(args[2], args[3], args[1]);
		 
		return true;
	}
	
	public static void codificar(String arquivoEntrada, String arquivoSaida, String arquivoChave)
	{
		try 
		{
			fDecif = new File(arquivoEntrada);
			
            fChave = new File(arquivoChave);

            if (fDecif.length() > fChave.length()) 
            {
                System.out.println("A chave é menor do que o arquivo. Programa encerrado.");
            
                return;
            }

            brDecif = new BufferedReader(new FileReader(fDecif));
            
            brChave = new BufferedReader(new FileReader(fChave));

            fw = new FileWriter(arquivoSaida);
            
            bw = new BufferedWriter(fw);

            int charDecif, charChave, charVernan;
            
            while ((charDecif = brDecif.read()) != -1)
            {
                charChave = brChave.read();
                
                charVernan = charDecif ^ charChave;
                
                bw.write(charVernan);
            }
            
            bw.close();
            
            brDecif.close();
            
            brChave.close();
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
