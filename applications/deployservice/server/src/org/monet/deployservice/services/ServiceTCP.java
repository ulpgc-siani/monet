package org.monet.deployservice.services;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

import org.apache.log4j.Logger;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.*;
import org.monet.deployservice_engine.control.engine.Engine;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Md5;
import org.monet.deployservice_engine.xml.CommandXML;
import org.monet.deployservice_engine.xml.ServersXML;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.xml.ResultXML;

public class ServiceTCP {

	private Logger logger;
	private Engine engine;

	public ServiceTCP(Engine engine) {
		logger = Logger.getLogger(this.getClass());
		this.engine = engine;
	}

	@SuppressWarnings("resource")
  public void start() throws IOException {
		ServerSocket tcpServerSocket = null;
		String message = "";
		try {
			message = "Loading port: " + Configuration.getValue(Configuration.VAR_Port);
			logger.info(message);
			tcpServerSocket = new ServerSocket(Integer.parseInt(Configuration.getValue(Configuration.VAR_Port)));
		} catch (IOException | NumberFormatException e) {
			logger.error("Could not listen on specified port.", e);
			throw new IOException("Could not listen on specified port.");
		}

		ServersXML serversXML = new ServersXML();
		serversXML.unserialize(Configuration.getServersConfig());

		message = "Server id: " + serversXML.getIdServer();
		logger.info(message);
		message = "Container id: " + serversXML.getIdContainer();
		logger.info(message);
		
		if (! "".equals(Configuration.getValue(Configuration.VAR_StartCommandToBegin))) {
 	    CommandXML commandXMLStart = new CommandXML();
	  	Item command = commandXMLStart.unserialize(Configuration.getValue(Configuration.VAR_StartCommandToBegin));
		  engine.getItem(command);
		}
		
		while (true) {
			Socket clientSocket = null;
			try {
				logger.debug("Server listening");
				clientSocket = tcpServerSocket.accept();
			} catch (IOException e) {
				logger.error("Accept failed", e);
				throw new IOException("Accept failed");
			}

			PrintWriter out;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
				Writer w = new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8");
				out = new PrintWriter(w, true);

				String receiveText = in.readLine();
				if (receiveText == null) {
					logger.debug("Receive message is null.");
					continue;
				}
				
				InetAddress address = clientSocket.getInetAddress();
				
				if (! receiveText.contains("<command id=\"get_servers\">")) { 
				  logger.info("[" + address.getHostAddress() + "] Receive command: " + receiveText);
				} else {
				  logger.debug("[" + address.getHostAddress() + "] Receive command: " + receiveText);					
				}
				
				Item result = new Item();
				result.setProperty("id", ResultIDs.InvalidCommand);

				ResultXML resultXML = new ResultXML();
				CommandXML commandXML = new CommandXML();
				Item command = commandXML.unserialize(receiveText);

				Boolean execute = false;
				if (! "".equals(Configuration.getValue(Configuration.VAR_User))) {
					String remotePassword = "";
					Md5 md5 = Md5.getInstance();
          remotePassword = md5.hashData(command.getItem("password").getContent().getBytes(StandardCharsets.UTF_8));

					if ((command.getItem("user") == null)
					    || (command.getItem("password") == null)
					    || (!command.getItem("user").getContent()
					        .equals(Configuration.getValue(Configuration.VAR_User)))
					    || (!remotePassword.equals(Configuration
					        .getValue(Configuration.VAR_Password)))) {
						result.setProperty("id", ResultIDs.InvalidUserOrPassword);
					} else
						execute = true;

				} else execute = true;
				
				if (execute) {					
						if (! command.getProperty("id").equals(OperationIDs.GetServers)) {
						  logger.info("Execute command " + command.getProperty("id"));
						} else {
						  logger.debug("Execute command " + command.getProperty("id"));						
						}						
						
						try {
						  result = engine.getItem(command);
	
						  if (! command.getProperty("id").equals(OperationIDs.GetServers)) {
							  logger.info("Finnish command " + receiveText);
							} else {
							  logger.debug("Finnish command " + receiveText);						
							}						
						} catch (Exception e) {
							String error = "Error execute command.";
							ExceptionPrinter exeptionPrinter = new ExceptionPrinter();					
							logger.error(error + " Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));											
							result = null;
						}
				}

				String sendText = resultXML.serialize(result);
				out.println(sendText);

				if (out != null) out.close();
				if (in != null) in.close();

			} catch (IOException e) {
				logger.error("Error loading command.", e);
			} catch (Exception e) {
				logger.error("Error " + e.getMessage(), e);
			}

			clientSocket.close();
		}
	}
}
