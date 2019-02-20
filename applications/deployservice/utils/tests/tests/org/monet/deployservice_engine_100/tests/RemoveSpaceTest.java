package org.monet.deployservice_engine_100.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.TestCase;

public class RemoveSpaceTest extends TestCase {

	private static String Domain = "127.0.0.1";
	private static String Port = "4323";
	private static String Command = "<command id=\"remove_space\"><parameter id=\"name\">a</parameter><parameter id=\"url\">http://localhost:8091/a</parameter></command>";		

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

	public void testRemoveSpaceTest() {
		SendTCP();
	}

	public void main() {
		System.out.println("RemoveSpace Test");
		testRemoveSpaceTest();
	}
	
}
