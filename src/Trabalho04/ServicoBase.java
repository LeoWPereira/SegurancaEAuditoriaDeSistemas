package Trabalho04;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ServicoBase 
{
	protected static SecretKey lerChaveArquivo(String arquivoChave, int idChave) 
	{
		String chaveDES = null;
        
		BufferedReader br;
        
		SecretKey chave = null;

        try 
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoChave), "UTF8"));
            
            int linhaAtual = 0;

            while ((chaveDES = br.readLine()) != null) 
            {
                if (linhaAtual == idChave) 
                {
                    System.out.println("A chave de " + idChave + " é: " + chaveDES);
                    
                    break;
                }
                
                linhaAtual++;
            }
            
            br.close();

        } 
        
        catch (FileNotFoundException e) 
        {
            System.out.println("Arquivo inexistente.");
        } 
        
        catch (IOException e) 
        {
            System.out.println("Erro na leitura do arquivo.");
        }
        
        if (chaveDES != null) 
        {
            byte[] senhaBytes = Base64.getDecoder().decode(chaveDES);
            
            chave = new SecretKeySpec(senhaBytes, "DES");
            
            //System.out.println("Texto encryptado: " + clienteKey.encrypt("123"));
        }
        
        return chave;
	}
	
	protected static byte converterIntEmByte(Integer valor) 
	{
		return valor.byteValue();
	}
	
	protected static final byte[] append(final byte[]... arrays) 
	{
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
        
		if (arrays != null) 
		{
            for (final byte[] array : arrays)
            {
                if (array != null)
                    out.write(array, 
                    		  0, 
                    		  array.length);
            }
        }
		
        return out.toByteArray();
	}
	
	protected static SecretKey geraChave() throws NoSuchAlgorithmException 
	{
		SecretKey chave = KeyGenerator.getInstance("DES").generateKey();

        //System.out.println("Senha gerada: " + Base64.getEncoder().withoutPadding().encodeToString(chave.getEncoded()));

        return chave;
	}
}
