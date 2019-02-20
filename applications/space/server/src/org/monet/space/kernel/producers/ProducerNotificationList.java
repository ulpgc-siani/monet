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
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Notification;
import org.monet.space.kernel.model.NotificationList;

import java.sql.ResultSet;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerNotificationList extends Producer {

	public NotificationList load(String userId, int startPos, int limit) {
		ProducerNotification producerNotification = (ProducerNotification) this.producersFactory.get(Producers.NOTIFICATION);
		NotificationList notificationList = new NotificationList();
		ResultSet resultSet = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID_USER, userId);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NOTIFICATION_LIST_LOAD_COUNT, parameters);
			resultSet.next();
			notificationList.setTotalCount(resultSet.getInt("totalCount"));
			notificationList.setUnread(notificationList.getTotalCount() - resultSet.getInt("read"));
			this.agentDatabase.closeQuery(resultSet);

			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(startPos));
			parameters.put(Database.QueryFields.LIMIT, limit);

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NOTIFICATION_LIST_LOAD, parameters);

			while (resultSet.next()) {
				Notification notification = new Notification();
				producerNotification.fill(notification, resultSet);
				notificationList.add(notification);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NOTIFICATIONLIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return notificationList;
	}

	public NotificationList loadWithPublication(String publicationId) {
		ProducerNotification producerNotification = (ProducerNotification) this.producersFactory.get(Producers.NOTIFICATION);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		NotificationList notificationList = new NotificationList();
		ResultSet resultSet = null;

		try {
			parameters.put(Database.QueryFields.ID_PUBLICATION, publicationId);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NOTIFICATION_LIST_LOAD_WITH_PUBLICATION, parameters, null);

			while (resultSet.next()) {
				Notification notification = new Notification();
				producerNotification.fill(notification, resultSet);
				notificationList.add(notification);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NOTIFICATIONLIST, publicationId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return notificationList;
	}

	public Object newObject() {
		return new Notification();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
