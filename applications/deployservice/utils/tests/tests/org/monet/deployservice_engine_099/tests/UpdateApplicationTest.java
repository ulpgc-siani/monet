package org.monet.deployservice_engine_099.tests;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.monet.deployservice.utils.Network;

import junit.framework.TestCase;

public class UpdateApplicationTest extends TestCase{

	private static String Domain = "127.0.0.1";
	private static String Port = "4343";
	private static String Command = "<command id=\"update_application\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter></command>";

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

	private static void SendHTTPS() throws UnsupportedEncodingException {
		System.setProperty("javax.net.ssl.keyStore","/home/rlopez/.deployservice/deployservice.jks");
    System.setProperty("javax.net.ssl.keyStorePassword","12345678");		
		
		Network network = new Network();
		String receiveMessage = network.downloadString("https://"+Domain+":"+Port+"/"+URLEncoder.encode(Command, "UTF-8"));
		System.out.println("Received message:");	
		System.out.println(receiveMessage);
		assertFalse(receiveMessage.equals(""));		
	}
	
	public void testUpdateApplicationTest() throws UnsupportedEncodingException {
		SendHTTPS();		
	}

	public void main() throws UnsupportedEncodingException {
		System.out.println("UpdateApplication Test");
		testUpdateApplicationTest();
	}
	
}
