package org.monet.deployservice_engine.control.commands;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Db;
import org.monet.deployservice.utils.DbData;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.utils.Spaces;
import org.monet.deployservice_engine.xml.DocServersXML;
import org.monet.deployservice_engine.xml.ServersXML;
import org.monet.deployservice_engine.configuration.Configuration;

public class ResetMonet implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String container;
	private String server;
	private String space;
	private String dirTemp;
	private String fileWar;
	private String homeDir;
	private String user;
	private String group;
	private String baseUrl;
	private DbData dbData;

	private Boolean replaceTheme;

	private String dirNameSpaceWar;
	private String fileNameSpaceWar;
	private String fileSpaceWar;

	private Boolean ignoreSpaceNotExists;

	public ResetMonet() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.ResetMonet);
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
			replaceTheme = command.getItem("replace-theme").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			replaceTheme = false;
		}

		try {
			baseUrl = command.getItem("base-url").getContent();
		} catch (Exception e) {
			try {
				baseUrl = "";
			} catch (Exception e_ds) {
				String error = "I can not get base-url.";
				logger.error(error);
				status.setContent(ResultIDs.InvalidDbUrl);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		try {
			ignoreSpaceNotExists = command.getItem("ignore-space-not-exists").getContent().toLowerCase().equals("true");
		} catch (Exception e) {
			ignoreSpaceNotExists = false;
		}

		Item itemServer = servers.getItem(server);
		Spaces itemSpaces = new Spaces();
		String pathUrl = "";
		
		if (ignoreSpaceNotExists) {
			Pattern pattern = Pattern.compile(".*://.*/(.*)");
			Matcher matcher = pattern.matcher(baseUrl);
			if (matcher.find()) {
				pathUrl = matcher.group(1);
			}

		} else {
			try {
				Item itemSpace = itemSpaces.getSpace(container, itemServer, space);

				pathUrl = itemSpace.getProperty("base-url-path");
				if (pathUrl.equals("")) {
					Pattern pattern = Pattern.compile(".*://.*/(.*)");
					Matcher matcher = pattern.matcher(baseUrl);
					if (matcher.find()) {
						pathUrl = matcher.group(1);
					}
				}

			} catch (Exception e) {
				String error = "Space " + space + " not exists.";
				ExceptionPrinter exeptionPrinter = new ExceptionPrinter();
				logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
				status.setContent(ResultIDs.InvalidSpace);
				caption.setContent("Error: " + error);
				return false;
			}
		}
		if (pathUrl.equals("")) { pathUrl = space;}

		try {
			Item itemSpace = itemSpaces.getSpace(container, itemServer, space);
			String userlocalContainer = docservers.getItem(servers.getItem(server).getItem(container).getProperty("docserver-server")).getItem(servers.getItem(server).getItem(container).getProperty("docserver-container")).getProperty("name");
			String localContainer = itemSpace.getProperty("docserver-container");

			if (!userlocalContainer.equals(localContainer)) {
				String error = "Container selected and space container not equal (" + userlocalContainer + " != " + localContainer + ").";
				status.setContent(ResultIDs.InvalidContainer);
				caption.setContent("Error: " + error);
				return false;
			}

		} catch (Exception e) {
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

		fileWar = Configuration.getPathWar() + "/" + Configuration.getValue(Configuration.VAR_FileMonetWar);
		String deployDir = servers.getItem(server).getItem(container).getProperty("path") + "/webapps";

		dirNameSpaceWar = pathUrl;
		fileNameSpaceWar = dirNameSpaceWar + ".war";
		fileSpaceWar = deployDir + "/" + fileNameSpaceWar;

		homeDir = servers.getItem(server).getItem(container).getProperty("user-path") + "/" + servers.getItem(server).getItem(container).getProperty("user") + "/." + space;

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

		String fileWebDistXML = dirTemp + "/WEB-INF/web.dist.xml";
		String fileWebXML = dirTemp + "/WEB-INF/web.xml";

		Files files = new Files();
		try {

			files.renameFile(fileWebDistXML, fileWebXML);
			files.replaceTextInFile(fileWebXML, "#monet#", space);

		} catch (Exception e) {
			String error = "Monet file web.xml i can not read.";
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
			if (dbData.type.equals("oracle")) {
				files.replaceTextInFile(fileContextXML, "#resource#", Configuration.getValue(Configuration.VAR_ResourceOracle));
			} else
				files.replaceTextInFile(fileContextXML, "#resource#", Configuration.getValue(Configuration.VAR_ResourceMysql));

			files.replaceTextInFile(fileContextXML, "#url#", dbData.url);
			files.replaceTextInFile(fileContextXML, "#username#", dbData.user);
			files.replaceTextInFile(fileContextXML, "#password#", dbData.password);
		} catch (Exception e) {
			String error = "Monet file context.xml i can not read.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			closeWar();
			return false;
		}

		Zip zip = new Zip();
		zip.compress(fileSpaceWar, dirTemp);
    try {
      files.chmod(fileSpaceWar, "a+r");
    } catch (Exception e) {
      String error = "I can't change file permissions '"+fileSpaceWar+"'.";
      logger.error(error);
      status.setContent(ResultIDs.InternalError);
      caption.setContent(error);
      closeWar();
      return false;
    }

    return true;
	}

	private Boolean replaceTheme() {
		String dirImagesSource = dirTemp + "/WEB-INF/user_data/theme/_images";
		String dirStylesSource = dirTemp + "/WEB-INF/user_data/theme/_styles";
		String dirTemplatesSource = dirTemp + "/WEB-INF/user_data/theme/templates";
		String fileMainCSSSource = dirTemp + "/WEB-INF/user_data/theme/main.css";

		String dirImagesDest = homeDir + "/theme/_images";
		String dirStylesDest = homeDir + "/theme/_styles";
		String dirTemplatesDest = homeDir + "/theme/templates";
		String fileMainCSSDest = homeDir + "/theme/main.css";

		Files files = new Files();
		try {
			if (files.fileExists(dirImagesDest))
				files.removeDir(dirImagesDest);
			files.copyFiles(new File(dirImagesSource), new File(dirImagesDest));

			if (files.fileExists(dirStylesDest))
				files.removeDir(dirStylesDest);
			files.copyFiles(new File(dirStylesSource), new File(dirStylesDest));

			if (files.fileExists(dirTemplatesDest))
				files.removeDir(dirTemplatesDest);
			files.copyFiles(new File(dirTemplatesSource), new File(dirTemplatesDest));

			if (files.fileExists(fileMainCSSDest))
				files.remove(fileMainCSSDest);
			files.copyFiles(new File(fileMainCSSSource), new File(fileMainCSSDest));

			files.chown(homeDir, user, group);
		} catch (Exception e) {
			String error = "Failed to replace theme.";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent(error);
			closeWar();
			return false;
		}

		return true;
	}

	public Item execute(Item command) {
		if (!initialize(command))
			return result;

		if (!openWar())
			return result;

		if (!processWar())
			return result;

		if (replaceTheme && !replaceTheme())
			return result;

		if (!closeWar())
			return result;

		return result;
	}

}
