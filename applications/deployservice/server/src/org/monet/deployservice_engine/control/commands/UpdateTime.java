package org.monet.deployservice_engine.control.commands;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.Shell;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.configuration.Configuration;

public class UpdateTime implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;


	private String time;
	
	public UpdateTime() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.UpdateTime);
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
			time = command.getItem("time").getContent();
			if (time.equals(""))
				throw new Exception("Time is empty.");
		} catch (Exception e) {
			String error = "Time not found in parameters.";
			logger.error(error);
			status.setContent(ResultIDs.InvalidTime);
			caption.setContent("Error: " + error);
			return false;
		}
		
		return true;
	}
	
	private boolean update() {
		logger.info("Update system time");

	  DateFormat dateFormat = new SimpleDateFormat("MMddHHmmyyyy.ss");
	  Date date = new Date();
	  String currentDate = dateFormat.format(date);			
		logger.info("Current time: " + currentDate);
		logger.info("New time: " + time);

		if (! currentDate.equals(time)) {
      String command = "date " + time;
      Shell shell = new Shell();
      shell.executeCommand(command, new File(Configuration.getPath()));
    } else {
      logger.info("The time is same.");
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
