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
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Network;
import org.monet.deployservice.utils.Shell;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;
import org.monet.deployservice_engine.control.commands.OperationIDs;
import org.monet.deployservice_engine.control.commands.ResetMonet;
import org.monet.deployservice_engine.control.commands.ResultIDs;
import org.monet.deployservice_engine.utils.Federations;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;

public class AddPublicServices {

	private String server;
	private String container;
	private String space;
	private String user;
	private String group;
	private DbData dbData;

	private String homeDir;

	private String fileWar;

	private String frontserverUrl;

	private String spaceURI;
	private String spaceSecret;
	private String spaceLabelES;

	private String baseUrl;

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String docserverServer;
	private String docserverContainer;
	private Boolean installSaiku;
	
	private Item federation;
	private String dbFederationPrefix;

	public AddPublicServices() {
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
			if (space.equals(""))
				throw new Exception("Space name is empty.");
		} catch (Exception e) {
			String error = "Space not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidSpace);
			caption.setContent("Error: " + error);
			return false;
		}

		user = servers.getItem(server).getItem(container).getProperty("user");
		if (user.equals("")) {
			String error = "User container Monet not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		group = servers.getItem(server).getItem(container).getProperty("group");
		if (group.equals("")) {
			String error = "Group container Monet not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		try {
			frontserverUrl = servers.getItem(server).getItem(container).getProperty("frontserver-url");

			Network network = new Network();
			frontserverUrl = frontserverUrl.replaceAll("#deploy-server-ip#", network.getCurrentEnvironmentNetworkIp(Configuration.getValue(Configuration.VAR_SendUDPInterface)));
			if (space.equals("ROOT"))
				frontserverUrl = frontserverUrl.replaceAll("#space#", "");
			else
				frontserverUrl = frontserverUrl.replaceAll("#space#", space);
		} catch (Exception e) {
			String error = "Frontserver url not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidFrontserverUrl);
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

			if (space.equals("ROOT"))
				dbData.user = servers.getItem(server).getItem(container).getProperty("db-default-user");

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

			if (space.equals("ROOT"))
				dbData.password = servers.getItem(server).getItem(container).getProperty("db-default-password");
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

			if (space.equals("ROOT"))
				dbData.name = dbPrefix + servers.getItem(server).getItem(container).getProperty("db-default-name");

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

		installSaiku = false;
		try {
			installSaiku = command.getItem("install-saiku").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			installSaiku = false;
		}

		try {
			baseUrl = command.getItem("base-url").getContent();
		} catch (Exception e) {
			try {
				baseUrl = servers.getItem(server).getItem(container).getProperty("frontserver-url");
			} catch (Exception e_ds) {
				String error = "I can not get base-url.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbUrl);
				caption.setContent("Error: " + error);
				return false;
			}
		}
		if ( ((baseUrl.length() <= "http://".length()) || (!baseUrl.substring(0, "http://".length()).toLowerCase().equals("http://"))) && ((baseUrl.length() <= "https://".length()) || (!baseUrl.substring(0, "https://".length()).toLowerCase().equals("https://")))) {
			String error = "Base url (" + baseUrl + ") must begin by 'http://' or 'https://' and have a domain";
			logger.error(error);
			status.setContent(ResultIDs.InvalidDbUrl);
			caption.setContent("Error: " + error);
			return false;
		}

		Network network = new Network();
		baseUrl = baseUrl.replaceAll("#deploy-server-ip#", network.getCurrentEnvironmentNetworkIp(Configuration.getValue(Configuration.VAR_SendUDPInterface)));
		baseUrl = baseUrl.replaceAll("#space#", space);

		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());
		if ((docservers.getItem(docserverServer) == null)) {
			String error = "DocService server: '" + docserverServer + "' not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if ((docservers.getItem(docserverServer).getItem(docserverContainer) == null)) {
			String error = "DocService container: '" + docserverContainer + "' not found.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (Configuration.MonetVersionMayor() >= 3) {
			try {
				spaceURI = command.getItem("space-uri").getContent();
			} catch (Exception e) {
				try {
					spaceURI = Configuration.getValue(Configuration.VAR_MonetSpaceURI);
				} catch (Exception e_ds) {
					String error = "I can not get space URI.";
					logger.error(error);
					status.setContent(ResultIDs.InvalidSpaceURI);
					caption.setContent("Error: " + error);
					return false;
				}
			}

			try {
				spaceSecret = command.getItem("space-secret").getContent();
			} catch (Exception e) {
				try {
					spaceSecret = Configuration.getValue(Configuration.VAR_MonetSpaceSecret);
				} catch (Exception e_ds) {
					String error = "I can not get space secret.";
					logger.error(error);
					status.setContent(ResultIDs.InvalidSpaceSecret);
					caption.setContent("Error: " + error);
					return false;
				}
			}

			try {
				spaceLabelES = command.getItem("space-label-es").getContent();
			} catch (Exception e) {
				String error = "I can not get space label spanish.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidSpaceLabelES);
				caption.setContent("Error: " + error);
				return false;
			}
			
			String federationName = Configuration.getValue(Configuration.VAR_FederationDefault, Configuration.CONST_FederationDefault);
			try {
				federationName = command.getItem("federation").getContent();			
			} catch (Exception e) {
				String error = "I can not get federation name.";
				logger.warn(error);
			}
			
			logger.info("Federation name: " + federationName);
			Item itemServer = servers.getItem(server);
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

			dbFederationPrefix = servers.getItem(server).getItem(container).getProperty("db-federation-prefix");
			if (dbFederationPrefix == null) dbFederationPrefix = "";			
		}
		
		Item itemServer = servers.getItem(server);
		Spaces itemSpaces = new Spaces();

		Item spaces = itemSpaces.getSpaces(container, itemServer);
		if ((spaces.getCount() > 0) && (dbData.type.equals("oracle"))) {
			String error = "I can not add more spaces.";
			logger.error(error);
			status.setContent(ResultIDs.TooSpaces);
			caption.setContent("Error: " + error);
			return false;						
		}		

		fileWar = Configuration.getPathWar() + "/" + Configuration.getValue(Configuration.VAR_FileMonetWar);
		homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user") + "/." + space;

		return true;
	}

	private Boolean updateConfigMonet() {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());
		DocServersXML docserversXML = new DocServersXML();
		Item docservers = docserversXML.unserialize(Configuration.getDocServersConfig());

		String homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user") + "/." + space;

		String fileName = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetKernel).replaceAll(".dist", "");

		Properties properties = new Properties();
		try {
			properties.loadFromXML(new FileInputStream(fileName));
		
			if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() < 2))) {
				properties.setProperty("MONET_BUSINESS_UNIT_DATABASE_TYPE", dbData.type);				
			} else {
				properties.setProperty("Jdbc.Type", dbData.type);
			}
			
			properties.setProperty("MONET_USER_DATA_DIR", homeDir);

			String urlDocServer = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("url") + "/" + Configuration.CONST_WarDocServerPrefix + space;
			properties.setProperty("MONET_COMPONENT_DOCUMENTS_MONET_URL", urlDocServer);
			properties.setProperty("MONET_SERVICES_BASE_URL", baseUrl);

			
			if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
			  String urlBiEngine = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("url") + "/" + Configuration.CONST_WarBiEnginePrefix + space;
			  properties.setProperty("MONET_COMPONENT_DATAWAREHOUSE_URL", urlBiEngine + "/servlet/datawarehouse/");
			}				

			String urlSaiku = docservers.getItem(docserverServer).getItem(docserverContainer).getProperty("url") + "/" + Configuration.CONST_WarSaikuPrefix + space;

			if (Configuration.MonetVersionMayor() == 2) {
				properties.setProperty("MONET_COMPONENT_OLAP_URL", urlSaiku);
			} else {
				if ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0)) {
					properties.setProperty("MONET_COMPONENT_ANALYTICS_DYNAMIC_URL", urlSaiku);
					
				}
			}

			String monetModelType = Configuration.getValue(Configuration.VAR_MonetModelType);
			if (monetModelType.equals(""))
				monetModelType = "OFFICE";
			properties.setProperty("MONET_MODEL_TYPE", monetModelType);

			properties.setProperty("MONET_CERTIFICATE_FILENAME", Configuration.getValue(Configuration.VAR_FileCertificateSpace));
			properties.setProperty("MONET_CERTIFICATE_PASSWORD", Configuration.getValue(Configuration.VAR_FileCertificateSpacePassword));

			properties.setProperty("MONET_MAIL_ADMIN_HOST", Configuration.getValue(Configuration.VAR_MailAdminSpaceHost));
			properties.setProperty("MONET_MAIL_ADMIN_FROM", Configuration.getValue(Configuration.VAR_MailAdminSpaceFrom));
			properties.setProperty("MONET_MAIL_ADMIN_TO", Configuration.getValue(Configuration.VAR_MailAdminSpaceTo));
			properties.setProperty("MONET_MAIL_ADMIN_USERNAME", Configuration.getValue(Configuration.VAR_MailAdminSpaceUsername));
			properties.setProperty("MONET_MAIL_ADMIN_PASSWORD", Configuration.getValue(Configuration.VAR_MailAdminSpacePassword));
			properties.setProperty("MONET_MAIL_ADMIN_PORT", Configuration.getValue(Configuration.VAR_MailAdminSpacePort));
			properties.setProperty("MONET_MAIL_ADMIN_SECURE", Configuration.getValue(Configuration.VAR_MailAdminSpaceSecure));

			if (Configuration.MonetVersionMayor() >= 3) {
				properties.setProperty("MONET_CERTIFICATE_FILENAME", "businessunit-" + space + ".p12");
				if (baseUrl.substring(0, "https://".length()).toLowerCase().equals("https://"))				
					properties.setProperty("MONET_USE_SSL", "true");
				
				String SpacesDir = Configuration.getValue(Configuration.VAR_DataWareHouseDir);
				if (SpacesDir.equals("")) SpacesDir = Configuration.getValue(Configuration.VAR_SpacesDir);			
				if (! SpacesDir.equals("")) {
					String dataWareHouseDir =  SpacesDir + "/" + space + "/datawarehouse";
				  properties.setProperty("MONET_DATAWAREHOUSE_DIR", dataWareHouseDir);
//					Files files = new Files();
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

		return true;
	}

	private Boolean updateConfigSetupMonet() {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());

		String homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user") + "/." + space;

		String fileName = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigSetup).replaceAll(".dist", "");

		Properties properties = new Properties();
		try {
			properties.loadFromXML(new FileInputStream(fileName));

			properties.setProperty("SETUP_API_KEY", space);
			properties.setProperty("SETUP_API_SECRET", Configuration.getValue(Configuration.VAR_FederationServiceGridSecret));
			properties.setProperty("SETUP_FEDERATION_SERVICE", Configuration.getValue(Configuration.VAR_FederationServiceGridHost));
			properties.setProperty("SETUP_FEDERATION_SOCKET_PORT", Configuration.getValue(Configuration.VAR_FederationServiceGridSocketPort));
			properties.setProperty("SETUP_FEDERATION_SERVICE_PORT", Configuration.getValue(Configuration.VAR_FederationServiceGridServicePort));
			properties.setProperty("SETUP_FEDERATION_BASE_PATH", Configuration.getValue(Configuration.VAR_FederationServiceGridPath));
			properties.setProperty("SETUP_FEDERATION_SSL_MODE", Configuration.getValue(Configuration.VAR_FederationServiceGridSsl));

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

		return true;
	}

	private Boolean updateConfigDeployServer() {
		ServersXML serversXML = new ServersXML();
		Item servers = serversXML.unserialize(Configuration.getServersConfig());
		Properties properties = new Properties();

		properties.clear();
		String containerPathUser = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user");
		String fileDeployConfigName = containerPathUser + "/." + space + "/configuration/" + Configuration.CONST_CONFIG_FILE;
		if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
			fileDeployConfigName = containerPathUser + "/." + space + "/" + Configuration.CONST_CONFIG_FILE;			
		}

		try {
			// TODO: Los datos de la base de datos deben ser obtenidos del contexto.
			properties.setProperty("MONET_DB_URL", dbData.url);
			properties.setProperty("DOCSERVICE_DB_URL", dbData.url);

			properties.setProperty("FRONTSERVER_URL", frontserverUrl);
			properties.setProperty("URL", baseUrl);
			properties.setProperty("DOCSERVICE_SERVER", docserverServer);
			properties.setProperty("DOCSERVICE_CONTAINER", docserverContainer);

			if (installSaiku)
				properties.setProperty("SAIKU_INSTALLED", "true");
			else
				properties.setProperty("SAIKU_INSTALLED", "false");

			if (Configuration.MonetVersionMayor() >= 3) {
				properties.setProperty("SPACE_LABEL_ES", spaceLabelES);
				properties.setProperty("SPACE_URI", spaceURI);
				properties.setProperty("SPACE_SECRET", spaceSecret);
				properties.setProperty("FEDERATION_NAME", federation.getProperty("id"));
			}

			properties.storeToXML(new FileOutputStream(fileDeployConfigName), null);
		} catch (FileNotFoundException e) {
			String error = "File not found: '" + fileDeployConfigName;
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		} catch (IOException e) {
			String error = "I cant not read the file: '" + fileDeployConfigName;
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		return true;
	}

	private Boolean updateConfig() {

		if (!updateConfigMonet())
			return false;
		
		if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
  		if (!updateConfigSetupMonet())
	  		return false;
		}
		
		if (!updateConfigDeployServer())
			return false;

		return true;
	}

	private Boolean configure() {
		Files files = new Files();

		File fileHomeDir = new File(homeDir);

		if (fileHomeDir.exists()) {
			String error = "Space config directory exists.";
			logger.error(error);
			status.setContent(ResultIDs.ExistsSpace);
			caption.setContent("Error: " + error);
			return false;
		}
		fileHomeDir.mkdir();
			
		if (Configuration.MonetVersionMayor() >= 3) {
			String SpacesDir = Configuration.getValue(Configuration.VAR_DataWareHouseDir);
			if (SpacesDir.equals("")) SpacesDir = Configuration.getValue(Configuration.VAR_SpacesDir);			
			if (! SpacesDir.equals("")) {
			  String spaceDir = SpacesDir + "/" + space;
				String dataWareHouseDir =  spaceDir + "/datawarehouse";
				String businessModelDir =  spaceDir + "/businessmodel";
				String certificatesDir = spaceDir + "/certificates";
				String imagesDir = spaceDir + "/images";
				String themeDir = spaceDir + "/theme";
				
        try {
          files.makeDir(spaceDir, user, group);

          files.makeDir(dataWareHouseDir, user, group);

          files.makeDir(businessModelDir, user, group);
          files.ln(businessModelDir, homeDir + "/businessmodel");                        

          files.makeDir(certificatesDir, user, group);
          files.ln(certificatesDir, homeDir + "/certificates");

          files.makeDir(imagesDir, user, group);
          files.ln(imagesDir, homeDir + "/images");

          files.makeDir(themeDir, user, group);
          files.ln(themeDir, homeDir + "/theme");
          
    		} catch (Exception e) {
    			String error = "I can not deploy datawarehouse dir: " + dataWareHouseDir;      			
    			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();					
    			logger.error(error + " Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
    			status.setContent(ResultIDs.InternalError);
    			caption.setContent("Error: " + error);
    			return false;
    		}
			} else {
        try {
          files.makeDir(homeDir + "/businessmodel", user, group);
        } catch (Exception e) {
          String error = "I can not make businessmodel dir: " + homeDir + "/businessmodel";
          ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
          logger.error(error + " Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
          status.setContent(ResultIDs.InternalError);
          caption.setContent("Error: " + error);
          return false;
        }
			}
		}
		
		Zip zip = new Zip();
		try {
			String dirConfig = Configuration.getValue(Configuration.VAR_DirConfigMonet);

			String fileConfigMonetKernel = Configuration.getValue(Configuration.VAR_FileConfigMonetKernel);
			String fileConfigMonetKernelDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetKernel).replaceAll(".dist", "");
			String fileLog4jMonet = Configuration.getValue(Configuration.VAR_FileLog4jMonet);
			String fileLog4jMonetDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileLog4jMonet).replaceAll(".dist", "");

			String fileConfigMonetBackoffice = Configuration.getValue(Configuration.VAR_FileConfigMonetBackoffice);
			String fileConfigMonetBackofficeDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetBackoffice).replaceAll(".dist", "");
			String fileConfigMonetBoot = Configuration.getValue(Configuration.VAR_FileConfigMonetBoot);
			String fileConfigMonetBootDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetBoot).replaceAll(".dist", "");
			String fileConfigMonetConsole = Configuration.getValue(Configuration.VAR_FileConfigMonetConsole);
			String fileConfigMonetConsoleDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetConsole).replaceAll(".dist", "");
			String fileConfigMonetManager = Configuration.getValue(Configuration.VAR_FileConfigMonetManager);
			String fileConfigMonetManagerDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetManager).replaceAll(".dist", "");

			String fileConfigMonetCache = Configuration.getValue(Configuration.VAR_FileConfigMonetCache);
			String fileConfigMonetCacheDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetCache).replaceAll(".dist", "");

			String fileConfigMonetAnalytics = Configuration.getValue(Configuration.VAR_FileConfigMonetAnalytics);
			String fileConfigMonetAnalyticsDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileConfigMonetAnalytics).replaceAll(".dist", "");

			String fileConfigMonetExplorer = Configuration.getValue(Configuration.VAR_FileConfigMonetExplorer, "configuration/explorer.dist.config");
			String fileConfigMonetExplorerDest = homeDir + "/" + fileConfigMonetExplorer.replaceAll(".dist", "");
			
			String fileConfigSetup = Configuration.getValue(Configuration.VAR_FileConfigSetup);
			String fileConfigSetupDest = Configuration.getValue(Configuration.VAR_FileConfigSetup).replaceAll(".dist", "");

			String fileCertificate = Configuration.getPathCertificates() + "/" + Configuration.getValue(Configuration.VAR_FileCertificateSpace);
			String fileCertificateDest = homeDir + "/" + Configuration.getValue(Configuration.VAR_FileCertificateSpace);

			zip.unCompressOnlyFile(fileWar, dirConfig, homeDir);

      if (Configuration.MonetVersionMayor() >= 3) {
        String SpacesDir = Configuration.getValue(Configuration.VAR_DataWareHouseDir);
        if (SpacesDir.equals("")) SpacesDir = Configuration.getValue(Configuration.VAR_SpacesDir);
        if (!SpacesDir.equals("")) {
          String spaceDir = SpacesDir + "/" + space;
          files.chown(spaceDir, user, group);
        }
      }

			files.renameFile(homeDir + "/" + fileConfigMonetKernel, fileConfigMonetKernelDest);
			files.chmod(fileConfigMonetKernelDest, "g+w");

						
			if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
			  files.renameFile(homeDir + "/" + fileConfigMonetBackoffice, fileConfigMonetBackofficeDest);
			  files.renameFile(homeDir + "/" + fileConfigMonetBoot, fileConfigMonetBootDest);
			  files.renameFile(homeDir + "/" + fileConfigMonetConsole, fileConfigMonetConsoleDest);
			  files.renameFile(homeDir + "/" + fileConfigMonetManager, fileConfigMonetManagerDest);

			  files.remove(fileCertificateDest);
			}

			files.renameFile(homeDir + "/" + fileLog4jMonet, fileLog4jMonetDest);
			files.replaceTextInFile(fileLog4jMonetDest, "#config_path#", homeDir);
			
			if (Configuration.getValue(Configuration.VAR_AddSocketLog, "false").equals("true")) {
			  files.replaceTextInFile(fileLog4jMonetDest, "<entry key=\"log4j.rootCategory\">INFO, CA</entry>", "<entry key=\"log4j.rootCategory\">INFO, CA, logginghub</entry>");
			  files.replaceTextInFile(fileLog4jMonetDest, "<!--#appender_socket#-->", "<entry key=\"log4j.appender.logginghub\">com.vertexlabs.logging.log4j.SocketAppender</entry>\n  <entry key=\"log4j.appender.logginghub.host\">localhost</entry>\n  <entry key=\"log4j.appender.logginghub.sourceApplication\">monet</entry>\n");
			}
			

			if (Configuration.MonetVersionMayor() >= 3) {
				files.renameFile(homeDir + "/" + fileConfigMonetCache, fileConfigMonetCacheDest);
				files.replaceTextInFile(fileConfigMonetCacheDest, "#space_dir#", homeDir);

				if (! fileConfigMonetAnalytics.equals("")) 
					files.renameFile(homeDir + "/" + fileConfigMonetAnalytics, fileConfigMonetAnalyticsDest);				

				if (! fileConfigMonetExplorer.equals("") && files.fileExists(homeDir + "/" + fileConfigMonetExplorer)) {
					files.renameFile(homeDir + "/" + fileConfigMonetExplorer, fileConfigMonetExplorerDest);				
				  files.chmod(fileConfigMonetExplorerDest, "g+w");
				}
				
				if (((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() > 0)) || (Configuration.MonetVersionMayor() > 3)) {
					files.makeDir(homeDir + "/certificates");
				}
				
				String FileNameSSL = "businessunit-" + space;
				if (((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() > 0)) || (Configuration.MonetVersionMayor() > 3)) {
				  FileNameSSL = homeDir + "/certificates/businessunit-" + space;
				}
				
				String command = "";
				Shell shell = new Shell();
				Integer result;

				logger.info("Generate space key");
				command = "openssl genrsa -des3 -passout pass:"+federation.getProperty("password-key")+" -out " + FileNameSSL + ".key 4096";
        result = shell.executeCommand(command, new File(new File(FileNameSSL).getParent()));
        files.chown(FileNameSSL + ".key", user, group);
        if (result > 0) {
    			String error = "I can not generate certificates.";   			
    			status.setContent(ResultIDs.InvalidCertificate);
    			caption.setContent("Error: " + error);
    			return false;        	
        }
			
				logger.info("Generate space certificate");
				command = "openssl req -new -passin pass:"+federation.getProperty("password-key")+" -key " + FileNameSSL + ".key -out " + FileNameSSL + ".csr -subj \"/C=ES/ST=Las Palmas de Gran Canaria/L=Tafira/O=Gisc/OU=Grupo de ingenieria del software y el conocimiento/CN=business_unit|" + space + "/emailAddress=info@openmonet.com/\"";
				result = shell.executeCommand(command, new File(new File(FileNameSSL).getParent()));
        files.chown(FileNameSSL + ".csr", user, group);
        if (result > 0) {
    			String error = "I can not generate certificates.";   			
    			status.setContent(ResultIDs.InvalidCertificate);
    			caption.setContent("Error: " + error);
    			return false;        	
        }
				
				logger.info("Signing certificate");
				command = "openssl x509 -req -passin pass:"+federation.getProperty("password-ca")+" -days 3650 -CA " + federation.getProperty("certificate") + " -CAkey " + federation.getProperty("key") + " -set_serial 01 -in " + FileNameSSL + ".csr -out " + FileNameSSL + ".crt";
				result = shell.executeCommand(command, new File(new File(FileNameSSL).getParent()));
        files.chown(FileNameSSL + ".crt", user, group);
        if (result > 0) {
    			String error = "I can not generate certificates.";   			
    			status.setContent(ResultIDs.InvalidCertificate);
    			caption.setContent("Error: " + error);
    			return false;        	
        }

				logger.info("Export certificate");
				command = "openssl pkcs12 -export -passin pass:"+federation.getProperty("password-key")+" -passout pass:"+federation.getProperty("password-key")+" -out " + FileNameSSL + ".p12 -inkey " + FileNameSSL + ".key -in " + FileNameSSL + ".crt -certfile " + federation.getProperty("certificate");
				result = shell.executeCommand(command, new File(new File(FileNameSSL).getParent()));
        files.chown(FileNameSSL + ".p12", user, group);
        if (result > 0) {
    			String error = "I can not generate certificates.";   			
    			status.setContent(ResultIDs.InvalidCertificate);
    			caption.setContent("Error: " + error);
    			return false;        	
        }

				String federationPort = files.loadTextFile(Configuration.CONST_FileEtcFederationPort);
				String federationDomain = files.loadTextFile(Configuration.CONST_FileEtcFederationDomainOld);
				if (federation.getProperty("id") != null) {
					if (! federation.getProperty("domain").equals("")) federationDomain = federation.getProperty("domain"); 
					if (! federation.getProperty("port").equals("")) federationPort = federation.getProperty("port");
				}
				
				logger.info("Insert unit configuration to federation: " + federationDomain + ":" + federationPort);
				
				String federationDbUrl = dbData.url;
				String federationDbUser = dbData.user;
				String federationDbPassword = dbData.password;
				String federationDbName = dbFederationPrefix+federation.getProperty("id");

				
				if ((federation.getProperty("db-url") != null) && (! federation.getProperty("db-url").equals(""))) {
					federationDbUrl = federation.getProperty("db-url");
					federationDbUser = federation.getProperty("db-user");
					federationDbPassword = federation.getProperty("db-password");
					federationDbName = "";
				}
				Db db = new Db(federationDbUrl, federationDbUser, federationDbPassword, federationDbName);
							
				String sentence = "INSERT INTO `fs$business_units` (`name`,`label`,`type`,`uri`,`secret`,`enable`) VALUES (\"" + space + "\", \"" + spaceLabelES + "\", \"member\", \"" + baseUrl + "\",\"1234\",\"1\")";
				db.executeSentence(sentence);			
				
			} else {
				files.copy(fileCertificate, fileCertificateDest);
			}

			if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
  			files.renameFile(homeDir + "/" + fileConfigSetup, homeDir + "/" + fileConfigSetupDest);
			}	

			files.makeDir(homeDir + "/logs");
			files.chown(homeDir, user, group);
		} catch (Exception e) {
			String error = "I can not deploy Monet configuration.";
			
			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();					
			logger.error(error + " Details: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (!updateConfig())
			return false;
		if (!database())
			return false;

		return true;
	}

	private Boolean database_mysql() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (dbData.create) {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			try {
        Connection connectionAdmin = null;
        try {
          connectionAdmin = (Connection) DriverManager.getConnection(dbData.urlAdmin, dbData.user, dbData.password);

          Statement stAdmin = null;
          try {
            stAdmin = (Statement) connectionAdmin.createStatement();
            stAdmin.executeUpdate("CREATE DATABASE " + dbData.name);
          } finally {
            if (stAdmin != null) stAdmin.close();
          }

          logger.info("Created database: " + dbData.name);
        } finally {
          if (connectionAdmin != null) connectionAdmin.close();
        }

			} catch (SQLException e1) {
				String error = "I can not create database(" + dbData.type + "): " + dbData.name;
				logger.info(error);
      }
		}

		Files file = new Files();
		Zip zip = new Zip();
		try {
			String fileDBClean = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbMysqlMonetClean));
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbMysqlMonetClean), fileDBClean);

			String fileDB = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbMysqlMonet));
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbMysqlMonet), fileDB);

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
		Boolean result = true;

		return result;
	}

	private Boolean database_oracle() {
		if (dbData.create) {
		}

		Files file = new Files();
		Zip zip = new Zip();

		String fileDBClean = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbOracleMonetClean));
		logger.info("Deploy oracle file: " + fileDBClean);
		try {
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbOracleMonetClean), fileDBClean);

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

		String fileDB = homeDir + "/" + file.baseName(Configuration.getValue(Configuration.VAR_FileDbOracleMonet));
		logger.info("Deploy oracle file: " + fileDB);
		try {
			zip.unCompressOnlyFile(fileWar, Configuration.getValue(Configuration.VAR_FileDbOracleMonet), fileDB);

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

		} catch (Exception e) {
			String error = "I can not deploy Monet database.";
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
			ResetMonet resetMonet = new ResetMonet();

			Item iIgnoreSpaceNotExists = new Item();
			iIgnoreSpaceNotExists.setProperty("id", "ignore-space-not-exists");
			iIgnoreSpaceNotExists.setContent("true");
			command.addItem(iIgnoreSpaceNotExists);

			Item status = null;
			status = resetMonet.execute(command);
			if (!status.getItem("status").getContent().equals("ok")) {
				String error = "I can't reset Monet in space: " + space;
				logger.error(error);
				return status;
			}							
		}		

		return result;
	}

}
