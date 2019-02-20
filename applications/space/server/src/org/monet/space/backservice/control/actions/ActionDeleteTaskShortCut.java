package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Task;

public class ActionDeleteTaskShortCut extends Action {

	public ActionDeleteTaskShortCut() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String name = (String) this.parameters.get(Parameter.NAME);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		task = taskLayer.loadTask(id);
		task.removeShortcutInstance(LibraryEncoding.decode(name));
		taskLayer.saveTask(task);

		return MessageCode.TASK_SHORTCUT_DELETED;
	}

}
