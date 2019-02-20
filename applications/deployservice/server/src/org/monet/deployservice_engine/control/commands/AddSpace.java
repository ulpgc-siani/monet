package org.monet.deployservice_engine.control.commands;

import org.apache.log4j.Logger;
import org.monet.deployservice.utils.ExceptionPrinter;
import org.monet.deployservice.xml.Item;
import org.monet.deployservice_engine.control.commands.tasks.AddLocalServices;
import org.monet.deployservice_engine.control.commands.tasks.AddPublicServices;

public class AddSpace implements ICommand {

	private Logger logger;
	private Item result;
	private Item caption;
	private Item status;

	public AddSpace() {
		logger = Logger.getLogger(this.getClass());
		result = new Item();
		result.setProperty("id", OperationIDs.AddSpace);
		caption = new Item();
		caption.setProperty("id", "caption");
		result.addItem(caption);
		status = new Item();
		status.setProperty("id", "status");
//		status.setContent(ResultIDs.InternalError);
		status.setContent("ok");		
		result.addItem(status);
	}

	public Item execute(Item command) {

		try {
			AddPublicServices addSpaceMonet = new AddPublicServices();
			Item resultMonet = addSpaceMonet.execute(command);

			if (resultMonet.getItem("status").getContent().equals("ok")) {
				AddLocalServices addSpaceDocServer = new AddLocalServices();

				Item status = addSpaceDocServer.execute(command);
				if (!status.getItem("status").getContent().equals("ok")) {
					String error = "I can't add docservice in space.";
					logger.error(error);
					return status;
				}								
			} else {
				//result = resultMonet;
				String error = "I can't add monet in space.";
				logger.error(error);
				return resultMonet;			
			}
		} catch (Exception e) {
			String error = "Unknown error.";
			ExceptionPrinter exeptionPrinter = new ExceptionPrinter();			
			
			logger.error(error + " Message: " + e.getMessage() + "\nStackTrace: " + exeptionPrinter.printToString(e));
			status.setContent(ResultIDs.InternalError);
			caption.setContent("Error: " + error);
		}

		return result;
	}

}
