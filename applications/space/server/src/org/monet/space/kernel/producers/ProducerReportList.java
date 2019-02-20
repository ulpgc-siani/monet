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

import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.DataRequest.SortBy;
import org.monet.space.kernel.model.Report;
import org.monet.space.kernel.model.ReportList;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

public class ProducerReportList extends ProducerList {

	public ProducerReportList() {
		super();
	}

	private void addSortsByToQuery(DataRequest dataRequest, HashMap<String, String> queryParams) {
		String queryParameters = new String();
		List<SortBy> sortsBy = dataRequest.getSortsBy();

		for (SortBy sortBy : sortsBy)
			queryParameters += sortBy.attribute() + Strings.SPACE + sortBy.mode() + ",";

		if (queryParameters.length() > 0)
			queryParameters = queryParameters.substring(0, queryParameters.length() - 1);
		else
			queryParameters = "label" + Strings.SPACE + this.agentDatabase.getOrderMode(Common.OrderMode.ASCENDANT);

		queryParams.put(Database.QueryFields.SORTS_BY, queryParameters);
	}

	private LinkedHashMap<String, Report> fillQueryResultToModel(String idCube, ResultSet resultSet, DataRequest dataRequest) throws Exception {
		LinkedHashMap<String, Report> resultMap = new LinkedHashMap<String, Report>();
		ProducerReport producerReport = (ProducerReport) this.producersFactory.get(Producers.REPORT);

		while (resultSet.next()) {
			Report report = new Report();
			Timestamp createDate = (Timestamp) this.agentDatabase.getDateAsTimestamp(resultSet.getTimestamp("create_date"));
			Timestamp updateDate = (Timestamp) this.agentDatabase.getDateAsTimestamp(resultSet.getTimestamp("update_date"));

			report.setId(resultSet.getString("id"));
			report.setIdCube(idCube);
			report.setLabel(resultSet.getString("label"));
			report.setDescription(resultSet.getString("description"));
			report.setCreateDate(createDate);
			report.setUpdateDate(updateDate);
			report.setIsValid(resultSet.getInt("is_valid") == 1);
			report.linkLoadListener(producerReport);

			resultMap.put(report.getId(), report);
		}

		return resultMap;
	}

	public LinkedHashMap<String, Report> loadItems(String idCube, DataRequest dataRequest) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		String condition;
		LinkedHashMap<String, Report> resultMap;

		this.addSortsByToQuery(dataRequest, queryParams);
		condition = this.agentDatabase.prepareAsFullTextCondition(dataRequest.getCondition());

		parameters.put(Database.QueryFields.ID_CUBE, idCube);
		parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
		parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());
		parameters.put(Database.QueryFields.CONDITION, "%" + condition);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.CUBE_REPORT_LIST_LOAD_ITEMS, parameters, queryParams);
			resultMap = this.fillQueryResultToModel(idCube, resultSet, dataRequest);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_REPORTLIST, dataRequest.getCode(), exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return resultMap;
	}

	public Integer loadItemsCount(String idCube, DataRequest dataRequest) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		Integer count = 0;
		String condition;

		condition = this.agentDatabase.prepareAsFullTextCondition(dataRequest.getCondition());

		parameters.put(Database.QueryFields.ID_CUBE, idCube);
		parameters.put(Database.QueryFields.CONDITION, "%" + condition);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.CUBE_REPORT_LIST_LOAD_ITEMS_COUNT, parameters, queryParams);

			resultSet.next();
			count = resultSet.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_REPORTLIST, idCube, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return count;
	}

	public Object newObject() {
		ReportList reportList;
		reportList = new ReportList();
		return reportList;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}
