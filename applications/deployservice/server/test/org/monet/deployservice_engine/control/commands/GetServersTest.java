package org.monet.deployservice_engine.control.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.monet.deployservice.main.DeployService;
import org.monet.deployservice.utils.Network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GetServersTest {

  private DeployService deployservice;
  private static String Domain = "127.0.0.1";
  private static String Port = "4343";
//  private static String Port = "4320";
  private static String Command = "<command id=\"get_servers\"><parameter id=\"user\">admin</parameter><parameter id=\"password\">1234</parameter></command>";

  static {
    //for testing only
    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
            new javax.net.ssl.HostnameVerifier() {

              public boolean verify(String hostname,
                                    javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals(Domain)) {
                  return true;
                }
                return false;
              }
            });
  }

  @Before
  public void startServer() throws IOException {
    deployservice = new DeployService();
    deployservice.start();
  }

  @After
  public void shutdownServer() throws Exception {
    System.exit(0);
  }

  @Test
  public void checkGetServers() throws UnsupportedEncodingException {
    System.setProperty("javax.net.ssl.keyStore", "/home/rlopez/.deployservice/deployservice.jks");
    System.setProperty("javax.net.ssl.keyStorePassword", "12345678");

    Network network = new Network();
    String receiveMessage = network.downloadString("https://" + Domain + ":" + Port + "/" + URLEncoder.encode(Command, "UTF-8"));
    System.out.println("Received message:");
    System.out.println(receiveMessage);
//  assertFalse(receiveMessage.equals(""));
  }

}
