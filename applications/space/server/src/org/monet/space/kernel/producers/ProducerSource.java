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

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProducerSource extends Producer {
	private ProducerSourceStore producerSourceStore;

	public ProducerSource() {
		super();
		this.producerSourceStore = (ProducerSourceStore) this.producersFactory.get(Producers.SOURCESTORE);
	}

	public boolean exists(String code, FeederUri uri) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		ResultSet result = null;

		parameters.put(Database.QueryFields.CODE, code);

		try {

			if (uri == null)
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_EXISTS, parameters);
			else {
				parameters.put(Database.QueryFields.URI, uri.toString());
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_FEEDER_EXISTS, parameters);
			}

			return result.next();
		} catch (SQLException exception) {
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public void fill(Source<SourceDefinition> source, MonetResultSet result) throws SQLException {
		source.setId(result.getString("id"));
		source.setName(result.getString("name"));
		source.setPartnerName(result.getString("partner_name"));
		source.setPartnerLabel(result.getString("partner_label"));
		source.setUri(FeederUri.build(result.getString("uri")));
		source.setLabel(result.getString("label"));
		source.setCreateDate(result.getTimestamp("create_date"));
		source.setUpdateDate(result.getTimestamp("update_date"));
		source.setEnabled(Boolean.valueOf(result.getString("enabled")));
	}

	public Source<SourceDefinition> load(String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		MonetResultSet result = null;
		Source<SourceDefinition> source = null;

		parameters.put(Database.QueryFields.ID, id);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_LOAD, parameters);

			if (!result.next())
				throw new Exception(String.format("Source '%s' not found", id));

			source = Source.createInstance(result.getString("code"), SourceProvider.getInstance());
			this.fill(source, result);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_SOURCE, id, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return source;
	}

	public Source<SourceDefinition> locate(String code, FeederUri uri) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		SourceDefinition definition = this.getDictionary().getSourceDefinition(code);
		MonetResultSet result = null;
		Source<SourceDefinition> source = null;

		parameters.put(Database.QueryFields.CODE, definition.getCode());

		try {
			if (uri == null)
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_LOCATE, parameters);
			else {
				parameters.put(Database.QueryFields.URI, uri.toString());
				result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_FEEDER_LOCATE, parameters);
			}

			if (!result.next())
				return source;

			source = Source.createInstance(definition.getCode(), SourceProvider.getInstance());
			this.fill(source, result);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_SOURCE, definition.getName(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return source;
	}

	public Source<SourceDefinition> create(String code) {
		SourceDefinition definition = Dictionary.getInstance().getSourceDefinition(code);
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, Object> emptyParameters = new HashMap<String, Object>();
		HashMap<String, String> queryParameters = new HashMap<String, String>();
		Source<SourceDefinition> source = Source.createInstance(definition.getCode(), SourceProvider.getInstance());
		ArrayList<DatabaseRepositoryQuery> queriesSet = new ArrayList<DatabaseRepositoryQuery>();
		DatabaseRepositoryQuery[] queriesArray;

		source.setCode(definition.getCode());
		source.setName(definition.getName());
		source.setLabel(definition.getLabelString());
		source.setCreateDate(new Date());
		source.setUpdateDate(new Date());
		source.setEnabled(true);

		try {
			parameters.put(Database.QueryFields.CODE, definition.getCode());
			parameters.put(Database.QueryFields.NAME, source.getName());
			parameters.put(Database.QueryFields.TYPE, source.getType().toString());
			parameters.put(Database.QueryFields.ONTOLOGY, definition.getOntology());
			parameters.put(Database.QueryFields.PARTNER_NAME, source.getPartnerName());
			parameters.put(Database.QueryFields.PARTNER_LABEL, source.getPartnerLabel());
			parameters.put(Database.QueryFields.URI, source.getUri()!=null?source.getUri().toString():null);
			parameters.put(Database.QueryFields.LABEL, source.getLabel());
			parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(source.getCreateDate()));
			parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(source.getUpdateDate()));
			parameters.put(Database.QueryFields.ENABLED, String.valueOf(source.isEnabled()));

			String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.SOURCE_ADD, parameters);
			source.setId(id);

			queryParameters.put(Database.QueryFields.SOURCE, source.getKey());

			queriesSet.add(new DatabaseRepositoryQuery(Database.Queries.SOURCE_STORE_CREATE_ANCESTORS, emptyParameters, queryParameters));
			queriesSet.add(new DatabaseRepositoryQuery(Database.Queries.SOURCE_STORE_CREATE, emptyParameters, queryParameters));

			queriesArray = new DatabaseRepositoryQuery[queriesSet.size()];
			this.agentDatabase.executeRepositoryUpdateTransaction(queriesSet.toArray(queriesArray));
		} catch (Exception oException) {
			this.remove(source);
			throw new DataException(ErrorCode.CREATE_SOURCE, definition.getCode(), oException);
		}

		return source;
	}

	public void save(Source<SourceDefinition> source) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		source.setUpdateDate(new Date());

		parameters.put(Database.QueryFields.ID, source.getId());
		parameters.put(Database.QueryFields.PARTNER_NAME, source.getPartnerName());
		parameters.put(Database.QueryFields.PARTNER_LABEL, source.getPartnerLabel());
		parameters.put(Database.QueryFields.URI, source.getUri()!=null?source.getUri().toString():null);
		parameters.put(Database.QueryFields.LABEL, source.getLabel());
		parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(source.getUpdateDate()));
		parameters.put(Database.QueryFields.ENABLED, String.valueOf(source.isEnabled()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SOURCE_SAVE, parameters);
	}

	public void saveUpdateDate(Source<SourceDefinition> source) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		source.setUpdateDate(new Date());

		parameters.put(Database.QueryFields.ID, source.getId());
		parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(source.getUpdateDate()));

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SOURCE_SAVE_UPDATE_DATE, parameters);
	}

	public void populate(Source<SourceDefinition> source) {
		this.producerSourceStore.populate(source);
		this.saveUpdateDate(source);
	}

	public void synchronize(Source<SourceDefinition> source) {
		if (this.producerSourceStore.synchronize(source))
			this.saveUpdateDate(source);
	}

	public void remove(Source<SourceDefinition> source) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		HashMap<String, String> subQueries = new HashMap<String, String>();
		HashSet<DatabaseRepositoryQuery> queriesSet = new HashSet<DatabaseRepositoryQuery>();
		DatabaseRepositoryQuery[] queriesArray;

		parameters.put(Database.QueryFields.ID, source.getId());
		subQueries.put(Database.QueryFields.SOURCE, source.getKey());

		queriesSet.add(new DatabaseRepositoryQuery(Database.Queries.SOURCE_DELETE, parameters));
		queriesSet.add(new DatabaseRepositoryQuery(Database.Queries.SOURCE_STORE_REMOVE, null, subQueries));
		queriesSet.add(new DatabaseRepositoryQuery(Database.Queries.SOURCE_STORE_REMOVE_ANCESTORS, null, subQueries));

		queriesArray = new DatabaseRepositoryQuery[queriesSet.size()];
		try {
			this.agentDatabase.executeRepositoryUpdateTransaction(queriesSet.toArray(queriesArray));
		} catch (SQLException e) {
			this.agentLogger.error(e);
		}
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}