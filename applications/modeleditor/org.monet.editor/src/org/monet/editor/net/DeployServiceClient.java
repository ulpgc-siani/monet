package org.monet.editor.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import org.monet.editor.MonetLog;
import org.monet.editor.library.StreamHelper;

public class DeployServiceClient {

  private static final String ADD_CMD_TEMPLATE                   = "<command id='add_space'><parameter id='user'>admin</parameter><parameter id='password'>1234</parameter><parameter id='server'>98c012a8b263929cf4cf4271bc105df6</parameter><parameter id='container'>c48c3d013d02e15b7fd1502a2e6bc82f</parameter><parameter id='space'>%s</parameter><parameter id='restart-docserver'>true</parameter><parameter id='base-url'>%s</parameter><parameter id='replace-theme'>true</parameter><parameter id='space-label-es'>%s</parameter></command>";
  private static final String RESET_CMD_TEMPLATE                 = "<command id='reset_space'><parameter id='user'>admin</parameter><parameter id='password'>1234</parameter><parameter id='server'>98c012a8b263929cf4cf4271bc105df6</parameter><parameter id='container'>c48c3d013d02e15b7fd1502a2e6bc82f</parameter><parameter id='space'>%s</parameter><parameter id='restart-docserver'>true</parameter><parameter id='replace-theme'>true</parameter></command>";
  private static final String DELETE_CMD_TEMPLATE                = "<command id='delete_space'><parameter id='user'>admin</parameter><parameter id='password'>1234</parameter><parameter id='server'>98c012a8b263929cf4cf4271bc105df6</parameter><parameter id='container'>c48c3d013d02e15b7fd1502a2e6bc82f</parameter><parameter id='space'>%s</parameter><parameter id='restart-docserver'>true</parameter><parameter id='replace-theme'>true</parameter></command>";

  private static final String RESTART_CMD_TEMPLATE               = "<command id='restart_container'><parameter id='user'>admin</parameter><parameter id='password'>1234</parameter><parameter id='server'>98c012a8b263929cf4cf4271bc105df6</parameter><parameter id='container'>c48c3d013d02e15b7fd1502a2e6bc82f</parameter><parameter id='replace-theme'>true</parameter><parameter id='restart-debug'>true</parameter></command>";

  private static final String DOWNLOAD_UPDATES_CMD_TEMPLATE      = "<command id='update_wars'><parameter id='user'>admin</parameter><parameter id='password'>1234</parameter><parameter id='server'>Select...</parameter><parameter id='restart-docserver'>true</parameter><parameter id='url'>http://files.gisc.siani.es/monet/v3/beta</parameter><parameter id='replace-theme'>true</parameter></command>";
  private static final String UPDATE_CMD_TEMPLATE                = "<command id='reset_monet'><parameter id='user'>admin</parameter><parameter id='password'>1234</parameter><parameter id='server'>98c012a8b263929cf4cf4271bc105df6</parameter><parameter id='container'>c48c3d013d02e15b7fd1502a2e6bc82f</parameter><parameter id='space'>%s</parameter><parameter id='restart-docserver'>true</parameter><parameter id='replace-theme'>true</parameter></command>";

  private static final String DEPLOY_TCP_PORT_QUERY_URL_TEMPLATE = "http://%s:%s/ports/deployservice.jsp";

  private static final String STATUS_OK                          = "<status>ok</status>";
  private static final String STATUS_INVALID_SPACE               = "<status>invalid_space</status>";
  private static final String STATUS_SPACE_EXISTS                = "<status>space_exists</status>";

  public static boolean createSpace(String deployUrl, String space, String label) throws Exception {
    URL url = new URL(deployUrl);
    return sendCmd(url.getHost(), getPort(url), String.format(ADD_CMD_TEMPLATE, space, deployUrl, label));
  }

  public static boolean cleanUpSpace(String deployUrl, String space) throws Exception {
    URL url = new URL(deployUrl);
    return sendCmd(url.getHost(), getPort(url), String.format(RESET_CMD_TEMPLATE, space));
  }

  public static boolean deleteSpace(String deployUrl, String space) throws Exception {
    URL url = new URL(deployUrl);
    return sendCmd(url.getHost(), getPort(url), String.format(DELETE_CMD_TEMPLATE, space));
  }

  public static boolean restartSpace(String deployUrl, String space) throws Exception {
    URL url = new URL(deployUrl);
    return sendCmd(url.getHost(), getPort(url), String.format(RESTART_CMD_TEMPLATE, space));
  }

  public static boolean updateSpace(String deployUrl, String space) throws Exception {
    URL url = new URL(deployUrl);
    if (sendCmd(url.getHost(), getPort(url), DOWNLOAD_UPDATES_CMD_TEMPLATE))
      return sendCmd(url.getHost(), getPort(url), String.format(UPDATE_CMD_TEMPLATE, space));
    else
      return false;
  }

  private static String getPort(URL deployUrl) throws Exception {
    String tcpPortQueryUrl = String.format(DEPLOY_TCP_PORT_QUERY_URL_TEMPLATE, deployUrl.getHost(), deployUrl.getPort() > 0 ? deployUrl.getPort() : 80);
    String port = AgentRestfullClient.getInstance().execute(tcpPortQueryUrl, null).trim();
    if (port.isEmpty()) {
      throw new RuntimeException("Can't get port");
    }
    return port;
  }

  private static boolean sendCmd(String domain, String port, String command) throws InvalidSpaceException, IOException, SpaceExistsException {
    InetAddress destAddr;
    destAddr = InetAddress.getByName(domain);
    int destPort = Integer.parseInt(port); // Destination port

    Socket sock = new Socket(destAddr, destPort);

    String SendMessage = command;

    System.out.println("Sending message (" + SendMessage.length() + " bytes): ");
    System.out.println(SendMessage);

    PrintWriter out = null;
    BufferedReader in = null;

    try {
      out = new PrintWriter(sock.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

      out.println(SendMessage);

      String receiveMessage = in.readLine();

      System.out.println("Received message:");
      System.out.println(receiveMessage);

      if (receiveMessage.indexOf(STATUS_OK) > -1)
        return true;
      else if (receiveMessage.indexOf(STATUS_INVALID_SPACE) > -1)
        throw new InvalidSpaceException();
      else if (receiveMessage.indexOf(STATUS_SPACE_EXISTS) > -1)
        throw new SpaceExistsException();
      else {
        MonetLog.print(receiveMessage);
        return false;
      }
    } finally {
      if (sock != null && !sock.isClosed())
        sock.close();
      StreamHelper.close(out);
      StreamHelper.close(in);
    }
  }

  public static class InvalidSpaceException extends Exception {
    private static final long serialVersionUID = -596584367520771966L;
  }

  public static class SpaceExistsException extends Exception {
    private static final long serialVersionUID = -7753659912277435951L;
  }

}
