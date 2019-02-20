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

import com.vividsolutions.jts.geom.Polygon;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.model.map.LocationList;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerLocationList extends Producer {
	private ProducerReference producerReference;
	private ProducerLocation producerLocation;

	public ProducerLocationList() {
		super();
		this.producerReference = (ProducerReference) this.producersFactory.get(Producers.REFERENCE);
		this.producerLocation = (ProducerLocation) this.producersFactory.get(Producers.LOCATION);
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

	public LocationList loadInNode(Node node, String locationId, Polygon boundingBox, String indexCode) {
		LocationList locationList = new LocationList();
		ResultSet resultSet = null;
		HashMap<String, String> subQueries = new HashMap<String, String>();
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			String parentSubQuery = "";
			String locationIdSubQuery = "";
			String boundingBoxSubQuery = "";

			if (!node.getDefinition().isCatalog()) {
				parameters.put(Database.QueryFields.ID_PARENT, node.getId());
				parentSubQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LIST_LOAD_IN_NODE_WITH_PARENT);
			}
			subQueries.put("parent", parentSubQuery);

			if (locationId != null) {
				parameters.put(Database.QueryFields.ID_LOCATION, locationId);
				locationIdSubQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LIST_LOAD_IN_NODE_WITH_LOCATION);
			}
			subQueries.put("location", locationIdSubQuery);

			if (boundingBox != null) {
				parameters.put(Database.QueryFields.BOUNDING_BOX, boundingBox);
				boundingBoxSubQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LIST_LOAD_IN_NODE_WITHIN_BOX);
			}

			subQueries.put(Database.QueryFields.BOUNDING_BOX, boundingBoxSubQuery);
			subQueries.put(Database.QueryFields.REFERENCE, this.getReference(indexCode));
			subQueries.put(Database.QueryFields.REFERENCE_ATTRIBUTES, this.getReferenceAttributes(indexCode));

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.LOCATION_LIST_LOAD_IN_NODE, parameters, subQueries);

			while (resultSet.next()) {
				Location location = this.producerLocation.fill(resultSet, indexCode);
				locationList.add(location);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_LOCATIONLIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return locationList;
	}

	public Object newObject() {
		return new Location();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
