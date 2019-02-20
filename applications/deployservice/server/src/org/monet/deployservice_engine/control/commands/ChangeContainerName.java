package org.monet.deployservice_engine.control.commands;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.xml.ServersXML;

public class ChangeContainerName implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;
	private String newName;
	
	public ChangeContainerName() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.ChangeContainerName);
		caption = new Item();
		caption.setProperty("id", "caption");
		result.addItem(caption);
		status = new Item();
		status.setProperty("id", "status");
		status.setContent("ok");
		result.addItem(status);				
	}

	private boolean initialize(Item command) {
		try {
			server = command.getItem("server").getContent();
		} catch (Exception e) {
			String error = "Server not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidServer);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			container = command.getItem("container").getContent();
		} catch (Exception e) {
			String error = "Container not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidId);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			newName = command.getItem("newname").getContent();
		} catch (Exception e) {
			String error = "New name not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidNewName);
			caption.setContent("Error: " + error);
			return false;
		}
		
		return true;
	}

	private boolean changeName() {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		servers.getItem(server).getItem(container).setProperty("name", newName);
		
		try {
			Configuration.setServersConfig(serversXML.serializeLite(servers));
		} catch (IOException e) {
			String error = "I can't save: " + Configuration.getServersConfig();
			logger.error(error + "\nDetails: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}
		
		return true;		
	}
	
	public Item execute(Item command) {
		if (!initialize(command))
			return result;
		
		if (!changeName())
			return result;
						
	  return result;
  }

}
