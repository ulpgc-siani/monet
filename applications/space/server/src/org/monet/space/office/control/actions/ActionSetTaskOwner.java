package org.monet.space.office.control.actions;

import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;
import org.monet.space.office.control.constants.Actions;

public class ActionSetTaskOwner extends Action {

	public ActionSetTaskOwner() {
	}

	@Override
	public String execute() {
		String username = LibraryRequest.getParameter(Parameter.USERNAME, this.request);
		String idTask = LibraryRequest.getParameter(Parameter.ID_TASK, this.request);
        String reason = LibraryRequest.getParameter(Parameter.REASON, this.request);
		FederationLayer federationLayer;
		Account account = this.getAccount();
		User owner, sender;
		Task task;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (username == null || idTask == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SET_TASKS_OWNER);

		federationLayer = this.getFederationLayer();
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();

		owner = federationLayer.locateAccount(username).getUser();
		task = taskLayer.loadTask(idTask);

		if (!componentSecurity.canWrite(task, account)) {
			throw new TaskAccessException(ErrorCode.SAVE_TASK_PERMISSIONS, idTask);
		}

		taskLayer.saveTaskOwner(task, owner, reason);

		return Language.getInstance().getMessage(MessageCode.TASK_SAVED);
	}

}
