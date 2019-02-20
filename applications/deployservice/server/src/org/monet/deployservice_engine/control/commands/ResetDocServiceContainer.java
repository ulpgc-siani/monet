package org.monet.deployservice_engine.control.commands;

import java.util.Iterator;

import org.apache.log4j.Logger;
//import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.xml.Item;
//import org.monet.deployservice_engine.utils.RestartServers;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.ServersXML;
import org.monet.deployservice_engine.configuration.Configuration;

public class ResetDocServiceContainer implements ICommand {
	
	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;

	public ResetDocServiceContainer() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.ResetDocServiceContainer);
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

	private boolean reset() {

		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		Item command = new Item();
		command.setProperty("server", server);
		command.setProperty("container", container);

//		Item result = new Item();
		ResetDocService resetDocService = new ResetDocService();		
		
		Item itemServer = servers.getItem(server);
		Spaces itemSpaces = new Spaces();

		Item spaces = itemSpaces.getSpaces(container, itemServer);
		Iterator<Item> ispaces = spaces.getItems().iterator();
		
		Item status = new Item();
		while (ispaces.hasNext()) {
			Item space = (Item) ispaces.next();

			String spaceName = space.getProperty("id");
			logger.info("Reset docservice in space: " + spaceName + " from server: " + servers.getItem(server).getProperty("name") + ", container: "
			    + servers.getItem(server).getItem(container).getProperty("name"));
			
			Item iserver = new Item();
			iserver.setProperty("id", "server");
			iserver.setContent(server);
			command.addItem(iserver);

			Item icontainer = new Item();
			icontainer.setProperty("id", "container");
			icontainer.setContent(container);
			command.addItem(icontainer);

			Item ispace = new Item();
			ispace.setProperty("id", "space");
			ispace.setContent(spaceName);
			command.addItem(ispace);

			command.setProperty("id", OperationIDs.ResetDocService);
			status = resetDocService.execute(command);

			if (!status.getItem("status").getContent().equals("ok")) {
				String error = "I can't reset docservice in space: " + spaceName;
				logger.error(error);
				this.result = status;
				return false;
			}				
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
