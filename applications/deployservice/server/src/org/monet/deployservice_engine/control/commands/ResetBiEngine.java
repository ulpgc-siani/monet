package org.monet.deployservice_engine.control.commands;

import java.io.File;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;
import org.monet.deployservice_engine.configuration.Configuration;

public class ResetBiEngine implements ICommand {

	private Logger logger;
	private String space;
	private String container;
	private String server;

	private Item result;
	private Item caption;
	private Item status;

  private String dirTemp;
	private String fileWar;

	private String docserverServer;
	private String docserverContainer;
	private DbData dbData;
	
	public ResetBiEngine() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.ResetBiEngine);
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
				
		try {
			dbData.url = command.getItem("db-url-monet").getContent();
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
			dbData.user = command.getItem("db-user-monet").getContent();
		} catch (Exception e) {
			dbData.user = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-user");
			
			if (space.equals("ROOT"))
				dbData.user = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-default-user");
			
			if (dbData.user.equals("")) {
				String error = "User database (db-user) not found in parameters.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbUser);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		try {
			dbData.password = command.getItem("db-password-monet").getContent();
		} catch (Exception e) {
			dbData.password = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-password");
			
			if (space.equals("ROOT"))
				dbData.password = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-default-password");									
		}

		String dbPrefix = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-prefix");

		try {
			dbData.create = command.getItem("db-create-monet").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			try {
				dbData.create = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-create").toLowerCase().equals("true");
			} catch (Exception e1) {
				dbData.create = false;
			}
		}

		Db db = new Db();
		try {
			dbData.name = command.getItem("db-name-monet").getContent();
		} catch (Exception e) {
			dbData.name = db.getDbNameFromUrl(dbData.url);

			if (dbData.name.equals("#dbname#"))
				dbData.name = dbPrefix + space;

			if (space.equals("ROOT"))
				dbData.name = dbPrefix + docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-default-name");
			
			if (dbData.name.equals("")) {
				String error = "Name database (db-name) not found in parameters.";
				logger.error(error + " Database type: " + db.getDbTypeFromUrl(dbData.url) + " Url: " + dbData.url);
				status.setContent(ResultIDs.InvalidDbName);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		dbData.urlAdmin = dbData.url.replaceAll("#dbname#", "mysql");
		dbData.url = dbData.url.replaceAll("#dbname#", dbData.name);
		dbData.type = dbData.url.split(":")[1];
		dbData.user = dbData.user.replaceAll("#dbname#", dbData.name);
		dbData.password = dbData.password.replaceAll("#dbname#", dbData.name);

		
		fileWar = Configuration.getPathWar() + "/" + Configuration.CONST_FileBiEngineWar;
		
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

	private Boolean processWar() {
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		String fileSpaceWar = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("path") + "/webapps/"
		    + Configuration.CONST_WarBiEnginePrefix + space + ".war";

		String fileWebDistXML = dirTemp + "/WEB-INF/web.dist.xml";
		String fileWebXML = dirTemp + "/WEB-INF/web.xml";

		Files files = new Files();		
		try {
			files.renameFile(fileWebDistXML, fileWebXML);			
			files.replaceTextInFile(fileWebXML, "#biengine#", Configuration.CONST_WarBiEnginePrefix + space);
			
			
		} catch (Exception e) {
			String error = "BiEngine file web.xml i can not read.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			return false;
		}

		String fileContextDistXML = dirTemp + "/META-INF/context.dist.xml";
		String fileContextXML = dirTemp + "/META-INF/context.xml";	
		try {		
			files.renameFile(fileContextDistXML, fileContextXML);
			if (dbData.type.equals("oracle")) {
				files.replaceTextInFile(fileContextXML, "#resource#", Configuration.getValue(Configuration.VAR_ResourceOracle));
			} else
				files.replaceTextInFile(fileContextXML, "#resource#", Configuration.getValue(Configuration.VAR_ResourceMysql));
			
			files.replaceTextInFile(fileContextXML, "#url#", dbData.url);
			files.replaceTextInFile(fileContextXML, "#username#", dbData.user);
			files.replaceTextInFile(fileContextXML, "#password#", dbData.password);			
		} catch (Exception e) {
			String error = "BiEngine file context.xml i can not read.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			closeWar();
			return false;
		}
		
		
		Zip zip = new Zip();
		zip.compress(fileSpaceWar, dirTemp);

		return true;
	}
	
	public Item execute(Item command) {
		if (!initialize(command))
			return result;

		if (!openWar())
			return result;
		if (!processWar())
			return result;
		if (!closeWar())
			return result;

		
		if (result.getItem("status").getContent().equals("ok")) {

			DocServersXML docserversXML = new DocServersXML();
			Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());
			String homeDir = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user-path") + "/" + docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user") + "/." + Configuration.CONST_WarBiEnginePrefix + space;
			
			Item ifileCatalog = new Item();
			ifileCatalog.setProperty("id", "file-catalog");
			ifileCatalog.setContent(homeDir + "/catalog.xml");
			command.addItem(ifileCatalog);			
			
		  ResetSaiku resetSaiku = new ResetSaiku();
		  result = resetSaiku.execute(command);
		}
		
		return result;
	}
	
}
