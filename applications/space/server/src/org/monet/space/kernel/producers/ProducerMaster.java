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
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SessionException;
import org.monet.space.kernel.model.Master;
import org.monet.space.kernel.model.UserInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerMaster extends Producer {

	public ProducerMaster() {
		super();
	}

	public void fill(Master master, ResultSet result) throws SQLException {
		master.setId(result.getString("id"));
		master.setName(result.getString("username"));
		master.setCertificateAuthority(result.getString("certificate_authority"));
		master.setIsColonizer(Boolean.valueOf(result.getString("colonizer")));
		master.getInfo().setEmail(result.getString("email"));
		master.getInfo().setFullname(result.getString("fullname"));
		master.setRegistrationDate(result.getTimestamp("register_date"));
	}

	public boolean exists(String username, String certificateAuthority) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put(Database.QueryFields.USERNAME, username);
			parameters.put(Database.QueryFields.CERTIFICATE_AUTHORITY, certificateAuthority);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MASTER_EXISTS, parameters);

			return result.next();
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public Master load(String username, String certificateAuthority) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		Master master = new Master();

		try {
			parameters.put(Database.QueryFields.USERNAME, username);
			parameters.put(Database.QueryFields.CERTIFICATE_AUTHORITY, certificateAuthority);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MASTER_LOAD, parameters);

			if (!result.next())
				throw new Exception(String.format("Master '%s - %s' not found", username, certificateAuthority));

			this.fill(master, result);
		} catch (Exception oException) {
			throw new SessionException(ErrorCode.LOAD_USER, username, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return master;
	}

	public Master create(String username, String certificateAuthority, boolean colonizer, UserInfo info) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Master master = new Master();

		master.setName(username);
		master.setCertificateAuthority(certificateAuthority);
		master.setIsColonizer(colonizer);
		master.setInfo(info);

		parameters.put(Database.QueryFields.USERNAME, username);
		parameters.put(Database.QueryFields.CERTIFICATE_AUTHORITY, certificateAuthority);
		parameters.put(Database.QueryFields.EMAIL, info.getEmail());
		parameters.put(Database.QueryFields.FULLNAME, info.getFullname());
		parameters.put(Database.QueryFields.COLONIZER, String.valueOf(colonizer));
		parameters.put(Database.QueryFields.REGISTER_DATE, this.agentDatabase.getDateAsTimestamp(master.getInternalRegistrationDate()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.MASTER_CREATE, parameters);
		master.setId(id);

		return master;
	}

	public void remove(String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, id);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MASTER_REMOVE, parameters);
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
