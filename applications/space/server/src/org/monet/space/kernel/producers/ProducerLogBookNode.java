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
import org.monet.space.kernel.constants.LogBookNodeEvent;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;

import java.sql.ResultSet;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerLogBookNode extends ProducerLogBook {

	public ProducerLogBookNode() {
		super();
	}

	public void addEntry(NodeLogBookEntry entry) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		String userId = "-1";
		if (this.getAccount() != null)
			userId = this.getAccount().getUser().getId();

		Context context = Context.getInstance();
		int eventType = entry.getType();
		String host = context.getHost(Thread.currentThread().getId());

		parameters.put(Database.QueryFields.HOST, host!=null?host:"localhost");
		parameters.put(Database.QueryFields.LAYER, context.getApplicationInterface(Thread.currentThread().getId()));
		parameters.put(Database.QueryFields.ID_USER, userId);
		parameters.put(Database.QueryFields.ID_NODE, entry.getIdObject());
		parameters.put(Database.QueryFields.VISITED, ((LogBookNodeEvent.VISITED & eventType) > 0) ? "1" : "0");
		parameters.put(Database.QueryFields.CREATED, ((LogBookNodeEvent.CREATED & eventType) > 0) ? "1" : "0");
		parameters.put(Database.QueryFields.DELETED, ((LogBookNodeEvent.DELETED & eventType) > 0) ? "1" : "0");
		parameters.put(Database.QueryFields.MODIFIED, ((LogBookNodeEvent.MODIFIED & eventType) > 0) ? "1" : "0");
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.LOGBOOKNODE_CREATE_ENTRY, parameters);
	}

	public LogBookNode load() {
		LogBookNode logBookNode = new LogBookNode();
		logBookNode.setBookLink(NodeLogBookProvider.getInstance());
		return logBookNode;
	}

	public BookEntryList search(int eventType, Date from, Date to) {
		BookEntryList logEntryList;
		String condition = Strings.EMPTY;
		ResultSet result;
		HashMap<String, String> subQueries = new HashMap<String, String>();
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		if ((LogBookNodeEvent.VISITED & eventType) > 0) condition += " OR visited=1";
		if ((LogBookNodeEvent.CREATED & eventType) > 0) condition += " OR created=1";
		if ((LogBookNodeEvent.DELETED & eventType) > 0) condition += " OR deleted=1";
		if ((LogBookNodeEvent.MODIFIED & eventType) > 0) condition += " OR modified=1";

		if (condition.length() > 0) condition = condition.substring(4);

		subQueries.put(Database.QueryFields.CONDITION, condition);

		parameters.put(Database.QueryFields.FROM_DATE, this.agentDatabase.getDateAsTimestamp(from));
		parameters.put(Database.QueryFields.TO_DATE, this.agentDatabase.getDateAsTimestamp(to));
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.LOGBOOKNODE_SEARCH, parameters, subQueries);

		try {
			logEntryList = new BookEntryList();
			while (result.next()) {
				NodeLogBookEntry entry = new NodeLogBookEntry(result.getString("id_node"), eventType);
				logEntryList.add(entry);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.SEARCH_LOGBOOK, String.valueOf(eventType), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return logEntryList;
	}

	public BookEntryList request(DataRequest oDataRequest) {
		return null;
	}

	public int getCount(String nodeId) {
		return -1;
	}

	public LogSubscriberList loadSubscribers() {
		LogSubscriberList subscriberList;
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.LOGBOOKNODE_SUBSCRIBER_LIST_LOAD, parameters);

			subscriberList = new LogSubscriberList();
			while (result.next()) {
				LogSubscriber subscriber = new LogSubscriber();
				subscriber.getServerConfiguration().setName(result.getString("server_name"));
				subscriber.getServerConfiguration().setHost(result.getString("server_host"));
				subscriber.getServerConfiguration().setPort(result.getString("server_port"));
				subscriber.setType(result.getInt("type"));
				subscriber.setRegistrationDate(result.getTimestamp("register_date"));
				subscriberList.add(subscriber);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_LOGBOOK_SUBSCRIBERLIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return subscriberList;
	}

	public void addSubscriber(ServerConfiguration configuration, int eventType) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		String userId = this.getAccount().getUser().getId();

		parameters.put(Database.QueryFields.SERVER_NAME, configuration.getName());
		parameters.put(Database.QueryFields.SERVER_HOST, configuration.getHost());
		parameters.put(Database.QueryFields.SERVER_PORT, configuration.getPort());
		parameters.put(Database.QueryFields.TYPE, String.valueOf(eventType));
		parameters.put(Database.QueryFields.ID_USER, userId);
		parameters.put(Database.QueryFields.REGISTER_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.LOGBOOKNODE_SUBSCRIBER_LIST_ADD, parameters);
	}

	public void removeSubscriber(ServerConfiguration configuration, int eventType) {
		HashMap<String, Object> hmParameters = new HashMap<String, Object>();

		hmParameters.put(Database.QueryFields.SERVER_HOST, configuration.getHost());
		hmParameters.put(Database.QueryFields.SERVER_PORT, configuration.getPort());
		hmParameters.put(Database.QueryFields.TYPE, String.valueOf(eventType));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.LOGBOOKNODE_SUBSCRIBER_LIST_DELETE, hmParameters);
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
