package org.monet.space.setupservice.control.actions;

import org.monet.space.setupservice.core.constants.MessageCode;
import org.monet.space.kernel.Kernel;

public class ActionRun extends Action {

	public ActionRun() {
	}

	@Override
	public String execute() {
		Kernel kernel = Kernel.getInstance();
		kernel.run();
		return MessageCode.RUNNING;
	}

}
