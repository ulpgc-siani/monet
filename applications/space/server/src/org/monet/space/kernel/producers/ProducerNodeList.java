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
import org.monet.metamodel.*;
import org.monet.metamodel.AttributeProperty.TypeEnumeration;
import org.monet.metamodel.ContainerDefinitionBase.ViewProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.FilterProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentDatabaseOracle;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.DataRequest.GroupBy;
import org.monet.space.kernel.model.DataRequest.GroupBy.Operator;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.map.Location;
import org.monet.space.kernel.model.map.LocationList;
import org.monet.space.kernel.model.wrappers.NodeSetWrapper;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProducerNodeList extends ProducerList {
	public static final String NodesDescriptorsTable = "ts$nodes_descriptors";
	private ProducerNode producerNode;
	private ProducerReference producerReference;
	private ProducerDataLink producerDataLink;
	private List<String> views = new ArrayList<>();

	public ProducerNodeList() {
		super();
		this.producerNode = producersFactory.get(Producers.NODE);
		this.producerReference = producersFactory.get(Producers.REFERENCE);
		this.producerDataLink = producersFactory.get(Producers.DATALINK);
	}

	private String getReferenceAttributes(String reference) {
		if (!Dictionary.getInstance().existsDefinition(reference))
			return this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_DESCRIPTOR_ATTRIBUTES);

		String baseQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_REFERENCE_ATTRIBUTES);
		QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
		queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE, this.producerReference.getReferenceTableName(reference));

		return queryBuilder.build();
	}

	private String getReferenceSubQuery(String reference) {
		if (!Dictionary.getInstance().existsDefinition(reference))
			return this.producerReference.getReferenceTableName(DescriptorDefinition.CODE);

		String baseQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_REFERENCE_SUBQUERY);
		QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
		queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, this.producerReference.getReferenceTableName(reference));

		return queryBuilder.build();
	}

	private String getReferenceSubQueryForLocations(String reference) {
		if (!Dictionary.getInstance().existsDefinition(reference))
			return this.producerReference.getReferenceTableName(DescriptorDefinition.CODE);

		String baseQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_REFERENCE_SUBQUERY_FOR_LOCATIONS);
		QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
		queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, this.producerReference.getReferenceTableName(reference));

		return queryBuilder.build();
	}

	private String getReferenceSubQueryConditionForLocations(String reference) {
		if (!Dictionary.getInstance().existsDefinition(reference))
			return "";

		String baseQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_REFERENCE_SUBQUERY_CONDITION_FOR_LOCATIONS);
		QueryBuilder queryBuilder = new QueryBuilder(baseQuery);
		queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, this.producerReference.getReferenceTableName(reference));

		return queryBuilder.build();
	}

	private void addSortsByToQuery(NodeDataRequest dataRequest, Map<String, Object> parameters, Map<String, String> queryParams, boolean queryUsingView) {
		String queryParameters = "";
		List<SortBy> sortsBy = dataRequest.getSortsBy();
		IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(dataRequest.getCodeReference());

		for (SortBy sortBy : sortsBy) {
			QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_SORTBY));
			queryBuilder.insertSubQuery(Database.QueryFields.NAME, this.producerReference.getReferenceTableColumnName(sortBy.attribute(), referenceDefinition));
			queryBuilder.insertSubQuery(Database.QueryFields.MODE, sortBy.mode());

			queryParameters += queryBuilder.build();
			queryParameters += Strings.COMMA;
		}
		if (queryParameters.length() > 0)
			queryParameters = queryParameters.substring(0, queryParameters.length() - 1);
		else
			queryParameters = DescriptorDefinition.ATTRIBUTE_LABEL + Strings.SPACE + this.agentDatabase.getOrderMode(Common.OrderMode.ASCENDANT);

		queryParams.put(Database.QueryFields.SORTS_BY, queryParameters);
	}

	private void addGroupsByToQuery(NodeDataRequest dataRequest, Map<String, Object> parameters, Map<String, String> queryParams, boolean queryUsingView) {
		StringBuilder queryParametersBuilder = new StringBuilder();
		List<GroupBy> groupsBy = dataRequest.getGroupsBy();
		IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(dataRequest.getCodeReference());

		for (GroupBy groupBy : groupsBy) {
			StringBuilder options = new StringBuilder();

			for (Object data : groupBy.values()) {
				String query = queryForGroupByAttribute(referenceDefinition.getAttribute(groupBy.attribute()), groupBy.operator(), data);
				String value = valueForGroupByAttribute(referenceDefinition.getAttribute(groupBy.attribute()), data);

				QueryBuilder builder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(query));
				builder.insertSubQuery(Database.QueryFields.OR, options.length() <= 0 ? "" : " OR ");
				builder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, !queryUsingView ? this.producerReference.getReferenceTableName(dataRequest.getCodeReference()) + "." : "");
				builder.insertSubQuery(Database.QueryFields.NAME, groupBy.attribute());

				if (value != null)
					builder.insertSubQuery(Database.QueryFields.VALUE, value.replaceAll("\'", "\'\'"));

				options.append(builder.build());
			}

			QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_GROUPBY));
			queryBuilder.insertSubQuery(Database.QueryFields.OPTIONS, options.toString());

			queryParametersBuilder.append(Strings.SPACE);
			queryParametersBuilder.append(queryBuilder.build());
		}

		queryParams.put(Database.QueryFields.GROUPS_BY, queryParametersBuilder.toString());
	}

	private String queryForGroupByAttribute(AttributeProperty attribute, Operator operator, Object value) {

		if (value == null)
			return Database.Queries.NODE_LIST_LOAD_ITEMS_GROUPBY_OPTION_WITH_NULL;

		if (value instanceof String)
			return Database.Queries.NODE_LIST_LOAD_ITEMS_GROUPBY_OPTION;

		if (value instanceof Date && attribute.getType() == TypeEnumeration.DATE) {
			switch (operator) {
				case Gt: return Database.Queries.NODE_LIST_LOAD_ITEMS_GROUPBY_DATE_GT_OPTION;
				case Lt: return Database.Queries.NODE_LIST_LOAD_ITEMS_GROUPBY_DATE_LT_OPTION;
			}
		}

		return Database.Queries.NODE_LIST_LOAD_ITEMS_GROUPBY_OPTION;
	}

	private String valueForGroupByAttribute(AttributeProperty attribute, Object value) {
		DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

		if (value == null)
			return null;

		if (value instanceof String)
			return (String) value;

		if (value instanceof Date)
			return dateFormatter.format((Date)value);

		return value.toString();
	}

	private void addCodeNodesToQuery(NodeDataRequest dataRequest, Map<String, Object> parameters, Map<String, String> queryParams, boolean queryUsingView) {
		String codeDomainNode = dataRequest.getCodeDomainNode();
		List<String> codeNodes = new ArrayList<>();
		String[] dataRequestCodeNodes = dataRequest.getCodeNodes();
		NodeDefinition domainDefinition;

		if (dataRequestCodeNodes != null) {
			for (String dataRequestCodeNode : dataRequestCodeNodes)
				codeNodes.add(dataRequestCodeNode);
		}

		if (Dictionary.getInstance().existsDefinition(codeDomainNode)) {
			domainDefinition = Dictionary.getInstance().getNodeDefinition(codeDomainNode);

			if (domainDefinition.isCollection() || domainDefinition.isCatalog()) {
				SetViewProperty viewDefinition = (SetViewProperty) domainDefinition.getNodeView(dataRequest.getCodeView());

				if (viewDefinition != null && viewDefinition.getSelect() != null) {
					for (Ref select : viewDefinition.getSelect().getNode())
						codeNodes.add(Dictionary.getInstance().getDefinitionCode(select.getValue()));
				}
			} else if (domainDefinition.isContainer()) {
				ContainerDefinitionBase.ViewProperty viewDefinition = (ViewProperty) domainDefinition.getNodeView(dataRequest.getCodeView());
				ContainerDefinitionBase.ViewProperty.ShowProperty showDefinition = viewDefinition.getShow();
				List<Ref> nodes = new ArrayList<>();
				if (showDefinition != null && showDefinition.getLinksIn() != null)
					nodes = showDefinition.getLinksIn().getNode();
				else if (showDefinition != null && showDefinition.getLinksOut() != null)
					nodes = showDefinition.getLinksOut().getNode();

				for (Ref node : nodes)
					codeNodes.add(Dictionary.getInstance().getDefinitionCode(node.getValue()));
			} else if (domainDefinition.isForm()) {
				FormViewProperty viewDefinition = (FormViewProperty) domainDefinition.getNodeView(dataRequest.getCodeView());
				FormDefinitionBase.FormViewProperty.ShowProperty showDefinition = viewDefinition.getShow();
				List<Ref> nodes = new ArrayList<>();
				if (showDefinition != null && showDefinition.getLinksIn() != null)
					nodes = showDefinition.getLinksIn().getNode();
				else if (showDefinition != null && showDefinition.getLinksOut() != null)
					nodes = showDefinition.getLinksOut().getNode();

				for (Ref node : nodes)
					codeNodes.add(Dictionary.getInstance().getDefinitionCode(node.getValue()));
			}
		}

		if ((codeNodes.size() > 0)) {
			queryParams.put(Database.QueryFields.CODE_NODES_SUBQUERY, this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_CODENODES));
			queryParams.put(Database.QueryFields.CODE_NODES, "'" + LibraryArray.implode(codeNodes, "','") + "'");
			queryParams.put(Database.QueryFields.NODES_DESCRIPTORS_TABLE, !queryUsingView ? NodesDescriptorsTable + "." : "");
		} else {
			queryParams.put(Database.QueryFields.CODE_NODES_SUBQUERY, "");
		}
	}

	private void addFiltersToQuery(NodeDataRequest dataRequest, Map<String, Object> parameters, Map<String, String> queryParams, String nodeId, boolean queryUsingView) {
		String codeDomainNode = dataRequest.getCodeDomainNode();

		if (codeDomainNode == null) {
			queryParams.put(Database.QueryFields.PARAMETERS, "");
			return;
		}

		SetViewProperty view = viewOfDomain(dataRequest, codeDomainNode);
		if (view == null) {
			queryParams.put(Database.QueryFields.PARAMETERS, "");
			return;
		}

		StringBuilder queryFiltersBuilder = new StringBuilder();
		String codeReference = dataRequest.getCodeReference();
		IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(codeReference);

		for (FilterProperty filterDefinition : view.getFilterList()) {
			String[] result = SetViewProperty.AnalyzeProperty.getValues(filterDefinition, getFilterParameters(nodeId, dataRequest));
			AttributeProperty attributeDefinition = referenceDefinition.getAttribute(filterDefinition.getAttribute().getValue());
			for (String value : result) addFilterToQuery(attributeDefinition, queryFiltersBuilder, codeReference, value, queryUsingView);
		}

		String filters = "";
		if (queryFiltersBuilder.length() > 0) {
			QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_PARAMETERS));
			queryBuilder.insertSubQuery(Database.QueryFields.PARAMETERS, queryFiltersBuilder.toString());
			filters = queryBuilder.build();
		}

		queryParams.put(Database.QueryFields.PARAMETERS, filters);
	}

	private void addFilterToQuery(AttributeProperty attributeDefinition, StringBuilder query, String codeReference, String value, boolean queryUsingView) {
		String queryName = null;

		switch (attributeDefinition.getType()) {
			case BOOLEAN:
				value = value.equalsIgnoreCase("true") ? "1" : value.equalsIgnoreCase("false") ? "0" : value;
			case STRING:
			case CATEGORY:
				queryName = Database.Queries.NODE_LIST_LOAD_ITEMS_PARAMETER;
				break;
			case CHECK:
			case DATE:
			case PICTURE:
			case TERM:
			case LINK:
			case INTEGER:
			case REAL:
				queryName = Database.Queries.NODE_LIST_LOAD_ITEMS_PARAMETER_WITH_EXTRA_DATA;
				break;
		}

		QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(queryName));
		queryBuilder.insertSubQuery(Database.QueryFields.OR, query.length() <= 0 ? "" : " OR ");
		queryBuilder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, !queryUsingView ? this.producerReference.getReferenceTableName(codeReference) + "." : "");
		queryBuilder.insertSubQuery(Database.QueryFields.NAME, attributeDefinition.getCode());
		queryBuilder.insertSubQuery(Database.QueryFields.DATA, value.replace("'", "''"));

		query.append(Strings.SPACE);
		query.append(queryBuilder.build());
	}

	private void addCondition(NodeDataRequest dataRequest, String searchQuery, Map<String, Object> parameters, Map<String, String> queryParams, boolean queryUsingView) {
		String condition = this.agentDatabase.prepareAsFullTextCondition(dataRequest.getCondition());

		if (condition == null || condition.isEmpty())
			queryParams.put(Database.QueryFields.PART_1, "");
		else {
			String baseQuery = this.agentDatabase.getRepositoryQuery(searchQuery);

			QueryBuilder builder = new QueryBuilder(baseQuery);
			builder.insertSubQuery(Database.QueryFields.CONDITION_SUBQUERY, dataRequest.getCondition());
			builder.insertSubQuery(Database.QueryFields.NODES_DESCRIPTORS_TABLE, !queryUsingView ? NodesDescriptorsTable + "." : "");
			queryParams.put(Database.QueryFields.PART_1, builder.build());

			if (searchQuery.equals(Database.Queries.NODE_LIST_SEARCH_PART_1)) {
				queryParams.put(Database.QueryFields.REFERENCE_TABLE, !queryUsingView ? this.producerReference.getReferenceTableName(dataRequest.getCodeReference()) + "." : "");
				parameters.put(Database.QueryFields.CONDITION, condition);
			}
		}
	}

	private void addBoundingBoxToQuery(NodeDataRequest dataRequest, Map<String, Object> parameters, Map<String, String> queryParams) {
		Polygon boundingBox = dataRequest.getBoundingBox();

		if (boundingBox != null) {
			QueryBuilder builder = new QueryBuilder(agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LIST_LOAD_IN_NODE_WITHIN_BOX));
			parameters.put(Database.QueryFields.BOUNDING_BOX, boundingBox);
			queryParams.put(Database.QueryFields.BOUNDING_BOX, builder.build());
		}
		else
			queryParams.put(Database.QueryFields.BOUNDING_BOX, "");
	}

	private void addLocationToQuery(NodeDataRequest dataRequest, Map<String, Object> parameters, Map<String, String> queryParams) {
		String locationId = dataRequest.getLocationId();

		if (locationId != null) {
			QueryBuilder builder = new QueryBuilder(agentDatabase.getRepositoryQuery(Database.Queries.LOCATION_LIST_LOAD_IN_NODE_WITH_LOCATION));
			parameters.put(Database.QueryFields.ID_LOCATION, locationId);
			queryParams.put(Database.QueryFields.LOCATION, builder.build());
		}
		else
			queryParams.put(Database.QueryFields.LOCATION, "");
	}

	private SetViewProperty viewOfDomain(NodeDataRequest dataRequest, String codeDomainNode) {
		SetViewProperty view = null;
		NodeDefinition domainDefinition = this.getDictionary().getNodeDefinition(codeDomainNode);

		if (domainDefinition.isCollection())
			view = (SetViewProperty) (domainDefinition).getNodeView(dataRequest.getCodeView());
		else if (domainDefinition.isCatalog())
			view = (SetViewProperty) (domainDefinition).getNodeView(dataRequest.getCodeView());

		return view;
	}

	private Map<String, String> getFilterParameters(String nodeId, NodeDataRequest dataRequest) {
		Node node = producerNode.loadAttributes(nodeId);
		Map<String, String> filterParameters = new NodeSetWrapper(node).getFilterParameters();

		filterParameters.putAll(dataRequest.getParameters());

		return filterParameters;
	}

	private Map<String, Node> query(NodeDataRequest dataRequest, String query, Map<String, Object> parameters, Map<String, String> queryParams) {
		ResultSet resultSet = null;
		Map<String, Node> resultMap;

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(query, parameters, queryParams);
			resultMap = this.fillQueryResultToModel(resultSet, dataRequest);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, dataRequest.getCode(), exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return resultMap;
	}

	private Integer queryCount(String nodeId, String query, Map<String, Object> parameters, Map<String, String> queryParams) {
		Integer count;
		ResultSet resultSet = null;
		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(query, parameters, queryParams);
			resultSet.next();
			count = resultSet.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, nodeId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}
		return count;
	}

	private Map<String, Node> fillQueryResultToModel(ResultSet resultSet, NodeDataRequest dataRequest) throws Exception {
		Map<String, Node> resultMap = new LinkedHashMap<>();
		String codeReference = dataRequest.getCodeReference();

		while (resultSet.next()) {
			Node node = new Node();
			Reference reference, descriptor;

			node.setId(resultSet.getString("desc_id_node"));
			node.setParentId(resultSet.getString("desc_id_parent"));
			node.setOwnerId(resultSet.getString("id_owner"));
			node.setOrder(resultSet.getInt("ordering"));
			node.setCode(resultSet.getString("desc_code"));

			if (codeReference == null)
				codeReference = DescriptorDefinition.CODE;

			reference = new Reference(codeReference);
			descriptor = new Reference(DescriptorDefinition.CODE);

			this.producerReference.fill(resultSet, reference);
			this.producerReference.fill(resultSet, descriptor);

			node.addReference(reference);
			node.addReference(descriptor);

			node.linkLoadListener(this.producerNode);
			node.setNodeLink(NodeProvider.getInstance());
			node.setReferenceLink(ReferenceProvider.getInstance());

			resultMap.put(node.getId(), node);
		}

		return resultMap;
	}

	private Map<String, Node> loadNodeListItems(String nodeId, NodeDataRequest dataRequest, String query, String searchQuery) {
		Map<String, Object> parameters = new LinkedHashMap<>();
		Map<String, String> queryParams = new LinkedHashMap<>();

		addSortsByToQuery(dataRequest, parameters, queryParams, false);
		addGroupsByToQuery(dataRequest, parameters, queryParams, false);
		addCodeNodesToQuery(dataRequest, parameters, queryParams, false);
		addFiltersToQuery(dataRequest, parameters, queryParams, nodeId, false);
		addParent(nodeId, dataRequest, query, parameters, queryParams);
		addCondition(dataRequest, searchQuery, parameters, queryParams, false);

		queryParams.put(Database.QueryFields.REFERENCE_ATTRIBUTES, this.getReferenceAttributes(dataRequest.getCodeReference()));
		queryParams.put(Database.QueryFields.REFERENCE_SUBQUERY, this.getReferenceSubQuery(dataRequest.getCodeReference()));

		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
		parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());

		return query(dataRequest, query, parameters, queryParams);
	}

	private Map<String, Node> loadNodeListItemsUsingView(String nodeId, NodeDataRequest dataRequest, String query, String searchQuery) {
		Map<String, Object> parameters = new LinkedHashMap<>();
		Map<String, String> queryParams = new LinkedHashMap<>();

		addSortsByToQuery(dataRequest, parameters, queryParams, true);
		addGroupsByToQuery(dataRequest, parameters, queryParams, true);
		addCodeNodesToQuery(dataRequest, parameters, queryParams, true);
		addFiltersToQuery(dataRequest, parameters, queryParams, nodeId, true);
		addCondition(dataRequest, searchQuery, parameters, queryParams, true);

		queryParams.put(Database.QueryFields.ID_NODE, nodeId);
		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
		parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());

		return query(dataRequest, query, parameters, queryParams);
	}

	private Integer loadNodeListItemsCount(String nodeId, NodeDataRequest dataRequest, String query, String searchQuery) {
		Map<String, Object> parameters = new LinkedHashMap<>();
		Map<String, String> queryParams = new LinkedHashMap<>();

		addGroupsByToQuery(dataRequest, parameters, queryParams, false);
		addCodeNodesToQuery(dataRequest, parameters, queryParams, false);
		addFiltersToQuery(dataRequest, parameters, queryParams, nodeId, false);
		addParent(nodeId, dataRequest, query, parameters, queryParams);
		addCondition(dataRequest, searchQuery, parameters, queryParams, false);

		queryParams.put(Database.QueryFields.REFERENCE_SUBQUERY, this.getReferenceSubQuery(dataRequest.getCodeReference()));

		return queryCount(nodeId, query, parameters, queryParams);
	}

	private Integer loadNodeListItemsCountUsingView(String nodeId, NodeDataRequest dataRequest, String query, String searchQuery) {
		Map<String, Object> parameters = new LinkedHashMap<>();
		Map<String, String> queryParams = new LinkedHashMap<>();

		addGroupsByToQuery(dataRequest, parameters, queryParams, true);
		addCodeNodesToQuery(dataRequest, parameters, queryParams, true);
		addFiltersToQuery(dataRequest, parameters, queryParams, nodeId, true);
		addCondition(dataRequest, searchQuery, parameters, queryParams, true);

		queryParams.put(Database.QueryFields.ID_NODE, nodeId);

		return queryCount(nodeId, query, parameters, queryParams);
	}

	private void addParent(String nodeId, NodeDataRequest dataRequest, String query, Map<String, Object> parameters, Map<String, String> queryParams) {
		if (query.equals(Database.Queries.NODE_LIST_LOAD_ITEMS_VIEW) || query.equals(Database.Queries.NODE_LIST_LOAD_ITEMS) || query.equals(Database.Queries.NODE_LIST_LOAD_ITEMS_COUNT)) {
			String parentSubquery = "";

			if (!dataRequest.getCodeDomainNode().isEmpty() && !Dictionary.getInstance().isCatalogDefinition(dataRequest.getCodeDomainNode())) {
				if (query.equals(Database.Queries.NODE_LIST_LOAD_ITEMS_VIEW)) {
					parentSubquery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_VIEW_PARENT_SUBQUERY);
					parentSubquery = parentSubquery.replace("::idparent::", nodeId);
				}
				else {
					parentSubquery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_PARENT_SUBQUERY);
					parameters.put(Database.QueryFields.ID_PARENT, nodeId);
				}
			}

			queryParams.put(Database.QueryFields.PARENT, parentSubquery);
		} else {
			parameters.put(Database.QueryFields.ID_NODE, nodeId);
		}
	}

	private LocationList loadNodeListItemsLocations(String nodeId, NodeDataRequest dataRequest, String searchQuery) {
		ResultSet resultSet = null;
		Map<String, Object> parameters = new LinkedHashMap<>();
		Map<String, String> queryParams = new LinkedHashMap<>();
		String condition;
		LocationList result = new LocationList();

		addGroupsByToQuery(dataRequest, parameters, queryParams, false);
		addCodeNodesToQuery(dataRequest, parameters, queryParams, false);
		addFiltersToQuery(dataRequest, parameters, queryParams, nodeId, false);
		addBoundingBoxToQuery(dataRequest, parameters, queryParams);
		addLocationToQuery(dataRequest, parameters, queryParams);

		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
		parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());

		queryParams.put(Database.QueryFields.REFERENCE_ATTRIBUTES, this.getReferenceAttributes(dataRequest.getCodeReference()));
		queryParams.put(Database.QueryFields.REFERENCE_SUBQUERY, this.getReferenceSubQueryForLocations(dataRequest.getCodeReference()));
		queryParams.put(Database.QueryFields.REFERENCE_SUBQUERY_CONDITION, this.getReferenceSubQueryConditionForLocations(dataRequest.getCodeReference()));

		String parentSubquery = "";

		if (!Dictionary.getInstance().isCatalogDefinition(dataRequest.getCodeDomainNode())) {
			parentSubquery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_PARENT_SUBQUERY);
			parameters.put(Database.QueryFields.ID_PARENT, nodeId);
		}

		queryParams.put(Database.QueryFields.PARENT, parentSubquery);

		condition = this.agentDatabase.prepareAsFullTextCondition(dataRequest.getCondition());

		if (condition == null || condition.isEmpty())
			queryParams.put(Database.QueryFields.PART_1, "");
		else {
			String baseQuery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_SEARCH_PART_1);

			QueryBuilder builder = new QueryBuilder(baseQuery);
			builder.insertSubQuery(Database.QueryFields.CONDITION_SUBQUERY, dataRequest.getCondition());
			builder.insertSubQuery(Database.QueryFields.NODES_DESCRIPTORS_TABLE, NodesDescriptorsTable + ".");
			queryParams.put(Database.QueryFields.PART_1, builder.build());
			queryParams.put(Database.QueryFields.REFERENCE_TABLE, this.producerReference.getReferenceTableName(dataRequest.getCodeReference()));

			parameters.put(Database.QueryFields.CONDITION, condition);
		}

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_LOCATIONS, parameters, queryParams);

			ProducerLocation producerLocation = producersFactory.get(Producers.LOCATION);
			while (resultSet.next()) {
				Location location = producerLocation.fill(resultSet, dataRequest.getCodeReference());
				result.add(location);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, dataRequest.getCode(), exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	public NodeList load() {
		ResultSet result = null;
		NodeList nodeList;

		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(Database.QueryFields.FIELD, DescriptorDefinition.ATTRIBUTE_LABEL);
		subQueries.put(Database.QueryFields.MODE, this.agentDatabase.getOrderMode(Common.OrderMode.ASCENDANT));

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_ALL_FROM_SYSTEM, null, subQueries);
			nodeList = new NodeList();

			while (result.next()) {
				nodeList.add(createNode(result));
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, "all nodes", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return nodeList;
	}

	public void load(NodeList nodeList) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.ID_PARENT, nodeList.getIdNode());

		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(Database.QueryFields.FIELD, DescriptorDefinition.ATTRIBUTE_LABEL);
		subQueries.put(Database.QueryFields.MODE, this.agentDatabase.getOrderMode(Common.OrderMode.ASCENDANT));

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_FROM_SYSTEM, parameters, subQueries);

			nodeList.clear();
			while (result.next()) {
				nodeList.add(createNode(result));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, nodeList.getIdNode(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

	}

	private Node createNode(ResultSet result) throws SQLException {
		Node currentNode = new Node();
		Reference currentReference = new Reference(DescriptorDefinition.CODE);

		currentNode.setId(result.getString("id_node"));
		currentNode.setParentId(result.getString("id_parent"));
		currentNode.setOwnerId(result.getString("id_owner"));
		currentNode.setOrder(result.getInt("ordering"));
		currentNode.setCode(result.getString("code"));

		currentNode.setName(Dictionary.getInstance().getNodeDefinition(currentNode.getCode()).getName());

		this.producerReference.fill(result, currentReference);

		currentNode.addReference(currentReference);
		currentNode.linkLoadListener(this.producerNode);
		currentNode.setNodeLink(NodeProvider.getInstance());
		currentNode.setReferenceLink(ReferenceProvider.getInstance());
		return currentNode;
	}

	public List<String> loadIds(String code) {
		List<String> nodeIds = new ArrayList<>();
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.CODE, code);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_IDS, parameters, null);

			while (result.next()) {
				nodeIds.add(result.getString("id"));
			}
			return nodeIds;
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, code, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public Map<String, Node> loadItems(String idNode, NodeDataRequest dataRequest) {
		boolean onlyTitle = searchInTitleOnly(dataRequest);
		createItemsView(idNode, dataRequest);
		return this.loadNodeListItemsUsingView(idNode, dataRequest, Database.Queries.NODE_LIST_LOAD_ITEMS, onlyTitle ? Database.Queries.NODE_LIST_SEARCH_PART_1_IN_TITLE : Database.Queries.NODE_LIST_SEARCH_PART_1);
	}

	public void createItemsView(String idNode, NodeDataRequest dataRequest) {
		ResultSet resultSet = null;
		Map<String, Object> parameters = new LinkedHashMap<>();
		Map<String, String> queryParams = new LinkedHashMap<>();

		if (views.contains("Items_" + idNode))
			return;

		if (existsItemsView(idNode)) {
			views.add("Items_" + idNode);
			return;
		}

		addParent(idNode, dataRequest, Database.Queries.NODE_LIST_LOAD_ITEMS_VIEW, parameters, queryParams);

		queryParams.put(Database.QueryFields.REFERENCE_ATTRIBUTES, this.getReferenceAttributes(dataRequest.getCodeReference()));
		queryParams.put(Database.QueryFields.REFERENCE_SUBQUERY, this.getReferenceSubQuery(dataRequest.getCodeReference()));
		queryParams.put(Database.QueryFields.ID_NODE, idNode);

		try {
			this.agentDatabase.executeRepositoryQueries(new DatabaseRepositoryQuery[] { new DatabaseRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_VIEW, parameters, queryParams) });
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, idNode, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		views.add("Items_" + idNode);
	}

	private boolean existsItemsView(String idNode) {
		Map<String, String> subQueries = new HashMap<>();
		ResultSet result;

		subQueries.put(Database.QueryFields.ID_NODE, idNode);
		if (!(agentDatabase instanceof AgentDatabaseOracle)) subQueries.put(Database.QueryFields.SCHEMA, this.agentDatabase.getSchemaName());
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_VIEW_EXISTS, null, subQueries);

		try {
			String viewName = "";
			boolean next = result.next();
			if (next) viewName = result.getString("viewname");
			return !viewName.isEmpty();
		} catch (SQLException exception) {
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public Integer loadItemsCount(String idNode, NodeDataRequest dataRequest) {
		boolean onlyTitle = searchInTitleOnly(dataRequest);
		createItemsView(idNode, dataRequest);
		return this.loadNodeListItemsCountUsingView(idNode, dataRequest, Database.Queries.NODE_LIST_LOAD_ITEMS_COUNT, onlyTitle ? Database.Queries.NODE_LIST_SEARCH_PART_1_IN_TITLE : Database.Queries.NODE_LIST_SEARCH_PART_1);
	}

	public LocationList loadItemsLocations(String idNode, NodeDataRequest dataRequest) {
		return this.loadNodeListItemsLocations(idNode, dataRequest, Database.Queries.NODE_LIST_SEARCH_PART_1);
	}

	public int loadItemsLocationsCount(String idNode, NodeDataRequest dataRequest) {
		ResultSet resultSet = null;
		Map<String, Object> parameters = new LinkedHashMap<>();
		Map<String, String> queryParams = new LinkedHashMap<>();

		addGroupsByToQuery(dataRequest, parameters, queryParams, false);
		addCodeNodesToQuery(dataRequest, parameters, queryParams, false);
		addFiltersToQuery(dataRequest, parameters, queryParams, idNode, false);
		addBoundingBoxToQuery(dataRequest, parameters, queryParams);
		addLocationToQuery(dataRequest, parameters, queryParams);

		queryParams.put(Database.QueryFields.REFERENCE_SUBQUERY, this.getReferenceSubQueryForLocations(dataRequest.getCodeReference()));
		queryParams.put(Database.QueryFields.REFERENCE_SUBQUERY_CONDITION, this.getReferenceSubQueryConditionForLocations(dataRequest.getCodeReference()));

		String parentSubquery = "";

		if (!Dictionary.getInstance().isCatalogDefinition(dataRequest.getCodeDomainNode())) {
			parentSubquery = this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_PARENT_SUBQUERY);
			parameters.put(Database.QueryFields.ID_PARENT, idNode);
		}

		queryParams.put(Database.QueryFields.PARENT, parentSubquery);

		addCondition(dataRequest, Database.Queries.NODE_LIST_SEARCH_PART_1, parameters, queryParams, false);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_LOCATIONS_COUNT, parameters, queryParams);
			return resultSet.next() ? resultSet.getInt("counter") : 0;
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, dataRequest.getCode(), exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}
	}

	public Map<String, Node> loadSetItems(String idNode, String contentType, NodeDataRequest dataRequest) {
		String query = null;

		if (contentType.toLowerCase().equals("linksin")) query = Database.Queries.NODE_LIST_LOAD_SET_LINKS_IN_ITEMS;
		else if (contentType.toLowerCase().equals("linksout"))
			query = Database.Queries.NODE_LIST_LOAD_SET_LINKS_OUT_ITEMS;
		else if (contentType.toLowerCase().equals("ownedprototypes"))
			query = Database.Queries.NODE_LIST_LOAD_SET_OWNED_PROTOTYPES_ITEMS;
		else if (contentType.toLowerCase().equals("sharedprototypes"))
			query = Database.Queries.NODE_LIST_LOAD_SET_SHARED_PROTOTYPES_ITEMS;

		if (query == null) return new LinkedHashMap<>();

		return this.loadNodeListItems(idNode, dataRequest, query, Database.Queries.NODE_LIST_SEARCH_PART_1_IN_NODES_DESCRIPTORS);
	}

	public Integer loadSetItemsCount(String idNode, String contentType, NodeDataRequest dataRequest) {
		String query = null;

		if (contentType.toLowerCase().equals("linksin"))
			query = Database.Queries.NODE_LIST_LOAD_SET_LINKS_IN_ITEMS_COUNT;
		else if (contentType.toLowerCase().equals("linksout"))
			query = Database.Queries.NODE_LIST_LOAD_SET_LINKS_OUT_ITEMS_COUNT;
		else if (contentType.toLowerCase().equals("ownedprototypes"))
			query = Database.Queries.NODE_LIST_LOAD_SET_OWNED_PROTOTYPES_ITEMS_COUNT;
		else if (contentType.toLowerCase().equals("sharedprototypes"))
			query = Database.Queries.NODE_LIST_LOAD_SET_SHARED_PROTOTYPES_ITEMS_COUNT;

		if (query == null) return 0;

		return this.loadNodeListItemsCount(idNode, dataRequest, query, Database.Queries.NODE_LIST_SEARCH_PART_1_IN_NODES_DESCRIPTORS);
	}

	public String locateId(String code) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();

		parameters.put(Database.QueryFields.CODE, code);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOCATE, parameters);

			if (!result.next())
				return Strings.EMPTY;
			return result.getString("id_node");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, "all nodes", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public NodeList search(String idNode, SearchRequest searchRequest) {
		String condition = searchRequest.getCondition();
		Date from = searchRequest.getFromDate();
		Date to = searchRequest.getToDate();
		NodeList nodeList = new NodeList();
		ResultSet queryResultSet = null;
		ProducerNode producerNode = producersFactory.get(Producers.NODE);

		condition = this.agentDatabase.prepareAsFullTextCondition(condition);

		if (condition.isEmpty())
			return nodeList;

		if (to == null)
			to = new Date();

		try {
			Map<String, Object> parameters = new HashMap<>();
			Map<String, String> subQueries = new HashMap<>();

			if (from != null) {
				parameters.put(Database.QueryFields.FROM_DATE, this.agentDatabase.getDateAsTimestamp(from));
				parameters.put(Database.QueryFields.TO_DATE, this.agentDatabase.getDateAsTimestamp(to));
				subQueries.put(Database.QueryFields.DATE_FILTER, this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_SEARCH_DATE_FILTER));
			}
			else
				subQueries.put(Database.QueryFields.DATE_FILTER, null);

			if (!condition.isEmpty()) {
				parameters.put(Database.QueryFields.CONDITION, condition);
				subQueries.put(Database.QueryFields.CONDITION, this.agentDatabase.getRepositoryQuery(Database.Queries.NODE_LIST_SEARCH_CONDITION));
			} else
				subQueries.put(Database.QueryFields.CONDITION, "");

			List<String> ancestors = producerNode.loadNodeIds(idNode, Database.Queries.NODE_LIST_SEARCH_ANCESTORS);
			subQueries.put(Database.QueryFields.ANCESTORS, "'" + LibraryArray.implode(ancestors.toArray(new String[0]), "','") + "'");

			queryResultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_SEARCH_ITEMS_COUNT, parameters, subQueries);

			if (!queryResultSet.next())
				throw new RuntimeException("Can't get search total count");
			nodeList.setTotalCount(queryResultSet.getInt("counter"));
			if (nodeList.getTotalCount() == 0)
				return nodeList;

			this.agentDatabase.closeQuery(queryResultSet);

			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(searchRequest.getStartPos()));
			parameters.put(Database.QueryFields.LIMIT, searchRequest.getLimit());

			queryResultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_SEARCH_ITEMS, parameters, subQueries);

			while (queryResultSet.next()) {
				Node node = new Node();
				Reference descriptor = new Reference(DescriptorDefinition.CODE);

				node.setId(queryResultSet.getString("id_node"));
				node.setParentId(queryResultSet.getString("id_parent"));
				node.setOwnerId(queryResultSet.getString("id_owner"));
				node.setOrder(queryResultSet.getInt("ordering"));
				node.setCode(queryResultSet.getString("code"));

				this.producerReference.fill(queryResultSet, descriptor);

				descriptor.setCode(DescriptorDefinition.CODE);

				node.addReference(descriptor);

				node.linkLoadListener(this.producerNode);
				node.setNodeLink(NodeProvider.getInstance());
				node.setReferenceLink(ReferenceProvider.getInstance());

				nodeList.add(node);
			}
		} catch (SQLException exception) {
			throw new DataException(ErrorCode.SEARCH, condition, exception);
		} finally {
			if (queryResultSet != null)
				this.agentDatabase.closeQuery(queryResultSet);
		}

		return nodeList;
	}

	public Map<String, Attribute> loadAttribute(String[] nodesIds, String codeAttribute) {
		Map<String, Attribute> result = new HashMap<>();
		ResultSet resultSet = null;
		Map<String, String> subQueries = new HashMap<>();

		subQueries.put(Database.QueryFields.NODES, LibraryArray.implode(nodesIds, Strings.COMMA));
		subQueries.put(Database.QueryFields.CODE_ATTRIBUTE, codeAttribute);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_SUPER_DATA_LOAD_FOR_NODES, null, subQueries);
			while (resultSet.next()) {
				Attribute attribute = new Attribute();
				attribute.deserializeFromXML(resultSet.getString("data"), null);
				result.put(resultSet.getString("id_node"), attribute);
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.SAVE_NODE, null, oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	public void saveAttribute(String[] nodesIds, Attribute attribute) {
		ResultSet resultSet = null;
		Map<String, String> subQueries = new HashMap<>();
		List<DatabaseRepositoryQuery> queries = new ArrayList<>();

		subQueries.put(Database.QueryFields.NODES, LibraryArray.implode(nodesIds, Strings.COMMA));
		subQueries.put(Database.QueryFields.CODE_ATTRIBUTE, attribute.getCode());

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_SUPER_DATA_LOAD_FOR_NODES, null, subQueries);
			while (resultSet.next()) {
				Map<String, Object> parameters = new HashMap<>();

				parameters.put(Database.QueryFields.ID, resultSet.getString("id"));
				parameters.put(Database.QueryFields.CODE, attribute.getIndicatorValue(Indicator.CODE));
				parameters.put(Database.QueryFields.VALUE, attribute.getIndicatorValue(Indicator.VALUE));
				parameters.put(Database.QueryFields.DATA, attribute.getIndicatorList().serializeToXML());

				queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_SUPER_DATA_SAVE, parameters));
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.SAVE_NODE, null, oException);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[queries.size()]));
		} catch (SQLException e) {
			this.agentLogger.error(e);
		}
	}

	public NodeList loadFromTrash(DataRequest dataRequest) {
		ResultSet result = null;
		NodeList nodeList;
		Map<String, Object> parameters = new HashMap<>();

		try {
			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_FROM_TRASH, parameters);

			nodeList = new NodeList();

			while (result.next()) {
				Node node = new Node();
				Reference currentReference = new Reference(DescriptorDefinition.CODE);

				node.setId(result.getString("id_node"));
				node.setParentId(result.getString("id_parent"));
				node.setOwnerId(result.getString("id_owner"));
				node.setOrder(result.getInt("ordering"));
				node.setCode(result.getString("code"));

				this.producerReference.fill(result, currentReference);

				node.addReference(currentReference);
				node.linkLoadListener(this.producerNode);
				node.setNodeLink(NodeProvider.getInstance());
				node.setReferenceLink(ReferenceProvider.getInstance());

				nodeList.add(node);
			}
			this.agentDatabase.closeQuery(result);

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_FROM_TRASH_COUNT);
			if (!result.next())
				throw new Exception("Can't get total count of trash elements");
			nodeList.setTotalCount(result.getInt("counter"));

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST_FROM_TRASH, null, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return nodeList;
	}

	public void emptyTrash() {
		ResultSet result = null;

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_FROM_TRASH_OFPARENT);

			List<String> nodesToDelete = new ArrayList<>();
			while (result.next()) {
				nodesToDelete.add(result.getString("id_node"));
			}
			this.agentDatabase.closeQuery(result);

			for (String nodeId : nodesToDelete) {
				try {
					Node node = this.producerNode.load(nodeId);
					this.producerNode.removeFromTrash(node);
				} catch (Exception exception) {
					this.agentLogger.error(exception);
				}
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.EMPTY_NODES_TRASH, null, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public Object newObject() {
		NodeList nodeList = new NodeList();
		nodeList.linkLoadListener(this);
		nodeList.setNodeLink(NodeProvider.getInstance());
		return nodeList;
	}

	public void removeItemsView(IndexDefinition indexdefinition) {
		List<SetDefinition> setDefinitionsWithIndex = getDictionary().getSetDefinitionsWithIndex(indexdefinition);
		List<String> setDefinitionCodes = new ArrayList<>();
		Map<String, String> queryParameters = new HashMap<>();

		for (SetDefinition definition : setDefinitionsWithIndex)
			setDefinitionCodes.add(definition.getCode());

		List<String> setIds = loadSetIds(setDefinitionCodes);

		List<DatabaseRepositoryQuery> queries = new ArrayList<>();
		for (String id : setIds) {
			if (!existsItemsView(id)) continue;
			queryParameters.put(Database.QueryFields.ID_NODE, id);
			queries.add(new DatabaseRepositoryQuery(Database.Queries.NODE_LIST_LOAD_ITEMS_VIEW_DELETE, null, queryParameters));
			views.remove("Items_" + id);
		}

		if (queries.size() <= 0) return;
		this.agentDatabase.executeRepositoryQueries(queries.toArray(new DatabaseRepositoryQuery[0]));
	}

	private List<String> loadSetIds(List<String> definitionList) {
		ResultSet resultSet = null;
		List<String> result = new ArrayList<>();
		Map<String, String> queryParams = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();

		try {
			queryParams.put(Database.QueryFields.CODE_NODES, "'" + LibraryArray.implode(definitionList.toArray(new String[0]), "','") + "'");

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_LIST_LOAD_IDS_WITH_CODES, parameters, queryParams);

			while (resultSet.next()) {
				result.add(resultSet.getString("id"));
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODELIST, "", exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		NodeList nodeList = (NodeList) eventObject.getSource();

		if (attribute.equals(NodeList.ITEMS)) {
			this.load(nodeList);
		}
	}

	private boolean searchInTitleOnly(NodeDataRequest dataRequest) {
		String tag = dataRequest.getConditionTag();
		return tag != null && !tag.isEmpty() && tag.equals("title");
	}

}
