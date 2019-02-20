package org.monet.deployservice_engine.control.commands;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Network;
//import org.monet.deployservice.utils.Shell;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;

public class UpdateApplication implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String url;

	public UpdateApplication() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.UpdateApplication);
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
			url = command.getItem("url").getContent();
		} catch (Exception e) {
			url = Configuration.getValue(Configuration.VAR_UpdateUrl);
		}

		return true;
	}
	
	private boolean update() {
		String fileNameDeployServer = Configuration.CONST_AppName + ".jar";
    String fileDeployServerFull = Configuration.getJarPath() + "/" + fileNameDeployServer;
		String fileNameDeployServerTemp = Configuration.CONST_AppName + ".tmp.jar";
		String fileDestDeployServer = Configuration.getJarPath() + "/" + fileNameDeployServerTemp;
		Boolean restart = true;

		File fileDeployServer = new File(fileDestDeployServer);

		if (url.equals("")) {
			String error = "Incorrect url";
			logger.error(error);
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (!fileDeployServer.exists()) {
			Network network = new Network();
			try {
				logger.info("Download file: " + url + "/" + fileNameDeployServer + ", to " + fileDestDeployServer);
				network.downloadFile(url + "/" + fileNameDeployServer, fileDestDeployServer);
				Files files = new Files();
        
				String md5FileDeployServer = files.getMd5(fileDeployServerFull);
				String md5FileDestDeployServer = files.getMd5(fileDestDeployServer);				
			  logger.info("Md5 file '"+fileDeployServerFull+"': "+md5FileDeployServer + ", Md5 file '"+fileDestDeployServer+"': "+md5FileDestDeployServer);
				if (md5FileDeployServer.equals(md5FileDestDeployServer)) restart = false;

				if (restart) {
				  logger.info("Rename file: " + fileDestDeployServer + ", to " + fileDestDeployServer.replaceAll(".tmp", ""));
				  files.renameFile(fileDestDeployServer, fileDestDeployServer.replaceAll(".tmp", ""));
				} else {
					files.remove(fileDestDeployServer);
				}
			} catch (Exception e) {
				String error = "I can't get file from url: " + url + "/" + fileNameDeployServer + ", to file: " + fileDestDeployServer;
				logger.error(error + "\nDetails: " + e.getMessage());
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		try {
			fileDeployServer.delete();
		} catch (Exception e) {
			String error = "I can't delete file: " + fileDestDeployServer;
			logger.error(error + "\nDetails: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

//		if (Configuration.getValue(Configuration.VAR_MonetVersion).equals("2") || Configuration.getValue(Configuration.VAR_MonetVersion).equals("3")) {	
		if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
			String fileNameManager = Configuration.getValue(Configuration.VAR_UpdateManagerName);
			String fileDestManager = Configuration.getValue(Configuration.VAR_UpdateManagerPath) + "/" + Configuration.getValue(Configuration.VAR_UpdateManagerDestName);
			File fileManager = new File(fileDestManager);

			try {
				fileManager.delete();
			} catch (Exception e) {
				String error = "I can't delete file: " + fileDestManager;
				logger.error(error + "\nDetails: " + e.getMessage());
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}

			if (!fileManager.exists()) {
				Network network = new Network();
				try {
					logger.info("Download file: " + url + "/" + fileNameManager);
					network.downloadFile(url + "/" + fileNameManager, fileDestManager);
				} catch (IOException e) {
					String error = "I can't get file from url: " + url + "/" + fileNameManager + ", to file: " + fileDestManager;
					logger.error(error + "\nDetails: " + e.getMessage());
					status.setContent(ResultIDs.InternalError);
					caption.setContent("Error: " + error);
					return false;
				}
			}
		}

//		Shell shell = new Shell();
//		String fileApp = Configuration.getJarPath() + "/" + Configuration.CONST_AppName + ".sh";
//		shell.executeCommand("sudo " + fileApp, new java.io.File("."));
    if (restart) Configuration.restartApplication();
		
		return true;
	}

	public Item execute(Item command) {
		if (!initialize(command))
			return result;

		if (!update())
			return result;

		return result;
	}

}
