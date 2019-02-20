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
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.TaskOrder;
import org.monet.space.kernel.model.TaskOrderList;
import org.monet.space.kernel.model.TaskOrderProvider;

import java.sql.ResultSet;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProducerTaskOrderList extends ProducerList {
	private ProducerTaskOrder producerTaskOrder;

	public ProducerTaskOrderList() {
		super();
		this.producerTaskOrder = (ProducerTaskOrder) this.producersFactory.get(Producers.TASKORDER);
	}

	private LinkedHashMap<String, TaskOrder> fillQueryResultToModel(ResultSet resultSet, DataRequest dataRequest) throws Exception {
		LinkedHashMap<String, TaskOrder> resultMap = new LinkedHashMap<String, TaskOrder>();

		while (resultSet.next()) {
			TaskOrder taskOrder = new TaskOrder();
			this.producerTaskOrder.fillProperties(taskOrder, resultSet);
			taskOrder.linkLoadListener(this.producerTaskOrder);
			resultMap.put(taskOrder.getId(), taskOrder);
		}

		return resultMap;
	}

	public LinkedHashMap<String, TaskOrder> loadItems(String idTask, DataRequest dataRequest) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		LinkedHashMap<String, TaskOrder> resultMap;

		try {
			parameters.put(Database.QueryFields.ID_TASK, idTask);
			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(Database.QueryFields.LIMIT, dataRequest.getLimit());

			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ORDER_LIST_LOAD_ITEMS, parameters, queryParams);
			resultMap = this.fillQueryResultToModel(resultSet, dataRequest);
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASKORDERLIST, dataRequest.getCode(), exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return resultMap;
	}

	public Integer loadItemsCount(String idTask) {
		ResultSet resultSet = null;
		LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
		HashMap<String, String> queryParams = new HashMap<String, String>();
		Integer count = 0;

		try {
			parameters.put(Database.QueryFields.ID_TASK, idTask);
			resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ORDER_LIST_LOAD_ITEMS_COUNT, parameters, queryParams);

			resultSet.next();
			count = resultSet.getInt("counter");
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_TASKORDERLIST, idTask, exception);
		} finally {
			this.agentDatabase.closeQuery(resultSet);
		}

		return count;
	}

	public Object newObject() {
		TaskOrderList taskOrderList = new TaskOrderList();
		taskOrderList.linkLoadListener(this);
		taskOrderList.setTaskOrderLink(TaskOrderProvider.getInstance());
		return taskOrderList;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}
