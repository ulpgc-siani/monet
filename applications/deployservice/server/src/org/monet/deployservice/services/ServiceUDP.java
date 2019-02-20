package org.monet.deployservice.services;

import java.io.IOException;
import java.net.*;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.*;
import org.monet.deployservice_engine.control.engine.Engine;
import org.monet.deployservice.utils.Network;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.xml.ResultXML;

public class ServiceUDP extends Thread {

	private Logger logger;
	private Engine engine;

	public ServiceUDP(Engine engine) {		
		logger = Logger.getLogger(this.getClass());
		this.engine = engine;
	}		

	public void run() {
		while (! Configuration.getValue(Configuration.VAR_Debug).equals("true")) {
			try {
				DatagramSocket elSocket = new DatagramSocket();
				int port = 1234;

				Network network = new Network();			
				String ipString = network.getCurrentEnvironmentNetworkIp(Configuration.getValue(Configuration.VAR_SendUDPInterface));
				if (ipString.equals("127.0.1.1")) ipString = network.getCurrentEnvironmentNetworkIp("eth0");
				
				StringTokenizer tokens = new StringTokenizer(ipString, ".");

				byte[] ipByte = new byte[4];

				int num;
				num = new Integer(tokens.nextToken());
				ipByte[0] = (byte) num;
				num = new Integer(tokens.nextToken());
				ipByte[1] = (byte) num;
				num = new Integer(tokens.nextToken());
				ipByte[2] = (byte) num;
				
				String data = get_message();

				byte[] string = data.getBytes();

				for (int i = 1; i < 255; i++) {
					ipByte[3] = (byte) i;
					InetAddress addr = InetAddress.getByAddress(ipByte);
					DatagramPacket message = new DatagramPacket(string, data.length(), addr, port);
					elSocket.send(message);
				}

				elSocket.close();
			} catch (UnknownHostException e) {
	  		logger.error("Unknown: " + e.getMessage());		   	
			} catch (SocketException e) {
	  		logger.error("Unknown: " + e.getMessage());		   	
			} catch (IOException e) {
	  		logger.error("Unknown: " + e.getMessage());		   	
			}

			try {
				sleep(Long.parseLong(Configuration.getValue(Configuration.VAR_SendUDPInterval)));
			} catch (NumberFormatException e) {
	  		logger.error("Number format incorrect in config value.");		   	
				try {
	        sleep(1000);
        } catch (InterruptedException e1) {
  	  		logger.error("Thread interrupted.");		   	
        }
			} catch (InterruptedException e) {
	  		logger.error("Thread interrupted.");		   	
			}
		}
	}

	private String get_message() {
		Item command = new Item();
		command.setProperty("id", OperationIDs.GetServers);

//		Item result = new Item();
//		result = factory.getCommand(OperationIDs.GetServers).execute(command);
		Item result = engine.getItem(command);
		
		ResultXML resultXML = new ResultXML();
		return resultXML.serialize(result);
	}
}
