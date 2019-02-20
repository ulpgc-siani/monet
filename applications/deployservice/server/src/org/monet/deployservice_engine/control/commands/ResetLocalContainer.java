package org.monet.deployservice_engine.control.commands;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.utils.RestartServers;
import org.monet.deployservice_engine.xml.ServersXML;

public class ResetLocalContainer implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;

	public ResetLocalContainer() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.ResetLocalContainer);
		caption = new Item();
		caption.setProperty("id", "caption");
		result.addItem(caption);
		status = new Item();
		status.setProperty("id", "status");
		status.setContent("ok");
		result.addItem(status);
	}

	private Boolean initialize(Item command) {

		try {
			server = command.getItem("server").getContent();
			if (server.equals(""))
				throw new Exception("Server name is empty.");
		} catch (Exception e) {
			String error = "Server not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidServer);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			container = command.getItem("container").getContent();
			if (container.equals(""))
				throw new Exception("Container name is empty.");
		} catch (Exception e) {
			String error = "Container not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidContainer);
			caption.setContent("Error: " + error);
			return false;
		}

		return true;
	}

//	@SuppressWarnings("static-access")
	private boolean reset() {

		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		RestartServers resetServers = new RestartServers();								
		try {
			String docserverServer = servers.getItem(server).getItem(container).getProperty("docserver-server");
			String docserverContainer = servers.getItem(server).getItem(container).getProperty("docserver-container");
			
			resetServers.container_docserver_stop(docserverServer, docserverContainer);
		} catch (Exception e) {
			String error = "I can not stop containers.";
			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
			logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

//		try {
//			Thread.currentThread().sleep(5000);
//		} catch (InterruptedException e1) {
//		}

		try {
			String docserverServer = servers.getItem(server).getItem(container).getProperty("docserver-server");
			String docserverContainer = servers.getItem(server).getItem(container).getProperty("docserver-container");

			resetServers.container_docserver_start(docserverServer, docserverContainer);
		} catch (Exception e) {
			String error = "I can not start containers.";
			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
			logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		return true;
	}

	public Item execute(Item command) {
		if (!initialize(command))
			return result;
		if (!reset())
			return result;

		return result;
	}
}
