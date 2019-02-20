package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.TaskList;

public class ActionGetNodeTasks extends Action {

	public ActionGetNodeTasks() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		org.monet.space.kernel.model.Node node;
		TaskList taskList;

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		node = ComponentPersistence.getInstance().getNodeLayer().loadNode(id);
		taskList = ComponentPersistence.getInstance().getTaskLayer().loadTasks(node, Strings.ALL, Strings.ALL);

		return taskList.serializeToXML();
	}

}
