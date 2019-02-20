package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Task;

public class ActionAddTaskShortCut extends Action {

	public ActionAddTaskShortCut() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String name = (String) this.parameters.get(Parameter.NAME);
		String value = (String) this.parameters.get(Parameter.VALUE);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		Task task = taskLayer.loadTask(id);
		Node node = nodeLayer.loadNode(value);
		task.addShortcutInstance(LibraryEncoding.decode(name), node);
		taskLayer.saveTask(task);

		return MessageCode.TASK_SHORTCUT_ADDED;
	}

}
