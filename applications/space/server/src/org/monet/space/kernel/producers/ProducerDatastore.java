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
import org.monet.space.kernel.exceptions.DatabaseException;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.monet.space.kernel.model.DatastoreTransaction.*;

public class ProducerDatastore extends Producer {

	public ProducerDatastore() {
		super();
	}

	public void queue(Datastore datastore, Cube cube, CubeFact fact) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.TYPE, Type.FACT.toString());
		parameters.put(Database.QueryFields.CODE, cube.getCode());
		parameters.put(Database.QueryFields.DATA, fact.serializeToXML());
		queue(datastore, parameters);
	}

	public void queue(Datastore datastore, Dimension dimension, DimensionComponent component) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.TYPE, Type.DIMENSION_COMPONENT.toString());
		parameters.put(Database.QueryFields.CODE, dimension.getCode());
		parameters.put(Database.QueryFields.DATA, component.serializeToXML());
		queue(datastore, parameters);
	}

	public void queue(Datastore datastore, Map<String, Object> parameters) {
		parameters.put(Database.QueryFields.DATASTORE, datastore.getCode());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.DATASTORE_QUEUE_CREATE, parameters);
	}

	public List<DatastoreTransaction> loadPendingTransactions(Datastore datastore) {
		ResultSet resultSet = null;
		Map<String, Object> parameters = new HashMap<>();
		List<DatastoreTransaction> result = new ArrayList<>();

		try {
			parameters.put(Database.QueryFields.DATASTORE, datastore.getCode());
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.DATASTORE_QUEUE_LOAD, parameters);

			while (resultSet.next()) {
				final int id = resultSet.getInt("id");
				final BaseObject item = getItem(resultSet);
				final String code = resultSet.getString("code");
				final Type type = Type.fromString(resultSet.getString("type"));

				result.add(new DatastoreTransaction(id, type, code, item));
			}

			return result;
		} catch (SQLException ex) {
			throw new DatabaseException(ErrorCode.LOAD_DATASTORE_QUEUE, null, ex);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}
	}

	public void removePendingTransactions(Datastore datastore, List<DatastoreTransaction> transactions) {

		if (transactions.size() <= 0)
			return;

		List<Integer> ids = getNodeIdsOfTransactions(transactions);
		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(Database.QueryFields.IDS, "'" + LibraryArray.implode(ids.toArray(new Integer[0]), "','") + "'");

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.DATASTORE_QUEUE_REMOVE, null, subQueries);
	}

	private List<Integer> getNodeIdsOfTransactions(List<DatastoreTransaction> transactions) {
		List<Integer> result = new ArrayList<>();

		for (DatastoreTransaction transaction : transactions)
			result.add(transaction.getId());

		return result;
	}

	private BaseObject getItem(ResultSet resultSet) throws SQLException {
		Type type = Type.fromString(resultSet.getString("type"));
		BaseObject result = null;

		if (type == Type.FACT) {
			result = new CubeFact();
			result.deserializeFromXML(resultSet.getString("data"), null);
		}
		else if (type == Type.DIMENSION_COMPONENT) {
			result = new DimensionComponent(resultSet.getString("code"));
			result.deserializeFromXML(resultSet.getString("data"), null);
		}

		return result;
	}

	public Object newObject() {
		return new Event();
	}

	@Override
	public void loadAttribute(EventObject object, String attribute) {
	}

}
