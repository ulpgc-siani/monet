package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.Task;

public class ActionResumeTask extends Action {

	public ActionResumeTask() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		Task task;

		task = ComponentPersistence.getInstance().getTaskLayer().loadTask(id);
		task.getProcess().resume();

		return task.serializeToXML();
	}

}
