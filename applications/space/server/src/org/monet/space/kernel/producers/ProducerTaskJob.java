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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.monet.metamodel.JobDefinition;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

public class ProducerTaskJob extends Producer {

	public List<org.monet.mobile.model.Task> loadNotRead(String ownerId, long syncMark) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;
		Dictionary dictionary = Dictionary.getInstance();
		List<org.monet.mobile.model.Task> tasks = new ArrayList<>();

		parameters.put(Database.QueryFields.ID_OWNER, ownerId);
		parameters.put(Database.QueryFields.SYNC_MARK, this.agentDatabase.getDateAsTimestamp(new Date(syncMark)));

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_NOT_READ, parameters);

			while (result.next()) {
				tasks.add(createTask(result, dictionary));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			agentDatabase.closeQuery(result);
		}

		return tasks;
	}

	public List<org.monet.mobile.model.Task> loadAvailableNotRead(String userId, long syncMark) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;
		Dictionary dictionary = Dictionary.getInstance();
		List<org.monet.mobile.model.Task> tasks = new ArrayList<>();

		parameters.put(Database.QueryFields.ID_USER, userId);
		parameters.put(Database.QueryFields.DATE, agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.SYNC_MARK, agentDatabase.getDateAsTimestamp(new Date(syncMark)));

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_AVAILABLE_NOT_READ, parameters);

			while (result.next())
				tasks.add(createTask(result, dictionary));
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return tasks;
	}

	public List<String> loadAssignedJobsToDelete(String ownerId, List<String> ids) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subqueries = new HashMap<>();
		ResultSet result = null;
		List<String> taskIds = new ArrayList<>();

		if (ids.size() == 0)
			return taskIds;

		parameters.put(Database.QueryFields.ID_OWNER, ownerId);

		subqueries.put(Database.QueryFields.IDS, LibraryString.implodeAndWrap(ids.toArray(new String[ids.size()]), ",", "'"));

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_ASSIGNED_TO_DELETE, parameters, subqueries);

			while (result.next()) {
				taskIds.add(result.getString("id"));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return taskIds;
	}

	public List<String> loadFinishedJobsToDelete(String ownerId, List<String> ids) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subqueries = new HashMap<>();
		ResultSet result = null;
		List<String> taskIds = new ArrayList<>();

		if (ids.size() == 0)
			return taskIds;

		parameters.put(Database.QueryFields.ID_OWNER, ownerId);

		subqueries.put(Database.QueryFields.IDS, LibraryString.implodeAndWrap(ids.toArray(new String[ids.size()]), ",", "'"));

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_FINISHED_TO_DELETE, parameters, subqueries);

			while (result.next()) {
				taskIds.add(result.getString("id"));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return taskIds;
	}

	public List<String> loadUnassignedJobsToDelete(List<String> ids) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subqueries = new HashMap<>();
		ResultSet result = null;
		List<String> taskIds = new ArrayList<>();

		if (ids.size() == 0)
			return taskIds;

		subqueries.put(Database.QueryFields.IDS, LibraryString.implodeAndWrap(ids.toArray(new String[ids.size()]), ",", "'"));

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_UNASSIGNED_TO_DELETE, parameters, subqueries);

			while (result.next()) {
				taskIds.add(result.getString("id"));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return taskIds;
	}

	private org.monet.mobile.model.Task createTask(ResultSet result, Dictionary dictionary) throws Exception {
		org.monet.mobile.model.Task task = new org.monet.mobile.model.Task();
		task.ID = result.getString("id");
		task.Context = result.getString("partner_context");
		task.Label = result.getString("label");
		task.Description = result.getString("description");
		task.Urgent = result.getBoolean("urgent");
		Timestamp suggestedStartDate = result.getTimestamp("suggested_start_date");
		task.SuggestedStartDate = suggestedStartDate != null ? suggestedStartDate.getTime() : null;
		Timestamp suggestedEndDate = result.getTimestamp("suggested_end_date");
		task.SuggestedEndDate = suggestedEndDate != null ? suggestedEndDate.getTime() : null;
		task.Comments = result.getString("comments");

		String code = result.getString("code");
		JobDefinition jobDefinition = (JobDefinition) dictionary.getDefinition(code);
		task.StepCount = jobDefinition.getStepList().size();
		task.Code = code;

		Geometry geometry = agentDatabase.getGeometryColumn(result, "geometry");
		if (geometry != null) {
			Point centroid = geometry.getCentroid();
			task.PositionLat = centroid.getX();
			task.PositionLon = centroid.getY();
		}

		return task;
	}

	public void loadRequest(String taskId, OutputStream output) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;

		parameters.put(Database.QueryFields.ID_TASK, taskId);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_REQUEST, parameters);

			if (result.next()) {
				Blob blob = result.getBlob(1);
				if (blob != null)
					StreamHelper.copyData(blob.getBinaryStream(), output);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

	}

	public void loadResponse(String taskId, OutputStream output) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;

		parameters.put(Database.QueryFields.ID_TASK, taskId);

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_RESPONSE, parameters);

			if (result.next()) {
				Blob blob = result.getBlob(1);
				if (blob != null)
					StreamHelper.copyData(blob.getBinaryStream(), output);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			agentDatabase.closeQuery(result);
		}

	}

	public String create(Task task, InputStream request, String callbackTaskId, String callbackCode, String callbackOrderId) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_TASK, task.getId());
		parameters.put(Database.QueryFields.REQUEST, request);
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.CALLBACK_TASK_ID, callbackTaskId);
		parameters.put(Database.QueryFields.CALLBACK_CODE, callbackCode);
		parameters.put(Database.QueryFields.CALLBACK_ORDER_ID, callbackOrderId);

        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_CREATE, parameters);

		MonetEvent event = new MonetEvent(MonetEvent.TASK_JOB_NEW, null, task.getId());
		event.addParameter(MonetEvent.PARAMETER_USER_ID, task.getOwnerId());
		this.agentNotifier.notify(event);

        return task.getId();
	}

    public void remove(String taskId) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(Database.QueryFields.ID_TASK, taskId);
        agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_REMOVE, parameters);
    }

    public void save(String taskId, InputStream response) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_TASK, taskId);
		parameters.put(Database.QueryFields.RESPONSE, response);
		parameters.put(Database.QueryFields.FINISH_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_SAVE_RESPONSE, parameters);
	}

	public void addAttachment(String taskId, String attachmentId) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_TASK, taskId);
		parameters.put(Database.QueryFields.ID_ATTACHMENT, attachmentId);

		agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_ADD_ATTACHMENT, parameters);
	}

	public List<String> loadAttachments(String taskId) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;
		List<String> attachmentList = new ArrayList<>();

		parameters.put(Database.QueryFields.ID_TASK, taskId);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOB_LOAD_ATTACHMENTS, parameters);

			while (result.next())
				attachmentList.add(result.getString("id_attachment"));
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return attachmentList;
	}

	public JobCallbackTask getJobCallbackTask(String taskId) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;

		parameters.put(Database.QueryFields.ID_TASK, taskId);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_CALLBACK_TASK_ID, parameters);

			if (result.next()) {
				JobCallbackTask jobCallbackTask = new JobCallbackTask();
				jobCallbackTask.setId(result.getString("callback_task_id"));
				jobCallbackTask.setCode(result.getString("callback_code"));
				return jobCallbackTask;
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
		return null;
	}

	public List<org.monet.mobile.model.Task> loadAvailableTasks(String userId, int offset, int count) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;
		Dictionary dictionary = Dictionary.getInstance();
		List<org.monet.mobile.model.Task> tasks = new ArrayList<>();

		parameters.put(Database.QueryFields.ID_USER, userId);
		parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.START_POS, offset);
		parameters.put(Database.QueryFields.LIMIT, count);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOBS_LOAD_AVAILABLE, parameters);

			while (result.next())
				tasks.add(createTask(result, dictionary));
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return tasks;
	}

	public void assignJob(Account account, String jobId) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_TASK, jobId);
		parameters.put(Database.QueryFields.ID_OWNER, account.getId());
		parameters.put(Database.QueryFields.OWNER_FULLNAME, account.getUser().getInfo().getFullname());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_ASSIGN, parameters);

		parameters.clear();
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(Database.QueryFields.JOBS, jobId);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_REFRESH_TIME_MARK, parameters, subQueries);

		MonetEvent event = new MonetEvent(MonetEvent.TASK_JOB_NEW, null, jobId);
		event.addParameter(MonetEvent.PARAMETER_USER_ID, account.getId());
		this.agentNotifier.notify(event);
	}

	public void unassignJob(Account account, String jobId) {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_TASK, jobId);
		parameters.put(Database.QueryFields.ID_OWNER, account.getId());
		parameters.put(Database.QueryFields.AVAILABLE_DATE, this.agentDatabase.getDateAsTimestamp(new java.util.Date()));

		if (this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_JOB_UNASSIGN, parameters) != 1)
			throw new RuntimeException(String.format("Job %s not assigned to user %s", jobId, account.getId()));

		MonetEvent event = new MonetEvent(MonetEvent.TASK_JOB_UNASSIGN, null, jobId);
		event.addParameter(MonetEvent.PARAMETER_USER_ID, account.getId());
		this.agentNotifier.notify(event);
	}

	public void addChatListItem(String jobId, ChatItem chatItem) {
		ProducerTaskOrder producerTaskOrder = producersFactory.get(Producers.TASKORDER);
		ProducerTask producerTask = producersFactory.get(Producers.TASK);
		ResultSet resultSet = null;

		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.ID_TASK, jobId);

		String orderId = null;

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOB_LOAD_ORDER_ID, parameters);

			if (resultSet.next())
				orderId = resultSet.getString("id_order");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		if (orderId == null)
			return;

		TaskOrder taskOrder = producerTaskOrder.load(orderId);
		Task task = producerTask.load(taskOrder.getTaskId());
		producerTaskOrder.addChatItem(task, orderId, chatItem);

		MonetEvent event = new MonetEvent(MonetEvent.TASK_ORDER_CHAT_MESSAGE_RECEIVED, null, task.getId());
		event.addParameter(MonetEvent.PARAMETER_PROVIDER, taskOrder.getPartnerContext());
		event.addParameter(MonetEvent.PARAMETER_CODE, taskOrder.getPartnerContext());
		event.addParameter(MonetEvent.PARAMETER_ORDER_ID, orderId);
		agentNotifier.notify(event);
	}

	public List<ChatItem> loadJobNewChatItems(String userId, long syncMark) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet resultSet = null;
		List<ChatItem> chatItems = new ArrayList<>();

		parameters.put(Database.QueryFields.ID_USER, userId);
		parameters.put(Database.QueryFields.SYNC_MARK, this.agentDatabase.getDateAsTimestamp(new Date(syncMark)));

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOB_LOAD_NEW_CHAT_LIST_ITEM, parameters);

			while (resultSet.next()) {
				ChatItem chatItem = new ChatItem();
				chatItem.setId(resultSet.getString("id"));
				chatItem.setTaskId(resultSet.getString("id_task"));
				chatItem.setOrderId(resultSet.getString("id_order"));
				chatItem.setMessage(resultSet.getString("message"));
				chatItem.setType(resultSet.getString("type"));
				chatItem.setSent(resultSet.getBoolean("sent"));
				chatItem.setCreateDate(resultSet.getTimestamp("create_date"));
				chatItems.add(chatItem);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return chatItems;
	}

	public String loadUserIdForTaskOrder(String taskOrderId) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet resultSet = null;

		parameters.put(Database.QueryFields.ID_ORDER, taskOrderId);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_JOB_LOAD_USER_ID_FROM_ORDER, parameters);

			if (resultSet.next()) {
				return resultSet.getString("id_owner");
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return null;
	}

	public Object newObject() {
		return new TaskOrder();
	}

	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}
}
