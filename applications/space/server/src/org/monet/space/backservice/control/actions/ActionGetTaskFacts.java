package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskFactList;

public class ActionGetTaskFacts extends Action {

	public ActionGetTaskFacts() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;
		TaskFactList factList;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(id);
		factList = taskLayer.loadTaskFacts(task.getId());

		return factList.serializeToXML();
	}

}
