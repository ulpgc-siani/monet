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

import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.map.ExtendedDataWriter;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map.Entry;

public class ProducerLocation extends Producer {
	private ProducerReference producerReference;

	public ProducerLocation() {
		super();
		this.producerReference = this.producersFactory.get(Producers.REFERENCE);
	}

	public void create(Location location) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_NODE, location.getNodeId());
		parameters.put(Database.QueryFields.ID_LOCATION, location.getLocationId());
		parameters.put(Database.QueryFields.GEOMETRY, location.getGeometry());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(location.getInternalCreateDate()));

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.LOCATION_CREATE, parameters);

		location.setId(id);

	}

	public void save(Location location) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, location.getId());
		parameters.put(Database.QueryFields.ID_NODE, location.getNodeId());
		parameters.put(Database.QueryFields.ID_LOCATION, location.getLocationId());
		parameters.put(Database.QueryFields.GEOMETRY, location.getGeometry());
		parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(location.getInternalCreateDate()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.LOCATION_SAVE, parameters);
	}

	public boolean exists(Node node) {
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> subQueries = new HashMap<>();
		ResultSet resultSet = null;

		try {
			parameters.put(Database.QueryFields.ID_NODE, node.getId());
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.LOCATION_EXISTS, parameters, subQueries);
			return resultSet.next();
		} catch (Exception e) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), e);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}
	}

	private String getReference(String indexCode) {
		if (indexCode == null) return "";

		String baseQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LOAD_REFERENCE);
		QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
		queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, this.producerReference.getReferenceTableName(indexCode));

		return queryBuilder.build();
	}

	private String getReferenceAttributes(String indexCode) {
		if (indexCode == null) return "";

		String baseQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LOAD_REFERENCE_ATTRIBUTES);
		QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
		queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, this.producerReference.getReferenceTableName(indexCode));

		return queryBuilder.build();
	}

	public Location load(Node node, String indexCode) {
		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> subQueries = new HashMap<>();
		Location location = null;
		ResultSet resultSet = null;

		try {
			parameters.put(Database.QueryFields.ID_NODE, node.getId());
			subQueries.put(Database.QueryFields.REFERENCE, this.getReference(indexCode));
			subQueries.put(Database.QueryFields.REFERENCE_ATTRIBUTES, this.getReferenceAttributes(indexCode));

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.LOCATION_LOAD_BY_NODE_ID, parameters, subQueries);

			if (resultSet.next())
				location = fill(resultSet, indexCode);

		} catch (Exception e) {
			throw new DataException(ErrorCode.LOAD_NODE, node.getId(), e);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return location;
	}

	public Location fill(ResultSet resultSet, String indexCode) throws Exception {
		Location location = new Location();

		location.setId(resultSet.getString("id"));
		location.setNodeId(resultSet.getString("id_node"));
		location.setLocationId(resultSet.getString("id_location"));
		location.setLabel(resultSet.getString("label"));
		location.setDescription(resultSet.getString("description"));
		location.setColor(resultSet.getString("color"));

		if (indexCode != null) {
			IndexDefinition indexDefinition = Dictionary.getInstance().getIndexDefinition(indexCode);
			HashMap<String, String> attributes = this.producerReference.getIndexColumnsAsString(resultSet, indexDefinition);

			ExtendedDataWriter writer = new ExtendedDataWriter();
			for (Entry<String, String> attribute : attributes.entrySet()) {
				AttributeProperty attributeDefinition = indexDefinition.getAttribute(attribute.getKey());
				String label = attributeDefinition != null ? attributeDefinition.getLabel().toString() : attribute.getKey();
				writer.write(attribute.getKey(), label, attribute.getValue());
			}

			location.setMetadata(writer.getResult());
		}

		location.setGeometry(this.agentDatabase.getGeometryColumn(resultSet, "geometry"));
		location.setCreateDate(resultSet.getTimestamp("create_date"));

		return location;
	}

	public Object newObject() {
		return new Location();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
