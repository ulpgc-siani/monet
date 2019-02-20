package org.monet.deployservice_engine.control.commands.tasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.xml.ServersXML;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.OperationIDs;
import org.monet.deployservice_engine.control.commands.ResultIDs;
import org.monet.deployservice_engine.utils.Federations;
import org.monet.deployservice_engine.utils.RestartServers;
import org.monet.deployservice_engine.utils.Spaces;

public class DeletePublicServices {

	private String server;
	private String container;
	private String space;

	private Logger logger;
	private String homeDir;
	private DbData dbData;

	private Item result;
	private Item caption;
	private Item status;

	private String pathUrl;
	private String federationName;
	private Item federation;

	private String dbFederationPrefix;

	public DeletePublicServices() {
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

		String dbPrefixMonet = servers.getItem(server).getItem(container).getProperty("db-prefix");

		dbData.create = true;
		try {
			dbData.name = command.getItem("db-name-monet").getContent();
			dbData.create = false;
		} catch (Exception e) {
			dbData.name = dbPrefixMonet + space;
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

		Item itemServer = servers.getItem(server);
		Spaces itemSpaces = new Spaces();
		try {
			Item itemSpace = itemSpaces.getSpace(container, itemServer, space);

			if (itemSpace == null) {
				pathUrl = space;
			} else
				pathUrl = itemSpace.getProperty("base-url-path");
			if (pathUrl.equals(""))
				pathUrl = space;

			if (itemSpace == null) {
				federationName = Configuration.getValue(Configuration.VAR_FederationDefault, Configuration.CONST_FederationDefault);
			} else 
				federationName = itemSpace.getProperty("federation");
				
			if (federationName.equals(""))
				federationName = Configuration.getValue(Configuration.VAR_FederationDefault, Configuration.CONST_FederationDefault);

		} catch (Exception e) {
			String error = "Space " + space + " not exists.";

			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
			logger.warn(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
			status.setContent(ResultIDs.InvalidContainer);
			caption.setContent("Error: " + error);
			return false;
		}

		if (Configuration.MonetVersionMayor() >= 3) {
			dbFederationPrefix = servers.getItem(server).getItem(container).getProperty("db-federation-prefix");
		}
		if (dbFederationPrefix == null)
			dbFederationPrefix = "";
		
		logger.info("Federation name: " + federationName);
		Federations itemFederations = new Federations();
		try {
			federation = itemFederations.getFederation(itemServer, container, federationName);
			
			if (federation.getProperty("id") == null)
				throw new Exception("Federation not exists.");				
		} catch (Exception e) {
			String error = "I can not get federation '"+federationName+"'.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidFederation);
			caption.setContent("Error: " + error);
			return false;			
		}
		
		homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user") + "/." + space;

		return true;
	}

	private Boolean deleteHomeDir() {

		Files files = new Files();
		String sentence = "";
		try {
			files.removeDir(homeDir);

			if (Configuration.MonetVersionMayor() >= 3) {
				logger.info("Delete unit configuration to federation");
				
				String federationDbUrl = federation.getProperty("db-url");
				String federationDbUser = federation.getProperty("db-user");
				String federationDbPassword = federation.getProperty("db-password");
				String federationDbName = "";
				
				if (federationDbUrl.equals("")) {
					federationDbUrl = dbData.url;
					federationDbUser = dbData.user;
					federationDbPassword = dbData.password;
					federationDbName = dbFederationPrefix+federation.getProperty("id");
				}
				
				Db db = new Db(federationDbUrl, federationDbUser, federationDbPassword, federationDbName);

				sentence = "SELECT id FROM `fs$business_units` WHERE `name`=\"" + space + "\"";
				String id = db.executeSentence(sentence);

				if (!id.equals("")) {
					sentence = "DELETE FROM `fs$business_units` WHERE `id`=\"" + id + "\"";
					db.executeSentence(sentence);

					sentence = "DELETE FROM `fs$feeders` WHERE `id_business_unit`=\"" + id + "\"";
					db.executeSentence(sentence);

					sentence = "DELETE FROM `fs$services` WHERE `id_business_unit`=\"" + id + "\"";
					db.executeSentence(sentence);
				}
			}
		} catch (Exception e) {
			String error = "I can not delete space. Details: " + e.getMessage() + ". SQL: " + sentence;
			logger.error(error);
		}

		String SpacesDir = Configuration.getValue(Configuration.VAR_DataWareHouseDir);
		if (SpacesDir.equals("")) SpacesDir = Configuration.getValue(Configuration.VAR_SpacesDir);					
		if (!SpacesDir.equals("")) {
//			String dataWareHouseDir = SpacesDir + "/" + space + "/datawarehouse";
			String spaceDir = SpacesDir + "/" + space;
			files.removeDir(spaceDir);
		}

		return true;
	}

	private Boolean deleteDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		if (Configuration.getValue(Configuration.VAR_SpaceCleanForceDeleteDB, "false").equals("true")) {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			try {
				Connection connectionAdmin;
				connectionAdmin = (Connection) DriverManager.getConnection(dbData.urlAdmin, dbData.user, dbData.password);
				Statement stAdmin = (Statement) connectionAdmin.createStatement();
				stAdmin.executeUpdate("DROP DATABASE " + dbData.name);
				logger.info("Drop database: " + dbData.name);
				connectionAdmin.close();
			} catch (SQLException e1) {
				String error = "I can not drop database(" + dbData.type + "): " + dbData.name;
				logger.info(error);
				return false;
			}
		}

		return true;
	}

	private Boolean deleteWar() {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		String deployDir = servers.getItem(server).getItem(container).getProperty("path") + "/webapps";
		String fileSpaceWar = deployDir + "/" + pathUrl + ".war";

		logger.info("Deleting file: " + fileSpaceWar);

		Files files = new Files();
		files.remove(fileSpaceWar);

		return true;
	}
	
	@SuppressWarnings("static-access")
  private Boolean startServer() {
		RestartServers resetServers = new RestartServers();						
	
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e1) {
		}

		try {
			resetServers.container_monet_start(server, container);
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

  private Boolean stopServer() {
		RestartServers resetServers = new RestartServers();						
	
		try {
			resetServers.container_monet_stop(server, container);
		} catch (Exception e) {
			String error = "I can not stop container.";
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

//		if (SystemOS.isWindows() && (!stopServer()))
  	if (!stopServer())
			return result;
		
		if (!deleteHomeDir())
			return result;

//		if (SystemOS.isWindows() && (!startServer()))
		if (!startServer())
			return result;
		
		try {
			if (!deleteDatabase())
				return result;
		} catch (Exception e) {
			String error = "I can not drop Monet database.";
			logger.error(error + " Message: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return result;
		}

		return result;
	}

}
