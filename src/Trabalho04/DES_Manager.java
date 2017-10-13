package Trabalho04;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class DES_Manager 
{
	private Cipher 		cifra_criptografada;
	private Cipher 		cifra_decriptografada;
	private SecretKey	chave_secreta;
	
	DES_Manager(SecretKey chave) throws Exception
	{
		this.chave_secreta		= chave;
		
		cifra_criptografada 	= Cipher.getInstance("DES");
		
		cifra_decriptografada 	= Cipher.getInstance("DES");
	    
		cifra_criptografada.init(Cipher.ENCRYPT_MODE, 
								 chave);
	    
		cifra_decriptografada.init(Cipher.DECRYPT_MODE, 
								   chave);
		
		return;
	}
	
	@SuppressWarnings("restriction")
	public String criptografarString(String string) throws Exception 
	{
		// criptografa a string em bytes utilizando padrao UTF8
		byte[] stringCriptografada	=	cifra_criptografada.doFinal(string.getBytes("UTF8"));
		
		return new sun.misc.BASE64Encoder().encode(stringCriptografada);
	}
	
	public byte[] criptografarByte(byte [] mensagem) throws Exception 
	{
		 return cifra_criptografada.doFinal(mensagem);
	}
	
	public String decriptografarString(String string) throws Exception 
	{
		// decriptografa mensagem para o padrao UTF8
		byte[] stringUTF8	=	cifra_decriptografada.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(string));
		
		return new String(stringUTF8, "UTF8");
	}
	
	public byte[] decriptografarByte(byte [] mensagem) throws Exception 
	{
		return cifra_decriptografada.doFinal(mensagem);
	}
	
	public String mostraChave()
	{
		return Base64.getEncoder().withoutPadding().encodeToString(this.chave_secreta.getEncoded());
	}
}
