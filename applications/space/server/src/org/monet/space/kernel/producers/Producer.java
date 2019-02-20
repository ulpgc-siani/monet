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

package org.monet.space.kernel.producers;

import org.monet.federation.accountservice.client.FederationService;
import org.monet.space.kernel.agents.AgentDatabase;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;

public abstract class Producer implements ILoadListener {
	protected AgentDatabase agentDatabase;
	protected ProducersFactory producersFactory;
	protected AgentSession oAgentSession;
	protected AgentNotifier agentNotifier;
	protected AgentLogger agentLogger;

	public Producer() {
		this.agentDatabase = AgentDatabase.getInstance();
		this.producersFactory = ProducersFactory.getInstance();
		this.oAgentSession = AgentSession.getInstance();
		this.agentNotifier = AgentNotifier.getInstance();
		this.agentLogger = AgentLogger.getInstance();
	}

	protected Dictionary getDictionary() {
		return BusinessUnit.getInstance().getBusinessModel().getDictionary();
	}

	protected Session getSession() {
		return Context.getInstance().getCurrentSession();
	}

	protected Account getAccount() {
		Session session = this.getSession();
		if (session == null) return null;
		return session.getAccount();
	}

	protected String getUserId() {
		Account account = this.getAccount();
		if (account == null) return "-1";
		return account.getUser().getId();
	}

	protected FederationService getAccountService() {
		AccountServiceProvider provider = AccountServiceProviderImpl.getInstance();
		return provider.getAccountService();
	}

	protected String getTableName(String sWord) {
		sWord = LibraryString.cleanString(sWord);
		return ((sWord.length() <= 15) ? sWord : sWord.substring(0, 15)) + Thread.currentThread().getId();
	}

	public void reset() {
	}

	public void fill(Event event, ResultSet resultSet) throws SQLException {
		event.setName(resultSet.getString("name"));
		event.setDueDate(resultSet.getTimestamp("due_date"));
		event.setProperties(SerializerData.deserialize(resultSet.getString("data")));
	}

	public abstract void loadAttribute(EventObject oEventObject, String sAttribute);

}
