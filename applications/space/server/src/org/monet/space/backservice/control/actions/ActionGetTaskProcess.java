package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Task;

public class ActionGetTaskProcess extends Action {

	public ActionGetTaskProcess() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(id);

		return task.getData().toString();
	}

}
