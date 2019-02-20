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

package org.monet.space.analytics.control.actions;

import org.monet.space.analytics.constants.Parameter;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.model.User;

public class ActionLogout extends Action {

	public ActionLogout() {
		super();
	}

	public String execute() {
		String instanceId = this.getParameterAsString(Parameter.INSTANCE_ID);
		User user = this.getFederationLayer().loadAccount().getUser();
		AgentPushService agentPushService = AgentPushService.getInstance();
		FederationLayer layer = this.getFederationLayer();

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		layer.logout();
		agentPushService.removeClient(user.getId(), PushClient.generateKey(this.idSession, instanceId));

		String sender = this.getParameterAsString(Parameter.SENDER);
		if ((sender != null) && (sender.equals("ajax")))
			return "done";

		Action action = (ActionShowApplication) this.actionsFactory.get(Actions.SHOW_APPLICATION, this.getRequest(), this.getResponse());
		return action.execute();
	}

}