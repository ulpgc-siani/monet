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

import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.DataRequest.SortBy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProducerTaskList extends Producer {
	ProducerTask producerTask;

	public ProducerTaskList() {
		super();
		this.producerTask = (ProducerTask) this.producersFactory.get(Producers.TASK);
	}

	public TaskList search(Account account, TaskSearchRequest searchRequest) {
		HashMap<String, Object> queryParameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		TaskList result = new TaskList();

		if (!prepareSearchLoadQuery(account.getId(), true, searchRequest, subQueries, queryParameters)) return result;

		this.addSortsByToQuery(searchRequest, subQueries);

		return this.loadItems(Database.Queries.TASK_LIST_LOAD, queryParameters, subQueries);
	}

	public int searchItemsCount(Account account, TaskSearchRequest searchRequest) {
		HashMap<String, Object> queryParameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ResultSet resultSet = null;
		int result = 0;

		if (!prepareSearchLoadQuery(account.getId(), false, searchRequest, subQueries, queryParameters)) return 0;

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_COUNT, queryParameters, subQueries);

			resultSet.next();
			result = resultSet.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASKLIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	public TaskList load(Account account, DataRequest dataRequest) {
		HashMap<String, Object> queryParameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();

		prepareLoadQuery(account.getId(), true, dataRequest.getStartPos(), dataRequest.getLimit(), subQueries, queryParameters, dataRequest.getParameters(), null);

		return this.loadItems(Database.Queries.TASK_LIST_LOAD, queryParameters, subQueries);
	}

	public Integer loadItemsCount(Account account, DataRequest dataRequest) {
		HashMap<String, Object> queryParameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ResultSet resultSet = null;
		Integer count;

		prepareLoadQuery(account.getId(), true, dataRequest.getStartPos(), dataRequest.getLimit(), subQueries, queryParameters, dataRequest.getParameters(), null);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_COUNT, queryParameters, subQueries);

			count = resultSet.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASKLIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return count;
	}

	public TaskList load(Node node, String state, String type) {
		HashMap<String, Object> queryParameters = new HashMap<String, Object>();
		String queryName, codeValue;

		queryName = Database.Queries.TASK_LIST_LOAD_NODE_ALL;
		if ((state.equals(TaskState.NEW)) || (state.equals(TaskState.PENDING))) {
			queryName = Database.Queries.TASK_LIST_LOAD_NODE_ACTIVE;
		}

		codeValue = this.getTypeValue(type);

		queryParameters.put(Database.QueryFields.ID_TARGET, node.getId());

		HashMap<String, String> subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.CODE, codeValue);

		return this.loadItems(queryName, queryParameters, subQueries);
	}

	public int loadLinkedWithNodeCount(String nodeId) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ArrayList<String> tasksIds = this.loadLinkedWithNodeTaskIds(nodeId);
		int counter;

		parameters.put(Database.QueryFields.ID, nodeId);
		subQueries.put(Database.QueryFields.IDS, "'" + LibraryArray.implode(tasksIds.toArray(new String[0]), "','") + "'");

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_LINKED_WITH_NODE_COUNT, parameters, subQueries);

			if (!result.next())
				throw new Exception(String.format("Can't get total count of linked nodes '%s'", nodeId));

			counter = result.getInt("counter");
		} catch (Exception oException) {
			throw new DataException(ErrorCode.NODE_LINKS_FROM_TASK_COUNT, nodeId, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return counter;
	}

	public TaskList loadLinkedWithNode(String nodeId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ArrayList<String> tasksIds = this.loadLinkedWithNodeTaskIds(nodeId);

		parameters.put(Database.QueryFields.ID, nodeId);
		subQueries.put(Database.QueryFields.IDS, "'" + LibraryArray.implode(tasksIds.toArray(new String[0]), "','") + "'");

		return this.loadItems(Database.Queries.TASK_LIST_LOAD_LINKED_WITH_NODE, parameters, subQueries);
	}

	public void save(TaskList taskList, String idTarget, String code) {
		Task task;
		Iterator<String> iter = taskList.get().keySet().iterator();
		String idOwner = this.getAccount().getUser().getId();
		ProducerTask producerTask = (ProducerTask) this.producersFactory.get(Producers.TASK);
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_TARGET, idTarget);
		parameters.put(Database.QueryFields.ID_OWNER, idOwner);
		parameters.put(Database.QueryFields.CODE, code);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_LIST_DELETE, parameters);

		while (iter.hasNext()) {
			task = (Task) taskList.get(iter.next());
			producerTask.create(task, null, false);
		}
	}

	public void delete(String tasks) {
		String[] tasksArray = tasks.split(Strings.COMMA);
		DatabaseRepositoryQuery[] queriesArray = new DatabaseRepositoryQuery[tasksArray.length];

		for (int iPos = 0; iPos < tasksArray.length; iPos++) {
			String name = Database.Queries.TASK_SAVE_END_DATE;
			HashMap<String, Object> parameters = new HashMap<String, Object>();

			parameters.put(Database.QueryFields.ID, tasksArray[iPos]);
			parameters.put(Database.QueryFields.END_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
			queriesArray[iPos] = new DatabaseRepositoryQuery(name, parameters);
		}

		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queriesArray);
		} catch (SQLException e) {
			this.agentLogger.error(e);
		}
	}

	public TaskFilters getFilters(String language) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		TaskFilters taskFilters = new TaskFilters();
		taskFilters.types = new ArrayList<TaskType>();

		parameters.put(Database.QueryFields.LANGUAGE, language);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_ACTIVE_TYPES, parameters);

			while (result.next()) {
				TaskType taskType = new TaskType();
				taskType.setCode(result.getString("code"));
				taskType.setLabel(result.getString("label"));

				taskFilters.types.add(taskType);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		taskFilters.states = new ArrayList<String>();
		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_STATES);

			while (result.next())
				taskFilters.states.add(result.getString("state"));

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return taskFilters;
	}

	public RoleList getRoles() {
		ResultSet result = null;
		RoleList roleList;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();

		try {
			roleList = new RoleList();

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_ROLES, parameters, subQueries);

			while (result.next()) {
				Role role = new UserRole();
				String roleCode = result.getString("code_role");
				if (roleCode == null || roleCode.isEmpty())
					continue;
				role.setCode(roleCode);
				roleList.add(role);
			}
			roleList.setTotalCount(roleList.getCount());

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_ROLELIST, "role list", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return roleList;
	}

	public UserList getOwners() {
		ResultSet result = null;
		UserList userList;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();

		try {
			userList = new UserList();

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_OWNERS, parameters, subQueries);

			while (result.next()) {
				User user = new User();
				user.setId(result.getString("id_owner"));
				user.getInfo().setFullname(result.getString("owner_fullname"));
				userList.add(user);
			}
			userList.setTotalCount(userList.getCount());

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERLIST, "owner list", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	public UserList getSenders() {
		ResultSet result = null;
		UserList userList;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();

		try {
			userList = new UserList();

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_SENDERS, parameters, subQueries);

			while (result.next()) {
				User user = new User();
				user.setId(result.getString("id_sender"));
				user.getInfo().setFullname(result.getString("sender_fullname"));
				userList.add(user);
			}
			userList.setTotalCount(userList.getCount());

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERLIST, "sender list", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	public UserList getSenders(String idOwner) {
		ResultSet result = null;
		UserList userList;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();

		try {
			userList = new UserList();

			parameters.put(Database.QueryFields.ID_USER, idOwner);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LIST_LOAD_SENDERS_OF_OWNER, parameters, subQueries);

			while (result.next()) {
				User user = new User();
				user.setId(result.getString("id_sender"));
				user.getInfo().setFullname(result.getString("sender_fullname"));
				userList.add(user);
			}
			userList.setTotalCount(userList.getCount());

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERLIST, "sender list", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	public Object newObject() {
		TaskList taskList;

		taskList = new TaskList();
		taskList.linkLoadListener(this);
		taskList.setTaskLink(TaskProvider.getInstance());

		return taskList;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

	private TaskList loadItems(String query, HashMap<String, Object> parameters, HashMap<String, String> subQueries) {
		ProducerTask producerTask = this.producersFactory.get(Producers.TASK);
		ResultSet resultSet = null;
		TaskList taskList;

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(query, parameters, subQueries);

			taskList = new TaskList();
			while (resultSet.next()) {
				Task task = new Task();
				producerTask.fillProperties(task, resultSet);
				task.linkLoadListener(this.producerTask);
				taskList.add(task);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASKLIST, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return taskList;
	}

	private String getTypeValue(String type) {
		String result;

		result = "'" + type + "'";
		if (type.equals(Strings.ALL)) {
			result = this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_TYPES);
		}

		return result;
	}

	private boolean prepareSearchLoadQuery(String idOwner, boolean isPaged, TaskSearchRequest searchRequest, HashMap<String, String> subQueries, HashMap<String, Object> queryParameters) {
		Map<String, String> searchParameters = searchRequest.getParameters();
		String taskSubQuery = null;
		String condition = searchRequest.getCondition();

		if (condition != null && !condition.isEmpty()) {
			taskSubQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_TASK);
			condition = this.agentDatabase.prepareAsFullTextCondition(condition);
			queryParameters.put(Database.QueryFields.CONDITION, condition);
		}

		prepareLoadQuery(idOwner, isPaged, searchRequest.getStartPos(), searchRequest.getLimit(), subQueries, queryParameters, searchParameters, taskSubQuery);

		return true;
	}

	private void prepareLoadQuery(String idOwner, boolean isPaged, int startPos, int limit, Map<String, String> subQueries, Map<String, Object> queryParameters, Map<String, String> searchParameters, String taskSubQuery) {
		String type, state, inbox, situation, role, urgent, sender, background;

		type = searchParameters.get(Task.Parameter.TYPE);
		state = searchParameters.get(Task.Parameter.STATE);
		inbox = searchParameters.get(Task.Parameter.INBOX);
		situation = searchParameters.get(Task.Parameter.SITUATION);
		role = searchParameters.get(Task.Parameter.ROLE);
		urgent = searchParameters.get(Task.Parameter.URGENT);
		sender = searchParameters.get(Task.Parameter.SENDER);
		background = searchParameters.get(Task.Parameter.BACKGROUND);

		if (inbox.equals(Task.Inbox.TASKBOARD)) {
			idOwner = null;
			String ownerParameter = searchParameters.get(Task.Parameter.OWNER);
			if (ownerParameter != null)
				idOwner = ownerParameter;
		}

		if (isPaged) {
			queryParameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(startPos));
			queryParameters.put(Database.QueryFields.LIMIT, limit);
		}

		subQueries.put(Database.QueryFields.TASK_SUBQUERY, taskSubQuery);
		subQueries.put(Database.QueryFields.TYPE_SUBQUERY, this.getTypeSubQuery(type, queryParameters));
		subQueries.put(Database.QueryFields.STATE_SUBQUERY, this.getStateSubQuery(state, queryParameters));
		subQueries.put(Database.QueryFields.SITUATION_SUBQUERY, this.getSituationSubQuery(situation, queryParameters));
		subQueries.put(Database.QueryFields.ROLE_SUBQUERY, this.getRoleSubQuery(role, queryParameters));
		subQueries.put(Database.QueryFields.URGENT_SUBQUERY, this.getUrgentSubQuery(urgent, queryParameters));
		subQueries.put(Database.QueryFields.OWNER_SUBQUERY, this.getOwnerSubQuery(inbox, idOwner, queryParameters));
		subQueries.put(Database.QueryFields.SENDER_SUBQUERY, this.getSenderSubQuery(sender, queryParameters));
		subQueries.put(Database.QueryFields.BACKGROUND_SUBQUERY, this.getBackgroundSubQuery(background, queryParameters));
	}

	private String getTypeSubQuery(String code, Map<String, Object> queryParameters) {
		if (code != null) {
			queryParameters.put(Database.QueryFields.CODE, code);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_TYPE);
		}
		return null;
	}

	private String getStateSubQuery(String description, Map<String, Object> queryParameters) {
		if (description != null) {
			queryParameters.put(Database.QueryFields.DESCRIPTION, description);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_DESCRIPTION);
		}
		return null;
	}

	private String getSituationSubQuery(String situation, Map<String, Object> queryParameters) {
		if (situation != null && !situation.equals("all")) {
			String queryName = null;

			if (situation.equals("alive")) queryName = Database.Queries.TASK_LIST_LOAD_SUBQUERY_SITUATION_ALIVE;
			if (situation.equals("active")) queryName = Database.Queries.TASK_LIST_LOAD_SUBQUERY_SITUATION_ACTIVE;
			if (situation.equals("pending")) queryName = Database.Queries.TASK_LIST_LOAD_SUBQUERY_SITUATION_PENDING;
			if (situation.equals("finished")) queryName = Database.Queries.TASK_LIST_LOAD_SUBQUERY_SITUATION_FINISHED;

			if (queryName == null) return null;

			return this.agentDatabase.getRepositoryQuery(queryName);
		}
		return null;
	}

	private String getRoleSubQuery(String role, Map<String, Object> queryParameters) {
		if (role != null && !role.equals("all")) {
			queryParameters.put(Database.QueryFields.ROLE, role);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_ROLE);
		}
		return null;
	}

	private String getUrgentSubQuery(String urgent, Map<String, Object> queryParameters) {
		if (urgent != null) {
			queryParameters.put(Database.QueryFields.URGENT, urgent);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_URGENT);
		}
		return null;
	}

	private String getOwnerSubQuery(String inbox, String idOwner, Map<String, Object> queryParameters) {

		if (inbox.equals("tasktray")) {
			queryParameters.put(Database.QueryFields.ID_OWNER, idOwner);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_OWNER);
		} else if (idOwner != null) {

			if (idOwner.equals("-1"))
				return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_OWNER_NULL);

			queryParameters.put(Database.QueryFields.ID_OWNER, idOwner);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_OWNER);
		}

		return null;
	}

	private String getSenderSubQuery(String sender, Map<String, Object> queryParameters) {

		if (sender != null && !sender.equals("all")) {
			queryParameters.put(Database.QueryFields.ID_SENDER, sender);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_SENDER);
		}

		return null;
	}

	private String getBackgroundSubQuery(String background, Map<String, Object> queryParameters) {

		if (background != null) {
			queryParameters.put(Database.QueryFields.BACKGROUND, background);
			return this.agentDatabase.getRepositoryQuery(Database.Queries.TASK_LIST_LOAD_SUBQUERY_BACKGROUND);
		}

		return null;
	}

	private void addSortsByToQuery(TaskSearchRequest searchRequest, Map<String, String> queryParams) {
		String queryParameters = new String();
		List<SortBy> sortsBy = searchRequest.getSortsBy();

		for (SortBy sortBy : sortsBy) {
			queryParameters += sortBy.attribute() + " " + sortBy.mode();
			queryParameters += Strings.COMMA;
		}
		if (queryParameters.length() > 0) queryParameters = queryParameters.substring(0, queryParameters.length() - 1);
		else queryParameters = "createdate " + this.agentDatabase.getOrderMode(Common.OrderMode.ASCENDANT);

		queryParams.put(Database.QueryFields.SORTS_BY, queryParameters);
	}

	private ArrayList<String> loadLinkedWithNodeTaskIds(String nodeId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ArrayList<String> tasksIds = new ArrayList<String>();
		ResultSet resultSet = null;

		try {
			parameters.put(Database.QueryFields.ID_TARGET, nodeId);
			parameters.put(Database.QueryFields.TYPE_TARGET, LinkType.NODE);
			parameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.TASK);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.LINK_SOURCES, parameters, null);

			while (resultSet.next())
				tasksIds.add(resultSet.getString("id_source"));

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK_IDS, nodeId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return tasksIds;
	}

}