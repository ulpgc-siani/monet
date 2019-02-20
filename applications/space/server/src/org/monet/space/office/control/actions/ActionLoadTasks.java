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

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.DataRequest.GroupBy;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskList;
import org.monet.space.kernel.model.TaskSearchRequest;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.util.List;
import java.util.Map;

public class ActionLoadTasks extends Action {
	private TaskLayer taskLayer;

	public ActionLoadTasks() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	public String execute() {
		String inbox = (String) LibraryRequest.getParameter(Parameter.INBOX, this.request);
		String folder = (String) LibraryRequest.getParameter(Parameter.FOLDER, this.request);
		String condition = (String) LibraryRequest.getParameter(Parameter.CONDITION, this.request);
		String sortsBy = (String) LibraryRequest.getParameter(Parameter.SORTS_BY, this.request);
		String groupsBy = (String) LibraryRequest.getParameter(Parameter.GROUPS_BY, this.request);
		Integer startPos = Integer.valueOf(LibraryRequest.getParameter(Parameter.START, this.request));
		Integer limit = Integer.valueOf(LibraryRequest.getParameter(Parameter.LIMIT, this.request));
		TaskSearchRequest searchRequest;
		TaskList taskList;
		Account account;
		List<SortBy> sortsByList;
		Map<String, GroupBy> groupsByMap;
		String type, state, role, urgent, owner, background;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		sortsByList = getSortsBy(sortsBy);
		groupsByMap = getGroupsByMap(groupsBy);

		type = groupsByMap.containsKey("type") ? (String) groupsByMap.get("type").value(0) : null;
		state = groupsByMap.containsKey("state") ? (String) groupsByMap.get("state").value(0) : null;
		role = groupsByMap.containsKey("role") ? (String) groupsByMap.get("role").value(0) : null;
		urgent = groupsByMap.containsKey("urgent") ? (String) groupsByMap.get("urgent").value(0) : null;
		owner = groupsByMap.containsKey("owner") ? (String) groupsByMap.get("owner").value(0) : null;
		background = groupsByMap.containsKey("background") ? (String) groupsByMap.get("background").value(0) : "0";

		if (type != null && (type.isEmpty() || type.equals("all"))) type = null;
		if (state != null && (state.isEmpty() || state.equals("all"))) state = null;
		if (folder == null || folder.isEmpty()) folder = Task.Situation.ACTIVE;
		if (role != null && (role.isEmpty() || role.equals("all"))) role = null;
		if (urgent != null && (urgent.isEmpty() || urgent.equals("all"))) urgent = null;
		if (owner != null && (owner.isEmpty() || owner.equals("all"))) owner = null;
		if (background != null && background.equals("2")) background = null;

		searchRequest = new TaskSearchRequest();
		searchRequest.setCondition(condition);
		searchRequest.addParameter(Task.Parameter.TYPE, type);
		searchRequest.addParameter(Task.Parameter.STATE, state);
		searchRequest.addParameter(Task.Parameter.INBOX, inbox);
		searchRequest.addParameter(Task.Parameter.SITUATION, folder);
		searchRequest.addParameter(Task.Parameter.ROLE, role);
		searchRequest.addParameter(Task.Parameter.URGENT, urgent);
		searchRequest.addParameter(Task.Parameter.BACKGROUND, background);
		searchRequest.addParameter(Task.Parameter.OWNER, owner);
		searchRequest.setSortsBy(sortsByList);
		searchRequest.setStartPos(startPos);
		searchRequest.setLimit(limit);

		account = this.getAccount();
		taskList = this.taskLayer.searchTasks(account, searchRequest);
		taskList.setTotalCount(this.taskLayer.searchTasksCount(account, searchRequest));

		return taskList.toJson().toJSONString();
	}

}