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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.Strings;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskList extends BaseModelList<Task> {
	protected TaskLink taskLink;

	public TaskList() {
		super();
		this.taskLink = null;
	}

	public void setTaskLink(TaskLink taskLink) {
		this.taskLink = taskLink;
	}

	public void add(Task baseModel) {
		String id;

		id = baseModel.getId();
		if ((id.equals(Strings.UNDEFINED_ID)) || (id.equals(Strings.EMPTY))) id = String.valueOf(this.items.size());

		this.items.put(id, baseModel);
	}

	public void removeOfType(String taskType) {
		Iterator<String> iterator = this.items.keySet().iterator();
		ArrayList<String> ids = new ArrayList<String>();

		while (iterator.hasNext()) {
			String id = iterator.next();
			Task task = this.items.get(id);
			if (task.getCode().equals(taskType)) ids.add(id);
		}

		iterator = ids.iterator();
		while (iterator.hasNext()) this.items.remove(iterator.next());

	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "tasklist");
		for (Task task : this.get().values())
			task.serializeToXML(serializer, depth);
		serializer.endTag("", "tasklist");
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();
		Boolean partialLoading = this.isPartialLoading();

		if (partialLoading) this.disablePartialLoading();

		for (Task task : this) {
			rows.add(task.toJson());
		}

		result.put("nrows", this.totalCount);
		result.put("rows", rows);

		if (partialLoading) this.enablePartialLoading();

		return result;
	}

}
