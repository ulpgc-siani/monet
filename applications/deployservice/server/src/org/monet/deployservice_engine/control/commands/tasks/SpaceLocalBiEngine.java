package org.monet.deployservice_engine.control.commands.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Shell;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.ResetBiEngine;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;

public class SpaceLocalBiEngine {

	private String server;
	private String container;
	private String space;
	private String user;
	private String group;
	private DbData dbData;

	private Logger logger;

	private String homeDir;
	private String fileWar;

	private String docserverServer;
	private String docserverContainer;

	private Item command;

	public SpaceLocalBiEngine(Item command) {
		this.command = command;
		logger = Logger.getLogger(this.getClass());
		dbData = new DbData();
		initialize(command);
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
			return false;
		}

		try {
			container = command.getItem("container").getContent();
		} catch (Exception e) {
			String error = "Container not found in parameters.";
			logger.error(error);
			return false;
		}

		try {
			space = command.getItem("space").getContent();
		} catch (Exception e) {
			String error = "Space not found in parameters.";
			logger.error(error);
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
				return false;
			}
		}

		user = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user");
		if (user.equals("")) {
			String error = "User container DocServer not found.";
			logger.error(error);
			return false;
		}

		group = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("group");
		if (group.equals("")) {
			String error = "Group container DocServer not found.";
			logger.error(error);
			return false;
		}
		
		try {
			dbData.url = command.getItem("db-url-docserver").getContent();
		} catch (Exception e) {
			dbData.url = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-url");
			if (dbData.url.equals("")) {
				String error = "Url database (db-url) not found in parameters.";
				logger.error(error);
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
				return false;
			}
		}

		try {
			dbData.password = command.getItem("db-password-docserver").getContent();
		} catch (Exception e) {
			dbData.password = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-password");
		}

		String dbPrefix = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("db-prefix");

		Db db = new Db();
		try {
			dbData.name = command.getItem("db-name-docserver").getContent();
		} catch (Exception e) {

			dbData.name = db.getDbNameFromUrl(dbData.url);

			if (dbData.name.equals("#dbname#"))
				dbData.name = dbPrefix + space;

			if (dbData.name.equals("")) {
				String error = "Name database (db-name) not found in parameters.";
				logger.error(error + " Database type: " + db.getDbTypeFromUrl(dbData.url) + " Url: " + dbData.url);
				return false;
			}
		}

		dbData.type = dbData.url.split(":")[1];
		dbData.url = dbData.url.replaceAll("#dbname#", dbData.name);
		dbData.user = dbData.user.replaceAll("#dbname#", dbData.name);
		dbData.password = dbData.password.replaceAll("#dbname#", dbData.name);

		homeDir = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user-path") + "/"
		    + docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("user") + "/." + Configuration.CONST_WarBiEnginePrefix + space;

		fileWar = Configuration.getPathWar() + "/" + Configuration.CONST_FileBiEngineWar;

		return true;
	}

	public Boolean updateConfig() {
		
		Properties properties = new Properties();

		properties.clear();
		Files files = new Files();
		String fileConfigName = homeDir + "/" + files.baseName(Configuration.getValue(Configuration.VAR_FileConfigBiEngine).replaceAll(".dist", ""));
		try {
			properties.loadFromXML(new FileInputStream(fileConfigName));
			
			properties.setProperty("CATALOG.FILE", homeDir + "/catalog.xml");		
			
			if (dbData.type.equals("oracle"))
				properties.setProperty("JDBC.DATABASE", "oracle");
			else
				properties.setProperty("JDBC.DATABASE", "mysql");

//			properties.setProperty("DEPLOY.DOMAIN", Configuration.getValue(Configuration.VAR_DeployDomainBiEngine));
//			properties.setProperty("DEPLOY.PORT", Configuration.getValue(Configuration.VAR_DeployPortBiEngine));					
//			properties.setProperty("DEPLOY.COMMAND", Configuration.getValue(Configuration.VAR_DeployCommandBiEngine));
			
			properties.setProperty("DEPLOY.URL", Configuration.getValue(Configuration.VAR_DeployUrlBiEngine) + Configuration.CONST_WarSaikuPrefix + space);
			
			properties.storeToXML(new FileOutputStream(fileConfigName), null);
			
		} catch (FileNotFoundException e) {
			String error = "File not found: '" + fileConfigName + "'";
			logger.error(error);
			return false;
		} catch (IOException e) {
			String error = "I cant not read the file: '" + fileConfigName + "'";
			logger.error(error);
			return false;
		}

		return true;
	}

	public Boolean configure() {
		Files files = new Files();
		File fileHomeDir = new File(homeDir);

		if (fileHomeDir.exists()) {
			String error = "BiEngine config directory exists.";
			logger.error(error);
			return false;
		}
		fileHomeDir.mkdir();

		Zip zip = new Zip();
		try {
			String dirConfig = Configuration.getValue(Configuration.VAR_DirConfigBiEngine);
			
			String fileConfig = Configuration.getValue(Configuration.VAR_FileConfigBiEngine);
			String fileConfigDest = Configuration.getValue(Configuration.VAR_FileConfigBiEngine).replaceAll(".dist", "");
			String fileLog4j = Configuration.getValue(Configuration.VAR_FileLog4jBiEngine);
			String fileLog4jDest = Configuration.getValue(Configuration.VAR_FileLog4jBiEngine).replaceAll(".dist", "");
			
			zip.unCompressOnlyFile(fileWar, dirConfig, homeDir);
			
			files.renameFile(homeDir + "/" + fileConfig, homeDir + "/" +fileConfigDest);
			files.renameFile(homeDir + "/" + fileLog4j, homeDir + "/" + fileLog4jDest);
			files.replaceTextInFile(homeDir + "/" + fileLog4jDest, "#config_path#", homeDir);						
			
			files.chown(homeDir, user, group);
						
		} catch (Exception e) {
			String error = "I can not deploy BiEngine configuration.";
			logger.error(error);
			return false;
		}
		
		return true;
	}

	private Boolean database_mysql() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		Files file = new Files();
		Zip zip = new Zip();
		try {
			String fileDBClean = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbMysqlBiEngineClean));
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbMysqlBiEngineClean), fileDBClean);

			String fileDB = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbMysqlBiEngine));
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbMysqlBiEngine), fileDB);

			Pattern pattern = Pattern.compile(".*:.*://(.*):(.*)/.*");
			Matcher matcher = pattern.matcher(dbData.url);

			String host = "";
			String port = "";
			if (matcher.find()) {
				host = matcher.group(1);
				if (host.equals("localhost"))
					host = "127.0.0.1";
				port = matcher.group(2);
			}

			if (host.equals("") || port.equals("")) {
				String error = "Failed to load database host or port.";
				logger.error(error);
				return false;
			}

			String command = "";
			String password = "";
			if (!dbData.password.equals(""))
				password = " --password=" + dbData.password + " ";

			logger.info("Deploy mysql file: " + fileDBClean);
			command = "mysql --default-character-set=utf8 --host=" + host + " --port=" + port + " --database=" + dbData.name + " --user=" + dbData.user + password + " < " + fileDBClean + " 2>&1";

			Shell shell = new Shell();
			shell.executeCommand(command, new File(new File(fileDBClean).getParent()));
			
			logger.info("Deploy mysql file: " + fileDB);

			command = "mysql --default-character-set=utf8 --host=" + host + " --port=" + port + " --database=" + dbData.name + " --user=" + dbData.user + password + " < " + fileDB + " 2>&1";
			logger.info(command);
			
			if (shell.executeCommand(command, new File(new File(fileDB).getParent())) > 0) {
				throw new RuntimeException("Error exec database script. Info: " + shell.lastInfo());				
			}
			
			(new File(fileDBClean)).delete();
			(new File(fileDB)).delete();
		} catch (Exception e) {
			String error = "I can not deploy Monet database script.";
			logger.error(error + " Message: " + e.getMessage());
			return false;
		}

		return true;
	}

	private Boolean database_oracle() {
		Files file = new Files();
		Zip zip = new Zip();

		String fileDBClean = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbOracleBiEngineClean));
		logger.info("Deploy oracle file: " + fileDBClean);
		try {
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbOracleBiEngineClean), fileDBClean);

			if (Configuration.getValue(Configuration.VAR_Debug).equals("true")) logger.info("\nDatabase url: "+dbData.url+"\nDatabase user: "+dbData.user+"\nDatabase password: "+dbData.password);
			
			Db db = new Db(dbData.url, dbData.user, dbData.password);
			db.executeScript(fileDBClean);
			
			(new File(fileDBClean)).delete();			
		} catch (SQLException SQLe) {
			String error = "I can not connect to database. File: "+ fileDBClean;
			logger.error(error + "\nDatabase url: "+dbData.url+"\nDatabase user: "+dbData.user+"\nMessage:\n" + SQLe.getMessage());
			return false;
		} catch (Exception e) {
			String error = "I can not deploy database.";
			logger.error(error + "\nMessage:\n" + e.getMessage());
			return false;
		}

		String fileDB = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbOracleBiEngine));
		logger.info("Deploy oracle file: " + fileDB);
		try {
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbOracleBiEngine), fileDB);

			Db db = new Db(dbData.url, dbData.user, dbData.password);
			db.executeScript(fileDB);
			
			(new File(fileDB)).delete();			
		} catch (SQLException SQLe) {
			String error = "I can not connect to database. File: "+ fileDB;
			logger.error(error + " Message: " + SQLe.getMessage());
			return false;
		} catch (Exception e) {
			String error = "I can not deploy database.";
			logger.error(error + " Message: " + e.getMessage());
			return false;
		}
		
		return true;
	}

	public Boolean database() {

		try {
			if (dbData.type.equals("mysql")) {
				database_mysql();
			}

			if (dbData.type.equals("oracle")) {
				database_oracle();
			}

		} catch (Exception e) {
			String error = "I can not deploy BiEngine database.";
			logger.error(error + " Message: " + e.getMessage());
			return false;
		}

		return true;
	}
	
	public Boolean deleteHomeDir() {
		Files files = new Files();
		try {
			if (new File(homeDir).exists())
				files.removeDir(homeDir);
		} catch (Exception e) {
			String error = "I can not delete directory: '" + homeDir + "'";
			logger.error(error);
		}

		return true;
	}

	public Boolean deleteWar() {
		Files files = new Files();
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		String fileBiEngineWar = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("path") + "/webapps/"
		    + Configuration.CONST_WarBiEnginePrefix + space + ".war";
		logger.info("Deleting file: " + fileBiEngineWar);
		files.remove(fileBiEngineWar);	
		
		String fileSaikuWar = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("path") + "/webapps/"
		    + Configuration.CONST_WarSaikuPrefix + space + ".war";
		logger.info("Deleting file: " + fileSaikuWar);
		files.remove(fileSaikuWar);
		
		return true;
	}
	
	public Item Reset() {
		ResetBiEngine resetBiEngine = new ResetBiEngine();
		Item result = resetBiEngine.execute(command);
				
		return result;
	}

}
