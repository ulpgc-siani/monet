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

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Master;
import org.monet.space.kernel.model.ReportList;

import java.sql.ResultSet;
import java.util.EventObject;
import java.util.LinkedHashMap;

public class ProducerMasterList extends ProducerList {

	public ProducerMasterList() {
		super();
	}

	private LinkedHashMap<String, Master> fillQueryResultToModel(ResultSet resultSet) throws Exception {
		ProducerMaster producerMaster = (ProducerMaster) this.producersFactory.get(Producers.MASTER);
		LinkedHashMap<String, Master> resultMap = new LinkedHashMap<String, Master>();

		while (resultSet.next()) {
			Master master = new Master();
			producerMaster.fill(master, resultSet);
			resultMap.put(master.getId(), master);
		}

		return resultMap;
	}

	public LinkedHashMap<String, Master> load() {
		ResultSet resultSet = null;
		LinkedHashMap<String, Master> resultMap;

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MASTER_LIST_LOAD);
			resultMap = this.fillQueryResultToModel(resultSet);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_MASTERLIST, null, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return resultMap;
	}

	public int loadCount() {
		ResultSet resultSet = null;
		int count = 0;

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.MASTER_LIST_LOAD_COUNT);

			resultSet.next();
			count = resultSet.getInt("counter");

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_MASTERLIST, null, exception);
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
