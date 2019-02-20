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
import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.ProjectBase.TypeEnumeration;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.model.map.LocationList;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.*;
import java.util.Map.Entry;

import static org.monet.space.kernel.model.DataRequest.OPERATOR_SEPARATOR;

public class ProducerDataLink extends Producer {

	private void prepareFilters(DataRequest dataRequest, String indexCode, Map<String, String> subQueries) {
		StringBuilder queryParametersBuilder = new StringBuilder();
		Dictionary dictionary = Dictionary.getInstance();
		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		String filters = dataRequest.getParameter("filters");
		IndexDefinition indexDefinition = dictionary.getIndexDefinition(indexCode);

		if (filters != null && !filters.isEmpty()) {
			Map<String, String> filtersMap = SerializerData.deserialize(filters);

			for (Entry<String, String> filter : filtersMap.entrySet()) {
				String attributeCode = filter.getKey();
				String rawValue = filter.getValue();
				String value = rawValue.contains(OPERATOR_SEPARATOR) ? rawValue.substring(0, rawValue.indexOf(OPERATOR_SEPARATOR)) : filter.getValue();
				String operator = rawValue.contains(OPERATOR_SEPARATOR) ? filterOperator(rawValue.substring(rawValue.indexOf(OPERATOR_SEPARATOR)+OPERATOR_SEPARATOR.length())) : "=";
				AttributeProperty attribute = indexDefinition.getAttribute(attributeCode);
				String query = null;

				switch (attribute.getType()) {
					case BOOLEAN:
						value = value.equalsIgnoreCase("true") ? "1" : value.equalsIgnoreCase("false") ? "0" : value;
					case STRING:
					case CATEGORY:
						query = Database.Queries.DATA_LINK_LOAD_PARAMETER;
						break;
					case CHECK:
					case DATE:
					case PICTURE:
					case TERM:
					case LINK:
					case INTEGER:
					case REAL:
						query = Database.Queries.DATA_LINK_LOAD_PARAMETER_WITH_EXTRA_DATA;
						break;
				}

				String baseQuery = agentDatabase.getRepositoryQuery(query);
				QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
				queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, producerReference.getReferenceTableName(indexDefinition.getCode()));
				queryBuilder.insertSubQuery(Database.QueryFields.NAME, attribute.getCode());
				queryBuilder.insertSubQuery(Database.QueryFields.OPERATOR, operator);
				queryBuilder.insertSubQuery(Database.QueryFields.DATA_PREFIX, operator.equalsIgnoreCase("like") ? "%" : "");
				queryBuilder.insertSubQuery(Database.QueryFields.DATA, value.replace("'", "''"));

				queryParametersBuilder.append(Strings.SPACE);
				queryParametersBuilder.append(queryBuilder.build());
			}
		}

		subQueries.put(Database.QueryFields.REFERENCE_TABLE, producerReference.getReferenceTableName(indexDefinition.getCode()));
		subQueries.put(Database.QueryFields.PARAMETERS, queryParametersBuilder.toString());
	}

	private String filterOperator(String operator) {
		if (operator.equalsIgnoreCase("contains")) return "LIKE";
		return "=";
	}

	private void addSortsByToQuery(DataRequest dataRequest, String indexCode, Map<String, String> subQueries) {
		String queryParameters = "";
		List<SortBy> sortsBy = dataRequest.getSortsBy();
		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(indexCode);

		for (SortBy sortBy : sortsBy) {
			QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.DATA_LINK_LOAD_SORTBY));
			queryBuilder.insertSubQuery(Database.QueryFields.NAME, producerReference.getReferenceTableColumnName(sortBy.attribute(), referenceDefinition));
			queryBuilder.insertSubQuery(Database.QueryFields.MODE, sortBy.mode());

			queryParameters += queryBuilder.build();
			queryParameters += Strings.COMMA;
		}

		if (queryParameters.length() > 0)
			queryParameters = queryParameters.substring(0, queryParameters.length() - 1);
		else
			queryParameters = DescriptorDefinition.ATTRIBUTE_LABEL + Strings.SPACE + this.agentDatabase.getOrderMode(Common.OrderMode.ASCENDANT);

		subQueries.put(Database.QueryFields.SORTS_BY, queryParameters);
	}

	private void addConditionToQuery(DataRequest dataRequest, String indexCode, Map<String, Object> parameters, Map<String, String> subQueries) {
		String condition = dataRequest.getCondition();
		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);
		Dictionary dictionary = Dictionary.getInstance();

		if (!condition.isEmpty() && !indexCode.equals(Strings.EMPTY) && dictionary.existsDefinition(indexCode)) {
			String codeReference = dictionary.getDefinitionCode(indexCode);
			QueryBuilder searchBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.DATA_LINK_LOAD_SEARCH));
			parameters.put(Database.QueryFields.CONDITION, this.agentDatabase.prepareAsFullTextCondition(condition));
			searchBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, producerReference.getReferenceTableName(codeReference));
			subQueries.put(Database.QueryFields.CONDITION, searchBuilder.build());
		} else {
			subQueries.put(Database.QueryFields.CONDITION, "");
		}
	}

	private void addOwnerToQuery(String idOwner, Map<String, Object> parameters, Map<String, String> subQueries) {
		boolean isBack = BusinessUnit.getInstance().getBusinessModel().getProject().getType().equals(TypeEnumeration.BACK);

		if (!isBack) {
			QueryBuilder ownerBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.DATA_LINK_LOAD_OWNER));
			parameters.put(Database.QueryFields.ID_OWNER, idOwner);
			subQueries.put(Database.QueryFields.OWNER, ownerBuilder.build());
		} else {
			subQueries.put(Database.QueryFields.OWNER, "");
		}
	}

	private void addBoundingBoxToQuery(NodeDataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries) {
		Polygon boundingBox = dataRequest.getBoundingBox();

		if (boundingBox != null) {
			QueryBuilder builder = new QueryBuilder(agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LIST_LOAD_IN_NODE_WITHIN_BOX));
			parameters.put(Database.QueryFields.BOUNDING_BOX, boundingBox);
			subQueries.put(Database.QueryFields.BOUNDING_BOX, builder.build());
		}
		else
			subQueries.put(Database.QueryFields.BOUNDING_BOX, "");
	}

	private NodeItemList readItems(DataLink dataLink, NodeDataRequest dataRequest) {
		NodeItemList list = new NodeItemList();
		ResultSet result = null;
		String idOwner = Context.getInstance().getCurrentSession().getAccount().getUser().getId();
		String indexCode = dataLink.getIndexCode();
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subQueries = new HashMap<>();

		try {
			this.prepareFilters(dataRequest, indexCode, subQueries);
			this.addConditionToQuery(dataRequest, indexCode, parameters, subQueries);
			this.addOwnerToQuery(idOwner, parameters, subQueries);

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.DATA_LINK_LOAD_COUNT, parameters, subQueries);
			if (!result.next())
				throw new Exception("Can't get total count of data links");

			list.setTotalCount(result.getInt("counter"));
			this.agentDatabase.closeQuery(result);

			this.addSortsByToQuery(dataRequest, indexCode, subQueries);
			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());

			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.DATA_LINK_LOAD, parameters, subQueries);

			while (result.next()) {
				NodeItem item = new NodeItem();
				int columnCount;
				ResultSetMetaData resultMetaData;

				resultMetaData = result.getMetaData();
				columnCount = resultMetaData.getColumnCount();
				for (int pos = 1; pos < columnCount + 1; pos++) {
					String columnName = resultMetaData.getColumnName(pos).toLowerCase();
					int type = resultMetaData.getColumnType(pos);

					if ((type == Types.DATE) || (type == Types.TIME) || (type == Types.TIMESTAMP))
						item.addAttribute(columnName, LibraryDate.getDateAndTimeString(result.getTimestamp(columnName), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.NUMERIC, true, Strings.MINUS));
					else if (type == Types.INTEGER)
						item.addAttribute(columnName, String.valueOf(result.getInt(pos)));
					else if (type == Types.BOOLEAN)
						item.addAttribute(columnName, String.valueOf(result.getBoolean(pos)));
					else
						item.addAttribute(columnName, result.getString(pos));
				}

				list.add(item);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.SOURCE_INDEX_LOAD, dataRequest.getCondition(), exception);
		} finally {
			if (result != null)
				this.agentDatabase.closeQuery(result);
		}

		return list;
	}

	private LocationList readItemsLocations(DataLink dataLink, NodeDataRequest dataRequest) {
		ResultSet result = null;
		String idOwner = Context.getInstance().getCurrentSession().getAccount().getUser().getId();
		String indexCode = dataLink.getIndexCode();
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subQueries = new HashMap<>();
		ProducerLocation producerLocation = producersFactory.get(Producers.LOCATION);
		LocationList locationList = new LocationList();

		try {
			this.prepareFilters(dataRequest, indexCode, subQueries);
			this.addConditionToQuery(dataRequest, indexCode, parameters, subQueries);
			this.addOwnerToQuery(idOwner, parameters, subQueries);
			this.addBoundingBoxToQuery(dataRequest, parameters, subQueries);

			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());

			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.DATA_LINK_LOAD_LOCATIONS, parameters, subQueries);

			while (result.next()) {
				Location location = producerLocation.fill(result, indexCode);
				locationList.add(location);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.SOURCE_INDEX_LOAD, dataRequest.getCondition(), exception);
		} finally {
			if (result != null)
				this.agentDatabase.closeQuery(result);
		}

		return locationList;
	}

	public DataLink load(String code) {
		DataLink dataLink = new DataLink();
		dataLink.setCode(code);
		return dataLink;
	}

	public NodeItemList loadItems(String codeDataLink, String codeIndicator, NodeDataRequest dataRequest) {
		return new NodeItemList();
	}

	public NodeItemList searchItems(String codeDataLink, String codeIndicator, NodeDataRequest dataRequest) {
		DataLink dataLink = this.load(codeDataLink);
		return this.readItems(dataLink, dataRequest);
	}

	public NodeItemList searchItems(DataLink dataLink, String codeIndicator, NodeDataRequest dataRequest) {
		return this.readItems(dataLink, dataRequest);
	}

	public NodeItemList searchItemsByIds(DataLink dataLink, NodeDataRequest dataRequest, List<String> nodeIds) {
		NodeItemList items = this.readItems(dataLink, dataRequest);
		NodeItemList result = new NodeItemList();
		for (String nodeId : nodeIds) {
			result.add(items.get(nodeId));
		}
		result.setTotalCount(nodeIds.size());
		return result;
	}

	public int countItemsLocations(DataLink dataLink, NodeDataRequest dataRequest) {
		ResultSet result = null;
		String idOwner = Context.getInstance().getCurrentSession().getAccount().getUser().getId();
		String indexCode = dataLink.getIndexCode();
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subQueries = new HashMap<>();

		try {
			this.prepareFilters(dataRequest, indexCode, subQueries);
			this.addConditionToQuery(dataRequest, indexCode, parameters, subQueries);
			this.addOwnerToQuery(idOwner, parameters, subQueries);
			this.addBoundingBoxToQuery(dataRequest, parameters, subQueries);

			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.DATA_LINK_LOAD_LOCATIONS_COUNT, parameters, subQueries);
			return result.next() ? result.getInt("counter") : 0;
		} catch (Exception exception) {
			throw new DataException(ErrorCode.SOURCE_INDEX_LOAD, dataRequest.getCondition(), exception);
		} finally {
			if (result != null)
				this.agentDatabase.closeQuery(result);
		}
	}

	public LocationList searchItemsLocations(DataLink dataLink, NodeDataRequest dataRequest) {
		return this.readItemsLocations(dataLink, dataRequest);
	}

	public NodeItem locateItem(DataLink dataLink, String condition) {
		NodeDataRequest dataRequest = new NodeDataRequest();
		dataRequest.setCondition(condition);
		return this.readItems(dataLink, dataRequest).first();
	}

	public void addItem(String codeDataStore, String codeIndicator, String value) {
	}

	public void save(Thesaurus dataStore) {

	}

	public Object newObject() {
		return new DataLink();
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}
}