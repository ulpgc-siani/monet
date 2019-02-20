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
import org.monet.space.kernel.exceptions.DatabaseException;
import org.monet.space.kernel.model.DatabaseRepositoryQuery;
import org.monet.space.kernel.model.EventLog;
import org.monet.space.kernel.model.EventLogList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

public class ProducerEventLog extends Producer {

	public ProducerEventLog() {
		super();
	}

	public void insert(List<EventLog> eventLogs) {
		DatabaseRepositoryQuery[] queries = new DatabaseRepositoryQuery[eventLogs.size()];
		int i = 0;
		for (EventLog eventLog : eventLogs) {
			HashMap<String, Object> parameters = new HashMap<String, Object>();

			parameters.put(Database.QueryFields.LOGGER, eventLog.getLogger());
			parameters.put(Database.QueryFields.PRIORITY, eventLog.getPriority());
			parameters.put(Database.QueryFields.MESSAGE, eventLog.getMessage());
			parameters.put(Database.QueryFields.STACKTRACE, eventLog.getStacktrace());
			parameters.put(Database.QueryFields.CREATION_TIME, this.agentDatabase.getDateAsTimestamp(eventLog.getCreationTime()));
			queries[i++] = new DatabaseRepositoryQuery(Database.Queries.EVENT_LOG_CREATE, parameters);
		}
		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queries);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void clear(String logger) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.LOGGER, logger);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.EVENT_LOG_CLEAR, parameters);
	}

	public EventLogList load(String logger, int offset, int pageSize) {
		ResultSet resultSet = null;
		EventLogList eventLogPage = new EventLogList();
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.LOGGER, logger);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.EVENT_LOG_LOAD_COUNT, parameters);
			resultSet.next();

			int totalCount = resultSet.getInt(1);
			eventLogPage.setTotalCount(totalCount);

			if (totalCount == 0) return eventLogPage;
		} catch (SQLException ex) {
			throw new DatabaseException(ErrorCode.LOAD_EVENT_LOGS, null, ex);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		parameters.put(Database.QueryFields.START_POS, offset);
		parameters.put(Database.QueryFields.LIMIT, pageSize);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.EVENT_LOG_LOAD, parameters);

			while (resultSet.next()) {
				EventLog eventLog = new EventLog();
				eventLog.setLogger(resultSet.getString(Database.QueryFields.LOGGER));
				eventLog.setPriority(resultSet.getString(Database.QueryFields.PRIORITY));
				eventLog.setMessage(resultSet.getString(Database.QueryFields.MESSAGE));
				eventLog.setStacktrace(resultSet.getString(Database.QueryFields.STACKTRACE));
				eventLog.setCreationTime(resultSet.getTimestamp("creation_time"));
				eventLogPage.add(eventLog);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ErrorCode.LOAD_EVENT_LOGS, null, ex);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return eventLogPage;
	}

	public Object newObject() {
		return new EventLog();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
