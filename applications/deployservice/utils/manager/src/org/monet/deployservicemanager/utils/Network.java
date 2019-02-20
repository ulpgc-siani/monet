package org.monet.deployservicemanager.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Network {

	private String domain;
	private String port;
	
	public Network(String domain, String port) {
		this.domain = domain;
		this.port = port;
	}
	
	public String SendTCP(String command) throws IOException {
		InetAddress destAddr;
		destAddr = InetAddress.getByName(domain);
		int destPort = Integer.parseInt(port); // Destination port

		Socket sock = new Socket(destAddr, destPort);

		String sendMessage = command;

		PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		out.println(sendMessage);

		String receiveMessage = in.readLine();

		sock.close();

		return receiveMessage;
	}
	
}
