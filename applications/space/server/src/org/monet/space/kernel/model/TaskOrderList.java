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

package org.monet.space.kernel.model;

import org.monet.space.kernel.constants.Strings;

import java.util.Map;

public class TaskOrderList extends BaseModelList<TaskOrder> {
	private TaskOrderLink taskOrderLink;
	private String taskId;

	public static final String ITEMS = "Items";

	public TaskOrderList() {
		this.taskOrderLink = null;
		this.taskId = Strings.UNDEFINED_ID;
	}

	public TaskOrderList(TaskOrderList taskOrderList) {
		this.taskOrderLink = null;
		this.taskId = taskOrderList.taskId;
		this.items.putAll(taskOrderList.items);
		this.codes.putAll(taskOrderList.codes);
	}

	public void setTaskOrderLink(TaskOrderLink taskOrderLink) {
		this.taskOrderLink = taskOrderLink;
	}

	public String getIdTask() {
		return this.taskId;
	}

	public void setIdTask(String idTask) {
		this.taskId = idTask;
	}

	public MonetHashMap<TaskOrder> get() {
		onLoad(this, TaskOrderList.ITEMS);
		return this.items;
	}

	public Map<String, TaskOrder> get(int start, int limit) {
		DataRequest dataRequest = new DataRequest();
		dataRequest.setCode(this.getId());
		dataRequest.setStartPos(start);
		dataRequest.setLimit(limit);
		return this.taskOrderLink.requestTaskOrderListItems(this.taskId, dataRequest);
	}

	public int getCount() {
		return this.taskOrderLink.requestTaskOrderListItemsCount(this.taskId);
	}

}
