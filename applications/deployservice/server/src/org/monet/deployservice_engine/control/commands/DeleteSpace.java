package org.monet.deployservice_engine.control.commands;

import org.apache.log4j.Logger;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.tasks.DeleteLocalServices;
import org.monet.deployservice_engine.control.commands.tasks.DeletePublicServices;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.ServersXML;

public class DeleteSpace implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;
	
	private String server;
	private String container;
	private String space;
	
	private Item resultTemp;

	public DeleteSpace() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.DeleteSpace);
		caption = new Item();
		caption.setProperty("id", "caption");
		result.addItem(caption);
		status = new Item();
		status.setProperty("id", "status");
		status.setContent(ResultIDs.InternalError);
		result.addItem(status);
	}

	public Item execute(Item command) {

		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());
		
		try {
			server = command.getItem("server").getContent();
		} catch (Exception e) {
			String error = "Server not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidServer);
			caption.setContent("Error: " + error);
			return result;
		}

		try {
			container = command.getItem("container").getContent();
		} catch (Exception e) {
			String error = "Container not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidContainer);
			caption.setContent("Error: " + error);
			return result;
		}

		try {
			space = command.getItem("space").getContent();
		} catch (Exception e) {
			String error = "Space not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidSpace);
			caption.setContent("Error: " + error);
			return result;
		}		

		Boolean spaceExists = true;
		
		try {
			Item itemServer = servers.getItem(server);
			Spaces itemSpaces = new Spaces();

			try {
				Item ds_space = itemSpaces.getSpace(container, itemServer, space);

				String docserverServer = ds_space.getProperty("docserver-server");
				String docserverContainer = ds_space.getProperty("docserver-container");
				
				Item iDocServerServer = new Item();
				iDocServerServer.setProperty("id", "docserver-server");
				iDocServerServer.setContent(docserverServer);
				command.addItem(iDocServerServer);
				
				Item iDocServerContainer = new Item();
				iDocServerContainer.setProperty("id", "docserver-container");
				iDocServerContainer.setContent(docserverContainer);
				command.addItem(iDocServerContainer);						
			} catch (Exception e) {
				spaceExists = false;
			}
			

			DeletePublicServices deleteSpaceMonet = new DeletePublicServices();
			resultTemp = deleteSpaceMonet.execute(command);

			if (resultTemp.getItem("status").getContent().equals("ok")) {
				DeleteLocalServices deleteSpaceDocServer = new DeleteLocalServices();
				resultTemp = deleteSpaceDocServer.execute(command);
			}
			
			if (! spaceExists) {
				String error = "Space " + space + " not exists.";
				logger.error(error);
				status.setContent(ResultIDs.ExistsSpace);
				caption.setContent("Error: " + error);
			} else result = resultTemp;
			
		} catch (Exception e) {
			String error = "User wrong parameters.";
			logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + e.getStackTrace());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
		}

		return result;
	}

}
