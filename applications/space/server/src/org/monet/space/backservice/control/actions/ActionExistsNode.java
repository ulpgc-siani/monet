package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentPersistence;

public class ActionExistsNode extends Action {

	public ActionExistsNode() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		return String.valueOf(ComponentPersistence.getInstance().getNodeLayer().existsNode(id));
	}

}
