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

import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Notification;
import org.monet.space.kernel.model.NotificationList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerNotification extends Producer {

	public void fill(Notification notification, ResultSet resultSet) throws SQLException {
		notification.setId(resultSet.getString("id"));
		notification.setUserId(resultSet.getString("id_user"));
		notification.setPublicationId(resultSet.getString("id_publication"));
		notification.setLabel(resultSet.getString("label"));
		notification.setIcon(resultSet.getString("icon"));
		notification.setRead(resultSet.getBoolean("read"));
		notification.setTarget(resultSet.getString("target"));
		notification.setCreateDate(resultSet.getTimestamp("create_date"));
	}

	public boolean exists(String publicationId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;

		parameters.put(Database.QueryFields.ID_PUBLICATION, publicationId);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NOTIFICATION_EXISTS, parameters);
			return result.next();
		} catch (SQLException oException) {
			AgentLogger.getInstance().error(oException);
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public void create(Notification notification) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_USER, notification.getUserId());
		parameters.put(Database.QueryFields.ID_PUBLICATION, notification.getPublicationId());
		parameters.put(Database.QueryFields.TARGET, notification.getTarget());
		parameters.put(Database.QueryFields.ICON, notification.getIcon());
		parameters.put(Database.QueryFields.LABEL, notification.getLabel());
		parameters.put(Database.QueryFields.READ, notification.isRead());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(notification.getCreateDate()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.NOTIFICATION_CREATE, parameters);

		notification.setId(id);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.NOTIFICATION_CREATED, null, notification));
	}

	public void priorize(String publicationId) {
		ProducerNotificationList producerNotificationList = (ProducerNotificationList) this.producersFactory.get(Producers.NOTIFICATIONLIST);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();

		parameters.put(Database.QueryFields.ID_PUBLICATION, publicationId);
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NOTIFICATION_PRIORIZE, parameters, subQueries);

		NotificationList notificationList = producerNotificationList.loadWithPublication(publicationId);
		for (Notification notification : notificationList)
			this.agentNotifier.notify(new MonetEvent(MonetEvent.NOTIFICATION_PRIORIZED, null, notification));
	}

	public void markAsRead(String ids) {
		HashMap<String, String> subQueries = new HashMap<String, String>();

		subQueries.put(Database.QueryFields.IDS, ids);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NOTIFICATION_MARK_READ, null, subQueries);
	}

	public void markAllAsRead() {
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NOTIFICATION_MARK_ALL_READ);
	}

	public Object newObject() {
		return new Notification();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
