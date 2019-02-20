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

import org.monet.bpi.types.Number;
import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Reference;
import org.monet.space.kernel.model.ReferenceList;

import java.sql.ResultSet;
import java.util.*;

import static org.monet.metamodel.AttributeProperty.TypeEnumeration.*;

public class ProducerReferenceList extends Producer {

	public List<String> loadNodeId(String codeReference) {
		List<String> referenceList = new ArrayList<>();
		ResultSet resultSet = null;
		Map<String, String> subQueries = new HashMap<>();
		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);

		try {
			String tableName = producerReference.getReferenceTableName(codeReference);

			subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.REFERENCE_LIST_LOAD_NODE_ID, null, subQueries);

			while (resultSet.next()) {
				referenceList.add(resultSet.getString("id_node"));
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_REFERENCELIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return referenceList;
	}

	public ReferenceList load(String code, String filter, String ordersBy, Map<String, Object> parameters, int startPos, int limit) {
		ReferenceList referenceList = new ReferenceList();
		ResultSet resultSet = null;
		Map<String, String> subQueries = new HashMap<>();
		ProducerReference producerReference = producersFactory.get(Producers.REFERENCE);

		try {
			String tableName = producerReference.getReferenceTableName(code);

			if (parameters == null)
				parameters = new HashMap<>();
			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(startPos));
			parameters.put(Database.QueryFields.LIMIT, limit);

			if (ordersBy == null || ordersBy.trim().isEmpty())
				ordersBy = "code,id_node";

			subQueries.put(Database.QueryFields.REFERENCE_TABLE, tableName);
			subQueries.put(Database.QueryFields.WHERE, filter == null || filter.trim().isEmpty() ? "1=1" : filter);
			subQueries.put(Database.QueryFields.ORDERBY, "ORDER BY " + addOrdersBy(code, ordersBy));
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.REFERENCE_LIST_LOAD, parameters, subQueries);

			while (resultSet.next()) {
				Reference reference = new Reference(code);
				producerReference.fill(resultSet, reference);
				reference.setAttributeValue(DescriptorDefinition.ATTRIBUTE_ID_NODE, new Number(resultSet.getInt("id_node"), resultSet.getString("id_node")));
				referenceList.add(reference);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_REFERENCELIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return referenceList;
	}

	public int loadTotalCount(String code, String filter, Map<String, Object> parameters) {
		ResultSet resultSet = null;
		Map<String, String> subQueries = new HashMap<>();
		ProducerReference producerReference = this.producersFactory.get(Producers.REFERENCE);

		try {
			subQueries.put(Database.QueryFields.REFERENCE_TABLE, producerReference.getReferenceTableName(code));
			subQueries.put(Database.QueryFields.WHERE, filter == null || filter.trim().isEmpty() ? "1=1" : filter);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.REFERENCE_LIST_LOAD_COUNT, parameters, subQueries);

			if (resultSet.next())
				return resultSet.getInt("totalCount");
			else
				return 0;

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_REFERENCELIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}
	}

	public Object newObject() {
		return new ReferenceList();
	}

	@Override
	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

	private String addOrdersBy(String index, String ordersBy) {
		String[] orderByArray = ordersBy.split(",");
		StringBuilder result = new StringBuilder();

		for (String originalOrderBy : orderByArray) {
			String[] originalOrderByArray = originalOrderBy.trim().split(" ");
			String orderByAttribute = originalOrderByArray[0];
			String orderByAttributeType = originalOrderByArray.length>=2?originalOrderByArray[1]:"ASC";
			AttributeProperty attributeDefinition = getIndexAttribute(index, orderByAttribute.replace(ProducerReference.REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX, ""));
            String column;

			if (attributeDefinition.getType() == DATE)
				column = attributeDefinition.getCode() + ProducerReference.REFERENCE_ATTRIBUTE_COLUMN_EXTRA_SUFFIX;
            else
                column = orderByAttribute;

            if (result.length() > 0)
                result.append(",");

			result.append(column + " " + orderByAttributeType);
		}

		return result.toString();
	}

	private AttributeProperty getIndexAttribute(String index, String attributeCode) {
		IndexDefinition indexDefinition = this.getDictionary().getIndexDefinition(index);
		AttributeProperty attributeDefinition = indexDefinition.getAttribute(attributeCode);

		if (attributeDefinition == null) {
			indexDefinition = new DescriptorDefinition();
			attributeDefinition = indexDefinition.getAttribute(attributeCode);
		}

		return attributeDefinition;
	}

}
