package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;

public class ActionEmptyTrash extends Action {

	public ActionEmptyTrash() {
	}

	@Override
	public String execute() {
		ComponentPersistence.getInstance().getNodeLayer().emptyTrash();
		return MessageCode.TRASH_EMPTY;
	}

}
