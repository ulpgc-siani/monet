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

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.ThesaurusDefinition;
import org.monet.metamodel.ThesaurusDefinitionBase.SelfGeneratedProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentFederationUnit;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.agents.AgentRestfullClient;
import org.monet.space.kernel.agents.AgentRestfullClient.RequestParameter;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.DatabaseException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import static org.monet.space.kernel.constants.Database.Queries.*;
import static org.monet.space.kernel.constants.Database.QueryFields.*;

public class ProducerSourceStore extends Producer {

	public ProducerSourceStore() {
		super();
	}

	public boolean existsTerm(Source<SourceDefinition> source, String code) {
		ResultSet result = null;

		try {
			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, source.getKey());

			Map<String, Object> parameters = new HashMap<>();
			parameters.put(CODE, code);

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_EXISTS_TERM, parameters, subQueries);

			return result.next();
		} catch (Exception exception) {
			this.agentLogger.error(exception);
		} finally {
			if (result != null) this.agentDatabase.closeQuery(result);
		}

		return false;
	}

	public Term loadTerm(Source<SourceDefinition> source, String code) {
		ResultSet result = null;
		Term term = new Term();

		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(CODE, code);

			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, source.getKey());

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_LOAD_TERM, parameters, subQueries);

			result.next();
			this.fillTerm(source, term, result);
		} catch (Exception exception) {
			this.agentLogger.error(exception);
		} finally {
			if (result != null) this.agentDatabase.closeQuery(result);
		}

		return term;
	}

	public void updateTerm(Source<SourceDefinition> source, Term term) {
		ResultSet result = null;

		try {
			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, source.getKey());

			Map<String, Object> updateTermParams = new HashMap<>();
			updateTermParams.put(CODE, term.getCode());
			updateTermParams.put(LABEL, term.getLabel());
			updateTermParams.put(TAGS, SerializerData.serializeSet(term.getTags()));
			updateTermParams.put(TYPE, term.getType());
			updateTermParams.put(IS_ENABLE, term.isEnabled());
			updateTermParams.put(IS_NEW, term.isNew());
			updateTermParams.put(IS_LEAF, term.getType() == Term.TERM);
			updateTermParams.put(UPDATE_DATE, this.agentDatabase.getDateAsMilliseconds(new Date()));
			this.agentDatabase.executeRepositoryUpdateQuery(SOURCE_STORE_SAVE_TERM, updateTermParams, subQueries);

			subQueries.put(CODE, term.getCode());
			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_CALCULATE_TERM_FLATTEN_LABEL, null, subQueries);

			if (!result.next())
				throw new Exception("Can't calculate flatten label for source store term");

			Map<String, Object> flattenTermParams = new HashMap<>();
			flattenTermParams.put(CODE, term.getCode());
			flattenTermParams.put(FLATTEN_LABEL, result.getString("label"));
			subQueries.remove(CODE);

			this.agentDatabase.closeQuery(result);
			this.agentDatabase.executeRepositoryUpdateQuery(SOURCE_STORE_UPDATE_TERM_FLATTEN_LABEL, flattenTermParams, subQueries);

			MonetEvent monetEvent = new MonetEvent(MonetEvent.SOURCE_TERM_MODIFIED, null, source);
			monetEvent.addParameter(MonetEvent.PARAMETER_TERM, term);
			AgentNotifier.getInstance().notify(monetEvent);

		} catch (Exception exception) {
			this.agentLogger.error(exception);
		} finally {
			if (result != null) this.agentDatabase.closeQuery(result);
		}
	}

	public void deleteTerm(Source<SourceDefinition> source, String code) {
		ResultSet result = null;

		try {
			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, source.getKey());

			Map<String, Object> parameters = new HashMap<>();
			parameters.put(CODE, code);

			DatabaseRepositoryQuery[] queries = new DatabaseRepositoryQuery[2];
			queries[0] = new DatabaseRepositoryQuery(SOURCE_STORE_DELETE_TERM, parameters, subQueries);
			queries[1] = new DatabaseRepositoryQuery(SOURCE_STORE_DELETE_TERM_ANCESTORS, parameters, subQueries);

			this.agentDatabase.executeRepositoryUpdateTransaction(queries);
		} catch (Exception exception) {
			this.agentLogger.error(exception);
		} finally {
			if (result != null) this.agentDatabase.closeQuery(result);
		}
	}

	public boolean publishTerms(Source<SourceDefinition> source, String[] terms) {
		StringBuilder builder = new StringBuilder();

        for (String term : terms)
            builder.append("'").append(term).append("',");
		builder.deleteCharAt(builder.length() - 1);

		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(SOURCE, source.getKey());
		subQueries.put(TERMS, builder.toString());

		this.agentDatabase.executeRepositoryUpdateQuery(SOURCE_STORE_PUBLISH_TERMS, null, subQueries);

		return true;
	}

	public TermList loadTerms(Source<SourceDefinition> source, DataRequest dataRequest, boolean onlyPublished) {
		if (source.isSelfGenerated())
			return this.loadSelfGeneratedTerms(source, dataRequest, onlyPublished);

		TermList termList = new TermList();
		ResultSet result = null;
		String key = source.getKey();

		try {

            Map<String, Object> parameters = new HashMap<>();

            Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, key);

			addPublishedToQuery(key, onlyPublished, parameters, subQueries, false);
			addConditionToQuery(key, dataRequest, parameters, subQueries, false);
			addFiltersToQuery(key, dataRequest, parameters, subQueries, null, null, false);
			addAncestorLevelToQuery(key, dataRequest, parameters, subQueries, false);
			addLeafToQuery(key, dataRequest, parameters, subQueries, false);
			addFromToQuery(key, dataRequest, parameters, subQueries, null);
			addTypeToQuery(key, dataRequest, parameters, subQueries, false);

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_LOAD_DATA_COUNT, parameters, subQueries);
			if (!result.next())
				throw new Exception("Can't get total count of source store");
			termList.setTotalCount(result.getInt("counter"));
			this.agentDatabase.closeQuery(result);

            String mode = dataRequest.getParameter(DataRequest.MODE);
			int limit = dataRequest.getLimit();
			if (mode != null && mode.equals(DataRequest.Mode.TREE)) limit = 0;

			parameters.put(START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(LIMIT, (limit > 0) ? limit : termList.getTotalCount());

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_LOAD_DATA, parameters, subQueries);
			this.fillTermList(source, termList, mode, result);

		} catch (DatabaseException exception) {
			AgentLogger.getInstance().error(exception);
			return termList;
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
			return termList;
		} finally {
			agentDatabase.closeQuery(result);
		}

		return termList;
	}

    public TermList loadTerms(Source<SourceDefinition> source, boolean onlyPublished) {
		DataRequest dataRequest = new DataRequest();

		dataRequest.setStartPos(0);
		dataRequest.setLimit(-1);

		return this.loadTerms(source, dataRequest, onlyPublished);
	}

	public List<Term> loadAncestorsTerms(Source<SourceDefinition> source, Term term) {
		if (source.isSelfGenerated())
			return this.loadSelfGeneratedAncestorsTerms(source, term);

		List<Term> ancestors = new ArrayList<>();
		ResultSet result = null;

		try {

			Map<String, Object> parameters = new HashMap<>();
			parameters.put(CODE, term.getCode());

			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, source.getKey());

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_LOAD_ANCESTORS, parameters, subQueries);

			while (result.next()) {
				Term ancestorTerm = new Term();
				this.fillTerm(source, ancestorTerm, result);
				ancestors.add(ancestorTerm);
			}

		} catch (DatabaseException exception) {
			AgentLogger.getInstance().error(exception);
			return ancestors;
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
			return ancestors;
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return ancestors;
	}

    public TermList loadNewTerms(Source<SourceDefinition> source) {
		TermList termList = new TermList();
		ResultSet result = null;

		try {

			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, source.getKey());

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_LOAD_NEW_DATA, null, subQueries);
			this.fillTermList(source, termList, DataRequest.Mode.TREE, result);

		} catch (DatabaseException exception) {
			AgentLogger.getInstance().error(exception);
			return termList;
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
			return termList;
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return termList;
	}

	public TermList loadGlossaryTermList(Source<SourceDefinition> feeder) {
		TermList termList = new TermList();

		try {
			ArrayList<Entry<String, ContentBody>> parameters = new ArrayList<>();
            FeederUri uri = feeder.getUri();
            String feederUrl = AgentFederationUnit.getFeederUrl(uri);

			parameters.add(new RequestParameter(RequestParameter.SOURCE_NAME, new StringBody(uri.getId(), ContentType.TEXT_PLAIN)));
			parameters.add(new RequestParameter(RequestParameter.ACTION, new StringBody("synchronize", ContentType.TEXT_PLAIN)));
			String result = AgentRestfullClient.getInstance().executeWithAuth(feederUrl, parameters);

			termList.deserializeFromXML(result, null);
		} catch (Exception e) {
			this.agentLogger.error(e);
			return null;
		}

		return termList;
	}

	public TermList searchTerms(Source<SourceDefinition> source, DataRequest dataRequest) {
		if (source.isSelfGenerated())
			return this.searchSelfGeneratedTerms(source, dataRequest);

		TermList termList = new TermList();
		ResultSet result = null;
		String key = source.getKey();

		try {

			Map<String, Object> parameters = new HashMap<>();

			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, key);

			this.addPublishedToQuery(key, true, parameters, subQueries, false);
			this.addConditionToQuery(key, dataRequest, parameters, subQueries, false);
			this.addFiltersToQuery(key, dataRequest, parameters, subQueries, null, null, false);
			this.addAncestorLevelToQuery(key, dataRequest, parameters, subQueries, false);
			this.addLeafToQuery(key, dataRequest, parameters, subQueries, false);
			this.addFromToQuery(key, dataRequest, parameters, subQueries, null);
			this.addTypeToQuery(key, dataRequest, parameters, subQueries, false);

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_SEARCH_COUNT, parameters, subQueries);
			if (!result.next())
				throw new Exception("Can't get total count of source store terms");
			termList.setTotalCount(result.getInt("counter"));

			this.agentDatabase.closeQuery(result);

			String mode = dataRequest.getParameter(DataRequest.MODE);
			int limit = dataRequest.getLimit();
			if (mode != null && mode.equals(DataRequest.Mode.TREE)) limit = 0;

			parameters.put(START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(LIMIT, (limit > 0) ? limit : termList.getTotalCount());

			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_SEARCH, parameters, subQueries);
			this.fillTermList(source, termList, mode, result);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.SOURCE_INDEX_LOAD, dataRequest.getCondition(), exception);
		} finally {
			if (result != null) this.agentDatabase.closeQuery(result);
		}

		return termList;
	}

    public String locateTermCode(Source<SourceDefinition> source, String label) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;
		String resultCode = Strings.EMPTY;

		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(SOURCE, source.getKey());

		parameters.put(LABEL, this.agentDatabase.prepareAsFullTextCondition(label));

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_SEARCH_CODE, parameters, subQueries);

			if (result.next()) {
				resultCode = result.getString("code");
			}
		} catch (SQLException exception) {
			throw new DataException(ErrorCode.SOURCE_INDEX_SEARCH_CODE, label, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return resultCode;
	}

	public String locateTermLabel(Source<SourceDefinition> source, String code) {
		Map<String, Object> parameters = new HashMap<>();
		ResultSet result = null;
		String resultLabel = Strings.EMPTY;

		Map<String, String> subQueries = new HashMap<>();
		subQueries.put(SOURCE, source.getKey());

		parameters.put(CODE, "%" + code);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_SEARCH_LABEL, parameters, subQueries);

			if (result.next()) {
				resultLabel = result.getString("label");
			}
		} catch (SQLException exception) {
			throw new DataException(ErrorCode.SOURCE_INDEX_SEARCH_LABEL, code, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return resultLabel;
	}

	public void addTerm(Source<SourceDefinition> source, Term term, String parentCode) {
		SourceDefinition sourceDefinition = source.getDefinition();
		if (sourceDefinition instanceof ThesaurusDefinition && ((ThesaurusDefinition)sourceDefinition).isSelfGenerated())
			throw new RuntimeException("Source is self generated");

		DatabaseRepositoryQuery[] queries = new DatabaseRepositoryQuery[2];
		ResultSet result = null;

		try {
			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(SOURCE, source.getKey());

			Map<String, Object> addTermAncestorsParams = new HashMap<>();
			addTermAncestorsParams.put(CODE, term.getCode());
			addTermAncestorsParams.put(CODE_PARENT, parentCode);
			queries[0] = new DatabaseRepositoryQuery(SOURCE_STORE_ADD_TERM_ANCESTORS, addTermAncestorsParams, subQueries);

			Map<String, Object> addTermParams = new HashMap<>();
			addTermParams.put(CODE, term.getCode());
			addTermParams.put(CODE_PARENT, parentCode);
			addTermParams.put(LABEL, term.getLabel());
			addTermParams.put(FLATTEN_LABEL, "_");
			addTermParams.put(TAGS, SerializerData.serializeSet(term.getTags()));
			addTermParams.put(TYPE, term.getType());
			addTermParams.put(IS_ENABLE, term.isEnabled());
			addTermParams.put(IS_NEW, term.isNew());
			addTermParams.put(IS_LEAF, term.getType() == Term.TERM);
			addTermParams.put(UPDATE_DATE, this.agentDatabase.getDateAsMilliseconds(new Date()));
			queries[1] = new DatabaseRepositoryQuery(SOURCE_STORE_ADD_TERM, addTermParams, subQueries);

			this.agentDatabase.executeRepositoryUpdateTransaction(queries);

			subQueries.put(CODE, term.getCode());
			result = this.agentDatabase.executeRepositorySelectQuery(SOURCE_STORE_CALCULATE_TERM_FLATTEN_LABEL, null, subQueries);

			if (!result.next())
				throw new Exception("Can't calculate flatten label for source store term");

			Map<String, Object> flattenTermParams = new HashMap<>();
			flattenTermParams.put(CODE, term.getCode());
			flattenTermParams.put(FLATTEN_LABEL, result.getString("label"));
			subQueries.remove(CODE);

			this.agentDatabase.closeQuery(result);
			this.agentDatabase.executeRepositoryUpdateQuery(SOURCE_STORE_UPDATE_TERM_FLATTEN_LABEL, flattenTermParams, subQueries);

			MonetEvent monetEvent = new MonetEvent(MonetEvent.SOURCE_TERM_ADDED, null, source);
			monetEvent.addParameter(MonetEvent.PARAMETER_TERM, term);
			AgentNotifier.getInstance().notify(monetEvent);

		} catch (Exception exception) {
			this.agentLogger.error(exception);
		} finally {
			if (result != null) this.agentDatabase.closeQuery(result);
		}

	}

	public void clean(Source<SourceDefinition> source) {
		List<DatabaseRepositoryQuery> queries = new ArrayList<>();
		Map<String, String> subQueries = new HashMap<>();

		subQueries.put(SOURCE, source.getKey());
		queries.add(new DatabaseRepositoryQuery(SOURCE_STORE_CLEAN, null, subQueries));

		this.agentDatabase.executeRepositoryQueries(queries.toArray(new DatabaseRepositoryQuery[0]));
	}

	public void populate(Source<SourceDefinition> source) {
		TermList termList = null;

		if (source.isThesaurus())
			termList = ((ThesaurusDefinition) source.getDefinition()).getTermList();
		else if (source.isGlossary())
			termList = this.loadGlossaryTermList(source);

		if (termList == null)
			return;

		this.clean(source);
		this.addTerms(source, termList);

		AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.SOURCE_POPULATED, null, source));
	}

	public boolean synchronize(Source<SourceDefinition> source) {
		TermList termList = null;

		if (source.isGlossary())
			termList = this.loadGlossaryTermList(source);

		if (termList == null)
			return false;

		TermList currentTermList = this.loadTerms(source, false);

		this.clean(source);
		this.synchronizeTerms(source, termList, currentTermList);

		AgentNotifier.getInstance().notify(new MonetEvent(MonetEvent.SOURCE_SYNCHRONIZED, null, source));

		return true;
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

	private void addPublishedToQuery(String codeSource, boolean onlyPublished, Map<String, Object> parameters, Map<String, String> subQueries, boolean isIndexQuery) {

		if (onlyPublished) {
			parameters.put(IS_ENABLE, "1");
			subQueries.put(IS_ENABLE, this.agentDatabase.getRepositoryQuery(SOURCE_STORE_ENABLE));
			parameters.put(IS_NEW, "0");
			subQueries.put(IS_NEW, this.agentDatabase.getRepositoryQuery(SOURCE_STORE_NEW));
		} else {
			subQueries.put(IS_ENABLE, Strings.EMPTY);
			subQueries.put(IS_NEW, Strings.EMPTY);
		}
	}

	private void addConditionToQuery(String codeSource, DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries, boolean isIndexQuery) {
		String condition = this.agentDatabase.prepareAsFullTextCondition(dataRequest.getCondition());
		String queryName = isIndexQuery ? REFERENCE_AS_SOURCE_CONDITION : SOURCE_STORE_CONDITION;

		if (condition != null && !condition.isEmpty()  && !condition.trim().isEmpty()) {
			parameters.put(CONDITION, condition);
			subQueries.put(CONDITION, this.agentDatabase.getRepositoryQuery(queryName));
		} else {
			subQueries.put(CONDITION, Strings.EMPTY);
		}
	}

	private void addFiltersToQuery(String codeSource, DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries, String tableName, String fieldTag, boolean isIndexQuery) {
		String filters = dataRequest.getParameter(DataRequest.FILTERS);
		String queryName = isIndexQuery ? REFERENCE_AS_SOURCE_FILTERS : SOURCE_STORE_FILTERS;

		if (filters != null && !filters.isEmpty()) {
			LinkedHashSet<String> filtersSet = SerializerData.deserializeSet(filters);
			int pos = 0;

			filters = "";
			for (String filter : filtersSet) {
				if (pos > 0) filters += " o ";
				filters += filter;
				pos++;
			}

			QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(queryName));
			if (isIndexQuery) {
				queryBuilder.insertSubQuery(TABLE_NAME, tableName);
				queryBuilder.insertSubQuery(FIELD_TAG, fieldTag);
			}

			parameters.put(FILTERS, filters);
			subQueries.put(FILTERS, queryBuilder.build());
		} else {
			subQueries.put(FILTERS, Strings.EMPTY);
		}
	}

	private void addAncestorLevelToQuery(String key, DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries, boolean isIndexQuery) {
		String flatten = dataRequest.getParameter("flatten");
		String from = dataRequest.getParameter("from");
		String mode = dataRequest.getParameter(DataRequest.MODE);
		String queryName = isIndexQuery ? REFERENCE_AS_SOURCE_ANCESTOR_LEVEL : SOURCE_STORE_ANCESTOR_LEVEL;
		String fromTermQueryName = isIndexQuery ? REFERENCE_AS_SOURCE_ANCESTOR_LEVEL_FROM_TERM : SOURCE_STORE_ANCESTOR_LEVEL_FROM_TERM;

		if (mode != null && (mode.equals(DataRequest.Mode.TREE) || mode.equals(DataRequest.Mode.ALL))) {
			String depth = dataRequest.getParameter("depth");

			if (depth == null || depth.isEmpty() || depth.equals("-1"))
				subQueries.put(ANCESTOR_LEVEL, Strings.EMPTY);
			else {
				if (from != null && !from.isEmpty() && !from.equals("-1")) {
					parameters.put(FROM, from);
					parameters.put(DEPTH, Integer.parseInt(depth));
					QueryBuilder queryBuilder = new QueryBuilder(agentDatabase.getRepositoryQuery(fromTermQueryName));
					queryBuilder.insertSubQuery(SOURCE, key);
					subQueries.put(ANCESTOR_LEVEL, queryBuilder.build());
				} else {
					parameters.put(ANCESTOR_LEVEL, Integer.parseInt(depth));
					subQueries.put(ANCESTOR_LEVEL, agentDatabase.getRepositoryQuery(queryName));
				}
			}

			return;
		}

		if (flatten == null || flatten.isEmpty() || flatten.toLowerCase().equals("none")) {
			String depth = dataRequest.getParameter("depth");

			if (depth == null || depth.isEmpty() || depth.equals("-1")) depth = "1";

			if (from != null && !from.isEmpty() && !from.equals("-1")) {
				parameters.put(FROM, from);
				parameters.put(DEPTH, Integer.parseInt(depth));
				QueryBuilder queryBuilder = new QueryBuilder(agentDatabase.getRepositoryQuery(fromTermQueryName));
				queryBuilder.insertSubQuery(SOURCE, key);
				subQueries.put(ANCESTOR_LEVEL, queryBuilder.build());
			} else {
				parameters.put(ANCESTOR_LEVEL, Integer.parseInt(depth));
				subQueries.put(ANCESTOR_LEVEL, agentDatabase.getRepositoryQuery(queryName));
			}

		} else if (flatten.toLowerCase().equals("level")) {
			String depth = dataRequest.getParameter("depth");

			if (depth == null || depth.isEmpty() || depth.equals("-1")) return;

			if (from != null && !from.isEmpty() && !from.equals("-1")) {
				parameters.put(FROM, from);
				parameters.put(DEPTH, Integer.parseInt(depth));
				QueryBuilder queryBuilder = new QueryBuilder(agentDatabase.getRepositoryQuery(fromTermQueryName));
				queryBuilder.insertSubQuery(SOURCE, key);
				subQueries.put(ANCESTOR_LEVEL, queryBuilder.build());
			} else {
				parameters.put(ANCESTOR_LEVEL, Integer.parseInt(depth));
				subQueries.put(ANCESTOR_LEVEL, agentDatabase.getRepositoryQuery(queryName));
			}

		} else {
			subQueries.put(ANCESTOR_LEVEL, Strings.EMPTY);
		}
	}

	private void addLeafToQuery(String codeSource, DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries, boolean isIndexQuery) {
		String flatten = dataRequest.getParameter("flatten");

		if (flatten == null || flatten.isEmpty() || flatten.toLowerCase().equals("none")) {
			subQueries.put(IS_LEAF, Strings.EMPTY);
			return;
		}

		flatten = flatten.toLowerCase();

		if (flatten.equals("leaf")) {
			parameters.put(IS_LEAF, 1);
			subQueries.put(IS_LEAF, this.agentDatabase.getRepositoryQuery(SOURCE_STORE_LEAF));
		} else if (flatten.equals("internal")) {
			parameters.put(IS_LEAF, 0);
			subQueries.put(IS_LEAF, this.agentDatabase.getRepositoryQuery(SOURCE_STORE_LEAF));
		} else subQueries.put(IS_LEAF, Strings.EMPTY);
	}

	private void addFromToQuery(String key, DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries, AttributeProperty keyAttributeDefinition) {
		String from = dataRequest.getParameter(DataRequest.FROM);
		String queryName = keyAttributeDefinition != null ? REFERENCE_AS_SOURCE_FROM_TERM : SOURCE_STORE_FROM_TERM;

		if (from == null || from.isEmpty() || from.equals("-1")) {
			subQueries.put(FROM, Strings.EMPTY);
			return;
		}

		parameters.put(FROM, from);
		QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(queryName));

		if (keyAttributeDefinition == null)
			queryBuilder.insertSubQuery(SOURCE, key);
		else {
			queryBuilder.insertSubQuery(TABLE_NAME, subQueries.get(TABLE_NAME));
			queryBuilder.insertSubQuery(FIELD_KEY, keyAttributeDefinition.getCode());
		}

		subQueries.put(FROM, queryBuilder.build());
	}

	private void addTypeToQuery(String codeSource, DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries, boolean isIndexQuery) {
		String mode = dataRequest.getParameter(DataRequest.MODE);

		if (mode != null && (mode.equals(DataRequest.Mode.TREE) || mode.equals(DataRequest.Mode.ALL))) {
			subQueries.put(TERMS_ONLY, Strings.EMPTY);
			return;
		}

		QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(SOURCE_STORE_TERMS_ONLY));
		subQueries.put(TERMS_ONLY, queryBuilder.build());
	}

	private void createTermHierarchy(Term term, Map<String, Set<Term>> termsChildren) throws SQLException {
		if (!termsChildren.containsKey(term.getCode())) return;

		Set<Term> children = termsChildren.get(term.getCode());
		for (Term child : children) {
			this.createTermHierarchy(child, termsChildren);
			term.getTermList().add(child);
		}
	}

	private void fillTerm(Source<SourceDefinition> source, Term term, ResultSet result) throws SQLException {
		term.setSourceId(source.getId());
		term.setSourceCode(source.getCode());
		term.addAttribute(Term.CODE, result.getString("code"));
		term.addAttribute(Term.LABEL, result.getString("label"));
		term.addAttribute(Term.FLATTEN_LABEL, result.getString("flatten_label"));
		term.setTags(SerializerData.<LinkedHashSet<String>>deserializeSet(result.getString("tags")));
		term.setParent(result.getString("code_parent"));
		term.setType(result.getInt("type"));
		term.setEnabled(result.getBoolean("is_enable"));
		term.setNew(result.getBoolean("is_new"));
		term.setLeaf(result.getBoolean("is_leaf"));
		term.setAncestorLevel(result.getInt("ancestor_level"));
	}

	private void fillTermList(Source<SourceDefinition> source, TermList list, String mode, ResultSet result) throws SQLException {
		Map<String, Set<Term>> termsChildren = new HashMap<>();

		termsChildren.put("-1", new LinkedHashSet<Term>());

		while (result.next()) {
			Term term = new Term();

			this.fillTerm(source, term, result);

			if (mode != null && mode.equals(DataRequest.Mode.TREE)) {
				String codeParent = term.getParent();

				if (term.isCategory() || term.isSuperTerm())
					termsChildren.put(term.getCode(), new LinkedHashSet<Term>());

				if (codeParent == null || !termsChildren.containsKey(codeParent))
					termsChildren.get("-1").add(term);
				else
					termsChildren.get(codeParent).add(term);

			} else if (mode != null && mode.equals(DataRequest.Mode.ALL))
				list.add(term);
			else if (!term.isCategory())
				list.add(term);
		}

		if (mode != null && mode.equals(DataRequest.Mode.TREE)) {
			for (Term child : termsChildren.get("-1")) {
				this.createTermHierarchy(child, termsChildren);
				list.add(child);
			}
		}

	}

    private TermList loadSelfGeneratedTerms(Source<SourceDefinition> source, DataRequest dataRequest, boolean onlyPublished) {
        ThesaurusDefinition definition = (ThesaurusDefinition) source.getDefinition();
        SelfGeneratedProperty selfGeneratedDefinition = definition.getSelfGenerated();

        if (selfGeneratedDefinition.getFromIndex() != null)
            return this.loadTermsFromIndex(source, dataRequest, onlyPublished);
        if (selfGeneratedDefinition.getFromRoles() != null)
            return this.loadTermsFromRoles(source, dataRequest, onlyPublished);

        return null;
    }

	private TermList loadTermsFromIndex(Source<SourceDefinition> source, DataRequest dataRequest, boolean onlyPublished) {
		ThesaurusDefinition definition = (ThesaurusDefinition) source.getDefinition();
		SelfGeneratedProperty selfGeneratedDefinition = definition.getSelfGenerated();
        SelfGeneratedProperty.FromIndexProperty fromIndexDefinition = selfGeneratedDefinition.getFromIndex();
		SelfGeneratedProperty.FromIndexProperty.MappingProperty mappingDefinition = fromIndexDefinition.getMapping();
		IndexDefinition indexDefinition = Dictionary.getInstance().getIndexDefinition(fromIndexDefinition.getIndex().getValue());
		AttributeProperty keyAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getKey().getValue());
		AttributeProperty labelAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getLabel().getValue());
		AttributeProperty tagAttributeDefinition = null;

        if (mappingDefinition.getTag() != null)
			tagAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getTag().getValue());

        String codeParent = dataRequest.getParameter(DataRequest.FROM);
		int startPos = dataRequest.getStartPos();
		int limit = dataRequest.getLimit();
		ResultSet result = null;
		TermList terms = new TermList();
		String key = source.getKey();
		String tableName = indexDefinition.getCode();
		String fieldTag = tagAttributeDefinition != null ? tagAttributeDefinition.getCode() : keyAttributeDefinition.getCode();

		if (codeParent != null && codeParent.isEmpty())
			codeParent = null;

		try {
			Map<String, Object> parameters = new HashMap<>();

			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(TABLE_NAME, tableName);

			this.addConditionToQuery(key, dataRequest, parameters, subQueries, true);
			this.addFiltersToQuery(key, dataRequest, parameters, subQueries, tableName, fieldTag, true);
			this.addAncestorLevelToQuery(key, dataRequest, parameters, subQueries, true);
			this.addFromToQuery(key, dataRequest, parameters, subQueries, tagAttributeDefinition != null ? tagAttributeDefinition : keyAttributeDefinition);

			result = this.agentDatabase.executeRepositorySelectQuery(REFERENCE_AS_SOURCE_COUNT, parameters, subQueries);
			result.next();
			terms.setTotalCount(result.getInt("counter"));

			this.agentDatabase.closeQuery(result);

			parameters.put(START_POS, this.agentDatabase.getQueryStartPos(startPos));
			parameters.put(LIMIT, limit != -1 ? limit : terms.getTotalCount());

			subQueries.put(FIELD_KEY, keyAttributeDefinition.getCode());
			subQueries.put(FIELD_LABEL, labelAttributeDefinition.getCode());
			subQueries.put(FIELD_TAG, fieldTag);

			result = this.agentDatabase.executeRepositorySelectQuery(REFERENCE_AS_SOURCE, parameters, subQueries);

			while (result.next()) {
				String label = result.getString(LABEL);
				String code = result.getString(CODE);
				String tags = result.getString(TAGS);
				boolean isLeaf = result.getInt("childs_count") > 0;

				Term term = new Term(code, label);
				term.setSourceId(source.getId());
				term.setSourceCode(source.getCode());
				term.setLeaf(isLeaf);

				term.setTags(SerializerData.<LinkedHashSet<String>>deserializeSet(tags));

				terms.add(term);
			}
		} catch (Exception e) {
			throw new DataException(ErrorCode.LOAD_GLOSSARY, String.format("load(%s,%s)", codeParent, keyAttributeDefinition.getCode()), e);
		} finally {
			this.agentDatabase.closeQuery(result);
		}
		return terms;
	}

    private TermList loadTermsFromRoles(Source<SourceDefinition> source, DataRequest dataRequest, boolean onlyPublished) {
        ThesaurusDefinition definition = (ThesaurusDefinition) source.getDefinition();
        SelfGeneratedProperty selfGeneratedDefinition = definition.getSelfGenerated();
        SelfGeneratedProperty.FromRolesProperty fromRolesDefinition = selfGeneratedDefinition.getFromRoles();
        ProducerRoleList producerRoleList = producersFactory.get(Producers.ROLELIST);
        Set<String> roleSet = new HashSet<>();
        TermList terms = new TermList();

        DataRequest roleDataRequest = new DataRequest();
        roleDataRequest.setStartPos(0);
        roleDataRequest.setLimit(-1);
        roleDataRequest.addParameter(DataRequest.NATURE, Role.Nature.Both.toString());
        roleDataRequest.addParameter(DataRequest.NON_EXPIRED, "true");
        roleDataRequest.setCondition(dataRequest.getCondition());

        for (Ref roleRef : fromRolesDefinition.getRole()) {
            String roleCode = this.getDictionary().getDefinitionCode(roleRef.getValue());
            org.monet.space.kernel.model.RoleList monetRoleList = producerRoleList.load(roleCode, roleDataRequest);

            for (org.monet.space.kernel.model.Role role : monetRoleList) {
                String label = role.getLabel();
                String code = role.getId();

                if (roleSet.contains(code))
                    continue;

                Term term = new Term(code, label);
                term.setSourceId(source.getId());
                term.setSourceCode(source.getCode());
                term.setLeaf(true);
                term.setTags(new LinkedHashSet<String>());

                terms.add(term);
                roleSet.add(code);
            }
        }

	    terms.setTotalCount(terms.getCount());

        return terms;
    }

    private List<Term> loadSelfGeneratedAncestorsTerms(Source<SourceDefinition> source, Term term) {
        ThesaurusDefinition definition = (ThesaurusDefinition) source.getDefinition();
        SelfGeneratedProperty selfGeneratedDefinition = definition.getSelfGenerated();

        if (selfGeneratedDefinition.getFromIndex() != null)
            return this.loadAncestorsTermsFromIndex(source, term);
        if (selfGeneratedDefinition.getFromRoles() != null)
            return this.loadAncestorsTermsFromRoles(source, term);

        return null;
    }

	private List<Term> loadAncestorsTermsFromIndex(Source<SourceDefinition> source, Term term) {
		Map<String, Object> parameters;
		Map<String, String> subQueries;
		List<Term> ancestors = null;
		ThesaurusDefinition definition = (ThesaurusDefinition) source.getDefinition();
		SelfGeneratedProperty selfGeneratedDefinition = definition.getSelfGenerated();
        SelfGeneratedProperty.FromIndexProperty fromIndexDefinition = selfGeneratedDefinition.getFromIndex();
		IndexDefinition indexDefinition = Dictionary.getInstance().getIndexDefinition(fromIndexDefinition.getIndex().getValue());
		SelfGeneratedProperty.FromIndexProperty.MappingProperty mappingDefinition = fromIndexDefinition.getMapping();
		AttributeProperty keyAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getKey().getValue());
		AttributeProperty labelAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getLabel().getValue());
		AttributeProperty tagAttributeDefinition = null;
		ResultSet result = null;

		try {

			if (mappingDefinition.getTag() != null)
				tagAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getTag().getValue());

			ancestors = new ArrayList<>();
			parameters = new HashMap<>();
			parameters.put(ID_NODE, term.getCode());

			subQueries = new HashMap<>();
			subQueries.put(TABLE_NAME, indexDefinition.getCode());
			subQueries.put(FIELD_KEY, keyAttributeDefinition.getCode());
			subQueries.put(FIELD_LABEL, labelAttributeDefinition.getCode());
			subQueries.put(FIELD_TAG, tagAttributeDefinition != null ? tagAttributeDefinition.getCode() : keyAttributeDefinition.getCode());

			result = this.agentDatabase.executeRepositorySelectQuery(REFERENCE_AS_SOURCE_ANCESTORS, parameters, subQueries);

			while (result.next()) {
                String label = result.getString(LABEL);
                String code = result.getString(CODE);
                String tags = result.getString(TAGS);

                Term ancestorTerm = new Term(code, label);
                term.setSourceId(source.getId());
                term.setSourceCode(source.getCode());

                term.setTags(SerializerData.<LinkedHashSet<String>>deserializeSet(tags));

				ancestors.add(ancestorTerm);
			}

		} catch (DatabaseException exception) {
			AgentLogger.getInstance().error(exception);
			return ancestors;
		} catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
			return ancestors;
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return ancestors;
	}

    private List<Term> loadAncestorsTermsFromRoles(Source<SourceDefinition> source, Term term) {
        return new ArrayList<>();
    }

    private TermList searchSelfGeneratedTerms(Source<SourceDefinition> source, DataRequest dataRequest) {
        ThesaurusDefinition definition = (ThesaurusDefinition) source.getDefinition();
        SelfGeneratedProperty selfGeneratedDefinition = definition.getSelfGenerated();

        if (selfGeneratedDefinition.getFromIndex() != null)
            return this.searchTermsFromIndex(source, dataRequest);
        if (selfGeneratedDefinition.getFromRoles() != null)
            return this.searchTermsFromRoles(source, dataRequest);

        return null;
    }

	private TermList searchTermsFromIndex(Source<SourceDefinition> source, DataRequest dataRequest) {
		ThesaurusDefinition definition = (ThesaurusDefinition) source.getDefinition();
		SelfGeneratedProperty selfGeneratedDefinition = definition.getSelfGenerated();
		SelfGeneratedProperty.FromIndexProperty fromIndexDefinition = selfGeneratedDefinition.getFromIndex();
		SelfGeneratedProperty.FromIndexProperty.MappingProperty mappingDefinition = fromIndexDefinition.getMapping();
		IndexDefinition indexDefinition = Dictionary.getInstance().getIndexDefinition(fromIndexDefinition.getIndex().getValue());
		AttributeProperty keyAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getKey().getValue());
		AttributeProperty labelAttributeDefinition = indexDefinition.getAttribute(mappingDefinition.getLabel().getValue());
		AttributeProperty tagAttributeDefinition = mappingDefinition.getTag() != null ? indexDefinition.getAttribute(mappingDefinition.getTag().getValue()) : keyAttributeDefinition;
		String condition = dataRequest.getCondition();
		int startPos = dataRequest.getStartPos();
		int limit = dataRequest.getLimit();
		ResultSet result = null;
		TermList terms = new TermList();

		condition = this.agentDatabase.prepareAsFullTextCondition(condition);

		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put(CONDITION, condition);

			Map<String, String> subQueries = new HashMap<>();
			subQueries.put(TABLE_NAME, indexDefinition.getCode());

			result = this.agentDatabase.executeRepositorySelectQuery(REFERENCE_AS_SOURCE_SEARCH_COUNT, parameters, subQueries);
			result.next();
			terms.setTotalCount(result.getInt("counter"));

			this.agentDatabase.closeQuery(result);

			if (terms.getTotalCount() > 0) {
				parameters.put(START_POS, this.agentDatabase.getQueryStartPos(startPos));
				parameters.put(LIMIT, limit);

				subQueries.put(FIELD_KEY, keyAttributeDefinition.getCode());
				subQueries.put(FIELD_LABEL, labelAttributeDefinition.getCode());
				subQueries.put(FIELD_TAG, tagAttributeDefinition.getCode());
				result = this.agentDatabase.executeRepositorySelectQuery(REFERENCE_AS_SOURCE_SEARCH, parameters, subQueries);

				while (result.next()) {
					String label = result.getString(LABEL);
					String code = result.getString(CODE);
					String tags = result.getString(TAGS);

					Term term = new Term(code, label);
					term.setSourceId(source.getId());
					term.setSourceCode(source.getCode());
					term.setTags(SerializerData.<LinkedHashSet<String>>deserializeSet(tags));

					terms.add(term);
				}
			}

		} catch (Exception e) {
			throw new DataException(ErrorCode.SEARCH_REFERENCE, String.format("loadReferenceAsSource(%s,%s)", condition, keyAttributeDefinition.getCode()), e);
		} finally {
			if (result != null)
				this.agentDatabase.closeQuery(result);
		}
		return terms;
	}

    private TermList searchTermsFromRoles(Source<SourceDefinition> source, DataRequest dataRequest) {
        return this.loadTermsFromRoles(source, dataRequest, true);
    }

	private void addTerms(Source<SourceDefinition> source, TermList termList) {
		for (Term term : termList)
			this.addTermAndChildren(source, term);
	}

	private void addTermAndChildren(Source<SourceDefinition> source, Term term) {
		this.addTerm(source, term, term.getParent());

		for (Term childTerm : term.getTermList())
			addTermAndChildren(source, childTerm);
	}

	private void synchronizeTerms(Source<SourceDefinition> source, TermList termList, TermList currentTermList) {
		for (Term term : termList)
			synchronizeTerm(source, term, currentTermList);
	}

	private void synchronizeTerm(Source<SourceDefinition> source, Term term, TermList currentTermList) {
		Term currentTerm = currentTermList.get(term.getCode());

		term.setEnabled(true);

		if (currentTerm != null) {
			term.setTags(currentTerm.getTags());
			term.setEnabled(currentTerm.isEnabled());
		}

		this.addTerm(source, term, term.getParent());

		for (Term childTerm : term.getTermList())
			this.synchronizeTerm(source, childTerm, currentTermList);
	}

}