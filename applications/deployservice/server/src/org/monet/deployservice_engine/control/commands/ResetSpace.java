package org.monet.deployservice_engine.control.commands;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.utils.RestartServers;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.ServersXML;

public class ResetSpace implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;
	private String spaceName;
	private String dbnameMonet;
	private String dbnameDocServer;
	private String docserverServer;
	private String docserverContainer;
	private String baseUrl;
	
	private String spaceURI;
	private String spaceSecret;
	private String spaceLabelES;

	private String federationName;
	
/*
	private String installEtl;
	private String installPlanner;
*/	
	private String installSaiku;
	private Boolean restartDocServer;

	public ResetSpace() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.AddSpace);
		caption = new Item();
		caption.setProperty("id", "caption");
		result.addItem(caption);
		status = new Item();
		status.setProperty("id", "status");
		status.setContent("ok");
		result.addItem(status);
	}

	private Boolean initialize(Item command) {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

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
			spaceName = command.getItem("space").getContent();
			if (spaceName.equals(""))	throw new Exception("Space name is empty.");
		} catch (Exception e) {
			String error = "Space not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidSpace);
			caption.setContent("Error: " + error);
			return false;
		}

		restartDocServer = false;
		try {
			if (command.getItem("restart-docserver").getContent().toLowerCase().equals("true"))			
			  restartDocServer = true;			
		} catch (Exception e) {}
		
		
		Item itemServer = servers.getItem(server);
		Spaces itemSpaces = new Spaces();

		try {
			Item itemSpace = itemSpaces.getSpace(container, itemServer, spaceName);

			dbnameMonet = itemSpace.getProperty("dbname-monet");
			dbnameDocServer = itemSpace.getProperty("dbname-docserver");
			docserverServer = itemSpace.getProperty("docserver-server");
			docserverContainer = itemSpace.getProperty("docserver-container");
			installSaiku = itemSpace.getProperty("install-saiku");
			baseUrl = itemSpace.getProperty("base-url");
			spaceLabelES = itemSpace.getProperty("space-label-es");
			spaceURI = itemSpace.getProperty("space-uri");
			spaceSecret = itemSpace.getProperty("space-secret");
			federationName = itemSpace.getProperty("federation");
			
			if (dbnameMonet.equals("")) throw new Exception("Monet database name is empty.");
			if (dbnameDocServer.equals("")) throw new Exception("DocServer database name is empty.");
			
		} catch (Exception e) {
			String error = "Space " + spaceName + " not exists.";
		
			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();					
			logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));		
			status.setContent(ResultIDs.InvalidSpace);
			caption.setContent("Error: " + error);
			return false;
		}
		
		return true;
	}

	
	@SuppressWarnings("static-access")
  private boolean reset() {
		Item command = new Item();
		command.setProperty("server", server);
		command.setProperty("container", container);

		Item result = new Item();

		DeleteSpace deleteSpace = new DeleteSpace();
		AddSpace addSpace = new AddSpace();

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

		Item idbNameMonet = new Item();
		idbNameMonet.setProperty("id", "db-name-monet");
		idbNameMonet.setContent(dbnameMonet);
		command.addItem(idbNameMonet);

		Item idbNameDocServer = new Item();
		idbNameDocServer.setProperty("id", "db-name-docserver");
		idbNameDocServer.setContent(dbnameDocServer);
		command.addItem(idbNameDocServer);
/*		
		Item iinstallEtl = new Item();
		iinstallEtl.setProperty("id", "install-etl");
		iinstallEtl.setContent(installEtl);
		command.addItem(iinstallEtl);		

		Item iinstallPlanner = new Item();
		iinstallPlanner.setProperty("id", "install-planner");
		iinstallPlanner.setContent(installPlanner);
		command.addItem(iinstallPlanner);		
*/
		
		Item iinstallSaiku = new Item();
		iinstallSaiku.setProperty("id", "install-saiku");
		iinstallSaiku.setContent(installSaiku);
		command.addItem(iinstallSaiku);		
		
		Item ibaseUrl = new Item();
		ibaseUrl.setProperty("id", "base-url");
		ibaseUrl.setContent(baseUrl);
		command.addItem(ibaseUrl);		
		
		command.setProperty("id", OperationIDs.DeleteSpace);
		result = deleteSpace.execute(command);

		if (!result.getItem("status").getContent().equals("ok")) {
			String error = "I can't delete space: " + spaceName;
			logger.error(error);
			this.result = result;
			return false;
		}

		Item iDocServerServer = new Item();
		iDocServerServer.setProperty("id", "docserver-server");
		iDocServerServer.setContent(docserverServer);
		command.addItem(iDocServerServer);
		
		Item iDocServerContainer = new Item();
		iDocServerContainer.setProperty("id", "docserver-container");
		iDocServerContainer.setContent(docserverContainer);
		command.addItem(iDocServerContainer);		

		Item iSpaceURI = new Item();
		iSpaceURI.setProperty("id", "space-uri");
		iSpaceURI.setContent(spaceURI);
		command.addItem(iSpaceURI);		

		Item iSpaceLabelES = new Item();
		iSpaceLabelES.setProperty("id", "space-label-es");
		iSpaceLabelES.setContent(spaceLabelES);
		command.addItem(iSpaceLabelES);		

		Item iSpaceSecret = new Item();
		iSpaceSecret.setProperty("id", "space-secret");
		iSpaceSecret.setContent(spaceSecret);
		command.addItem(iSpaceSecret);		

		Item iFederationName = new Item();
		iFederationName.setProperty("id", "federation");
		iFederationName.setContent(federationName);
		command.addItem(iFederationName);		
		
		command.setProperty("id", OperationIDs.AddSpace);
		result = addSpace.execute(command);

		if (!result.getItem("status").getContent().equals("ok")) {
			String error = "I can't add space: " + spaceName + ", Details: " + result.getItem("caption").getContent();
			logger.error(error);
			this.result = result;
			return false;
		}

		if (restartDocServer) {
			RestartServers resetServers = new RestartServers();						

			try {
				resetServers.container_docserver_stop(docserverServer, docserverContainer);
			} catch (Exception e) {
				String error = "I can not stop containers.";
				ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
				logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}

			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e1) {
			}

			try {
				resetServers.container_docserver_start(docserverServer, docserverContainer);
			} catch (Exception e) {
				String error = "I can not start containers.";
				ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
				logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
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
