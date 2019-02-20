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
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.listeners.ListenerPushService;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.model.User;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.control.constants.RequestSender;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.io.IOException;

public class ActionLogout extends Action {

	public ActionLogout() {
		super();
	}

	public String execute() throws IOException {
		User user = this.getAccount().getUser();
		String instanceId = LibraryRequest.getParameter(Parameter.INSTANCE_ID, this.request);
		AgentPushService agentPushService = AgentPushService.getInstance();
		JSONObject jsonUser = new JSONObject();
		FederationLayer layer = this.getFederationLayer();

		if (!layer.isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		jsonUser.put("id", user.getId() + PushClient.generateKey(idSession, instanceId));
		jsonUser.put("fullname", user.getInfo().getFullname());

		PushClient client = agentPushService.getClientView(user.getId(), this.idSession, instanceId);
		if (client != null && client.getViewId() != null)
			agentPushService.pushToViewers(client, client.getViewId(), ListenerPushService.PushClientMessages.REMOVE_OBSERVER, jsonUser);

		layer.logout();
		agentPushService.removeClient(user.getId(), PushClient.generateKey(this.idSession, instanceId));

		String sender = LibraryRequest.getParameter(Parameter.SENDER, request);
		if ((sender != null) && (sender.equals(RequestSender.AJAX)))
			return "ok";

		Action action = (ActionShowApplication) this.actionsFactory.get(Actions.SHOW_APPLICATION, this.request, this.response);
		return action.execute();
	}

}