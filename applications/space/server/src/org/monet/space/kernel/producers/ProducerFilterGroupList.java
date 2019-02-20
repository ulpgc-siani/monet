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
import org.monet.space.kernel.model.FilterGroup;
import org.monet.space.kernel.model.FilterGroupList;
import org.monet.space.kernel.model.FilterList;

import java.sql.ResultSet;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProducerFilterGroupList extends ProducerList {

	public ProducerFilterGroupList() {
		super();
	}

	private FilterGroupList fill(ResultSet resultSet) throws Exception {
		FilterGroupList result = new FilterGroupList();
		ProducerFilterGroup producerFilterGroup = (ProducerFilterGroup) this.producersFactory.get(Producers.FILTERGROUP);

		while (resultSet.next()) {
			FilterGroup filterGroup = producerFilterGroup.fill(resultSet);
			filterGroup.setFilterList(new FilterList());
			result.add(filterGroup);
		}
		result.setTotalCount(result.getAll().size());

		return result;
	}

	public FilterGroupList load(String userId, String cubeId) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		FilterGroupList result;

		parameters.put(Database.QueryFields.ID_USER, userId);
		parameters.put(Database.QueryFields.ID_CUBE, cubeId);

		try {
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.CUBE_FILTERGROUP_LIST_LOAD, parameters, queryParams);
			result = this.fill(resultSet);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_FILTERGROUPLIST, userId, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return result;
	}

	public Object newObject() {
		FilterGroupList filterGroupList;
		filterGroupList = new FilterGroupList();
		return filterGroupList;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}
