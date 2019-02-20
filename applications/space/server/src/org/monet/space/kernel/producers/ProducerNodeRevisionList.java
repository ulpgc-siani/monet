package org.monet.space.kernel.producers;

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.Revision;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProducerNodeRevisionList extends Producer {

	private ArrayList<String> loadNodeIds(String nodeId) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ArrayList<String> nodesIds = new ArrayList<String>();
		ResultSet resultSet = null;

		try {
			parameters.put(Database.QueryFields.ID_NODE, nodeId);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_REVISION_LIST_LOAD_NODE_IDS, parameters, null);

			while (resultSet.next())
				nodesIds.add(resultSet.getString("id_node"));

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE_IDS, nodeId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return nodesIds;
	}

	public int loadItemsCount(String nodeId, DataRequest dataRequest) {
		ResultSet result;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		ArrayList<String> nodesIds = this.loadNodeIds(nodeId);
		int counter = 0;

		parameters.put(Database.QueryFields.ID_NODE, nodeId);
		subQueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_REVISION_LIST_LOAD_ITEMS_COUNT, parameters, subQueries);
		try {
			if (!result.next())
				throw new Exception(String.format("Can't get revisions total count of '%s'", nodeId));
			counter = result.getInt("counter");
		} catch (Exception e) {
			throw new DataException(ErrorCode.NODE_LOAD_REVISIONS, nodeId, e);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return counter;
	}

	public LinkedHashMap<String, Revision> loadItems(String nodeId, DataRequest dataRequest) {
		ResultSet result;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		LinkedHashMap<String, Revision> resultMap = new LinkedHashMap<String, Revision>();
		ProducerNodeRevision producerRevision = (ProducerNodeRevision) this.producersFactory.get(Producers.REVISION);
		ArrayList<String> nodesIds = this.loadNodeIds(nodeId);

		parameters.put(Database.QueryFields.ID_NODE, nodeId);
		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
		parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());
		subQueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_REVISION_LIST_LOAD_ITEMS, parameters, subQueries);
		try {
			while (result.next()) {
				Revision revision = (Revision) new Revision();
				producerRevision.fill(revision, result);
				resultMap.put(revision.getId(), revision);
			}

		} catch (Exception e) {
			throw new DataException(ErrorCode.NODE_LOAD_REVISIONS, nodeId, e);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return resultMap;
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
