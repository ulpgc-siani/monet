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

import org.monet.metamodel.Definition;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.MailBoxUri;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Service;
import org.monet.space.kernel.model.Task;

import java.sql.ResultSet;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerService extends Producer {

	public ProducerService() {
		super();
	}

	private void loadProperties(Service service, ResultSet resultSet) {

		try {
			service.setId(resultSet.getString("id"));
			service.setCode(resultSet.getString("code"));
			service.setTaskId(resultSet.getString("id_task"));
			service.setLocalMailBox(resultSet.getString("local_mailbox"));
			service.setReplyToMailBox(MailBoxUri.build(resultSet.getString("reply_mailbox")));
			service.setCreateDate(resultSet.getTimestamp("create_date"));
			service.setRemoteUnitLabel(resultSet.getString("remote_unit_label"));

			Definition definition = this.getDictionary().getDefinition(service.getCode());
			service.setDefinition(definition);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_SERVICE, service.getId(), oException);
		}
	}

	public Service load(String id) {
		Service service;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet resultSet = null;

		parameters.put(Database.QueryFields.ID, id);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SERVICE_LOAD, parameters);
			resultSet.next();
			service = new Service();
			this.loadProperties(service, resultSet);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_SERVICE, id, oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return service;
	}

	public Service loadForTask(String idTask) {
		Service service;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet resultSet = null;

		parameters.put(Database.QueryFields.ID_TASK, idTask);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SERVICE_LOAD_FOR_TASK, parameters);

			if (!resultSet.next()) return null;

			service = new Service();
			this.loadProperties(service, resultSet);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_SERVICE_FOR_TASK, idTask, oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return service;
	}

	public Service loadByRequestId(String requestId) {
		Service service;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet resultSet = null;

		parameters.put(Database.QueryFields.ID_REQUEST, requestId);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SERVICE_LOAD_BY_REQUEST_ID, parameters);
			resultSet.next();
			service = new Service();
			this.loadProperties(service, resultSet);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_SERVICE_BY_REQUEST_ID, requestId, oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return service;
	}

	public void save(Service service) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, service.getId());
		parameters.put(Database.QueryFields.LOCAL_MAILBOX, service.getLocalMailBox());
		parameters.put(Database.QueryFields.REPLY_MAILBOX, service.getReplyToMailBox()!=null?service.getReplyToMailBox().toString():null);
		parameters.put(Database.QueryFields.ID_TASK, service.getTaskId());
		parameters.put(Database.QueryFields.REMOTE_UNIT_LABEL, service.getRemoteUnitLabel());

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SERVICE_SAVE, parameters);
	}

	public Service create(String code) {
		Service service = new Service();
		service.setCode(code);
		return this.create(service);
	}

	public Service create(Service service) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		Definition definition = this.getDictionary().getDefinition(service.getCode());

		service.setCreateDate(new Date());
		service.setDefinition(definition);

		parameters.put(Database.QueryFields.CODE, service.getCode());
		parameters.put(Database.QueryFields.LOCAL_MAILBOX, service.getLocalMailBox());
		parameters.put(Database.QueryFields.REPLY_MAILBOX, service.getReplyToMailBox()!=null?service.getReplyToMailBox().toString():null);
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.ID_TASK, service.getTaskId());
		parameters.put(Database.QueryFields.REMOTE_UNIT_LABEL, service.getRemoteUnitLabel());

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.SERVICE_CREATE, parameters);
		service.setId(id);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.SERVICE_CREATED, null, service));

		return service;
	}

	public void remove(String serviceId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, serviceId);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SERVICE_REMOVE, parameters);
	}

	public Object newObject() {
		return new Task();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}


}
