package org.monet.space.kernel.producers;

import org.monet.space.kernel.agents.AgentException;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DatabaseException;
import org.monet.space.kernel.model.WorkQueueItem;
import org.monet.space.kernel.model.WorkQueueState;
import org.monet.space.kernel.model.WorkQueueType;

import java.sql.ResultSet;
import java.util.*;

public class ProducerWorkQueue extends Producer {

	public List<WorkQueueItem> loadPending() {
		ResultSet resultSet = null;
		ArrayList<WorkQueueItem> items = new ArrayList<WorkQueueItem>();

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.WORKQUEUE_LIST_LOAD_PENDING);
			while (resultSet.next()) {
				WorkQueueItem workItem = new WorkQueueItem();
				workItem.linkLoadListener(this);
				workItem.setId(resultSet.getString("id"));
				workItem.setType(WorkQueueType.valueOf(resultSet.getInt("type")));
				workItem.setState(WorkQueueState.valueOf(resultSet.getInt("state")));
				workItem.setRetries(resultSet.getInt("retries"));
				workItem.setLastUpdateTime(resultSet.getTimestamp("last_update_time"));
				items.add(workItem);
			}
		} catch (Exception e) {
			AgentException.getInstance().error(e);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return items;
	}

	public void queueNew(WorkQueueItem workItem) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.TARGET, workItem.getTarget());
		parameters.put(Database.QueryFields.TYPE, workItem.getType().ordinal());
		parameters.put(Database.QueryFields.STATE, workItem.getState().ordinal());
		parameters.put(Database.QueryFields.CREATION_TIME, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.LAST_UPDATE_TIME, this.agentDatabase.getDateAsTimestamp(new Date()));
		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.WORKQUEUE_ADD, parameters);
		workItem.setId(id);
	}

	public void updateWithError(String workItemId, String errorMessage) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, workItemId);
		parameters.put(Database.QueryFields.LAST_ERROR, errorMessage);
		parameters.put(Database.QueryFields.LAST_UPDATE_TIME, this.agentDatabase.getDateAsTimestamp(new Date()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.WORKQUEUE_UPDATE_WITH_ERROR, parameters);
	}

	public void updateToFinish(String workItemId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, workItemId);
		parameters.put(Database.QueryFields.LAST_UPDATE_TIME, this.agentDatabase.getDateAsTimestamp(new Date()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.WORKQUEUE_UPDATE_FINISHED, parameters);
	}

	public void remove(String workItemId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, workItemId);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.WORKQUEUE_REMOVE, parameters);
	}

	private void loadTarget(WorkQueueItem workItem) {
		ResultSet resultSet = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put(Database.QueryFields.ID, workItem.getId());
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.WORKQUEUE_LOAD_TARGET, parameters);
			if (resultSet.next()) {
				workItem.setTarget(resultSet.getString("target"));
			}
		} catch (Exception e) {
			throw new DatabaseException(ErrorCode.WORKQUEUE_ITEM, workItem.getId(), e);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}
	}

	@Override
	public void loadAttribute(EventObject eventObject, String attribute) {
		WorkQueueItem workItem = (WorkQueueItem) eventObject.getSource();

		if (attribute.equals(WorkQueueItem.TARGET))
			this.loadTarget(workItem);
	}

}
