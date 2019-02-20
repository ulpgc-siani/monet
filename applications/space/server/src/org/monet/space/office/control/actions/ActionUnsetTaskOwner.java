package org.monet.space.office.control.actions;

import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

public class ActionUnsetTaskOwner extends Action {

	public ActionUnsetTaskOwner() {
	}

	@Override
	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		Account account = this.getAccount();
		Task task;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (id == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SAVE_NODE);

		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();

		task = taskLayer.loadTask(id);

		if (!componentSecurity.canWrite(task, account))
			throw new TaskAccessException(ErrorCode.SAVE_TASK_PERMISSIONS, id);

		taskLayer.saveTaskOwner(task, null, "");

		return Language.getInstance().getMessage(MessageCode.TASK_SAVED);
	}

}
