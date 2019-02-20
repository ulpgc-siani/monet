package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Task;

public class ActionAddTaskFlag extends Action {

	public ActionAddTaskFlag() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String name = (String) this.parameters.get(Parameter.NAME);
		String value = (String) this.parameters.get(Parameter.VALUE);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(id);
		task.getProcess().setFlag(LibraryEncoding.decode(name), LibraryEncoding.decode(value));
		taskLayer.saveTaskProcess(task);

		return MessageCode.TASK_FLAG_ADDED;
	}

}
