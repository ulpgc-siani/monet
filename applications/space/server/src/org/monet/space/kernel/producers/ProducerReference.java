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

import org.monet.bpi.types.Date;
import org.monet.bpi.types.*;
import org.monet.bpi.types.Number;
import org.monet.bpi.types.Term;
import org.monet.metamodel.*;
import org.monet.metamodel.AttributeProperty.TypeEnumeration;
import org.monet.metamodel.FormDefinitionBase.MappingProperty;
import org.monet.metamodel.IndexDefinitionBase.ReferenceProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.FilterProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.kernel.agents.AgentDatabase;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProducerReference extends Producer {

	protected static final String REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX = "$ex";

	public ProducerReference() {
		super();
	}

	private void createReferenceTableColumn(StringBuilder result, String codeColumn, AttributeProperty attributeDefinition, String tableName, ArrayList<DatabaseRepositoryQuery> indexQueries) {
		HashMap<String, String> indexParameters = new HashMap<String, String>();
		Boolean isExtraColumn = (codeColumn.indexOf(REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX) != -1);
		String indexQuery;

		result.append(codeColumn);
		result.append(Strings.SPACE);

		if (isExtraColumn) {
			switch (attributeDefinition.getType()) {
				case BOOLEAN:
					result.append(this.agentDatabase.getColumnDefinition(AgentDatabase.ColumnTypes.BOOLEAN));
					indexQuery = Database.Queries.REFERENCE_TABLE_CREATE_INDEX;
					break;
				case DATE:
					result.append(this.agentDatabase.getColumnDefinition(AgentDatabase.ColumnTypes.DATE));
					indexQuery = Database.Queries.REFERENCE_TABLE_CREATE_INDEX;
					break;
				default:
					result.append(this.agentDatabase.getColumnDefinition(AgentDatabase.ColumnTypes.TEXT));
					indexQuery = Database.Queries.REFERENCE_TABLE_CREATE_TEXT_INDEX;
					break;
			}
		} else {
			switch (attributeDefinition.getType()) {
				case INTEGER:
					result.append(this.agentDatabase.getColumnDefinition(AgentDatabase.ColumnTypes.INTEGER));
					indexQuery = Database.Queries.REFERENCE_TABLE_CREATE_INDEX;
					break;
				case REAL:
					result.append(this.agentDatabase.getColumnDefinition(AgentDatabase.ColumnTypes.FLOAT));
					indexQuery = Database.Queries.REFERENCE_TABLE_CREATE_INDEX;
					break;
				default:
					result.append(this.agentDatabase.getColumnDefinition(AgentDatabase.ColumnTypes.TEXT));
					indexQuery = Database.Queries.REFERENCE_TABLE_CREATE_TEXT_INDEX;
					break;
			}
		}

		if (codeColumn.equals(DescriptorDefinition.ATTRIBUTE_CODE))
			return;

		indexParameters.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		indexParameters.put(Database.QueryFields.INDEX, codeColumn);
		indexQueries.add(new DatabaseRepositoryQuery(indexQuery, null, indexParameters));
	}

	public void createReferenceTable(IndexDefinition definition) {
		String tableName = this.getReferenceTableName(definition.getCode());
		if (this.existsReferenceTable(tableName))
			return;

		ArrayList<DatabaseRepositoryQuery> queries = createReferenceTableQueries(definition);

		this.agentDatabase.executeRepositoryQueries(queries.toArray(new DatabaseRepositoryQuery[0]));
	}

	public void refreshReferenceTable(IndexDefinition definition) {
		String tableName = this.getReferenceTableName(definition.getCode());
		if (!this.existsReferenceTable(tableName)) {
			createReferenceTable(definition);
			return;
		}

		ArrayList<DatabaseRepositoryQuery> queries = createReferenceTableQueries(definition);
		HashMap<String, String> subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		queries.add(0, new DatabaseRepositoryQuery(Database.Queries.REFERENCE_TABLE_REFRESH_PREPARE, null, subQueries));

		subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		queries.add(new DatabaseRepositoryQuery(Database.Queries.REFERENCE_TABLE_REFRESH_MIGRATE, null, subQueries));

		this.agentDatabase.executeRepositoryQueries(queries.toArray(new DatabaseRepositoryQuery[0]));
	}

	private ArrayList<DatabaseRepositoryQuery> createReferenceTableQueries(IndexDefinition definition) {
		StringBuilder attributes = new StringBuilder();
		ArrayList<DatabaseRepositoryQuery> queries, indexQueries;
		String tableName = this.getReferenceTableName(definition.getCode());
		StringBuilder triggerColumns = new StringBuilder();

		queries = new ArrayList<DatabaseRepositoryQuery>();
		indexQueries = new ArrayList<DatabaseRepositoryQuery>();

		ReferenceProperty referenceProperty = definition.getReference();
		if (referenceProperty != null) {
			for (AttributeProperty attributeDefinition : referenceProperty.getAttributePropertyList()) {
				String code = attributeDefinition.getCode();
				TypeEnumeration type = attributeDefinition.getType();

				QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.REFERENCE_TABLE_CREATE_TRIGGER_COLUMN));
				queryBuilder.insertSubQuery(Database.QueryFields.CODE, code);
				triggerColumns.append(queryBuilder.build());

				switch (type) {
					case BOOLEAN:
					case STRING:
					case CATEGORY:
						this.createReferenceTableColumn(attributes, code, attributeDefinition, tableName, indexQueries);
						attributes.append(Strings.COMMA);
						break;
					case DATE:
					case PICTURE:
					case TERM:
					case LINK:
					case INTEGER:
					case REAL:
						this.createReferenceTableColumn(attributes, code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, attributeDefinition, tableName, indexQueries);
						attributes.append(Strings.COMMA);
						this.createReferenceTableColumn(attributes, code, attributeDefinition, tableName, indexQueries);
						attributes.append(Strings.COMMA);
						break;
					case CHECK:
						break;
				}

			}
		}
		if (attributes.length() > 0)
			attributes = attributes.delete(attributes.length() - 1, attributes.length());

		HashMap<String, String> subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		subQueries.put(Database.QueryFields.ATTRIBUTES, attributes.toString());
		queries.add(new DatabaseRepositoryQuery(Database.Queries.REFERENCE_TABLE_CREATE, null, subQueries));

		HashMap<String, String> subQueriesParentIndex = new HashMap<String, String>();
		subQueriesParentIndex.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		subQueriesParentIndex.put(Database.QueryFields.INDEX, "id_parent");
		queries.add(new DatabaseRepositoryQuery(Database.Queries.REFERENCE_TABLE_CREATE_INDEX, null, subQueriesParentIndex));

		HashMap<String, String> subQueriesTrigger = new HashMap<String, String>();
		subQueriesTrigger.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		subQueriesTrigger.put(Database.QueryFields.COLUMNS, triggerColumns.toString());
		queries.add(new DatabaseRepositoryQuery(Database.Queries.REFERENCE_TABLE_CREATE_TRIGGER, null, subQueriesTrigger));

		HashMap<String, String> subQueriesIndex = new HashMap<String, String>();
		subQueriesIndex.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		queries.add(new DatabaseRepositoryQuery(Database.Queries.REFERENCE_TABLE_CREATE_FULLTEXT_INDEX, null, subQueriesIndex));

		queries.addAll(indexQueries);

		return queries;
	}

	public void deleteReferenceTable(IndexDefinition definition) {
		HashMap<String, String> subQueries;
		LinkedHashSet<DatabaseRepositoryQuery> queries;

		subQueries = new HashMap<String, String>();
		queries = new LinkedHashSet<DatabaseRepositoryQuery>();

		String tableName = this.getReferenceTableName(definition.getCode());

		subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		queries.add(new DatabaseRepositoryQuery(Database.Queries.REFERENCE_TABLE_DELETE, null, subQueries));

		this.agentDatabase.executeRepositoryQueries(queries.toArray(new DatabaseRepositoryQuery[0]));
	}

	private void create(String referenceTable, Node node, HashMap<String, Object> fields) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Iterator<String> iter = fields.keySet().iterator();
		StringBuilder attributes = new StringBuilder();
		StringBuilder values = new StringBuilder();

		while (iter.hasNext()) {
			String code = iter.next();
			Object value = fields.get(code);
			attributes.append(code);
			attributes.append(Strings.COMMA);
			values.append("@");
			values.append(code);
			values.append(Strings.COMMA);
			parameters.put(code, value);
		}
		if (attributes.length() > 0)
			attributes.deleteCharAt(attributes.length() - 1);
		if (values.length() > 0)
			values.deleteCharAt(values.length() - 1);

		HashMap<String, String> subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, referenceTable);
		subQueries.put(Database.QueryFields.ATTRIBUTES, attributes.toString());
		subQueries.put(Database.QueryFields.VALUES, values.toString());

		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		parameters.put(Database.QueryFields.ID_PARENT, node.getParentId());
		parameters.put(Database.QueryFields.CODE, node.getCode());

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.REFERENCE_ADD, parameters, subQueries);
	}

	private boolean existsReferenceItem(Node node, IndexDefinition definition) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		String tableName = this.getReferenceTableName(definition.getCode());
		ResultSet result = null;

		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		parameters.put(Database.QueryFields.ID_NODE, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.REFERENCE_EXISTS, parameters, subQueries);

			return result.next();
		} catch (SQLException exception) {
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	private void createReferenceItem(Reference nodeReference, Node node, IndexDefinition definition) {
		HashMap<String, Object> fields = new HashMap<String, Object>();
		String tableName = this.getReferenceTableName(definition.getCode());
		String descriptorsTableName = this.agentDatabase.getRepositoryProperty(Database.QueryProperties.TABLE_NODES_DESCRIPTORS);

		if (this.existsReferenceItem(node, definition)) {
			this.saveReferenceItem(nodeReference, node, definition, true, true);
			return;
		}

		ReferenceProperty referenceProperty = definition.getReference();
		if (referenceProperty != null) {
			for (AttributeProperty attributeDefinition : referenceProperty.getAttributePropertyList()) {
				String code = attributeDefinition.getCode();
				ReferenceAttribute<?> attribute = nodeReference.getAttribute(code);
				Object value = null;

				if (code.equals(DescriptorDefinition.ATTRIBUTE_ID_NODE))
					continue;
				if (code.equals(DescriptorDefinition.ATTRIBUTE_ID_PARENT))
					continue;
				if (code.equals(DescriptorDefinition.ATTRIBUTE_CODE))
					continue;

				if (attribute != null)
					value = attribute.getValue();

				if (value == null) {
					fields.put(code, null);
					switch (attributeDefinition.getType()) {
						case TERM:
						case LINK:
						case DATE:
						case PICTURE:
						case INTEGER:
						case REAL:
							if (!tableName.equals(descriptorsTableName))
								fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, null);
							break;
						case BOOLEAN:
						case STRING:
						case CHECK:
						case CATEGORY:
							break;
					}
				} else {
					switch (attributeDefinition.getType()) {
						case BOOLEAN:
						case STRING:
						case CATEGORY:
							fields.put(code, value);
							break;
						case INTEGER:
							Number integer = (Number) value;
							if (!tableName.equals(descriptorsTableName)) {
								String exValue = integer.formattedValue() != null && !integer.formattedValue().isEmpty() ? integer.formattedValue() : integer.textValue();
								fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, exValue);
							}
							fields.put(code, integer.intValue());
							break;
						case REAL:
							Number real = (Number) value;
							if (!tableName.equals(descriptorsTableName)) {
								String exValue = real.formattedValue() != null && !real.formattedValue().isEmpty() ? real.formattedValue() : real.textValue();
								fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, exValue);
							}
							fields.put(code, real.doubleValue());
							break;
						case DATE:
							Date dateTime = (Date) value;
							if (tableName.equals(descriptorsTableName)) {
								fields.put(code, this.agentDatabase.getDateAsTimestamp(dateTime.getValue()));
							} else {
								fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, this.agentDatabase.getDateAsTimestamp(dateTime.getValue()));
								fields.put(code, this.getFormattedValue(dateTime, attributeDefinition));
							}
							break;
						case PICTURE:
							Picture picture = (Picture) value;
							fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, picture.getFilename());
							fields.put(code, picture.getFilename());
							break;
						case TERM:
							Term term = (Term) value;
							if (!tableName.equals(descriptorsTableName))
								fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, term.getKey());
							fields.put(code, term.getLabel());
							break;
						case LINK:
							Link link = (Link) value;
							if (!tableName.equals(descriptorsTableName))
								fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, link.getId());
							fields.put(code, link.getLabel());
							break;
						case CHECK:
							break;
					}
				}
			}
		}

		this.create(tableName, node, fields);
	}

	private boolean save(String referenceTable, Node node, HashMap<String, Object> fieldsMap) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		Iterator<String> iter = fieldsMap.keySet().iterator();
		StringBuilder fieldsSubQuery = new StringBuilder();
		ArrayList<String> nodesIds = new ArrayList<String>();

		parameters.clear();
		while (iter.hasNext()) {
			String code = iter.next();
			Object value = fieldsMap.get(code);
			fieldsSubQuery.append(code);
			fieldsSubQuery.append(Strings.EQUAL);
			fieldsSubQuery.append("@");
			fieldsSubQuery.append(code);
			fieldsSubQuery.append(Strings.COMMA);
			parameters.put(code, value);
		}
		if (fieldsSubQuery.length() > 0)
			fieldsSubQuery.deleteCharAt(fieldsSubQuery.length() - 1);

		nodesIds.add(node.getId());
		subQueries.put(Database.QueryFields.ID_NODES, "'" + LibraryArray.implode(nodesIds.toArray(new String[0]), "','") + "'");
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, referenceTable);
		subQueries.put(Database.QueryFields.FIELDS, fieldsSubQuery.toString());

		return this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REFERENCE_SAVE, parameters, subQueries) > 0;
	}

	public void updateParent(Node node, IndexDefinition definition) {
		String tableName = this.getReferenceTableName(definition.getCode());
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		parameters.put(Database.QueryFields.ID_PARENT, node.getParentId());

		HashMap<String, String> subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REFERENCE_UPDATE_PARENT, parameters, subQueries);
	}

	public void saveUpdateDate(Node node) {
		String tableName = this.getReferenceTableName(DescriptorDefinition.CODE);
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID_NODE, node.getId());
		parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(node.getReference().getUpdateDate().getValue()));

		HashMap<String, String> subQueries = new HashMap<String, String>();
		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REFERENCE_SAVE_UPDATE_DATE, parameters, subQueries);
	}

	private void saveReferenceItem(Reference nodeReference, Node node, IndexDefinition definition) {
		saveReferenceItem(nodeReference, node, definition, false, false);
	}

	private void saveReferenceItem(Reference nodeReference, Node node, IndexDefinition definition, boolean fullSave, boolean saveGeoReference) {
		HashMap<String, Object> fields = new HashMap<String, Object>();
		String tableName = this.getReferenceTableName(definition.getCode());
		String descriptorsTableName = this.agentDatabase.getRepositoryProperty(Database.QueryProperties.TABLE_NODES_DESCRIPTORS);

		for (ReferenceAttribute<?> attribute : nodeReference.getAttributes().values()) {
			AttributeProperty attributeProperty = definition.getAttribute(attribute.getCode());
			String code = attribute.getCode();
			Object value = attribute.getValue();

			if (!fullSave && (code.equals(DescriptorDefinition.ATTRIBUTE_ID_NODE) || code.equals(DescriptorDefinition.ATTRIBUTE_CREATE_DATE) || code.equals(DescriptorDefinition.ATTRIBUTE_ID_OWNER) || code.equals(DescriptorDefinition.ATTRIBUTE_ID_PARENT) || code.equals(DescriptorDefinition.ATTRIBUTE_CODE)))
				continue;

			if (!saveGeoReference && code.equals(DescriptorDefinition.ATTRIBUTE_GEOREFERENCED))
				continue;

			if (attributeProperty == null)
				continue;

			if (value == null) {
				fields.put(code, null);
				switch (attributeProperty.getType()) {
					case INTEGER:
					case REAL:
					case TERM:
					case LINK:
					case DATE:
					case PICTURE:
						if (!tableName.equals(descriptorsTableName))
							fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, null);
						break;
					case STRING:
					case CHECK:
					case CATEGORY:
					case BOOLEAN:
						break;
				}
			} else {
				switch (attributeProperty.getType()) {
					case BOOLEAN:
					case STRING:
					case CATEGORY:
						fields.put(code, value);
						break;
					case INTEGER:
						Number integer = (Number) attribute.getValue();
						if (tableName.equals(descriptorsTableName)) {
							fields.put(code, integer.intValue());
						} else {
							fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, integer.formattedValue());
							fields.put(code, integer.intValue());
						}
						break;
					case REAL:
						Number real = (Number) attribute.getValue();
						if (tableName.equals(descriptorsTableName)) {
							fields.put(code, real.doubleValue());
						} else {
							fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, real.formattedValue());
							fields.put(code, real.doubleValue());
						}
						break;
					case DATE:
						Date dateTime = (Date) attribute.getValue();
						if (tableName.equals(descriptorsTableName)) {
							fields.put(code, this.agentDatabase.getDateAsTimestamp(dateTime.getValue()));
						} else {
							fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, this.agentDatabase.getDateAsTimestamp(dateTime.getValue()));
							fields.put(code, this.getFormattedValue(dateTime, attributeProperty));
						}
						break;
					case PICTURE:
						Picture picture = (Picture) attribute.getValue();
						fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, picture.getFilename());
						fields.put(code, picture.getFilename());
						break;
					case TERM:
						Term term = (Term) attribute.getValue();
						if (!tableName.equals(descriptorsTableName))
							fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, term.getKey());
						fields.put(code, term.getLabel());
						break;
					case LINK:
						Link link = (Link) attribute.getValue();
						if (!tableName.equals(descriptorsTableName))
							fields.put(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, link.getId());
						fields.put(code, link.getLabel());
						break;
					case CHECK:
						break;
				}
			}
		}

		if (fields.size() > 0) {
			if (!this.save(tableName, node, fields))
				this.create(tableName, node, fields);
			updateGeneratedThesaurus(definition);
		}
	}

	private String getFormattedValue(Date dateTime, AttributeProperty attributeProperty) {
		AttributeProperty.PrecisionEnumeration precisionEnumeration = attributeProperty.getPrecision();

		if (precisionEnumeration == null)
			return dateTime.getFormattedValue();

		String precision = null;
		switch (attributeProperty.getPrecision()) {
			case YEARS: precision = "yyyy"; break;
			case MONTHS: precision = "MMMM yyyy"; break;
			case DAYS: precision = "dd MMMM yyyy"; break;
			case HOURS: precision = "dd MMMM yyyy HH"; break;
			case MINUTES: precision = "dd MMMM yyyy HH:mm:ss,SS"; break;
			case SECONDS: precision = "dd MMMM yyyy HH:mm:ss,SS"; break;
		}

		if (precision == null)
			return dateTime.getFormattedValue();

		SimpleDateFormat formatter = new SimpleDateFormat(precision);
		return formatter.format(dateTime.getValue());
	}

	private void updateGeneratedThesaurus(IndexDefinition definition) {
		List<ThesaurusDefinition> thesaurusDefinitions = Dictionary.getInstance().getThesaurusGeneratedByIndex(definition);
		if (thesaurusDefinitions == null) return;
        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);
        ProducerSourceList producerSourceList = producersFactory.get(Producers.SOURCELIST);
        for (ThesaurusDefinition thesaurusDefinition : thesaurusDefinitions) {
            producerSource.saveUpdateDate(producerSourceList.load(thesaurusDefinition.getCode()).iterator().next());
        }
	}

	private void removeReferenceItem(Reference nodeReference, String idNode, IndexDefinition definition) {
		Map<String, String> subQueries = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();
		String tableName = this.getReferenceTableName(definition.getCode());

		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		parameters.put(Database.QueryFields.ID_NODE, idNode);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.NODE_REFERENCE_DELETE, parameters, subQueries);
	}

	private void addFilterNodesToQuery(String ownerId, List<String> filterNodes, String tableName, Map<String, String> subQueries) {
		String nodes = null;

		if (filterNodes != null && filterNodes.size() > 0) nodes = "'" + LibraryArray.implode(filterNodes, "','") + "'";

		if (nodes == null && ownerId == null) {
			String query = this.agentDatabase.getRepositoryQuery(Database.Queries.REFERENCE_LOAD_ATTRIBUTE_VALUES_CODES);
			QueryBuilder builder = new QueryBuilder(query);
			builder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, tableName);
			nodes = builder.build();
		}

		if (nodes != null) {
			String query = this.agentDatabase.getRepositoryQuery(ownerId != null ? Database.Queries.REFERENCE_LOAD_ATTRIBUTE_VALUES_CODENODES_SUBQUERY_FOR_OWNER : Database.Queries.REFERENCE_LOAD_ATTRIBUTE_VALUES_CODENODES_SUBQUERY);
			QueryBuilder builder = new QueryBuilder(query);
			if (ownerId == null) builder.insertSubQuery(Database.QueryFields.REFERENCE_TABLE, tableName);
			builder.insertSubQuery(Database.QueryFields.NODES, nodes);
			subQueries.put(Database.QueryFields.CODE_NODES_SUBQUERY, builder.build());
		}
		else subQueries.put(Database.QueryFields.CODE_NODES_SUBQUERY, "");
	}

	private void addFiltersToQuery(String nodeId, String codeReference, Map<String, String> subQueries, List<FilterProperty> filtersDefinition, boolean queryUsingView) {
		NodeDataRequest nodeDataRequest = defaultDataRequest(codeReference);
		((ProducerNodeList)producersFactory.get(Producers.NODELIST)).addFiltersToQuery(codeReference, subQueries, filtersDefinition, nodeId, nodeDataRequest.getParameters(), queryUsingView);
	}

	public String getReferenceTableName(String code) {
		if (code.equals(DescriptorDefinition.CODE))
			return this.agentDatabase.getRepositoryProperty(Database.QueryProperties.TABLE_NODES_DESCRIPTORS);
		return this.agentDatabase.getRepositoryProperty(Database.QueryProperties.TABLE_REFERENCES_PREFIX) + code;
	}

	public String getReferenceTableColumnName(String code, IndexDefinition referenceDefinition) {
		AttributeProperty attributeDefinition = referenceDefinition.getAttribute(code);

		if (attributeDefinition == null)
			return code;

		if (attributeDefinition.getType() == TypeEnumeration.DATE || attributeDefinition.getType() == TypeEnumeration.INTEGER || attributeDefinition.getType() == TypeEnumeration.REAL)
			return code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX;

		return code;
	}

	public void fill(ResultSet result, Reference reference) throws SQLException {
		IndexDefinition definition = reference.getDefinition();
		String tableName = this.getReferenceTableName(definition.getCode());
		String descriptorsTableName = this.agentDatabase.getRepositoryProperty(Database.QueryProperties.TABLE_NODES_DESCRIPTORS);
		List<AttributeProperty> attributeDefinitions = new ArrayList<>();
		boolean isDescriptorTable = tableName.equals(descriptorsTableName);

		ReferenceProperty referenceProperty = definition.getReference();
		if (referenceProperty != null)
			attributeDefinitions.addAll(referenceProperty.getAttributePropertyList());

		for (AttributeProperty attributeDefinition : attributeDefinitions) {
			String code = attributeDefinition.getCode();

			switch (attributeDefinition.getType()) {
				case BOOLEAN:
					reference.setAttributeValue(code, result.getBoolean(code));
					break;
				case INTEGER:
					if (isDescriptorTable) {
						Integer numberValue = result.getInt(code);
						reference.setAttributeValue(code, new Number(numberValue, String.valueOf(numberValue)));
					} else
						reference.setAttributeValue(code, new Number(result.getInt(code), result.getString(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX)));
					break;
				case REAL:
					if (isDescriptorTable) {
						Double numberValue = result.getDouble(code);
						reference.setAttributeValue(code, new Number(numberValue, String.valueOf(numberValue)));
					} else
						reference.setAttributeValue(code, new Number(result.getDouble(code), result.getString(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX)));
					break;
				case STRING:
				case CATEGORY:
					reference.setAttributeValue(code, result.getString(code));
					break;
				case DATE:
					if (isDescriptorTable) {
						java.util.Date date = result.getTimestamp(code);
						if (date != null)
							reference.setAttributeValue(code, new Date(date));
						else
							reference.setAttributeValue(code, null);
					} else {
						java.util.Date date = result.getTimestamp(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX);
						Date dateTime = null;
						if (date != null) {
							dateTime = new Date(date);
							dateTime.setFormattedValue(result.getString(code));
						}
						reference.setAttributeValue(code, dateTime);
					}
					break;
				case PICTURE:
					Picture picture = new Picture(result.getString(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX));
					picture.setFilename(result.getString(code));
					reference.setAttributeValue(code, picture);
					break;
				case TERM:
					reference.setAttributeValue(code, new Term(result.getString(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX), result.getString(code)));
					break;
				case LINK:
					reference.setAttributeValue(code, new Link(result.getString(code + REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX), result.getString(code)));
					break;
				case CHECK:
					break;
			}
		}
	}

	public Reference load(Node node) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Reference reference;

		parameters.put(Database.QueryFields.ID_NODE, node.getId());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_REFERENCE_LOAD_FROM_SYSTEM, parameters);

			reference = new Reference(DescriptorDefinition.CODE);
			if (result.next())
				this.fill(result, reference);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_NODE_REFERENCE, node.getId(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return reference;
	}

	public Reference load(Node node, String code) {
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		Reference reference;
		String tableName = this.getReferenceTableName(code);
		Node mainNode = node.getMainNode();
		String idNode = node.getId();

		subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);

		if (!code.equals(DescriptorDefinition.CODE))
			idNode = mainNode.getId();

		parameters.put(Database.QueryFields.ID_NODE, idNode);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.NODE_REFERENCE_LOAD, parameters, subQueries);
			reference = new Reference(code);
			if (result.next())
				this.fill(result, reference);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_NODE_REFERENCE, idNode, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return reference;
	}

	public List<String> loadAttributeValues(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filtersDefinition) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subQueries = new HashMap<>();
		String tableName = this.getReferenceTableName(codeReference);
		List<String> values = new ArrayList<>();

		createItemsView(ownerId, codeReference);

		subQueries.put(Database.QueryFields.ATTRIBUTE, codeAttribute);
		if (ownerId == null) subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		else subQueries.put(Database.QueryFields.ID_NODE, ownerId);
		addFilterNodesToQuery(ownerId, filterNodes, tableName, subQueries);
		addFiltersToQuery(ownerId, codeReference, subQueries, filtersDefinition, ownerId != null);

		try {
			String query = ownerId != null ? Database.Queries.REFERENCE_LOAD_ATTRIBUTE_VALUES_FOR_OWNER : Database.Queries.REFERENCE_LOAD_ATTRIBUTE_VALUES;
			result = this.agentDatabase.executeRepositorySelectQuery(query, parameters, subQueries);

			while (result.next()) {
				String value = result.getString(codeAttribute);
				if (value == null)
					continue;
				values.add(result.getString(codeAttribute));
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_REFERENCE_ATTRIBUTE_VALUES, codeReference, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return values;
	}

	public Map<String, Integer> loadAttributeValuesCount(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributes) {
		ResultSet result = null;
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subQueries = new HashMap<>();
		String tableName = this.getReferenceTableName(codeReference);
		Map<String, Integer> values = new HashMap<>();

		createItemsView(ownerId, codeReference);

		subQueries.put(Database.QueryFields.ATTRIBUTE, codeAttribute);
		if (ownerId == null) subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		else subQueries.put(Database.QueryFields.ID_NODE, ownerId);
		addFilterNodesToQuery(ownerId, filterNodes, tableName, subQueries);
		addFiltersToQuery(ownerId, codeReference, subQueries, filterAttributes, ownerId != null);

		try {
			String query = ownerId != null ? Database.Queries.REFERENCE_LOAD_ATTRIBUTE_VALUES_COUNT_FOR_OWNER : Database.Queries.REFERENCE_LOAD_ATTRIBUTE_VALUES_COUNT;
			result = this.agentDatabase.executeRepositorySelectQuery(query, parameters, subQueries);

			while (result.next()) {
				values.put(result.getString(codeAttribute), result.getInt("counter"));
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_REFERENCE_ATTRIBUTE_VALUES_COUNT, codeReference, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return values;
	}

	private void createItemsView(String ownerId, String codeReference) {
		((ProducerNodeList)producersFactory.get(Producers.NODELIST)).createItemsView(ownerId, defaultDataRequest(codeReference));
	}

	private NodeDataRequest defaultDataRequest(String codeReference) {
		NodeDataRequest result = new NodeDataRequest();
		result.setCodeDomainNode("");
		result.setCodeReference(codeReference);
		return result;
	}

	public boolean existsReferenceTable(String tableName) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;

		parameters.put(Database.QueryFields.REFERENCE_TABLE, tableName);
		parameters.put(Database.QueryFields.SCHEMA, this.agentDatabase.getSchemaName());

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.REFERENCE_TABLE_EXISTS, parameters);
			return result.next();
		} catch (SQLException oException) {
			AgentLogger.getInstance().error(oException);
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public void create(Node node, Reference reference) {
		Node mainNode = node.getMainNode();

		try {

			String code = reference.getCode();

			if (code.equals(DescriptorDefinition.CODE))
				this.createReferenceItem(node.getReference(), node, new DescriptorDefinition());
			else {
				IndexDefinition indexDefinition = Dictionary.getInstance().getIndexDefinition(code);
				this.createReferenceItem(reference, mainNode, indexDefinition);
				updateGeneratedThesaurus(indexDefinition);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.CREATE_NODE_REFERENCE, node.getId(), exception);
		}
	}

	public void save(Node node, Reference nodeReference) {
		IndexDefinition referenceDeclaration;
		Node mainNode = node.getMainNode();
		boolean saveGeoReference = true;

		if (!node.getDefinition().hasMappings())
			mainNode = node;

		if (node != mainNode && mainNode.getDefinition().isGeoreferenced()) {
			saveGeoReference = false;
			nodeReference.setGeoReferenced(new Number(mainNode.getReference().isGeoReferenced() ? 1 : 0));
		}

		if (nodeReference.getCode().equals(DescriptorDefinition.CODE)) {
			if (nodeReference.getLabel() == null || nodeReference.getLabel().isEmpty())
				nodeReference.setLabel(Language.getInstance().getLabel(LabelCode.NO_LABEL, Language.getCurrent()));

			referenceDeclaration = new DescriptorDefinition();
			this.saveReferenceItem(nodeReference, mainNode, referenceDeclaration, false, saveGeoReference);
			this.saveReferenceItem(nodeReference, node, referenceDeclaration, false, saveGeoReference);
		} else {
			referenceDeclaration = Dictionary.getInstance().getIndexDefinition(nodeReference.getCode());
			this.saveReferenceItem(nodeReference, mainNode, referenceDeclaration, false, saveGeoReference);
		}
	}

	public void remove(Node node) {
		Reference nodeReference;
		NodeDefinition nodeDefinition;
		Node mainNode = node.getMainNode();

		nodeReference = node.getReference();

		try {
			nodeDefinition = node.getDefinition();

			if (nodeDefinition.isForm()) {
				FormDefinition formDefinition = (FormDefinition) nodeDefinition;
				for (MappingProperty mapping : formDefinition.getMappingList()) {
					IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(mapping.getIndex().getValue());
					this.removeReferenceItem(nodeReference, mainNode.getId(), referenceDefinition);
				}
			} else if (nodeDefinition.isDocument()) {
				DocumentDefinition documentDefinition = (DocumentDefinition) nodeDefinition;
				for (org.monet.metamodel.DocumentDefinitionBase.MappingProperty mapping : documentDefinition.getMappingList()) {
					IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(mapping.getIndex().getValue());
					this.removeReferenceItem(nodeReference, mainNode.getId(), referenceDefinition);
				}
			}

			this.removeReferenceItem(nodeReference, node.getId(), new DescriptorDefinition());

		} catch (Exception oException) {
			throw new DataException(ErrorCode.REMOVE_NODE_REFERENCE, node.getId(), oException);
		}
	}

	public void remove(Node node, Reference nodeReference) {
		Node mainNode = node.getMainNode();

		try {
			IndexDefinition referenceDefinition = Dictionary.getInstance().getIndexDefinition(nodeReference.getCode());
			this.removeReferenceItem(nodeReference, mainNode.getId(), referenceDefinition);
		} catch (Exception oException) {
			throw new DataException(ErrorCode.REMOVE_NODE_REFERENCE, node.getId(), oException);
		}
	}

	public HashMap<String, String> getIndexColumnsAsString(ResultSet resultSet, IndexDefinition indexDefinition) throws SQLException {
		HashMap<String, String> result = new HashMap<String, String>();

		ReferenceProperty referenceProperty = indexDefinition.getReference();
		if (referenceProperty != null) {
			for (AttributeProperty attributeDefinition : referenceProperty.getAttributePropertyList()) {
				String code = attributeDefinition.getCode();

				switch (attributeDefinition.getType()) {
					case BOOLEAN:
						result.put(code, String.valueOf(resultSet.getBoolean(code)));
						break;
					case INTEGER:
					case REAL:
					case STRING:
					case CATEGORY:
					case DATE:
					case PICTURE:
					case TERM:
					case LINK:
						result.put(code, resultSet.getString(code));
						break;
					case CHECK:
						break;
				}
			}
		}

		return result;
	}

	public Object newObject() {
		return new Reference(DescriptorDefinition.CODE);
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}