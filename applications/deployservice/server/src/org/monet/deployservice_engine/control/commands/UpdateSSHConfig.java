package org.monet.deployservice_engine.control.commands;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Files;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;

public class UpdateSSHConfig implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;
	
	private String SSHPort;
	
	public UpdateSSHConfig() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.UpdateSSHConfig);
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
			SSHPort = command.getItem("ssh-port").getContent();
		} catch (Exception e) {
			String error = "SSH port not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidFederation);
			caption.setContent("Error: " + error);
			return false;
		}
		
		return true;
	}
	
	private boolean update() {
		Files files = new Files();
		files.saveTextFile(Configuration.CONST_FileEtcSSHPort, SSHPort);
			
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
