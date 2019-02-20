package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;

public class ActionRemoveNode extends Action {

	public ActionRemoveNode() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		ComponentPersistence.getInstance().getNodeLayer().deleteNode(id);

		return MessageCode.NODE_REMOVED;
	}

}
