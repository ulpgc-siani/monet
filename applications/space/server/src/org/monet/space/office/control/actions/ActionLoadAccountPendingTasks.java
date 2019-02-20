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

import net.minidev.json.JSONObject;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskSearchRequest;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadAccountPendingTasks extends Action {
	private NodeLayer nodeLayer;
	private DashboardLayer dashboardLayer;
	private RoleLayer roleLayer;

	public ActionLoadAccountPendingTasks() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.dashboardLayer = ComponentDatawareHouse.getInstance().getDashboardLayer();
		this.roleLayer = ComponentFederation.getInstance().getRoleLayer();
	}

	public String execute() {
        TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Account account = this.getAccount();
		JSONObject jsonResult = new JSONObject();

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

        TaskSearchRequest searchRequest = new TaskSearchRequest();

		searchRequest.addParameter(Task.Parameter.SITUATION, Task.Situation.ACTIVE);
		searchRequest.addParameter(Task.Parameter.INBOX, Task.Inbox.TASKBOARD);
		jsonResult.put("taskboard", taskLayer.searchTasksCount(account, searchRequest));

		searchRequest.addParameter(Task.Parameter.SITUATION, Task.Situation.ALIVE);
		searchRequest.addParameter(Task.Parameter.INBOX, Task.Inbox.TASKTRAY);
		jsonResult.put("tasktray", taskLayer.searchTasksCount(account, searchRequest));

		return jsonResult.toString();
	}

}