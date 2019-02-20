package org.monet.space.backservice.control.actions;

import org.monet.metamodel.TaskDefinition;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.Task;

public class ActionCreateTask extends Action {

	public ActionCreateTask() {
	}

	@Override
	public String execute() {
		String type = (String) this.parameters.get(Parameter.TYPE);
		TaskDefinition definition = this.dictionary.getTaskDefinition(type);
		Task task = ComponentPersistence.getInstance().getTaskLayer().createTask(definition.getCode());

		return task.serializeToXML();
	}

}
