package org.monet.deployservice_engine_099.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.TestCase;

public class ChangeServerNameTest extends TestCase {

	private static String Domain = "127.0.0.1";
	private static String Port = "4322";
	private static String Command = "<command id=\"change_server_name\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">bbd5662636ef98e2ee004e114780a741</parameter><parameter id=\"newname\">Server A</parameter></command>";

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

	public void testChangeServerIDTest() {
		SendTCP();
	}

	public void main() {
		System.out.println("ChangeServerID Test");
		testChangeServerIDTest();
	}
	
}
