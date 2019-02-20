package org.monet.space.setupservice.control.actions;

import org.monet.space.kernel.Kernel;
import org.monet.space.setupservice.core.constants.MessageCode;

public class ActionStop extends Action {

	public ActionStop() {
	}

	@Override
	public String execute() {
		Kernel kernel = Kernel.getInstance();
		kernel.stop();
		return MessageCode.STOPPED;
	}

}
