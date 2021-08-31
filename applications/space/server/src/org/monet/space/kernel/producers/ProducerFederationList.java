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
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;

import java.sql.ResultSet;
import java.util.*;

public class ProducerFederationList extends Producer {

	public ProducerFederationList() {
		super();
	}

	private void loadUser(User user, ResultSet result) {
		try {
			user.setId(result.getString("id"));
			user.setName(result.getString("username"));
			user.getInfo().setFullname(result.getString("username"));
			user.getInfo().setEmail(result.getString("email"));
			user.getInfo().setPhoto(result.getString("photo"));
			if (result.getString("preferences") != null)
				user.getInfo().setPreferences(SerializerData.<LinkedHashMap<String, String>>deserialize(result.getString("preferences")));
			user.setRegistrationDate(result.getTimestamp("register_date"));
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USER, user.getId(), exception);
		}
	}

	public UserList loadUsers() {
		UserList userList;
		ResultSet result;
		Account account = this.getAccount();

		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LIST_LOAD_USERS);

		try {
			userList = new UserList();
			while (result.next()) {
				User user = new User();
				this.loadUser(user, result);
				userList.add(user);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERS, account.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	public List<String> loadUsersIds() {
		List<String> userList;
		ResultSet result;

		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LIST_LOAD_USERS_IDS);

		try {
			userList = new ArrayList<String>();
			while (result.next()) {
				userList.add(result.getString("id"));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERS, null, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	public UserList loadUsers(Set<String> idUsersSet) {
		ResultSet result;
		Account account = this.getAccount();
		Iterator<String> iter = idUsersSet.iterator();
		UserList userList = new UserList();
		String idUsers = "";

		if (idUsersSet.size() == 0) return userList;

		while (iter.hasNext()) {
			idUsers += "'" + iter.next() + "',";
		}
		idUsers = idUsers.substring(0, idUsers.length() - 1);

		HashMap<String, String> subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.USERS, idUsers);
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LIST_LOAD_USERS_FILTERED, null, subQueries);

		try {
			while (result.next()) {
				User user = new User();
				this.loadUser(user, result);
				userList.add(user);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERS, account.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	public UserList searchUsers(DataRequest dataRequest) {
		return searchUsers(dataRequest, Database.Queries.ACCOUNT_LIST_SEARCH_USERS, Database.Queries.ACCOUNT_LIST_SEARCH_USERS_COUNT);
	}

	public UserList searchUsersWithRoles(DataRequest dataRequest) {
		return searchUsers(dataRequest, Database.Queries.ACCOUNT_LIST_SEARCH_USERS_WITH_ROLES, Database.Queries.ACCOUNT_LIST_SEARCH_USERS_WITH_ROLES_COUNT);
	}

	public UserList searchUsers(DataRequest dataRequest, String query, String countQuery) {
		UserList userList;
		ResultSet result = null;
		Account account = this.getAccount();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		String condition = dataRequest.getCondition();

		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
		parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());
		parameters.put(Database.QueryFields.CONDITION, condition);

		subQueries.put(Database.QueryFields.CONDITION, condition);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(query, parameters, subQueries);
			userList = new UserList();
			while (result.next()) {
				User user = new User();
				this.loadUser(user, result);
				userList.add(user);
			}
			this.agentDatabase.closeQuery(result);

			parameters.clear();
			parameters.put(Database.QueryFields.CONDITION, condition);
			result = this.agentDatabase.executeRepositorySelectQuery(countQuery, parameters, subQueries);
			if (!result.next())
				throw new Exception("Can't get total count of users");
			userList.setTotalCount(result.getInt("counter"));
		} catch (Exception exception) {
			throw new DataException(ErrorCode.SEARCH_USERS, account.getId(), exception);
		} finally {
			if (result != null) this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
