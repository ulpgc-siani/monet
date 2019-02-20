package org.monet.space.kernel.producers;

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.MessageQueueItem;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.*;

public class ProducerMessageQueue extends Producer {

	public List<MessageQueueItem> loadPendings() {
		List<MessageQueueItem> results = new ArrayList<MessageQueueItem>();

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet resultSet = null;

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MESSAGEQUEUE_LIST_LOAD_PENDING, parameters);

			while (resultSet.next()) {
				MessageQueueItem item = new MessageQueueItem();
				item.setId(resultSet.getString("id"));
				item.setCode(resultSet.getString("code"));
				item.setType(resultSet.getString("type"));
				item.setUri(resultSet.getString("uri"));
				item.setState(resultSet.getString("state"));
				item.setRetries(resultSet.getInt("retries"));
				item.setHash(resultSet.getString("hash"));
				item.setOrderId(resultSet.getString("order_id"));
				item.setLastUpdateTime(resultSet.getTimestamp("last_update_time"));

				results.add(item);
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_MESSAGE_QUEUE, "", oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return results;
	}

	public InputStream getMessageStream(MessageQueueItem item) {
		InputStream resultStream = null;

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, item.getId());

		ResultSet resultSet = null;
		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MESSAGEQUEUE_LOAD_CONTENT, parameters);

			if (resultSet.next()) {
				InputStream dbStream = null;
				ByteArrayOutputStream memStream = null;
				try {
					dbStream = resultSet.getBinaryStream("message");
					memStream = new ByteArrayOutputStream();
					StreamHelper.copyData(dbStream, memStream);
					resultStream = new ByteArrayInputStream(memStream.toByteArray());
				} finally {
					StreamHelper.close(dbStream);
					StreamHelper.close(memStream);
				}
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_MESSAGE_QUEUE, "", oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return resultStream;
	}

	public long insert(String orderId, String uri, InputStream message, String code, String type, String hash) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_ORDER, orderId);
		parameters.put(Database.QueryFields.URI, uri);
		parameters.put(Database.QueryFields.MESSAGE, message);
		parameters.put(Database.QueryFields.CODE, code);
		parameters.put(Database.QueryFields.TYPE, type);
		parameters.put(Database.QueryFields.HASH, hash);
		parameters.put(Database.QueryFields.CREATION_TIME, this.agentDatabase.getDateAsTimestamp(new Date()));
		parameters.put(Database.QueryFields.LAST_UPDATE_TIME, this.agentDatabase.getDateAsTimestamp(new Date()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.MESSAGEQUEUE_INSERT, parameters);

		return Long.parseLong(id);
	}

	public void save(MessageQueueItem item) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, item.getId());
		parameters.put(Database.QueryFields.STATE, item.getState());
		parameters.put(Database.QueryFields.RETRIES, item.getRetries());
		parameters.put(Database.QueryFields.LAST_ERROR, item.getErrorMessage());
		parameters.put(Database.QueryFields.LAST_UPDATE_TIME, this.agentDatabase.getDateAsTimestamp(new Date()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MESSAGEQUEUE_UPDATE, parameters);
	}

	public void delete(MessageQueueItem item) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, item.getId());

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.MESSAGEQUEUE_REMOVE, parameters);
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
