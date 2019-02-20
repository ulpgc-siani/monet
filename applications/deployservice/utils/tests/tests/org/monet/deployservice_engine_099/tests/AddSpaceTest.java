package org.monet.deployservice_engine_099.tests;

import java.io.*; 
import java.net.*; 

import junit.framework.TestCase;

public class AddSpaceTest extends TestCase {

	private static String Domain = "127.0.0.1";
	private static String Port = "4343";
//mysql	private static String Command = "<command id=\"add_space\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">bbd5662636ef98e2ee004e114780a741</parameter><parameter id=\"container\">a12c22d321a928ec46e4646a36d9a4de</parameter><parameter id=\"space\">a</parameter><parameter id=\"install-etl\">true</parameter><parameter id=\"install-planner\">true</parameter></command>";		
	private static String Command = "<command id=\"add_space\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">bbd5662636ef98e2ee004e114780a741</parameter><parameter id=\"container\">513bfd0337a95237431b25af61a42fcf</parameter><parameter id=\"space\">a</parameter><parameter id=\"base-url\">http://localhost:8091/a</parameter><parameter id='space-label-es'>Unidad de Negocio</parameter></command>";		
//	private static String Command = "<command id=\"add_space\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">bbd5662636ef98e2ee004e114780a741</parameter><parameter id=\"container\">b85af939d9aaa3301302320e54751f00</parameter><parameter id=\"space\">ROOT</parameter><parameter id=\"base-url\">http://localhost:8091</parameter></command>";		

	private static void SendTCP() {
		InetAddress destAddr;
		try {
			destAddr = InetAddress.getByName(Domain);
			int destPort = Integer.parseInt(Port);

			Socket sock = new Socket(destAddr, destPort);

			String SendMessage = Command;

			System.out.println("Sending message (" + SendMessage.length() + " bytes): ");
			System.out.println(SendMessage);
			try {
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				out.println(SendMessage);

				String receiveMessage = in.readLine();

				System.out.println("Received message:");
				System.out.println(receiveMessage);
				
				assertFalse(receiveMessage.equals(""));

				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (UnknownHostException e1) {
		  fail("Should raise a UnknownHostException");
		} // Destination address
		catch (IOException e) {
		  fail("Should raise a IOException");
		}
	}

	public void testAddSpaceTest() {
		SendTCP();
	}

	public void main() {
		System.out.println("CreateSpace Test");
		testAddSpaceTest();
	}

}
