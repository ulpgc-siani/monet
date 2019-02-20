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

import org.apache.commons.lang.StringEscapeUtils;
import org.monet.space.kernel.agents.AgentDatabase;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class ProducerHistoryStore extends Producer {

	public ProducerHistoryStore() {
		super();
	}

	public User getUser() {
		return Context.getInstance().getCurrentSession().getAccount().getUser();
	}

	private TermList readTerms(String codeStore, DataRequest dataRequest) {
		TermList list = new TermList();
		AgentDatabase agentDatabase = AgentDatabase.getInstance();
		ResultSet result = null;

		if (!this.exists(codeStore)) this.create(codeStore);

		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(Database.QueryFields.ID_USER, this.getUser().getId());
			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());
			parameters.put(Database.QueryFields.CONDITION, "%" + LibraryString.cleanAccents(dataRequest.getCondition().toLowerCase()) + "%");

			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(Database.QueryFields.STORE, codeStore);

			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.HISTORY_STORE_LOAD_DATA, parameters, subQueries);

			while (result.next()) {
				Term term = new Term();
				term.addAttribute(Term.CODE, result.getString("code"));
				term.addAttribute(Term.LABEL, result.getString("label"));
				list.add(term);
			}
			this.agentDatabase.closeQuery(result);

			parameters.clear();
			parameters.put(Database.QueryFields.ID_USER, this.getUser().getId());
			parameters.put(Database.QueryFields.CONDITION, "%" + LibraryString.cleanAccents(dataRequest.getCondition().toLowerCase()) + "%");
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.HISTORY_STORE_LOAD_DATA_COUNT, parameters, subQueries);
			if (!result.next())
				throw new Exception(String.format("History '%s' not found", codeStore));
			list.setTotalCount(result.getInt("counter"));
		} catch (Exception exception) {
			throw new DataException(ErrorCode.HISTORY_STORE_LOAD, dataRequest.getCondition(), exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return list;
	}

	private void addTermToHistory(String codeStore, String code, String label) {
		AgentDatabase agentDatabase = AgentDatabase.getInstance();
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result;
		String query;
		int frequency = 1;

		if (!this.exists(codeStore)) this.create(codeStore);

		label = StringEscapeUtils.unescapeHtml(label);
		code = ((code == null) || (code.equals(Strings.EMPTY))) ? label : code;

		parameters.put(Database.QueryFields.ID_USER, this.getUser().getId());
		parameters.put(Database.QueryFields.CODE, code);

		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(Database.QueryFields.STORE, codeStore);

		result = agentDatabase.executeRepositorySelectQuery(Database.Queries.HISTORY_STORE_EXIST_DATA, parameters, subQueries);

		try {
			if (result.next()) {
				query = Database.Queries.HISTORY_STORE_UPDATE_FREQUENCY;
				frequency = result.getInt("frequency") + 1;
			} else {
				query = Database.Queries.HISTORY_STORE_ADD_DATA;
			}
		} catch (SQLException exception) {
			throw new DataException(ErrorCode.HISTORY_STORE_EXIST, codeStore + Strings.SPACE + code, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		parameters.put(Database.QueryFields.ID_USER, this.getUser().getId());
		parameters.put(Database.QueryFields.CODE, code);
		if (query.equals(Database.Queries.HISTORY_STORE_ADD_DATA)) parameters.put(Database.QueryFields.LABEL, label);
		parameters.put(Database.QueryFields.FREQUENCY, frequency);

		subQueries = new HashMap<>();
		subQueries.put(Database.QueryFields.STORE, codeStore);

		agentDatabase.executeRepositoryUpdateQuery(query, parameters, subQueries);

	}

	public boolean exists(String code) {
		Map<String, String> subQueries = new HashMap<>();
		ResultSet result;

		subQueries.put(Database.QueryFields.STORE, code);
		subQueries.put(Database.QueryFields.SCHEMA, this.agentDatabase.getSchemaName());
		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.HISTORY_STORE_EXISTS, null, subQueries);

		try {
			return result.next();
		} catch (SQLException exception) {
			return false;
		} finally {
			this.agentDatabase.closeQuery(result);
		}
	}

	public HistoryStore load(String code) {
		return new HistoryStore(code, HistoryStoreProvider.getInstance());
	}

	public HistoryStore create(String code) {
		Map<String, String> queryParameters = new HashMap<>();
		HistoryStore store = new HistoryStore(code, HistoryStoreProvider.getInstance());

		try {
			queryParameters.put(Database.QueryFields.STORE, code);
			DatabaseRepositoryQuery[] queriesArray = new DatabaseRepositoryQuery[1];
			queriesArray[0] = new DatabaseRepositoryQuery(Database.Queries.HISTORY_STORE_CREATE, null, queryParameters);
			this.agentDatabase.executeRepositoryQueries(queriesArray);
		} catch (Exception oException) {
			this.remove(code);
			throw new DataException(ErrorCode.HISTORY_STORE_CREATE, code, oException);
		}

		return store;
	}

	public void remove(String code) {
		Map<String, String> subQueries = new HashMap<>();

		subQueries.put(Database.QueryFields.STORE, code);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.HISTORY_STORE_REMOVE, null, subQueries);
	}

	public TermList loadTerms(String codeStore, DataRequest oDataRequest) {
		if (this.getUser() == null) return new TermList();

		return this.readTerms(codeStore, oDataRequest);
	}

	public TermList searchTerms(String codeStore, DataRequest dataRequest) {
		if (dataRequest.getCondition().equals(Strings.EMPTY)) return this.loadTerms(codeStore, dataRequest);
		if (this.getUser() == null) return new TermList();

		return this.readTerms(codeStore, dataRequest);
	}

	public void addTerm(String codeStore, String code, String label) {
		if (this.getUser() == null) return;
		this.addTermToHistory(codeStore, code, label);
	}

	public MonetHashMap<String> loadDefaultValues(String codeStore, String codeNodeType) {
		AgentDatabase agentDatabase = AgentDatabase.getInstance();
		Map<String, Object> parameters = new HashMap<>();
		User user = Context.getInstance().getCurrentSession().getAccount().getUser();
		ResultSet resultSet = null;
		MonetHashMap<String> resultMap;

		parameters.put(Database.QueryFields.CODE_NODE, codeNodeType);
		parameters.put(Database.QueryFields.ID_USER, user.getId());


		try {
			resultSet = agentDatabase.executeRepositorySelectQuery(Database.Queries.HISTORY_STORE_LOAD_DEFAULT_VALUES, parameters);

			resultMap = new MonetHashMap<>();
			while (resultSet.next()) {
				resultMap.put(resultSet.getString("property"), resultSet.getString("data"));
			}
		} catch (SQLException exception) {
			throw new DataException(ErrorCode.HISTORY_STORE_LOAD_DEFAULT_VALUES, user.getId() + " - " + codeNodeType, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return resultMap;
	}

	public String loadDefaultValue(String codeStore, String codeNodeType, String codeProperty) {
		AgentDatabase agentDatabase = AgentDatabase.getInstance();
		Map<String, Object> parameters = new HashMap<>();
		User user = Context.getInstance().getCurrentSession().getAccount().getUser();
		ResultSet resultSet = null;
		String result;

		parameters.put(Database.QueryFields.CODE_NODE, codeNodeType);
		parameters.put(Database.QueryFields.ID_USER, user.getId());
		parameters.put(Database.QueryFields.PROPERTY, codeProperty);

		try {
			resultSet = agentDatabase.executeRepositorySelectQuery(Database.Queries.HISTORY_STORE_LOAD_DEFAULT_VALUE, parameters);

			if (resultSet.next()) result = resultSet.getString("data");
			else result = Strings.EMPTY;
		} catch (SQLException exception) {
			throw new DataException(ErrorCode.HISTORY_STORE_LOAD_DEFAULT_VALUE, user.getId() + " - " + codeNodeType + " - " + codeProperty, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	public void addDefaultValue(String codeStore, String codeNodeType, String codeProperty, String label) {
		AgentDatabase agentDatabase = AgentDatabase.getInstance();
		Map<String, Object> parameters = new HashMap<>();
		User user = Context.getInstance().getCurrentSession().getAccount().getUser();
		ResultSet result = null;
		String query;

		parameters.put(Database.QueryFields.CODE_NODE, codeNodeType);
		parameters.put(Database.QueryFields.ID_USER, user.getId());
		parameters.put(Database.QueryFields.PROPERTY, codeProperty);

		try {
			result = agentDatabase.executeRepositorySelectQuery(Database.Queries.HISTORY_STORE_LOAD_DEFAULT_VALUE, parameters);
			if (result.next()) query = Database.Queries.HISTORY_STORE_UPDATE_DEFAULT_VALUE;
			else query = Database.Queries.HISTORY_STORE_ADD_DEFAULT_VALUE;
		} catch (SQLException exception) {
			throw new DataException(ErrorCode.HISTORY_STORE_EXIST, codeNodeType + " - " + codeProperty + " - " + label, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		parameters.put(Database.QueryFields.DATA, label);
		agentDatabase.executeRepositoryUpdateQuery(query, parameters);

	}

	public void save(HistoryStore store) {
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject oEventObject, String sAttribute) {
	}

}
