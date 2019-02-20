package org.monet.space.setupservice.control.actions;

import org.monet.space.setupservice.core.constants.MessageCode;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.setupservice.control.constants.Parameter;

public class ActionPutLabel extends Action {

	public ActionPutLabel() {
	}

	@Override
	public String execute() {
		String label = (String) this.parameters.get(Parameter.LABEL);
		BusinessUnit businessUnit = BusinessUnit.getInstance();

		businessUnit.setLabel(label);
		businessUnit.save();

		return MessageCode.LABEL_PUT;
	}

}
