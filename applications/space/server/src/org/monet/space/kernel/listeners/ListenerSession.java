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

package org.monet.space.kernel.listeners;

import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Session;
import org.monet.space.kernel.model.User;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class ListenerSession implements HttpSessionListener {
	AgentSession agentSession;

	public ListenerSession() {
		this.agentSession = AgentSession.getInstance();
	}

	public void sessionCreated(HttpSessionEvent oEvent) {
		String sessionId = oEvent.getSession().getId();
		agentSession.add(sessionId);
        AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.SESSION_CREATED, null, agentSession.get(sessionId)));
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		String sessionId = event.getSession().getId();
		Session session = agentSession.get(sessionId);

		if (session == null)
			return;

		Account account = session.getAccount();
		User user = account.getUser();

		if (user != null && !user.getName().equals(Strings.EMPTY))
			AgentPushService.getInstance().removeChannels(user.getId(), sessionId);

        AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.SESSION_DESTROYED, null, this.agentSession.get(sessionId)));

		this.agentSession.remove(sessionId);
	}

}