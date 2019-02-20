package org.monet.deployservice_engine.control.commands;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;

public class ResetMonetContainer implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;

	private Boolean replaceTheme;

	public ResetMonetContainer() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.ResetMonetContainer);
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

		try {
			replaceTheme = command.getItem("replace-theme").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			replaceTheme = false;
		}

		return true;
	}

	// @SuppressWarnings("static-access")
	private boolean reset() {

		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		Item command = new Item();
		command.setProperty("server", server);
		command.setProperty("container", container);

		Item result = new Item();
		ResetMonet resetMonet = new ResetMonet();

		Item itemServer = servers.getItem(server);
		Spaces itemSpaces = new Spaces();

		Item spaces = itemSpaces.getSpaces(container, itemServer);
		Iterator<Item> ispaces = spaces.getItems().iterator();
		while (ispaces.hasNext()) {
			Item space = (Item) ispaces.next();

			String spaceName = space.getProperty("id");

			try {
				Item itemSpace = itemSpaces.getSpace(container, itemServer, spaceName);
				String userlocalContainer = docservers.getItem(servers.getItem(server).getItem(container).getProperty("docserver-server")).getItem(servers.getItem(server).getItem(container).getProperty("docserver-container")).getProperty("name");
				String localContainer = itemSpace.getProperty("docserver-container");

				if (userlocalContainer.equals(localContainer)) {

					logger.info("Reset monet in space: " + spaceName + " from server: " + servers.getItem(server).getProperty("name") + ", container: " + servers.getItem(server).getItem(container).getProperty("name"));

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

					Item ireplaceTheme = new Item();
					ireplaceTheme.setProperty("id", "replace-theme");
					if (replaceTheme)
						ireplaceTheme.setContent("true");
					else
						ireplaceTheme.setContent("false");
					command.addItem(ireplaceTheme);

					command.setProperty("id", OperationIDs.ResetMonet);
					result = resetMonet.execute(command);

					if (!result.getItem("status").getContent().equals("ok")) {
						String error = "I can't reset monet in space: " + spaceName;
						logger.error(error);
						this.result = result;
						return false;
					}

				}
			} catch (Exception e) {
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
