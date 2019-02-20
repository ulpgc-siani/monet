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

import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.TaskAccessException;
import org.monet.space.kernel.model.Role;
import org.monet.space.kernel.model.Task;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;

public class ActionSelectTaskSendJobRole extends Action {
	private TaskLayer taskLayer;
	private AgentUserClient agentUserClient;

	public ActionSelectTaskSendJobRole() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		this.agentUserClient = AgentUserClient.getInstance();
	}

	private String getUserMessage(String message) {
		AgentUserClient agentUserClient = AgentUserClient.getInstance();
		Long threadId = Thread.currentThread().getId();
		String userMessage = agentUserClient.getMessageForUser(threadId);

		if (userMessage != null && !userMessage.isEmpty())
			message += ": " + userMessage;

		return message;
	}

	public String execute() {
		String idTask = LibraryRequest.getParameter(Parameter.ID, this.request);
		String idRole = LibraryRequest.getParameter(Parameter.ROLE, this.request);
		Long threadId = Thread.currentThread().getId();
		Task task;
		Role role = null;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idTask == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SELECT_TASK_SEND_JOB_ROLE);

		task = this.taskLayer.loadTask(idTask);

		if (!this.componentSecurity.canWrite(task, this.getAccount()))
			throw new TaskAccessException(ErrorCode.WRITE_TASK_PERMISSIONS, task.getId());

		if (idRole != null) {
			RoleLayer roleLayer = ComponentFederation.getInstance().getRoleLayer();
			role = roleLayer.loadRole(idRole);
		}

		task.getProcess().selectSendJobActionRole(role);

		agentUserClient.clear(threadId);

		return this.getUserMessage(Language.getInstance().getMessage(MessageCode.TASK_SETUP_FINISHED));
	}

}