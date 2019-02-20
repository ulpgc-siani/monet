/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.office.control.actions;

import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionAbortTask extends Action {
	private TaskLayer taskLayer;

	public ActionAbortTask() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		Task task;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.ABORT_TASK);
		}

		try {
			task = this.taskLayer.loadTask(id);

			if (!this.componentSecurity.canWrite(task, this.getAccount())) {
				return ErrorCode.WRITE_TASK_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.WRITE_TASK_PERMISSIONS);
			}

			taskLayer.abortTask(id);
		} catch (TaskAccessException oException) {
			this.agentException.error(oException);
			return ErrorCode.WRITE_TASK_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.WRITE_TASK_PERMISSIONS);
		}

		return Language.getInstance().getMessage(MessageCode.TASK_ABORTED);
	}

}