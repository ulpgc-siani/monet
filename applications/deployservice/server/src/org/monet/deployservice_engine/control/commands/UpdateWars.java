package org.monet.deployservice_engine.control.commands;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.utils.Network;
import org.monet.deployservice.utils.Zip;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;

public class UpdateWars implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	private String url;

	public UpdateWars() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.UpdateWars);
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
			String error = "Url not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidServer);
			caption.setContent("Error: " + error);
			return false;
		}

		return true;
	}

	private boolean update() {
		Files files = new Files();
		Zip zip = new Zip();

		String fileNameMonet = Configuration.getValue(Configuration.VAR_FileMonetWar);
		String fileWarMonet = Configuration.getPathWar() + "/" + fileNameMonet;
		String fileWarMonetTemp = Configuration.getPathWar() + "/" + fileNameMonet + ".tmp";

		if (! files.fileExists(Configuration.getPathWar())) files.makeDir(Configuration.getPathWar());
		
		try {
			if (files.fileExists(fileWarMonetTemp)) files.remove(fileWarMonetTemp);
		} catch (Exception e) {
			String error = "I can't delete file: " + fileWarMonetTemp;
			logger.error(error + "\nDetails: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (! files.fileExists(fileWarMonetTemp)) {
			Network network = new Network();
			
			String urlMonet = url + "/" + fileNameMonet;
			if (url.contains("*")) urlMonet = url.replace("*", fileNameMonet);
			
			try {
				logger.info("Download file: " + urlMonet +" to " + fileWarMonetTemp);
				network.downloadFile(urlMonet, fileWarMonetTemp);
				
				if (zip.isValid(fileWarMonetTemp)) {				
				  if (files.fileExists(fileWarMonet)) files.remove(fileWarMonet);
				  files.renameFile(fileWarMonetTemp, fileWarMonet);
				} else {
					String error = "File: '" + fileWarMonetTemp + "' has errors.";
					logger.error(error);
					status.setContent(ResultIDs.InternalError);
					caption.setContent("Error: " + error);
					return false;					
				}					
			} catch (IOException e) {
				String error = "I can't get war file from url: " + urlMonet;
				logger.error(error + "\nDetails: " + e.getMessage());
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}
		}

		String fileNameDocServer = Configuration.getValue(Configuration.VAR_FileDocServiceWar);
		String fileWarDocServer = Configuration.getPathWar() + "/" + fileNameDocServer;
		String fileWarDocServerTemp = Configuration.getPathWar() + "/" + fileNameDocServer + ".tmp";

		try {
			if (files.fileExists(fileWarDocServerTemp)) files.remove(fileWarDocServerTemp);
		} catch (Exception e) {
			String error = "I can't delete file: " + fileWarDocServerTemp;
			logger.error(error + "\nDetails: " + e.getMessage());
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
			return false;
		}

		if (! files.fileExists(fileWarDocServerTemp)) {
			String urlDocServer = url + "/" + fileNameDocServer;
			if (url.contains("*")) urlDocServer = url.replace("*", fileNameDocServer);
			Network network = new Network();
			try {
				logger.info("Download file: " + urlDocServer + " to " + fileWarDocServerTemp);
				network.downloadFile(urlDocServer, fileWarDocServerTemp);
				
				if (zip.isValid(fileWarDocServerTemp)) {				
				  if (files.fileExists(fileWarDocServer)) files.remove(fileWarDocServer);
				  files.renameFile(fileWarDocServerTemp, fileWarDocServer);
				} else {
					String error = "File: '" + fileWarDocServerTemp + "' has errors.";
					logger.error(error);
					status.setContent(ResultIDs.InternalError);
					caption.setContent("Error: " + error);
					return false;					
				}																	
			} catch (IOException e) {
				String error = "I can't get war file from url: " + urlDocServer;
				logger.error(error + "\nDetails: " + e.getMessage());
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}
		}

  	if ((Configuration.MonetVersionMayor() == 2) || ((Configuration.MonetVersionMayor() == 3) && (Configuration.MonetVersionMinor() == 0))) {
			String fileNameBiEngine = Configuration.CONST_FileBiEngineWar;
			String fileWarBiEngine = Configuration.getPathWar() + "/" + fileNameBiEngine;
			File fileBiEngine = new File(fileWarBiEngine);

			try {
				fileBiEngine.delete();
			} catch (Exception e) {
				String error = "I can't delete file: " + fileWarBiEngine;
				logger.error(error + "\nDetails: " + e.getMessage());
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}

			if (!fileBiEngine.exists()) {
				String urlBiEngine = url + "/" + fileNameBiEngine;
				Network network = new Network();
				try {
					logger.info("Download file: " + urlBiEngine);
					network.downloadFile(url + "/" + fileNameBiEngine, fileWarBiEngine);
				} catch (IOException e) {
					String error = "I can't get war file from url: " + urlBiEngine;
					logger.error(error + "\nDetails: " + e.getMessage());
					status.setContent(ResultIDs.InternalError);
					caption.setContent("Error: " + error);
					return false;
				}
			}

			String fileNameSaiku = Configuration.CONST_FileSaikuWar;
			String fileWarSaiku = Configuration.getPathWar() + "/" + fileNameSaiku;
			File fileSaiku = new File(fileWarSaiku);

			try {
				fileSaiku.delete();
			} catch (Exception e) {
				String error = "I can't delete file: " + fileWarSaiku;
				logger.error(error + "\nDetails: " + e.getMessage());
				status.setContent(ResultIDs.InternalError);
				caption.setContent("Error: " + error);
				return false;
			}

			if (!fileSaiku.exists()) {
				String urlSaiku = url + "/" + fileNameSaiku;
				Network network = new Network();
				try {
					logger.info("Download file: " + urlSaiku);
					network.downloadFile(urlSaiku, fileWarSaiku);
				} catch (IOException e) {
					String error = "I can't get war file from url: " + urlSaiku;
					logger.error(error + "\nDetails: " + e.getMessage());
					status.setContent(ResultIDs.InternalError);
					caption.setContent("Error: " + error);
					return false;
				}
			}
		}

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
