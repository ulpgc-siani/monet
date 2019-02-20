package org.monet.space.office.control.actions;

import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

public class ActionSetTasksOwner extends Action {

	public ActionSetTasksOwner() {
	}

	@Override
	public String execute() {
		String username = LibraryRequest.getParameter(Parameter.USERNAME, this.request);
		String data = LibraryRequest.getParameter(Parameter.DATA, this.request);
		String reason = LibraryRequest.getParameter(Parameter.REASON, this.request);
		FederationLayer federationLayer;
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Account account = this.getAccount();
		String[] tasks;
		User owner, sender;
		Task task;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (username == null || data == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SET_TASKS_OWNER);

		federationLayer = this.getFederationLayer();
		taskLayer = ComponentPersistence.getInstance().getTaskLayer();

		tasks = data.split(Strings.COMMA);
		owner = federationLayer.locateAccount(username).getUser();
		sender = this.getAccount().getUser();
		for (int i = 0; i < tasks.length; i++) {
			task = taskLayer.loadTask(tasks[i]);

			if (!componentSecurity.canWrite(task, account)) {
				throw new TaskAccessException(ErrorCode.SAVE_TASK_PERMISSIONS, tasks[i]);
			}

			taskLayer.saveTaskOwner(task, owner, reason);
		}

		return Language.getInstance().getMessage(MessageCode.TASK_SAVED);
	}

}
