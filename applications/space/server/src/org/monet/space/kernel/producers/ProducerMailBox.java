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
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.MailBox.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.UUID;

public class ProducerMailBox extends Producer {

	public ProducerMailBox() {
		super();
	}

	private MailBox fillMailBox(ResultSet result) throws SQLException {
		MailBox mailBox = new MailBox();
		mailBox.setId(result.getString("id"));
		mailBox.setTaskId(result.getString("id_task"));
		mailBox.setCode(result.getString("code"));
		mailBox.setType(Type.valueOf(result.getString("type")));
		return mailBox;
	}

    public boolean exists(String mailBoxId) {
        ResultSet result = null;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID, mailBoxId);

        try {
            result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MAILBOX_EXISTS, parameters);

            if (result.next())
                return true;
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_MAILBOX, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(result);
        }

        return false;
    }

    public MailBox load(String id) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, id);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MAILBOX_LOAD, parameters);

			if (result.next())
				return fillMailBox(result);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_MAILBOX, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return null;
	}

	public void create(MailBox mailBox) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		mailBox.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put(Database.QueryFields.ID, mailBox.getId());
		parameters.put(Database.QueryFields.ID_TASK, mailBox.getTaskId());
		parameters.put(Database.QueryFields.CODE, mailBox.getCode());
		parameters.put(Database.QueryFields.TYPE, mailBox.getType().toString());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MAILBOX_CREATE, parameters);
	}

	public void delete(String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, id);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MAILBOX_REMOVE, parameters);
	}

	public void deleteWithTaskId(String taskId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID_TASK, taskId);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MAILBOX_REMOVE_WITH_TASK_ID, parameters);
	}

	public void addPermission(String mailBoxId, String userId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, mailBoxId);
		parameters.put(Database.QueryFields.ID_USER, userId);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MAILBOX_PERMISSION_CREATE, parameters);
	}

	public void removePermission(String mailBoxId, String userId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, mailBoxId);
		parameters.put(Database.QueryFields.ID_USER, userId);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MAILBOX_PERMISSION_REMOVE, parameters);
	}

    public boolean hasPermission(String mailBoxId, String userId) {
        ResultSet result = null;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID, mailBoxId);
        parameters.put(Database.QueryFields.ID_USER, userId);

        try {
            result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MAILBOX_PERMISSION_LOAD, parameters);

            if (result.next())
                return true;
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_MAILBOX, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(result);
        }

        return false;
    }

	public Object newObject() {
		return new MailBox();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
