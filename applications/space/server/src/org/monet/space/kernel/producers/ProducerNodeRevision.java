package org.monet.space.kernel.producers;

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Revision;
import org.monet.space.kernel.utils.TimeCache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerNodeRevision extends Producer {
	private static TimeCache<String, String> timeCache = new TimeCache<String, String>(3600L);

	public void fill(Revision revision, ResultSet result) throws SQLException {
		revision.setId(result.getString("id"));
		revision.setIdNode(result.getString("id_node"));
		revision.setData(new StringBuffer(result.getString("data")));
		revision.setIdUser(result.getString("id_user"));

    /*
    resultSuperdata = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LOAD_REVISIONS_SUPERDATA, parameters);
    try {
      resultSuperdata.next();
      RevisionSuperdata superdata = new RevisionSuperdata();
      superdata.setCode(idNode);
      superdata.setData(new StringBuffer(resultSuperdata.getString("data")));
      superdata.setValue(resultSuperdata.getString("value"));
      superdata.setIdSuperdata(resultSuperdata.getString("idSuperdata"));
      superdata.setRevisionDate(resultSuperdata.getTimestamp("revisionDate"));
      revision.setSuperdata(superdata);
    }
    finally {
      this.agentDatabase.closeQuery(resultSuperdata);
    }*/

		revision.setRevisionDate(result.getTimestamp("revision_date"));
	}

	public void create(Node node, String data) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		String idNode = node.getId();

		if (timeCache.get(idNode) != null)
			return;

		parameters.put(Database.QueryFields.ID_NODE, idNode);
		parameters.put(Database.QueryFields.ID_USER, this.getUserId());
		parameters.put(Database.QueryFields.DATA, data);
		parameters.put(Database.QueryFields.MERGED, 1);
		parameters.put(Database.QueryFields.REVISION_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REVISION_CREATE, parameters);

		timeCache.put(node.getId(), node.getId());
	}

	public Revision load(String id) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Revision revision = new Revision();

		parameters.put(Database.QueryFields.ID, id);
		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_REVISION_LOAD, parameters);

			if (!result.next())
				throw new Exception(String.format("Can't get revision '%s'", id));

			this.fill(revision, result);
		} catch (Exception e) {
			throw new DataException(ErrorCode.NODE_LOAD_REVISIONS, id, e);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return revision;
	}

	@Override
	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}
