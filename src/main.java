/**
 * @file   main.cpp
 * @author Leonardo Winter Pereira
 * @brief  
 */

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import Trabalho01.trabalho01;
import Trabalho02.trabalho02;
import Trabalho03.trabalho03;
import Trabalho04.trabalho04;
import Trabalho05.trabalho05;

public class main
{
	private static Scanner scanKeyboard = new Scanner(System.in);
	
	public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException
	{
		imprimirInfoAluno();
		
		System.out.println("\n\n\n");
	
		while(true)
		{
			System.out.println("***************** Segurança e Auditoria de Sistemas *****************");
			
			imprimirMenuTrabalhos();
		}
	}
	
	public static void imprimirInfoAluno()
	{
		System.out.println("Este trabalho está sendo desenvolvido por:");
		System.out.println("Aluno: Leonardo Winter Pereira\nMatrícula: 944424");
		System.out.println("Para acompanhar o desenvolvimento do mesmo, acessar o projeto pelo link: https://github.com/LeoWPereira/SegurancaEAuditoriaDeSistemas");
		System.out.println("Todos os direitos reservados");
		
		return;
	}
	
	public static void imprimirMenuTrabalhos() throws InterruptedException, NoSuchAlgorithmException
	{
		System.out.println("Escolha o trabalho a ser executado:");
		
		boolean opcaoValida = false;
		
		while(!opcaoValida)
		{
			System.out.println("1. Trabalho 01\n"
							+  "2. Trabalho 02\n"
							+  "3. Trabalho 03\n"
							+  "4. Trabalho 04\n"
							+  "5. Trabalho 05\n");
			
			switch (scanKeyboard.nextLine())
			{
				case "1":
					trabalho01.imprimirMenuTrabalho01();
					
					opcaoValida = true;
					break;
					
				case "2":
					trabalho02.imprimirMenuTrabalho02();
					
					opcaoValida = true;
					break;
					
				case "3":
					trabalho03.imprimirMenuTrabalho03();
					
					opcaoValida = true;
					break;
					
				case "4":
					trabalho04.imprimirMenuTrabalho04();
					
					opcaoValida = true;
					break;
					
				case "5":
					trabalho05.imprimirMenuTrabalho05();
					
					opcaoValida = true;
					break;
		
				default:
					System.out.println("Opção náo é válida. Escolha novamente\n");
					
					break;
			}
		}
		
		return;
	}
}
