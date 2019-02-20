package org.monet.deployservice_engine_099.tests;

import java.io.*; 
import java.net.*; 

import junit.framework.TestCase;

public class UpdateSSHConfigTest extends TestCase {

	private static String Domain = "127.0.0.1";
	private static String Port = "14323";
	private static String Command = "<command id=\"update_ssh_config\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">98c012a8b263929cf4cf4271bc105df6</parameter><parameter id=\"container\">c48c3d013d02e15b7fd1502a2e6bc82f</parameter><parameter id=\"ssh-port\">5020</parameter></command>";

	private static void SendTCP() {
		InetAddress destAddr;
		try {
			destAddr = InetAddress.getByName(Domain);
			int destPort = Integer.parseInt(Port); // Destination port

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

	public void testUpdateSSHConfigTest() {
		SendTCP();
	}

	public void main() {
		System.out.println("UpdateSSHConfig Test");
		testUpdateSSHConfigTest();
	}

}
