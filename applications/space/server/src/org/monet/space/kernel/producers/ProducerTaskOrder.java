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
import org.monet.space.kernel.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProducerTaskOrder extends Producer {

	public ProducerTaskOrder() {
		super();
	}

	private void loadChat(TaskOrder taskOrder) {
		Chat chat = new Chat();
		chat.linkLoadListener(this);
		chat.setChatLink(ChatProvider.getInstance());
		taskOrder.setChat(chat);
	}

	private void loadRole(TaskOrder taskOrder) {
		ProducerRole producerRole;
		Role role;

		if (taskOrder.getRoleId() == null)
			return;

		producerRole = this.producersFactory.get(Producers.ROLE);
		role = producerRole.load(taskOrder.getRoleId());
		taskOrder.setRole(role);
	}

	public void fillProperties(TaskOrder taskOrder, ResultSet result) throws SQLException {
		taskOrder.setId(result.getString("id"));
		taskOrder.setTaskId(result.getString("id_task"));
		taskOrder.setSetupNodeId(result.getString("id_setup_node"));
		taskOrder.setRoleId(result.getString("id_role"));
		taskOrder.setCode(result.getString("code"));
		taskOrder.setType(result.getString("type"));
		taskOrder.setPartnerContext(result.getString("partner_context"));
		taskOrder.setComments(result.getString("comments"));
		taskOrder.setUrgent(result.getBoolean("urgent"));
		taskOrder.setClosed(result.getBoolean("closed"));
		taskOrder.setNewMessagesCount(result.getInt("new_messages"));
		taskOrder.setCreateDate(result.getTimestamp("create_date"));
		taskOrder.setSuggestedStartDate(result.getTimestamp("suggested_start_date"));
		taskOrder.setSuggestedEndDate(result.getTimestamp("suggested_end_date"));
	}

	public TaskOrder load(String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		TaskOrder taskOrder;

		parameters.put(Database.QueryFields.ID, id);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ORDER_LOAD, parameters);

			if (!result.next())
				throw new Exception(String.format("Task order '%s' not found", id));

			taskOrder = new TaskOrder();
			taskOrder.setId(id);
			taskOrder.linkLoadListener(this);
			taskOrder.enablePartialLoading();

			this.fillProperties(taskOrder, result);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return taskOrder;
	}

	public TaskOrder loadByCode(String taskId, String code) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;
		TaskOrder taskOrder;

		parameters.put(Database.QueryFields.ID_TASK, taskId);
		parameters.put(Database.QueryFields.CODE, code);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ORDER_LOAD_BY_CODE, parameters);

			if (!result.next())
				throw new Exception(String.format("Task order '%s' not found", taskId + " - " + code));

			taskOrder = new TaskOrder();
			taskOrder.linkLoadListener(this);
			taskOrder.enablePartialLoading();

			this.fillProperties(taskOrder, result);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return taskOrder;
	}

	public RoleList loadRoles(String idTask) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		ProducerRole producerRole = this.producersFactory.get(Producers.ROLE);
		RoleList result = new RoleList();

		try {
			parameters.put(Database.QueryFields.ID_TASK, idTask);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ORDER_LIST_LOAD_ROLES, parameters, queryParams);

			while (resultSet.next()) {
				Role role = producerRole.fill(resultSet);
				result.add(role);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASKORDERLIST, idTask, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	public void save(TaskOrder taskOrder) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, taskOrder.getId());
		parameters.put(Database.QueryFields.ID_SETUP_NODE, taskOrder.getSetupNodeId());
		parameters.put(Database.QueryFields.COMMENTS, taskOrder.getComments());
		parameters.put(Database.QueryFields.URGENT, taskOrder.isUrgent() ? 1 : 0);
		parameters.put(Database.QueryFields.CLOSED, taskOrder.isClosed() ? 1 : 0);
		parameters.put(Database.QueryFields.SUGGESTED_START_DATE, this.agentDatabase.getDateAsTimestamp(taskOrder.getInternalSuggestedStartDate()));
		parameters.put(Database.QueryFields.SUGGESTED_END_DATE, this.agentDatabase.getDateAsTimestamp(taskOrder.getInternalSuggestedEndDate()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ORDER_SAVE, parameters);
	}

	public void saveHasMessages(TaskOrder taskOrder) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, taskOrder.getId());
		parameters.put(Database.QueryFields.NEW_MESSAGES, taskOrder.getNewMessagesCount());

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ORDER_SAVE, parameters);
	}

	private void create(TaskOrder taskOrder) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_TASK, taskOrder.getTaskId());
		parameters.put(Database.QueryFields.ID_SETUP_NODE, taskOrder.getSetupNodeId());
		parameters.put(Database.QueryFields.ID_ROLE, taskOrder.getRoleId());
		parameters.put(Database.QueryFields.CODE, taskOrder.getCode());
		parameters.put(Database.QueryFields.TYPE, taskOrder.getType().toString());
		parameters.put(Database.QueryFields.PARTNER_CONTEXT, taskOrder.getPartnerContext());
		parameters.put(Database.QueryFields.COMMENTS, taskOrder.getComments());
		parameters.put(Database.QueryFields.URGENT, taskOrder.isUrgent() ? 1 : 0);
		parameters.put(Database.QueryFields.CLOSED, taskOrder.isClosed() ? 1 : 0);
		parameters.put(Database.QueryFields.NEW_MESSAGES, taskOrder.getNewMessagesCount());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(taskOrder.getInternalCreateDate()));
		parameters.put(Database.QueryFields.SUGGESTED_START_DATE, this.agentDatabase.getDateAsTimestamp(taskOrder.getInternalSuggestedStartDate()));
		parameters.put(Database.QueryFields.SUGGESTED_END_DATE, this.agentDatabase.getDateAsTimestamp(taskOrder.getInternalSuggestedEndDate()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.TASK_ORDER_CREATE, parameters);
		taskOrder.setId(id);

		this.agentNotifier.notify(new MonetEvent(MonetEvent.TASK_ORDER_CREATED, null, taskOrder));

		taskOrder.linkLoadListener(this);
	}

	public TaskOrder create(Task task, String code, TaskOrder.Type type, Date suggestedStartDate, Date suggestedEndDate, String comments, boolean urgent, String partnerContext) {

		TaskOrder taskOrder = new TaskOrder();
		taskOrder.setCode(code);
		taskOrder.setTaskId(task.getId());
		taskOrder.setType(type);
		taskOrder.setPartnerContext(partnerContext);
		taskOrder.setComments(comments);
		taskOrder.setUrgent(urgent);
		taskOrder.setClosed(false);
		taskOrder.setCreateDate(new Date());
		taskOrder.setSuggestedStartDate(suggestedStartDate);
		taskOrder.setSuggestedEndDate(suggestedEndDate);

		this.create(taskOrder);

		return taskOrder;
	}

	public TaskOrder create(Task task, String code, String roleId, TaskOrder.Type type, boolean urgent) {

		TaskOrder taskOrder = new TaskOrder();
		taskOrder.setCode(code);
		taskOrder.setTaskId(task.getId());
		taskOrder.setRoleId(roleId);
		taskOrder.setType(type);
		taskOrder.setUrgent(urgent);
		taskOrder.setClosed(false);
		taskOrder.setCreateDate(new Date());

		this.create(taskOrder);

		return taskOrder;
	}

	public void remove(String id) {
	}

	public Object newObject() {
		return new TaskOrder();
	}

	public LinkedList<ChatItem> loadChatItems(String orderId, DataRequest dataRequest) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		LinkedList<ChatItem> resultList = new LinkedList<ChatItem>();
		int limit = dataRequest.getLimit();

		if (limit == -1)
			limit = this.loadChatItemsCount(orderId);

		parameters.put(Database.QueryFields.ID_ORDER, orderId);
		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
		parameters.put(Database.QueryFields.LIMIT, limit);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ORDER_CHAT_LOAD_ENTRIES, parameters);

			while (resultSet.next()) {
				ChatItem chatItem = new ChatItem();

				chatItem.setId(resultSet.getString("id"));
				chatItem.setOrderId(resultSet.getString("id_order"));
				chatItem.setMessage(resultSet.getString("message"));
				chatItem.setType(resultSet.getString("type"));
				chatItem.setSent(resultSet.getBoolean("sent"));
				chatItem.setCreateDate(resultSet.getTimestamp("create_date"));

				resultList.add(chatItem);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_CHAT_ENTRIES, orderId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return resultList;
	}

	public int loadChatItemsCount(String orderId) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		int count = 0;

		parameters.put(Database.QueryFields.ID_ORDER, orderId);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ORDER_CHAT_LOAD_ENTRIES_COUNT, parameters);
			resultSet.next();
			count = resultSet.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_CHAT_ENTRIES, orderId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return count;
	}

	public void addChatItem(Task task, String idOrder, ChatItem chatItem) {
		HashMap<String, Object> parameters = new HashMap<>();

		if (chatItem.getType().equals(ChatItem.Type.in)) {
			parameters.put(Database.QueryFields.ID, idOrder);
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ORDER_INCREMENT_NEW_MESSAGES, parameters);

			parameters.put(Database.QueryFields.ID, task.getId());
			this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_UPDATE_NEW_MESSAGES, parameters);

			HashMap<String, Object> notifyParameters = new HashMap<>();
			TaskOrder taskOrder = this.load(idOrder);

			notifyParameters.put(MonetEvent.PARAMETER_ORDER_ID, idOrder);
			notifyParameters.put(MonetEvent.PARAMETER_TITLE, getChatItemTitle(task, taskOrder));
			notifyParameters.put(MonetEvent.PARAMETER_BODY, getChatItemBody(task, taskOrder));
			this.agentNotifier.notify(new MonetEvent(MonetEvent.TASK_NEW_MESSAGES_RECEIVED, null, task, notifyParameters));
		} else {
			HashMap<String, Object> notifyParameters = new HashMap<>();
			notifyParameters.put(MonetEvent.PARAMETER_ORDER_ID, idOrder);
			this.agentNotifier.notify(new MonetEvent(MonetEvent.TASK_NEW_MESSAGES_SENT, null, task, notifyParameters));
		}

		parameters.clear();
		parameters.put(Database.QueryFields.ID_ORDER, idOrder);
		parameters.put(Database.QueryFields.MESSAGE, chatItem.getMessage());
		parameters.put(Database.QueryFields.TYPE, chatItem.getType().toString());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(chatItem.getInternalCreateDate()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.TASK_ORDER_CHAT_ADD_ENTRY, parameters);
		chatItem.setId(id);
	}

	private String getChatItemTitle(Task task, TaskOrder taskOrder) {
		return task.getLabel();
	}

	private String getChatItemBody(Task task, TaskOrder taskOrder) {
		Language language = Language.getInstance();
		String title = language.getMessage(MessageCode.NEW_MESSAGE_RECEIVED, Language.getCurrent());

		if (taskOrder.getRole() != null)
			title = language.getMessage(MessageCode.NEW_MESSAGE_RECEIVED_FROM, Language.getCurrent()) + " " + taskOrder.getRole().getLabel();

		return title;
	}

	public void resetOrderNewMessages(Task task, String idOrder) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, idOrder);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ORDER_RESET_NEW_MESSAGES, parameters);

		parameters.put(Database.QueryFields.ID, task.getId());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_UPDATE_NEW_MESSAGES, parameters);
	}

	public void updateChatItemStateToSent(String chatItemId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, chatItemId);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ORDER_CHAT_UPDATE_ENTRY_STATE_TO_SENT, parameters);
	}

	public void loadAttribute(EventObject oEventObject, String sAttribute) {
		TaskOrder taskOrder = (TaskOrder) oEventObject.getSource();

		if (sAttribute.equals(TaskOrder.CHAT)) {
			this.loadChat(taskOrder);
		} else if (sAttribute.equals(TaskOrder.ROLE)) {
			this.loadRole(taskOrder);
		}
	}

}
