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

import org.monet.metamodel.RoleDefinition;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SessionException;
import org.monet.space.kernel.library.LibraryHTML;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;

import java.sql.ResultSet;
import java.util.*;

public class ProducerFederation extends Producer {

	public ProducerFederation() {
		super();
	}

	private void loadUser(Account account, ResultSet result) {
		User user = account.getUser();

		try {
			account.setId(result.getString("id"));
			user.setId(result.getString("id"));
			user.setName(result.getString("username"));
			user.getInfo().setFullname(result.getString("fullname"));
			user.getInfo().setEmail(result.getString("email"));
			user.getInfo().setPhoto(result.getString("photo"));
			if (result.getString("preferences") != null)
				user.getInfo().setPreferences(SerializerData.<LinkedHashMap<String, String>>deserialize(result.getString("preferences")));
			user.setRegistrationDate(result.getTimestamp("register_date"));
			account.linkLoadListener(this);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USER, user.getId(), exception);
		}
	}

	private void loadRoleList(Account account) {
		ProducerRole producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		RoleList roleList;

		try {
			parameters.put(Database.QueryFields.ID, account.getId());
			parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_LIST_LOAD_FOR_ACCOUNT, parameters);

			roleList = new RoleList();

			while (result.next()) {
				Role role = producerRole.fill(result);
				User user = new User();

				user.setId(result.getString("id_user"));
				user.getInfo().setFullname(result.getString("username"));
				((UserRole) role).setUser(user);

				roleList.add(role);
			}

			account.setRoleList(roleList);
		} catch (Exception exception) {
			throw new SessionException(ErrorCode.LOAD_ROLE_LIST, account.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

	}

	private void loadEnvironmentNodes(Account account) {
		ProducerNode producerNode = (ProducerNode) this.producersFactory.get(Producers.NODE);
		ProducerRole producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);
		HashMap<String, ArrayList<Node>> environmentNodesByRole = new HashMap<String, ArrayList<Node>>();
		Dictionary dictionary = Dictionary.getInstance();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		ArrayList<ArrayList<String>> databaseRows = new ArrayList<ArrayList<String>>();

		try {

			parameters.put(Database.QueryFields.ID, account.getId());
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD_LINKED_NODES, parameters);

			while (result.next()) {
				ArrayList<String> row = new ArrayList<String>();
				row.add(result.getString("id_node"));
				row.add(result.getString("code"));
				row.add(result.getString("role"));
				databaseRows.add(row);
			}

			this.agentDatabase.closeQuery(result);

			for (ArrayList<String> row : databaseRows) {
				String id = row.get(0);
				String code = row.get(1);
				String role = row.get(2);

				if (!dictionary.existsRoleDefinition(role))
					continue;

				if (!producerRole.existsNonExpiredUserRole(role, account.getUser().getId()))
					continue;

				RoleDefinition roleDefinition = dictionary.getRoleDefinition(role);
				if (roleDefinition.isDisabled())
					continue;

				Node node = producerNode.lightLoad(id, code);

				if (!environmentNodesByRole.containsKey(role))
					environmentNodesByRole.put(role, new ArrayList<Node>());

				environmentNodesByRole.get(role).add(node);
			}

			if (environmentNodesByRole.size() > 0)
				account.setCurrentRole(environmentNodesByRole.keySet().iterator().next());

			account.setEnvironmentNodesByRole(environmentNodesByRole);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_ROOT_NODE, account.getUser().getName(), oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

	}

	private void loadDashboards(Account account) {
		ProducerRole producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);
		HashMap<String, ArrayList<Dashboard>> dashboardsByRole = new HashMap<String, ArrayList<Dashboard>>();
		Dictionary dictionary = Dictionary.getInstance();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		ArrayList<ArrayList<String>> databaseRows = new ArrayList<ArrayList<String>>();

		try {

			parameters.put(Database.QueryFields.ID, account.getId());
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD_LINKED_DASHBOARDS, parameters);

			while (result.next()) {
				ArrayList<String> row = new ArrayList<String>();
				row.add(result.getString("id_dashboard"));
				row.add(result.getString("role"));
				databaseRows.add(row);
			}

			this.agentDatabase.closeQuery(result);

			for (ArrayList<String> row : databaseRows) {
				String code = row.get(0);
				String role = row.get(1);

				if (!dictionary.existsRoleDefinition(role))
					continue;

				if (!producerRole.existsNonExpiredUserRole(role, account.getUser().getId()))
					continue;

				RoleDefinition roleDefinition = dictionary.getRoleDefinition(role);
				if (roleDefinition.isDisabled())
					continue;

				Dashboard dashboard = new Dashboard(dictionary.getDashboardDefinition(code));
				dashboard.setId(code);

				if (!dashboardsByRole.containsKey(role))
					dashboardsByRole.put(role, new ArrayList<Dashboard>());

				dashboardsByRole.get(role).add(dashboard);
			}

			if (dashboardsByRole.size() > 0 && (account.getCurrentRole() == null || account.getCurrentRole().isEmpty())) account.setCurrentRole(dashboardsByRole.keySet().iterator().next());

			account.setDashboardsByRole(dashboardsByRole);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_ROOT_NODE, account.getUser().getName(), oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

	}

	private void saveUser(User user) {
		UserInfo info = user.getInfo();
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, user.getId());
		parameters.put(Database.QueryFields.FULLNAME, info.getFullname());
		parameters.put(Database.QueryFields.EMAIL, info.getEmail());
		parameters.put(Database.QueryFields.PHOTO, info.getPhoto());
		parameters.put(Database.QueryFields.PREFERENCES, SerializerData.serialize(info.getPreferences()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ACCOUNT_SAVE, parameters);
	}

	public void injectAsCurrentAccount(String username, String language, UserInfo info) {
		Account account;
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Session session = this.getSession();

		if (session == null) throw new SessionException(ErrorCode.LOAD_USER, username);

		account = new Account();
		account.setCodeBusinessUnit(BusinessUnit.getInstance().getCode());
		session.setAccount(account); // DatabaseAgent needs codeBusinessUnit. DatabaseAgent recover account from session

		try {
			parameters.put(Database.QueryFields.USERNAME, username);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD_BY_USERNAME, parameters);

			if (result.next()) {
				account.getUser().setInfo(info);
				this.loadUser(account, result);
				this.agentDatabase.closeQuery(result);

				this.loadEnvironmentNodes(account);
				this.loadDashboards(account);
				if (!Language.supportsLanguage(language)) language = Language.SPANISH;

				account.getUser().setLanguage(language);
			} else {
				account = null;
			}
		} catch (Exception oException) {
			throw new SessionException(ErrorCode.LOAD_USER, username, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public void logout() {
		User user = new User();
		this.getAccount().setUser(user);
	}

	public Boolean exists(String id) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put(Database.QueryFields.ID, id);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_EXISTS, parameters);

			return result.next();
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public Account load(String id) {
		Account account;
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		account = new Account();

		try {
			parameters.put(Database.QueryFields.ID, id);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD, parameters);

			if (!result.next())
				throw new Exception(String.format("Account '%s' not found", id));

			this.loadUser(account, result);
			this.agentDatabase.closeQuery(result);

			this.loadEnvironmentNodes(account);
			this.loadDashboards(account);
		} catch (Exception oException) {
			throw new SessionException(ErrorCode.LOAD_USER, id, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return account;
	}

	public User loadUser(String id) {
		Account account;
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		account = new Account();

		try {
			parameters.put(Database.QueryFields.ID, id);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD, parameters);

			if (!result.next())
				throw new Exception(String.format("User with id '%s' not found", id));
			this.loadUser(account, result);
		} catch (Exception exception) {
			throw new SessionException(ErrorCode.LOAD_USER, id, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return account.getUser();
	}

	public boolean existsUserByUsername(String userName) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put(Database.QueryFields.USERNAME, userName);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD_BY_USERNAME, parameters);

			return result.next();
		} catch (Exception exception) {
			throw new SessionException(ErrorCode.LOAD_USER, userName, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public User loadUserByUsername(String userName) {
		Account account;
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		account = new Account();

		try {
			parameters.put(Database.QueryFields.USERNAME, userName);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD_BY_USERNAME, parameters);

			if (!result.next())
				throw new Exception(String.format("User '%s' not found", userName));

			this.loadUser(account, result);
		} catch (Exception exception) {
			throw new SessionException(ErrorCode.LOAD_USER, userName, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return account.getUser();
	}

	public ArrayList<String> loadUserIdsFromCodes(List<String> userCodes) {
		ArrayList<String> userIds = new ArrayList<String>();
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();

		StringBuilder builder = new StringBuilder();
		for (String userCode : userCodes) {
			builder.append("'");
			builder.append(userCode);
			builder.append("',");
		}
		builder.deleteCharAt(builder.length() - 1);

		subQueries.put(Database.QueryFields.USERS, builder.toString());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD_IDS_FROM_CODES, parameters, subQueries);

			while (result.next())
				userIds.add(result.getString("id"));
		} catch (Exception exception) {
			throw new SessionException(ErrorCode.LOAD_USER, builder.toString(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userIds;
	}

	public void save(Account account) {
		this.saveUser(account.getUser());

		MonetEvent event = new MonetEvent(MonetEvent.ACCOUNT_MODIFIED, null, account);
		event.addParameter(MonetEvent.PARAMETER_ACCOUNT, account);
		this.agentNotifier.notify(event);
	}

	public Account create(String id, String username, UserInfo info) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Account account = new Account();
		User user = account.getUser();

		account.setId(id);
		user.setId(id);
		user.setName(username);
		user.setCode(username);
		user.setInfo(info);

		parameters.put(Database.QueryFields.ID, id);
		parameters.put(Database.QueryFields.USERNAME, username);
		parameters.put(Database.QueryFields.FULLNAME, info.getFullname());
		parameters.put(Database.QueryFields.EMAIL, user.getInfo().getEmail());
		parameters.put(Database.QueryFields.PHOTO, user.getInfo().getPhoto());
		parameters.put(Database.QueryFields.PREFERENCES, SerializerData.serialize(user.getInfo().getPreferences()));
		parameters.put(Database.QueryFields.REGISTER_DATE, this.agentDatabase.getDateAsTimestamp(user.getInternalRegistrationDate()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ACCOUNT_CREATE, parameters);

		return account;
	}

	public void remove(String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.USERNAME, id);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ACCOUNT_REMOVE, parameters);
	}

	public Account locateAccount(String data) {
		Account account = null;
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put(Database.QueryFields.DATA, LibraryHTML.encode(data));
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOCATE, parameters);

			if (!result.next())
				return account;

			account = new Account();
			this.loadUser(account, result);
			this.agentDatabase.closeQuery(result);

			this.loadEnvironmentNodes(account);
			this.loadDashboards(account);
		} catch (Exception oException) {
			AgentLogger.getInstance().error(oException);
			account = null;
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return account;
	}

	public User getUserLinkedToNode(Node node) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		User user = null;

		try {
			parameters.put(Database.QueryFields.ID_NODE, node.getId());
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ACCOUNT_LOAD_LINKED_USER, parameters);

			if (!result.next())
				return user;

			user = this.loadUser(result.getString("id_user"));
		} catch (Exception oException) {
			AgentLogger.getInstance().error(oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return user;
	}

	public void linkToNode(Account account, Node node, String role) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_USER, account.getUser().getId());
		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		parameters.put(Database.QueryFields.ROLE, role);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ACCOUNT_LINK_TO_NODE, parameters);
	}

	public void unlinkFromNode(String idUser, String role) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_USER, idUser);
		parameters.put(Database.QueryFields.ROLE, role);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ACCOUNT_UNLINK_FROM_NODE, parameters);
	}

	public void linkToDashboard(Account account, Dashboard dashboard, String role) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_USER, account.getUser().getId());
		parameters.put(Database.QueryFields.ID_DASHBOARD, dashboard.getCode());
		parameters.put(Database.QueryFields.ROLE, role);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ACCOUNT_LINK_TO_DASHBOARD, parameters);
	}

	public void unlinkFromDashboard(String idUser, String role) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_USER, idUser);
		parameters.put(Database.QueryFields.ROLE, role);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ACCOUNT_UNLINK_FROM_DASHBOARD, parameters);
	}

	public Object newObject() {
		return new Account();
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		Account account = (Account) eventObject.getSource();

		if (attribute.equals(Account.ROLELIST)) {
			this.loadRoleList(account);
		}

	}

}
