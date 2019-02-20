package org.monet.space.setupservice.control.actions;

import org.monet.space.kernel.Kernel;

public class ActionGetVersion extends Action {

	public ActionGetVersion() {
	}

	@Override
	public String execute() {
		return Kernel.getInstance().getVersion();
	}

}
