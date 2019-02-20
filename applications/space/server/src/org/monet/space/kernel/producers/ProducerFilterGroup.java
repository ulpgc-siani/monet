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
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.FilterGroup;
import org.monet.space.kernel.model.FilterList;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerFilterGroup extends Producer {

	public ProducerFilterGroup() {
		super();
	}

	public FilterGroup fill(ResultSet resultSet) throws Exception {
		FilterGroup filterGroup = new FilterGroup();
		Timestamp createDate = (Timestamp) this.agentDatabase.getDateAsTimestamp(resultSet.getTimestamp("create_date"));

		FilterList filterList = new FilterList();
		filterList.fromJson(resultSet.getString("data"));
		filterList.setTotalCount(filterList.getAll().size());

		filterGroup.setId(resultSet.getString("id"));
		filterGroup.setCubeId(resultSet.getString("id_cube"));
		filterGroup.setUserId(resultSet.getString("id_user"));
		filterGroup.setLabel(resultSet.getString("label"));
		filterGroup.setFilterList(filterList);
		filterGroup.setCreateDate(createDate);

		return filterGroup;
	}

	public FilterGroup load(String id) {
		ResultSet result = null;
		FilterGroup filterGroup = null;

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, id);


		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.CUBE_FILTERGROUP_LOAD, parameters);

			if (result.next()) {
				filterGroup = this.fill(result);
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_FILTERGROUP, id, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return filterGroup;
	}

	public void save(FilterGroup filterGroup) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, filterGroup.getId());
		parameters.put(Database.QueryFields.LABEL, filterGroup.getLabel());
		parameters.put(Database.QueryFields.DATA, filterGroup.getFilterList().toJson().toJSONString());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.CUBE_FILTERGROUP_SAVE, parameters);
	}

	public FilterGroup create(String userId, String cubeId, String label, FilterList filterList) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Timestamp date = (Timestamp) this.agentDatabase.getDateAsTimestamp(new Date());
		String id;

		FilterGroup filterGroup = new FilterGroup();
		filterGroup.setCubeId(cubeId);
		filterGroup.setUserId(userId);
		filterGroup.setLabel(label);
		filterGroup.setFilterList(filterList);
		filterGroup.setCreateDate(date);

		parameters.put(Database.QueryFields.ID_CUBE, filterGroup.getCubeId());
		parameters.put(Database.QueryFields.ID_USER, filterGroup.getUserId());
		parameters.put(Database.QueryFields.LABEL, filterGroup.getLabel());
		parameters.put(Database.QueryFields.DATA, filterGroup.getFilterList().toJson().toJSONString());
		parameters.put(Database.QueryFields.CREATE_DATE, filterGroup.getCreateDate());

		id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.CUBE_FILTERGROUP_CREATE, parameters);
		filterGroup.setId(id);

		return filterGroup;
	}

	public void remove(String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, id);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.CUBE_FILTERGROUP_REMOVE, parameters);
	}

	public Object newObject() {
		return new FilterGroup();
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}