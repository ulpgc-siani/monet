/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2014  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

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

package org.monet.space.explorer.control.actions;

import net.minidev.json.JSONObject;
import org.monet.space.explorer.control.dialogs.LogoutDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.model.User;

import java.io.IOException;

import static org.monet.space.kernel.listeners.ListenerPushService.PushClientMessages;

public class LogoutAction extends Action<LogoutDialog, Display> {

    public void execute() throws IOException {
        User user = this.getAccount().getUser();
        String instanceId = dialog.getInstanceId();
        FederationLayer layer = getFederationLayer();

        AgentPushService agentPushService = AgentPushService.getInstance();
        PushClient client = agentPushService.getClientView(user.getId(), this.idSession, instanceId);
        if (client != null && client.getViewId() != null)
            agentPushService.pushToViewers(client, client.getViewId(), PushClientMessages.REMOVE_OBSERVER, createUserInfo(user));

        layer.logout();
        agentPushService.removeClient(user.getId(), PushClient.generateKey(this.idSession, instanceId));

        display.redirectTo(configuration.getUrl() + "/home");
    }

    private JSONObject createUserInfo(User user) {

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", user.getId() + PushClient.generateKey(idSession, dialog.getInstanceId()));
        jsonUser.put("fullname", user.getInfo().getFullname());

        return jsonUser;
    }

}