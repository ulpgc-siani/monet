package org.monet.space.office.control.actions;

import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionUnsetTasksOwner extends Action {

	public ActionUnsetTasksOwner() {
	}

	@Override
	public String execute() {
		String data = LibraryRequest.getParameter(Parameter.DATA, this.request);
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Account account = this.getAccount();
		String[] tasks;
		Task task;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (data == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.UNSET_TASKS_OWNER);

		taskLayer = ComponentPersistence.getInstance().getTaskLayer();

		tasks = data.split(Strings.COMMA);
		for (int i = 0; i < tasks.length; i++) {
			task = taskLayer.loadTask(tasks[i]);

			if (!componentSecurity.canWrite(task, account))
				throw new TaskAccessException(ErrorCode.SAVE_TASK_PERMISSIONS, tasks[i]);

			taskLayer.saveTaskOwner(task, null, "");
		}

		return Language.getInstance().getMessage(MessageCode.TASK_SAVED);
	}

}
