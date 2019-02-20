package org.monet.deployservice_engine.control.commands;

import org.apache.log4j.Logger;
import org.monet.deployservice.xml.Item;

public class UpdateContainer implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String url;
	private String server;
	private String container;

	public UpdateContainer() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.UpdateContainer);
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
			status.setContent(ResultIDs.InvalidContainer);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			url = command.getItem("url").getContent();
		} catch (Exception e) {
			String error = "Url not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidServer);
			caption.setContent("Error: " + error);
			return false;
		}
		
		return true;
	}
	
	private Boolean update() {
		Item command = new Item();
		command.setProperty("server", server);
		command.setProperty("container", container);

		Item result = new Item();

		Item iserver = new Item();
		iserver.setProperty("id", "server");
		iserver.setContent(server);
		command.addItem(iserver);

		Item icontainer = new Item();
		icontainer.setProperty("id", "container");
		icontainer.setContent(container);
		command.addItem(icontainer);

		Item iurl = new Item();
		iurl.setProperty("id", "url");
		iurl.setContent(url);
		command.addItem(iurl);

		UpdateWars updateWars = new UpdateWars();
		command.setProperty("id", OperationIDs.UpdateWars);
		result = updateWars.execute(command);

		if (!result.getItem("status").getContent().equals("ok")) {
			String error = "I can't update wars";
			logger.error(error);
			this.result = result;
			return false;
		}		
		
		ResetContainer resetContainer = new ResetContainer();
		command.setProperty("id", OperationIDs.ResetContainer);
		result = resetContainer.execute(command);

		if (!result.getItem("status").getContent().equals("ok")) {
			String error = "I can't reset container: " + container;
			logger.error(error);
			this.result = result;
			return false;
		}		
		
		return true;
	}
	
	public Item execute(Item command) {

		if (!initialize(command))
			return result;
		if (!update())
			return result;

		return result;
	}

}
