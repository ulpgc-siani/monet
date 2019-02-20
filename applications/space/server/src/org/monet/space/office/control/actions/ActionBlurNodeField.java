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
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.model.User;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import static org.monet.space.kernel.listeners.ListenerPushService.PushClientMessages;

public class ActionBlurNodeField extends Action {

	public ActionBlurNodeField() {
		super();
	}

	public String execute() {
		User user = this.getAccount().getUser();
		String userId = user.getId();
		String instanceId = LibraryRequest.getParameter(Parameter.INSTANCE_ID, this.request);
		String idNode = LibraryRequest.getParameter(Parameter.ID_NODE, this.request);
		String fieldPath = LibraryRequest.getParameter(Parameter.FIELD, this.request);
		AgentPushService agentPushService = AgentPushService.getInstance();
		JSONObject result = new JSONObject();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((instanceId == null) || (fieldPath == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.BLUR_NODE_FIELD);
		}

		JSONObject observer = new JSONObject();
		observer.put("id", userId + PushClient.generateKey(idSession, instanceId));
		observer.put("fullname", user.getInfo().getFullname());
		observer.put("field", fieldPath);

		PushClient client = agentPushService.getClientView(userId, this.idSession, instanceId);
		agentPushService.pushToViewers(client, PushClient.generateViewId(Node.class, idNode), PushClientMessages.BLUR_NODE_FIELD, observer);

		return result.toString();
	}

}