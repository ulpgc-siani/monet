package org.monet.deployservice_engine.control.commands.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.OperationIDs;
import org.monet.deployservice_engine.control.commands.ResetDocService;
import org.monet.deployservice_engine.control.commands.ResultIDs;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;

public class AddLocalServices {

	private Logger logger;
	private String space;
	private String container;
	private String server;
	private DbData dbData;
	private String user;
	private String group;

	private String homeDir;
	private String fileWar;

	private Item result;
	private Item caption;
	private Item status;

	private String docserverServer;
	private String docserverContainer;

	private SpaceLocalBiEngine spaceLocalBiEngine;
	private Boolean installSaiku;

	public AddLocalServices() {
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
			dbData.password = command.getItem("db-password-docserver").getContent();
		} catch (Exception e) {
			dbData.password = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-password");
			
			if (space.equals("ROOT"))
				dbData.password = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-default-password");			
		}

		String dbPrefix = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-prefix");

		try {
			dbData.create = command.getItem("db-create-docserver").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			dbData.create = false;
		}

		Db db = new Db();
		try {
			dbData.name = command.getItem("db-name-docserver").getContent();
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

		dbData.type = dbData.url.split(":")[1];
		dbData.urlAdmin = dbData.url.replaceAll("#dbname#", "mysql");
		dbData.url = dbData.url.replaceAll("#dbname#", dbData.name);
		dbData.user = dbData.user.replaceAll("#dbname#", dbData.name);
		dbData.password = dbData.password.replaceAll("#dbname#", dbData.name);

		installSaiku = false;
		try {
			installSaiku = command.getItem("install-saiku").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			installSaiku = false;
		}

		fileWar = Configuration.getPathWar() + "/" + Configuration.getValue(Configuration.VAR_FileDocServiceWar);

		homeDir = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user-path") + "/" + docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user") + "/." + Configuration.CONST_WarDocServerPrefix + space;

		spaceLocalBiEngine = new SpaceLocalBiEngine(command);

		return true;
	}

	private Boolean updateConfig(String fileName) {
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		String homeDir = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user-path") + "/" + docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user") + "/." + Configuration.CONST_WarDocServerPrefix + space;

		Properties properties = new Properties();
		try {
			properties.loadFromXML(new FileInputStream(fileName));

			if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() < 2))) {
  			properties.setProperty("JDBC.Database", dbData.type);
			} else {
  			properties.setProperty("Jdbc.Database", dbData.type);				
			}

			properties.setProperty("Path.TrueTypeFonts", homeDir + "/fonts");
			properties.setProperty("Path.Temp", Configuration.getValue(Configuration.VAR_DirTemp));
			properties.setProperty("PdfLicencePath", homeDir + "/license/Aspose.Words.lic");

			
			if (Configuration.MonetVersionMayor() >= 3) {
			  Files files = new Files();
			  
			  
				String DocumentsDir = Configuration.getValue(Configuration.VAR_DocumentDisksDir);
				if (DocumentsDir.equals("")) DocumentsDir = Configuration.getValue(Configuration.VAR_DocumentsDir);			
			  			  
			  
				if (! DocumentsDir.equals("")) {
					String documentDisksDirBase =  DocumentsDir.replaceAll(" ", "");
					String documentDisksDir = "";
					
					String[] disks = documentDisksDirBase.split(",");
					String diskDir = "";
					for (int i=0; i < disks.length; i++) {
						diskDir = disks[i] + "/" + space;
						documentDisksDir = documentDisksDir + "," + diskDir;
						
	          try {
	            files.makeDir(diskDir, user, group);
	      		} catch (Exception e) {
	      			String error = "I can not deploy document disks dir: " + diskDir;
	      			
	      			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();					
	      			logger.error(error + " Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
	      			status.setContent(ResultIDs.InternalError);
	      			caption.setContent("Error: " + error);
	      			return false;
	      		}
					}
					documentDisksDir = documentDisksDir.substring(1);
					properties.setProperty("Document.Disks", documentDisksDir);
				} else {
					properties.setProperty("Document.Disks", homeDir + "/documents");
          try {
  					files.makeDir(homeDir + "/documents", user, group);
      		} catch (Exception e) {
      			String error = "I can not deploy document disks dir: " + homeDir + "/documents";
      			
      			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();					
      			logger.error(error + " Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
      			status.setContent(ResultIDs.InternalError);
      			caption.setContent("Error: " + error);
      			return false;
      		}
			  }
			}
			
			
			properties.storeToXML(new FileOutputStream(fileName), null);
		} catch (FileNotFoundException e) {
			String error = "File not found: '" + fileName;
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		} catch (IOException e) {
			String error = "I cant not read the file: '" + fileName;
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (installSaiku && !spaceLocalBiEngine.updateConfig())
			return false;

		return true;
	}

	private Boolean configure() {
		Files files = new Files();

		String fileConfigName = homeDir + "/" + files.baseName(Configuration.getValue(Configuration.VAR_FileConfigDocServer).replaceAll(".dist", ""));

		File fileHomeDir = new File(homeDir);

		if (fileHomeDir.exists()) {
			String error = "DocServer config directory exists.";
			logger.error(error);
			status.setContent(ResultIDs.ExistsDocServer);
			caption.setContent("Error: " + error);
			return false;
		}
		fileHomeDir.mkdir();

		Zip zip = new Zip();
		try {
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_DirConfigDocServer), homeDir);
			files.renameFile(homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigDocServer), fileConfigName);
			files.chmod(fileConfigName, "g+w");

			String fileLog4jDocServerDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileLog4jDocServer).replaceAll(".dist", "");			
			files.renameFile(homeDir + "/" + Configuration.getValue(Configuration.VAR_FileLog4jDocServer), fileLog4jDocServerDest);
			files.replaceTextInFile(homeDir + "/" + Configuration.getValue(Configuration.VAR_FileLog4jDocServer).replaceAll(".dist", ""), "#config_path#", homeDir);
			if (Configuration.getValue(Configuration.VAR_AddSocketLog, "false").equals("true")) {
			  files.replaceTextInFile(fileLog4jDocServerDest, "<entry key=\"log4j.rootCategory\">INFO, CA, DocServiceFileAppender, ModelDatabaseAppender</entry>", "<entry key=\"log4j.rootCategory\">INFO, CA, DocServiceFileAppender, ModelDatabaseAppender, logginghub</entry>");
			  files.replaceTextInFile(fileLog4jDocServerDest, "<!--#appender_socket#-->", "<entry key=\"log4j.appender.logginghub\">com.vertexlabs.logging.log4j.SocketAppender</entry>\n  <entry key=\"log4j.appender.logginghub.host\">localhost</entry>\n  <entry key=\"log4j.appender.logginghub.sourceApplication\">docservice</entry>\n");
			}
			
			files.makeDir(homeDir + "/logs");
			files.chown(homeDir, user, group);

			if (installSaiku)
				spaceLocalBiEngine.configure();

		} catch (Exception e) {
			String error = "I can not deploy DocServer configuration.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (!updateConfig(fileConfigName))
			return false;
		if (!database())
			return false;

		return true;
	}

	private Boolean database_mysql() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		if (dbData.create) {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			try {
				Connection conexionAdmin;
				conexionAdmin = (Connection) DriverManager.getConnection(dbData.urlAdmin, dbData.user, dbData.password);
				Statement stAdmin = (Statement) conexionAdmin.createStatement();
				stAdmin.executeUpdate("CREATE DATABASE " + dbData.name);
				logger.info("Created database: " + dbData.name);
				conexionAdmin.close();
			} catch (SQLException e1) {
				String error = "I can not create database(" + dbData.type + "): " + dbData.name;
				logger.error(error);
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		Files file = new Files();
		Zip zip = new Zip();
		try {
			String fileDBClean = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbMysqlDocServerClean));
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbMysqlDocServerClean), fileDBClean);

			String fileDB = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbMysqlDocServer));
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbMysqlDocServer), fileDB);
			Db db = new Db(dbData.url, dbData.user, dbData.password);
			db.executeScript(fileDBClean);
			db.executeScript(fileDB);
			
			(new File(fileDBClean)).delete();
			(new File(fileDB)).delete();
		} catch (Exception e) {
			String error = "I can not deploy Monet database script.";
			logger.error(error + " Message: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		return true;
	}

	private Boolean database_oracle() {
		if (dbData.create) {
		}

		Files file = new Files();
		Zip zip = new Zip();

		String fileDBClean = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbOracleDocServerClean));
		logger.info("Deploy oracle file: " + fileDBClean);
		try {
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbOracleDocServerClean), fileDBClean);

			if (Configuration.getValue(Configuration.VAR_Debug).equals("true"))
				logger.info("\nDatabase url: " + dbData.url + "\nDatabase user: " + dbData.user + "\nDatabase password: " + dbData.password);

			Db db = new Db(dbData.url, dbData.user, dbData.password);
			db.executeScript(fileDBClean);

			(new File(fileDBClean)).delete();
		} catch (SQLException SQLe) {
			String error = "I can not connect to database. File: " + fileDBClean;
			logger.error(error + "\nDatabase url: " + dbData.url + "\nDatabase user: " + dbData.user + "\nMessage:\n" + SQLe.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		} catch (Exception e) {
			String error = "I can not deploy database.";
			logger.error(error + "\nMessage:\n" + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		String fileDB = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbOracleDocServer));
		logger.info("Deploy oracle file: " + fileDB);
		try {
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbOracleDocServer), fileDB);

			Db db = new Db(dbData.url, dbData.user, dbData.password);
			db.executeScript(fileDB);

			(new File(fileDB)).delete();
		} catch (SQLException SQLe) {
			String error = "I can not connect to database. File: " + fileDB;
			logger.error(error + " Message: " + SQLe.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		} catch (Exception e) {
			String error = "I can not deploy database.";
			logger.error(error + " Message: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		return true;
	}

	private Boolean database() {

		try {
			if (dbData.type.equals("mysql")) {
				database_mysql();
			}

			if (dbData.type.equals("oracle")) {
				database_oracle();
			}

			if (installSaiku)
				spaceLocalBiEngine.database();

		} catch (Exception e) {
			String error = "I can not deploy local services database.";
			logger.error(error + " Message: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		return true;
	}

	public Item execute(Item command) {
		if (!initialize(command))
			return result;

		if (!configure())
			return result;

		if (result.getItem("status").getContent().equals("ok")) {
			ResetDocService resetDocServer = new ResetDocService();

			Item status = resetDocServer.execute(command);
			
			if (!status.getItem("status").getContent().equals("ok")) {
				String error = "I can't reset docservice in space: " + space;
				logger.error(error);
				return status;
			}				
		}

//		if ((result.getItem("status").getContent().equals("ok"))) {
	  if (installSaiku)
			spaceLocalBiEngine.Reset();
//		}

		return result;
	}

}
