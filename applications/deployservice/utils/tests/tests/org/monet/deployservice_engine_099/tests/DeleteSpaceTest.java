package org.monet.deployservice_engine_099.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.monet.deployservice.utils.Network;

import junit.framework.TestCase;

public class DeleteSpaceTest extends TestCase {
	private static String Domain = "127.0.0.1";
	private static String Port = "4323";
//mysql	private static String Command = "<command id=\"delete_space\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">bbd5662636ef98e2ee004e114780a741</parameter><parameter id=\"container\">a12c22d321a928ec46e4646a36d9a4de</parameter><parameter id=\"space\">a</parameter></command>";
	private static String Command = "<command id=\"delete_space\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">ecfaeee7cf55fc480fc3a66ac8f1c90e</parameter><parameter id=\"container\">a58ce205c1510286da1c91f5c0e6af3f</parameter><parameter id=\"space\">test</parameter></command>";
//	private static String Command = "<command id=\"delete_space\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter><parameter id=\"server\">bbd5662636ef98e2ee004e114780a741</parameter><parameter id=\"container\">b85af939d9aaa3301302320e54751f00</parameter><parameter id=\"space\">ROOT</parameter></command>";

	static {
    //for testing only
    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
    new javax.net.ssl.HostnameVerifier(){

        public boolean verify(String hostname,
                javax.net.ssl.SSLSession sslSession) {
            if (hostname.equals(Domain)) {
                return true;
            }
            return false;
        }
    });
  }
	
	@SuppressWarnings("unused")	
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

	private static void SendHTTPS() throws UnsupportedEncodingException {
		System.setProperty("javax.net.ssl.keyStore","/home/rlopez/.deployservice/deployservice.jks");
    System.setProperty("javax.net.ssl.keyStorePassword","12345678");		
		
		Network network = new Network();
		String receiveMessage = network.downloadString("https://"+Domain+":"+Port+"/"+URLEncoder.encode(Command, "UTF-8"));
		System.out.println("Received message:");	
		System.out.println(receiveMessage);
		assertFalse(receiveMessage.equals(""));		
	}
	
	public void testDeleteSpaceTest() throws UnsupportedEncodingException {
//		SendTCP();
		SendHTTPS();		
	}

	public void main() throws UnsupportedEncodingException {
		System.out.println("RemoveSpace Test");
		testDeleteSpaceTest();
	}

}
