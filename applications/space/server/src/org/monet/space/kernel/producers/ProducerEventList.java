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

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DatabaseException;
import org.monet.space.kernel.model.Event;
import org.monet.space.kernel.model.EventList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;

public class ProducerEventList extends Producer {

	public ProducerEventList() {
		super();
	}

	public EventList loadDueEvents() {
		ProducerEvent producerEvent = producersFactory.get(Producers.EVENT);
		ResultSet resultSet = null;
		EventList eventList = new EventList();

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.EVENT_LIST_LOAD);

			while (resultSet.next()) {
				Event event = new Event();
				producerEvent.fill(event, resultSet);
				eventList.add(event);
			}

		} catch (SQLException ex) {
			throw new DatabaseException(ErrorCode.LOAD_EVENT_LIST, null, ex);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return eventList;
	}

	public Object newObject() {
		return new EventList();
	}

	@Override
	public void loadAttribute(EventObject object, String attribute) {
	}

}
