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
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

public class ActionCreateTask extends Action {
	private TaskLayer taskLayer;

	public ActionCreateTask() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String mode = LibraryRequest.getParameter(Parameter.MODE, this.request);
		String label = LibraryRequest.getParameter(Parameter.TITLE, this.request);
		ActionLoadTask action;
		Task task;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((code == null) || (label == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.CREATE_TASK);
		}

		task = this.taskLayer.createTask(code);
		task.setLabel(label);
		this.taskLayer.saveTask(task);

		task.getProcess().resumeAsync();

		if (mode != null) {
			this.request.setAttribute(Parameter.ID, task.getId());
			this.request.setAttribute(Parameter.MODE, mode);
			action = (ActionLoadTask) this.actionsFactory.get(Actions.LOAD_TASK, this.request, this.response);
			return action.execute();
		}

		return Language.getInstance().getMessage(MessageCode.TASK_CREATED);
	}

}