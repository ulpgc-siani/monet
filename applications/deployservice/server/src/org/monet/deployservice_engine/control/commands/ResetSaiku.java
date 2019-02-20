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

public class ResetSaiku implements ICommand {

	private Logger logger;
	private String space;
	private String container;
	private String server;
	private String user;
	private String group;

	private Item result;
	private Item caption;
	private Item status;

	private String dirTemp;
	private String fileWar;
	
	private String docserverServer;
	private String docserverContainer;
	private DbData dbData;

	private String fileCatalog;

	public ResetSaiku() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.ResetSaiku);
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

		user = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user");
		if (user.equals("")) {
			String error = "User container DocServer not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		group = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("group");
		if (group.equals("")) {
			String error = "Group container DocServer not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}
		
		try {
			dbData.url = command.getItem("db-url-monet").getContent();
		} catch (Exception e) {
			dbData.url = servers.getItem(server).getItem(container).getProperty("db-url");
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
			dbData.user = servers.getItem(server).getItem(container).getProperty("db-user");
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
			dbData.password = servers.getItem(server).getItem(container).getProperty("db-password");
		}

		String dbPrefix = servers.getItem(server).getItem(container).getProperty("db-prefix");

		try {
			dbData.create = command.getItem("db-create-monet").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			try {
				dbData.create = servers.getItem(server).getItem(container).getProperty("db-create").toLowerCase().equals("true");
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

		fileWar = Configuration.getPathWar() + "/" + Configuration.CONST_FileSaikuWar;

		try {
			fileCatalog = command.getItem("file-catalog").getContent();
		} catch (Exception e) {
			String error = "I can not get file-catalog.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
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

	private Boolean processWar() {
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		String fileSpaceWar = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("path") + "/webapps/" + Configuration.CONST_WarSaikuPrefix + space + ".war";

		String fileWebDistXML = dirTemp + "/WEB-INF/web.dist.xml";
		String fileWebXML = dirTemp + "/WEB-INF/web.xml";

		Files files = new Files();
		try {
			files.renameFile(fileWebDistXML, fileWebXML);
			files.replaceTextInFile(fileWebXML, "#saiku#", Configuration.CONST_WarSaikuPrefix + space);

		} catch (Exception e) {
			String error = "File: "+fileWebXML+" i can not read.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			return false;
		}
				
		String fileBeansDistXML = dirTemp + "/WEB-INF/saiku-beans.dist.xml";
		String fileBeansXML = dirTemp + "/WEB-INF/saiku-beans.xml";
		
		try {
			String homeDir = new File(fileCatalog).getParent();

			files.renameFile(fileBeansDistXML, fileBeansXML);
			files.replaceTextInFile(fileBeansXML, "#user_data#", homeDir);

			String saikuRep = homeDir + "/saiku-repository";
			
			if (! files.fileExists(saikuRep)) {
				files.makeDir(saikuRep);
				files.chown(homeDir, user, group);
			}

		} catch (Exception e) {
			String error = "File: "+fileBeansXML+" i can not read.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			return false;
		}
		
		String fileConfigDist = dirTemp + "/WEB-INF/classes/saiku-datasources/foodmart.dist";
		String fileConfig = dirTemp + "/WEB-INF/classes/saiku-datasources/foodmart";
		try {
			files.renameFile(fileConfigDist, fileConfig);

			String location = "jdbc:mondrian:Jdbc=" + dbData.url + ";Catalog=" + fileCatalog + ";";

			if (dbData.type.equals("oracle")) {
				location = location + "JdbcDrivers=oracle.jdbc.OracleDriver;";
			} else
				location = location + "JdbcDrivers=com.mysql.jdbc.Driver;";

			files.replaceTextInFile(fileConfig, "#location#", location);
			files.replaceTextInFile(fileConfig, "#username#", dbData.user);
			files.replaceTextInFile(fileConfig, "#password#", dbData.password);
		} catch (Exception e) {
			String error = "Saiku file foodmart i can not read.";
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

		return result;
	}

}
