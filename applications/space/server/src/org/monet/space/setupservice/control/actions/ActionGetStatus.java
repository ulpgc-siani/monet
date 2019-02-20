package org.monet.space.setupservice.control.actions;

import org.monet.space.setupservice.core.model.Status;
import org.monet.space.kernel.Kernel;

public class ActionGetStatus extends Action {

	public ActionGetStatus() {
	}

	@Override
	public String execute() {
		Kernel kernel = Kernel.getInstance();
		Status status = new Status();
		status.setRunningDate(kernel.getRunningDate());
		return status.serializeToXML();
	}

}
