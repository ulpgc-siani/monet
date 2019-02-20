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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;
import org.monet.space.kernel.model.UserPushContext;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;

import java.util.Collection;

import static org.monet.space.kernel.listeners.ListenerPushService.PushClientMessages;

public class ActionFocusTaskView extends Action {
	private TaskLayer taskLayer;

	public ActionFocusTaskView() {
		super();
		this.taskLayer = ComponentPersistence.getInstance().getTaskLayer();
	}

	private void addObservers(JSONObject object, String viewId, String instanceId) {
		JSONArray observers = new JSONArray();
		Collection<UserPushContext> contextList = AgentPushService.getInstance().getViewObservers(viewId);
		String key = PushClient.generateKey(this.idSession, instanceId);

		for (UserPushContext context : contextList) {
			for (PushClient pushClient : context.getClientsWithView(viewId)) {
				if (pushClient.getKey().equals(key)) continue;
				JSONObject observer = new JSONObject();
				observer.put("id", context.getUserId());
				observer.put("fullname", context.getUserFullname());
				observers.add(observer);
				break;
			}
		}

		object.put("observers", observers);
	}

	public String execute() {
		User user = this.getAccount().getUser();
		String sessionId = this.idSession;
		String instanceId = LibraryRequest.getParameter(Parameter.INSTANCE_ID, this.request);
		String idTask = LibraryRequest.getParameter(Parameter.ID_TASK, this.request);
		String timestampValue = LibraryRequest.getParameter(Parameter.TIMESTAMP, this.request);
		AgentPushService agentPushService = AgentPushService.getInstance();
		JSONObject result = new JSONObject(), jsonUser = new JSONObject();
		;
		Long timestamp = null;
		String viewId;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((instanceId == null) || (idTask == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.FOCUS_TASK_VIEW);
		}

		if (timestampValue != null && !timestampValue.equals("null")) timestamp = Long.parseLong(timestampValue);

		Task task = this.taskLayer.loadTask(idTask);

		if (task == null)
			return ErrorCode.READ_TASK_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.READ_TASK_PERMISSIONS);

		if ((timestamp == null) || (task.getInternalUpdateDate().getTime() != timestamp))
			result.put("task", task.toJson());

		viewId = PushClient.generateViewId(task);
		this.addObservers(result, viewId, instanceId);

		jsonUser.put("id", user.getId());
		jsonUser.put("fullname", user.getInfo().getFullname());

		PushClient client = agentPushService.getClientView(user.getId(), sessionId, instanceId);
		if (client != null) {
			jsonUser.put("field", client.getViewContext().get("field"));
			agentPushService.pushToViewers(client, client.getViewId(), PushClientMessages.REMOVE_OBSERVER, jsonUser);
		}

		agentPushService.updateClientView(user.getId(), sessionId, instanceId, viewId);
		agentPushService.pushToViewers(client, viewId, PushClientMessages.ADD_OBSERVER, jsonUser);

		return result.toString();
	}

}