package org.monet.deployservice_engine.control.commands.tasks;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.SystemOS;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.OperationIDs;
import org.monet.deployservice_engine.control.commands.ResultIDs;
import org.monet.deployservice_engine.utils.RestartServers;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;

public class DeleteLocalServices {
	private String server;
	private String container;
	private String space;

	private Logger logger;
	private String homeDir;
	private DbData dbData;

	private String docserverServer;
	private String docserverContainer;

	private Item result;
	private Item caption;
	private Item status;

	private SpaceLocalBiEngine spaceDocServerReportService;
	
	public DeleteLocalServices() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.DeleteSpace);
		caption = new Item();
		caption.setProperty("id", "caption");
		result.addItem(caption);
		status = new Item();
		status.setProperty("id", "status");
		status.setContent("ok");
		result.addItem(status);
		dbData = new DbData();
	}

	private Boolean initialize(Item command) {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

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
			space = command.getItem("space").getContent();
		} catch (Exception e) {
			String error = "Space not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidSpace);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			docserverServer = command.getItem("docserver-server").getContent();
		} catch (Exception e) {
			try {
				docserverServer = servers.getItem(server).getItem(container).getProperty("docserver-server");
			} catch (Exception e_ds) {
				String error = "I can not get docserver-server.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbUrl);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		try {
			docserverContainer = command.getItem("docserver-container").getContent();
		} catch (Exception e) {
			try {
				docserverContainer = servers.getItem(server).getItem(container).getProperty("docserver-container");
			} catch (Exception e_ds) {
				String error = "I can not get docserver-container.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbUrl);
				caption.setContent("Error: " + error);
				return false;
			}
		}
		
		if ((docservers.getItem(docserverServer) == null)) {
			String error = "DocService server: '"+docserverServer+"' not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;			
		}
		if ((docservers.getItem(docserverServer).getItem(docserverContainer) == null)) {
			String error = "DocService container: '"+docserverContainer+"' not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;			
		}

		
		try {
			dbData.url = command.getItem("db-url-docserver").getContent();
		} catch (Exception e) {
			dbData.url = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-url");
			if (dbData.url.equals("")) {
				String error = "Url database (db-url) not found in parameters.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbUrl);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		try {
			dbData.user = command.getItem("db-user-docserver").getContent();
		} catch (Exception e) {
			dbData.user = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-user");
			if (dbData.user.equals("")) {
				String error = "User database (db-user) not found in parameters.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbUser);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		try {
			dbData.password = command.getItem("db-password-docserver").getContent();
		} catch (Exception e) {
			dbData.password = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-password");
		}

		String dbPrefix = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-prefix");

		try {
			dbData.name = command.getItem("db-name-docserver").getContent();
		} catch (Exception e) {
			dbData.name = dbPrefix + space;
			if (dbData.name.equals("")) {
				String error = "Name database (db-name) not found in parameters.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbName);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		dbData.urlAdmin = dbData.url.replaceAll("#dbname#", "mysql");
		dbData.url = dbData.url.replaceAll("#dbname#", dbData.name);
		dbData.type = dbData.url.split(":")[1];

		homeDir = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user-path") + "/"
		    + docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user") + "/." + Configuration.CONST_WarDocServerPrefix + space;
		
		spaceDocServerReportService = new SpaceLocalBiEngine(command);		
		
		return true;
	}

	private Boolean deleteHomeDir() {

		Files files = new Files();
		try {
			files.removeDir(homeDir);
		} catch (Exception e) {
			String error = "Directory not found: '" + homeDir;
			logger.error(error);
		}


		if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
	 	  spaceDocServerReportService.deleteHomeDir();
		}

		String DocumentsDir = Configuration.getValue(Configuration.VAR_DocumentDisksDir);
		if (DocumentsDir.equals("")) DocumentsDir = Configuration.getValue(Configuration.VAR_DocumentsDir);			
		
		if (! DocumentsDir.equals("")) {
			String documentDisksDirBase =  DocumentsDir;
			
			String[] disks = documentDisksDirBase.split(",");
			String diskDir = "";
			for (int i=0; i < disks.length; i++) {
				diskDir = disks[i] + "/" + space;
				files.removeDir(diskDir);			
			}
		}
			
		return true;
	}

	private Boolean deleteWar() {
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		String fileSpaceWar = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("path") + "/webapps/"
		    + Configuration.CONST_WarDocServerPrefix + space + ".war";

		Files files = new Files();
		if (files.fileExists(fileSpaceWar)) {
		  logger.info("Deleting file: " + fileSpaceWar);
		  files.remove(fileSpaceWar);
		}

		if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
		  spaceDocServerReportService.deleteWar();
		}
		
		return true;
	}

	
	
	@SuppressWarnings("static-access")
  private Boolean resetServer() {
		RestartServers resetServers = new RestartServers();						
	
		try {
			resetServers.container_docserver_stop(docserverServer, docserverContainer);
		} catch (Exception e) {
			String error = "I can not stop container.";
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
			String error = "I can not start container.";
			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
			logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}
		return true;
	}	
	
	public Item execute(Item command) {
		try {
			if (!initialize(command))
				return result;
		} catch (Exception e) {
			String error = "User wrong parameters.";
			logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + e.getStackTrace());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return result;
		}

		if (!deleteWar())
			return result;
		
		if (SystemOS.isWindows() && (!resetServer()))
			return result;
		
		if (!deleteHomeDir())
			return result;

		return result;
	}

}
