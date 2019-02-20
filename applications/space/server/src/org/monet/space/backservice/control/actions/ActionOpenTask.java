package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.Task;

public class ActionOpenTask extends Action {

	public ActionOpenTask() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		Task task;

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		task = ComponentPersistence.getInstance().getTaskLayer().loadTask(id);

		return task.serializeToXML();
	}

}
