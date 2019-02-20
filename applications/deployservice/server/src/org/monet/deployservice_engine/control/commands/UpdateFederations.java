package org.monet.deployservice_engine.control.commands;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Network;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.utils.Federations;
import org.monet.deployservice_engine.xml.ServersXML;

public class UpdateFederations implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String server;
	private String container;
	private String url;

	private String fileNameWar;
	private String dirTemp;
	private String fileWar;
	private String fileWarDest;
	
	public UpdateFederations() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.UpdateFederations);
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
			status.setContent(ResultIDs.InvalidUrl);
			caption.setContent("Error: " + error);
			return false;
		}

		fileNameWar = "federation.war";
		fileWar = Configuration.getPathWar() + "/" + fileNameWar;
		
		return true;
	}
	
	private boolean download() {

		Files files = new Files();
		Zip zip = new Zip();
		
		String fileWarTemp = Configuration.getPathWar() + "/" + fileNameWar + ".tmp";
		
		if (! files.fileExists(Configuration.getPathWar())) files.makeDir(Configuration.getPathWar());
				
		try {
			if (files.fileExists(fileWarTemp)) files.remove(fileWarTemp);
		} catch (Exception e) {
			String error = "I can't delete file: " + fileWarTemp;
			logger.error(error + "\nDetails: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (! files.fileExists(fileWarTemp)) {
			String urlWar = url + "/" + fileNameWar;
			if (url.contains("*")) urlWar = url.replace("*", fileNameWar);		
			Network network = new Network();
			try {
				logger.info("Download file: " + urlWar +" to " + fileWarTemp);
				network.downloadFile(urlWar, fileWarTemp);
				
				if (zip.isValid(fileWarTemp)) {				
				  if (files.fileExists(fileWar)) files.remove(fileWar);
				  files.renameFile(fileWarTemp, fileWar);
				} else {
					String error = "File: '" + fileWarTemp + "' has errors.";
					logger.error(error);
					status.setContent(ResultIDs.InternalError);
					caption.setContent("Error: " + error);
					return false;					
				}					
			} catch (IOException e) {
				String error = "I can't get war file from url: " + urlWar;
				logger.error(error + "\nDetails: " + e.getMessage());
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}
		}
		return true;
	}

	private Boolean openWar() {

		long l = System.currentTimeMillis();
		dirTemp = Configuration.getValue(Configuration.VAR_DirTemp) + "/" + Configuration.CONST_AppName + "_" + l;

		File dir = new java.io.File(dirTemp);
		dir.mkdir();

		Zip zip = new Zip();
		zip.unCompress(fileWar, dirTemp);

		return true;
	}

	private Boolean closeWar() {
		Files files = new Files();
		files.removeDir(dirTemp);
		return true;
	}

	private Boolean processWar(String name) {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		String fileWebDistXML = dirTemp + "/WEB-INF/web.dist.xml";
		String fileWebXML = dirTemp + "/WEB-INF/web.xml";

		Files files = new Files();
		try {

			files.renameFile(fileWebDistXML, fileWebXML);
			files.replaceTextInFile(fileWebXML, "#federation#", name);

		} catch (Exception e) {
			String error = "File web.xml i can not read.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			closeWar();
			return false;
		}

		String fileContextDistXML = dirTemp + "/META-INF/context.dist.xml";
		String fileContextXML = dirTemp + "/META-INF/context.xml";
		try {
			files.renameFile(fileContextDistXML, fileContextXML);

			
			Federations itemFederations = new Federations();
			Item itemServer = servers.getItem(server);
			Item federation = itemFederations.getFederation(itemServer, container, name);
			
			String federationDbUrl = federation.getProperty("db-url");
			String federationDbUser = federation.getProperty("db-user");
			String federationDbPassword = federation.getProperty("db-password");
			String federationDbName = servers.getItem(server).getItem(container).getProperty("db-federation-prefix") + name;
			
			if (federationDbUrl.equals("")) {
				federationDbUrl = servers.getItem(server).getItem(container).getProperty("db-url");
				federationDbUser = servers.getItem(server).getItem(container).getProperty("db-user");
				federationDbPassword = servers.getItem(server).getItem(container).getProperty("db-password");
			}

			federationDbUrl = federationDbUrl.replaceAll("#dbname#", federationDbName);
			String DBType = federationDbUrl.split(":")[1];
			String resource = Configuration.getValue(Configuration.VAR_ResourceMysql);
			if (DBType.toLowerCase().equals("oracle")) {
				resource = Configuration.getValue(Configuration.VAR_ResourceOracle);
			}			

			resource = resource.replaceAll("jdbc/MonetDatabaseSource", "jdbc/FederationDatabase-"+name);
			resource = resource.replaceAll("#url#", federationDbUrl);
			resource = resource.replaceAll("#username#", federationDbUser);
			resource = resource.replaceAll("#password#", federationDbPassword);

			
			files.replaceTextInFile(fileContextXML, "#resource#", resource);
			
			
		} catch (Exception e) {
			String error = "Monet file context.xml i can not read.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			closeWar();
			return false;
		}

		Zip zip = new Zip();
		zip.compress(fileWarDest, dirTemp);

		return true;
	}
	
	private boolean update() {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());
			
		String deployDir = servers.getItem(server).getItem(container).getProperty("path") + "/webapps";
		
		Item itemServer = servers.getItem(server);
		Federations itemFederations = new Federations();
		
		Files files = new Files();

		Item federations = itemFederations.getFederations(itemServer, container);
		Iterator<Item> ispaces = federations.getItems().iterator();	
		while (ispaces.hasNext()) {
			Item space = (Item) ispaces.next();

			String federationName = space.getProperty("id");
			logger.info("Updating federation: " + federationName);
			
			fileWarDest = deployDir + "/" + federationName + ".war";
			
			String homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user") + "/." + federationName;
			String fileFederationXML = homeDir + "/federation.xml";			
			if (files.fileExists(fileFederationXML)) {
			  openWar();
			  processWar(federationName);
			  closeWar();
			}			
		}
		
		return true;
	}

	public Item execute(Item command) {

		if (!initialize(command))
			return result;

		if (! download())
			return result;
		
		if (!update())
			return result;

		return result;
	}

}
