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

import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DatabaseException;
import org.monet.space.kernel.model.Event;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.SerializerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class ProducerEvent extends Producer {

	public ProducerEvent() {
		super();
	}

	public void create(Event event) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.NAME, event.getName());
		parameters.put(Database.QueryFields.DUE_DATE, this.agentDatabase.getDateAsTimestamp(event.getDueDate()));
		parameters.put(Database.QueryFields.DATA, SerializerData.serialize(event.getProperties()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.EVENT_CREATE, parameters);
	}

	public Event load(String name) {
		ResultSet resultSet = null;
		Map<String, Object> parameters = new HashMap<>();

		try {
			parameters.put(Database.QueryFields.NAME, name);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.EVENT_LOAD, parameters);

			if (! resultSet.next())
				return null;

			Event event = new Event();
			event.setName(resultSet.getString("name"));
			event.setDueDate(resultSet.getTimestamp("due_date"));
			event.setProperties(SerializerData.deserialize(resultSet.getString("data")));

			return event;
		} catch (SQLException ex) {
			throw new DatabaseException(ErrorCode.LOAD_EVENT_LIST, null, ex);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}
	}

	public void notify(Event event) {
		AgentNotifier agentNotifier = AgentNotifier.getInstance();
		boolean fired = false;

		try {
			agentNotifier.notify(new MonetEvent(MonetEvent.MODEL_EVENT_FIRED, null, event));
			fired = true;
		}
		finally {
			saveFired(event, fired);
		}
	}

	public void saveFired(Event event, boolean fired) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.NAME, event.getName());
		parameters.put(Database.QueryFields.FIRED, fired ? "1" : "0");
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.EVENT_SAVE_FIRED, parameters);
	}

	public boolean cancel(String name) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.NAME, name);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.EVENT_REMOVE, parameters);

		return true;
	}

	public Object newObject() {
		return new Event();
	}

	@Override
	public void loadAttribute(EventObject object, String attribute) {
	}

}
