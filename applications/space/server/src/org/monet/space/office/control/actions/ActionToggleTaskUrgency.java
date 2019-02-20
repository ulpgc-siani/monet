package org.monet.space.office.control.actions;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

public class ActionToggleTaskUrgency extends Action {

	public ActionToggleTaskUrgency() {
	}

	@Override
	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		Task task;
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (id == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SAVE_NODE);

		taskLayer = ComponentPersistence.getInstance().getTaskLayer();

		task = taskLayer.loadTask(id);
		task.setUrgent(!task.isUrgent());
		taskLayer.saveTaskUrgency(task);

		return String.valueOf(task.isUrgent());
	}

}
