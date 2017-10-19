package Trabalho04;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
                    System.out.println("\nA chave de " + idChave + " e: " + chaveDES);
                    
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
	
	protected boolean verificaSeTicketValido(byte[] ticket) 
	{
		Calendar tempoAtual 	= new GregorianCalendar();
		
		////////////////////////////////////////////////////
		// Primeiramente pegamos os valores da data atual //
		////////////////////////////////////////////////////
		
        int anoAtual 			= tempoAtual.get(GregorianCalendar.YEAR);
        int mesAtual 			= tempoAtual.get(GregorianCalendar.MONTH);
        int diaAtual 			= tempoAtual.get(GregorianCalendar.DAY_OF_MONTH);
        int horaAtual 			= tempoAtual.get(GregorianCalendar.HOUR_OF_DAY);
        int minAtual 			= tempoAtual.get(GregorianCalendar.MINUTE);
        
        ////////////////////////////////////////////////////////////////////
        // Agora pegamos os valores da data referentes ao Ticket recebido //
        ////////////////////////////////////////////////////////////////////
        
        int ticket_idCliente	= (int)ticket[0];
        int ticket_dia			= (int)ticket[1];
        int ticket_mes			= (int)ticket[2];
        int ticket_ano			= (int)ticket[3] + 2000;
        int ticket_hora			= (int)ticket[4];
        int ticket_minuto		= (int)ticket[5];
        
        if(trabalho04.debug)
        {
	        System.out.println("\n\nTicket contem as seguintes informacoes:"
	        				 + "\nId do cliente: " 				+ (int)ticket_idCliente 
	        				 + "\nData de validade do ticket: "	+ ticket_dia + "/" + (ticket_mes + 1) + "/" + ticket_ano + " " + ticket_hora + ":" + ticket_minuto);
        }
        
        if((anoAtual   > ticket_ano) 									||
           ((anoAtual  <= ticket_ano)  && (mesAtual  > ticket_mes)) 	||
           ((mesAtual  <= ticket_mes)  && (diaAtual  > ticket_dia))		||
           ((diaAtual  <= ticket_dia)  && (horaAtual > ticket_hora))	||
           ((horaAtual <= ticket_hora) && (minAtual  > (ticket_minuto + 60))))
        {
        	if(trabalho04.debug)
        		System.out.println("\n\nTicket invalido!\n\n");
        	
            return false;
        }
        
        if(trabalho04.debug)
    		System.out.println("\n\nTicket valido. Continuando o procedimento\n\n");
        
        return true;
	}
}
