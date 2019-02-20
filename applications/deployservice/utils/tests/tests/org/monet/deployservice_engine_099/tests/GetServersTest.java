package org.monet.deployservice_engine_099.tests;

import java.io.*; 
import java.net.*; 

import org.monet.deployservice.utils.Network;






//import junit.framework.Test;
import junit.framework.TestCase;

public class GetServersTest extends TestCase {

	private static String Domain = "127.0.0.1";
	private static String Port = "4343";
//	private static String Port = "14327";
//	private static String Domain = "deployservice.monet32.dev.gisc.siani.es";
//	private static String Port = "443";
	private static String Command = "<command id=\"get_servers\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter></command>";
	// <command id="get_servers"><parameter id="user">admin</parameter><parameter id="password">1234</parameter></command>

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

	private static void SendHTTPS() throws UnsupportedEncodingException {
		System.setProperty("javax.net.ssl.keyStore","/home/rlopez/.deployservice/deployservice.jks");
    System.setProperty("javax.net.ssl.keyStorePassword","12345678");		
		
		Network network = new Network();
		String receiveMessage = network.downloadString("https://"+Domain+":"+Port+"/"+URLEncoder.encode(Command, "UTF-8"));
		System.out.println("Received message:");	
		System.out.println(receiveMessage);
		assertFalse(receiveMessage.equals(""));		
	}
	
	
	public void testGetServers() throws UnsupportedEncodingException {
//		SendTCP();
		SendHTTPS();
	}

	public void main() throws UnsupportedEncodingException {
		System.out.println("GetServers Test");
		testGetServers();
	}

}
