package org.monet.deployservicesend.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class DeployServiceSendMain {

	private static void SendTCP(String domain, String port, String command) {
		InetAddress destAddr;
		try {
			destAddr = InetAddress.getByName(domain);
			int destPort = Integer.parseInt(port);

			Socket sock = new Socket(destAddr, destPort);

			String SendMessage = command;

			System.out.println("Sending message (" + SendMessage.length() + " bytes): ");
			System.out.println(SendMessage);
			try {
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				out.println(SendMessage);

				String receiveMessage = in.readLine();

				System.out.println("Received message:");
				System.out.println(receiveMessage);
				
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (UnknownHostException e1) {
		  System.out.println("Error: Should raise a UnknownHostException");
		} // Destination address
		catch (IOException e) {
			System.out.println("Error: Should raise a IOException");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
    if (args.length < 3) {
    	System.out.println("Usage: deployservice_send.jar <host> <port> <command>");
			Runtime.getRuntime().exit(1);    	
    }
    
		
		String domain = args[0];
		String port = args[1];
		String command = args[2];
		
		SendTCP(domain, port, command);
/*
		idConfig = "";
		String message = "";
		try {
			idConfig = args[0];
			message = args[1];
		} catch (Exception e) {
			System.out.println("Usage: "+Configuration.CONST_AppName+" <config name> '<message>'");
			Runtime.getRuntime().exit(1);
		}
  
 
 */
		
		
	}

}
