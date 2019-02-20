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
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

public class ProducerSourceList extends ProducerList {

	public ProducerSourceList() {
		super();
	}

	private SourceList loadItems(String query, HashMap<String, Object> parameters) {
		MonetResultSet result = null;
		SourceList sourceList;
		ProducerSource producerSource = this.producersFactory.get(Producers.SOURCE);


		try {
			sourceList = new SourceList();
			result = this.agentDatabase.executeRepositorySelectQuery(query, parameters);

			while (result.next()) {
				Source<SourceDefinition> source = Source.createInstance(result.getString("code"), SourceProvider.getInstance());
				producerSource.fill(source, result);
				sourceList.add(source);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_SOURCELIST, "", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return sourceList;
	}

	public SourceList load() {
		return this.loadItems(Database.Queries.SOURCE_LIST_LOAD, null);
	}

	public SourceList load(String code) {

		if (code == null)
			return this.load();

		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.CODE, code);
		return this.loadItems(Database.Queries.SOURCE_LIST_LOAD_WITH_CODE, parameters);
	}

	public SourceList load(String code, String partner) {

		if (partner == null)
			return this.load(code);

		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put(Database.QueryFields.CODE, code);
		parameters.put(Database.QueryFields.PARTNER_NAME, partner);
		return this.loadItems(Database.Queries.SOURCE_LIST_LOAD_WITH_CODE_AND_PARTNER, parameters);
	}

	public SourceList loadGlossaries() {
		MonetResultSet result;
		SourceList sourceList;
		ProducerSource producerSource = this.producersFactory.get(Producers.SOURCE);

		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_LIST_LOAD_GLOSSARIES);

		try {
			sourceList = new SourceList();

			while (result.next()) {
				Source<SourceDefinition> source = Source.createInstance(result.getString("code"), SourceProvider.getInstance());
				producerSource.fill(source, result);
				sourceList.add(source);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_SOURCELIST, "", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return sourceList;
	}

	public int loadCount() {
		ResultSet result = null;
		int count = 0;

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_LIST_LOAD_COUNT);

			result.next();
			count = result.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_SOURCELIST_COUNT, "load count", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return count;
	}

	public List<String> loadCodes() {
		ResultSet result;
		List<String> sourceCodes;

		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_LIST_LOAD);

		try {
			sourceCodes = new ArrayList<>();

			while (result.next())
				sourceCodes.add(result.getString("code"));

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_SOURCELIST, "load codes", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return sourceCodes;
	}

	public HashMap<String, FederationUnit> loadPartners(List<String> ontologies) {
		HashMap<String, FederationUnit> partnersMap = new HashMap<>();
		HashMap<String, String> subQueries = new HashMap<>();
		ResultSet result;

		if (ontologies != null && ontologies.size() > 0) {
			QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.SOURCE_LIST_LOAD_PARTNERS_SUBQUERY_ONTOLOGIES));
			queryBuilder.insertSubQuery(Database.QueryFields.ONTOLOGIES, "'" + LibraryArray.implode(ontologies.toArray(new String[ontologies.size()]), "','") + "'");
			subQueries.put(Database.QueryFields.ONTOLOGIES_SUBQUERY, queryBuilder.build());
		} else
			subQueries.put(Database.QueryFields.ONTOLOGIES_SUBQUERY, "");

		result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SOURCE_LIST_LOAD_PARTNERS, null, subQueries);

		try {

			while (result.next()) {
				String partnerName = result.getString("partner_name");
				String partnerLabel = result.getString("partner_label");
				FederationUnit partner;

				if (!partnersMap.containsKey(partnerName)) {
					partner = new FederationUnit();
					partner.setName(partnerName);
					partner.setLabel(partnerLabel);
					partnersMap.put(partnerName, partner);
				} else
					partner = partnersMap.get(partnerName);

				partner.getOntologies().add(result.getString("ontology"));
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_SOURCELIST, "load codes", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return partnersMap;
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}
