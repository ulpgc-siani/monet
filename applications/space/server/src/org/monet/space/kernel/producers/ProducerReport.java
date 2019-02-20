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
import org.monet.space.kernel.model.Report;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerReport extends Producer {

	public ProducerReport() {
		super();
	}

	public Report load(String idCube, String id) {
		ResultSet result = null;
		Report report = null;

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, id);

		try {
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.CUBE_REPORT_LOAD, parameters);

			if (result.next()) {
				Timestamp createDate = (Timestamp) this.agentDatabase.getDateAsTimestamp(result.getTimestamp("create_date"));
				Timestamp updateDate = (Timestamp) this.agentDatabase.getDateAsTimestamp(result.getTimestamp("update_date"));

				report = new Report();
				report.setId(id);
				report.setIdCube(idCube);
				report.setLabel(result.getString("label"));
				report.setDescription(result.getString("description"));
				report.setData(result.getString("data"));
				report.setCreateDate(createDate);
				report.setUpdateDate(updateDate);
				report.setIsValid(result.getInt("is_valid") == 1);
				report.linkLoadListener(this);
			}
		} catch (Exception oException) {
			throw new DataException(ErrorCode.LOAD_REPORT, id, oException);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return report;
	}

	public void save(String idCube, Report report) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(Database.QueryFields.ID, report.getId());
		parameters.put(Database.QueryFields.LABEL, report.getLabel());
		parameters.put(Database.QueryFields.DESCRIPTION, report.getDescription());
		parameters.put(Database.QueryFields.DATA, report.getData());
		parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(report.getUpdateDate()));
		parameters.put(Database.QueryFields.IS_VALID, report.isValid() ? 1 : 0);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.CUBE_REPORT_SAVE, parameters);
	}

	public Report create(String idCube, String label, String description, String data) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Timestamp date = (Timestamp) this.agentDatabase.getDateAsTimestamp(new Date());
		String id;

		Report report = new Report();
		report.setIdCube(idCube);
		report.setLabel(label);
		report.setDescription(description);
		report.setData(data);
		report.setCreateDate(date);
		report.setUpdateDate(date);
		report.setIsValid(false);

		parameters.put(Database.QueryFields.ID_CUBE, report.getIdCube());
		parameters.put(Database.QueryFields.LABEL, report.getLabel());
		parameters.put(Database.QueryFields.DESCRIPTION, report.getDescription());
		parameters.put(Database.QueryFields.DATA, report.getData());
		parameters.put(Database.QueryFields.CREATE_DATE, report.getInternalCreateDate());
		parameters.put(Database.QueryFields.UPDATE_DATE, report.getUpdateDate());
		parameters.put(Database.QueryFields.IS_VALID, report.isValid() ? 1 : 0);

		id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.CUBE_REPORT_CREATE, parameters);
		report.setId(id);

		return report;
	}

	public void remove(String idCube, String id) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, id);
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.CUBE_REPORT_REMOVE, parameters);
	}

	public Object newObject() {
		return new Report();
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}